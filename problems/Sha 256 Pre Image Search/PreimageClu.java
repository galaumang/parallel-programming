import edu.rit.crypto.SHA256;
import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import edu.rit.pj2.Job;
import edu.rit.pj2.LongLoop;
import edu.rit.pj2.Task;
import edu.rit.pj2.Tuple;
import edu.rit.pj2.vbl.LongVbl;
import edu.rit.util.Hex;
import edu.rit.util.Packing;

import java.io.IOException;
import java.util.ArrayList;

/**
 * PreimageClu
 *
 * @author Umang G
 */
public class PreimageClu extends Job {
  @Override
  public void main(String[] arguments) throws Exception {
    // Validate command line arguments.
    if (arguments.length != 2) usageMsg();

    // Convert the first command line argument into int.
    int n = 0;
    try {
      n = Integer.parseInt(arguments[0]);
    } catch (NumberFormatException e) {
      System.err.println("First argument is not an Integer.");
      usageMsg();
    }

    // Validate number is non negative and non zero.
    if (n < 1) {
      System.err.println("First argument is negative or zero");
      usageMsg();
    }

    // Validate second argument for hexadecimal digits.
    if (!arguments[1].matches("^[0-9A-Fa-f]+$")) {
      System.err.println("Second argument does not contain hexadecimal digits.");
      usageMsg();
    }

    long totalValues = (long) Math.pow(2, n);
    // Divide the work into workers.
    masterFor(0L, totalValues, WorkerTask.class).args(arguments[1]);

    // To reduce the preimage lists and count of preimages from all workers.
    rule().atFinish().task(ReduceTask.class).args("" + workers()).runInJobProcess();
  }

  private void usageMsg() {
    System.err.println("Usage: java pj2 PreimageSeq <N> <digest>");
    System.err.println("where <N> is a number of type int such that 1 <= N <= 63, and");
    System.err.println("      <digest> is a truncated digest value consisting of sixteen hexadecimal digits (0-9, A-F, a-f).");
    System.err.flush();
    throw new IllegalArgumentException();
  }

  private static class WorkerTask extends Task {
    // Count the preimages for worker.
    LongVbl count;

    @Override
    public void main(String[] args) throws Exception {
      // Truncated digest to compare.
      final String digest = args[0];

      count = new LongVbl.Sum(0);

      // List of preimages for each thread in worker.
      final ArrayList<ArrayList<Long>> workerThreadPreimage = new ArrayList<ArrayList<Long>>();
      for (int index = 0; index < 4; index++)
        workerThreadPreimage.add(new ArrayList<Long>());

      // Divides the work between workers.
      // Implicitly divide work between threads.
      workerFor().exec(new LongLoop() {
        // Local variables for thread.
        LongVbl thrCount;
        ArrayList<Long> preimage;

        public void start() {
          thrCount = threadLocal(count);
          preimage = new ArrayList<Long>();
        }

        public void run(long value) {
          SHA256 sha = new SHA256();
          byte[] numInBytes = new byte[8];
          Packing.unpackLongBigEndian(value, numInBytes, 0);
          sha.hash(numInBytes);
          byte[] digestOfNumInBytes = new byte[sha.digestSize()];
          sha.digest(digestOfNumInBytes);
          String digestOfNum = Hex.toString(digestOfNumInBytes);
          if (digestOfNum.substring(digestOfNum.length() - digest.length()).equals(digest)) {
            preimage.add(new Long(value));
            ++thrCount.item;
          }
        }

        public void finish() {
          workerThreadPreimage.add(rank(), preimage);
        }
      });

      // All preimages for a worker.
      ArrayList<Long> all = new ArrayList<Long>();
      for (int index = 0; index < workerThreadPreimage.size(); index++)
        if (workerThreadPreimage.get(index) != null)
          all.addAll(workerThreadPreimage.get(index));

      // Put the list of preimage and count in to tuple space.
      putTuple(new WorkerTuple(all, taskRank()));
      putTuple(count);
    }
  }

  private static class WorkerTuple extends Tuple {
    int worker;
    ArrayList<Long> preimage;

    public WorkerTuple() {
      preimage = new ArrayList<Long>();
    }

    public WorkerTuple(ArrayList<Long> preimage, int worker) {
      this.preimage = preimage;
      this.worker = worker;
    }

    @Override
    public void writeOut(OutStream out) throws IOException {
      long[] preimageArray = new long[preimage.size()];
      for (int index = 0; index < preimageArray.length; index++)
        preimageArray[index] = preimage.get(index);
      out.writeInt(worker);
      out.writeLongArray(preimageArray);
    }

    @Override
    public void readIn(InStream in) throws IOException {
      this.worker = in.readInt();
      long[] preimageArray = in.readLongArray();
      for (int index = 0; index < preimageArray.length; index++)
        this.preimage.add(new Long(preimageArray[index]));
    }
  }

  private static class ReduceTask extends Task {
    @Override
    public void main(String[] args) throws Exception {
      // Reading WorkerTuple that contains list of preimages.
      ArrayList<ArrayList<Long>> allPreimages = new ArrayList<ArrayList<Long>>();
      for (int index = 0; index < Integer.parseInt(args[0]); index++)
        allPreimages.add(new ArrayList<Long>());

      // Template for reading from tuple space.
      WorkerTuple workerTemplate = new WorkerTuple();
      WorkerTuple workerTuple;

      // Read and store the lists of preimage from tuple space.
      while ((workerTuple = tryToTakeTuple(workerTemplate)) != null)
        allPreimages.add(workerTuple.worker, workerTuple.preimage);

      // Iterate and print all the preimages.
      for (int worker = 0; worker < allPreimages.size(); worker++)
        for (int preimage = 0; preimage < allPreimages.get(worker).size(); preimage++)
          System.out.println(Hex.toString(allPreimages.get(worker).get(preimage)));

      // Reduction variable for preimage count.
      LongVbl count = new LongVbl.Sum(0L);

      // Template for reading from tuple space.
      LongVbl countTemplate = new LongVbl();
      LongVbl taskCount;

      // Read and reduce the count of preimages.
      while ((taskCount = tryToTakeTuple(countTemplate)) != null)
        count.reduce(taskCount);
      System.out.println(count);
    }
  }
}
