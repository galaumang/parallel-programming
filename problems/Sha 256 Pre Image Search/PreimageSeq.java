import edu.rit.crypto.SHA256;
import edu.rit.pj2.Task;
import edu.rit.util.Hex;
import edu.rit.util.Packing;

/**
 * PreimageSeq
 * PreimageSeq is a sequential program that find preimages of a given truncated digest.
 * A preimage is an input message whose truncated digest is the given value.
 *
 * <p>
 * Usage: <TT>java pj2 PreimageSeq <I>N</I> <I>digest</I> </TT>
 *
 * @author Umang G
 */
public class PreimageSeq extends Task {
  /**
   * To count the total number of preimage found.
   */
  private int count;

  /**
   * Main program.
   *
   * @param arguments input arguments
   * @throws Exception IllegalArgumentException or NumberFormatException
   */
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
    findPreimage(totalValues, arguments[1]);
    System.out.println(count);
  }

  /**
   * Prints each value that is a preimage of the given truncated digest
   * value. Also counts the number of such values.
   *
   * @param totalValues Total numbers to search for preimage.
   * @param digest      Given truncated digest.
   */
  private void findPreimage(long totalValues, String digest) {
    SHA256 sha = new SHA256();
    byte[] numInBytes = new byte[8];
    count = 0;
    for (long value = 0; value < totalValues; value++) {
      sha.reset();
      Packing.unpackLongBigEndian(value, numInBytes, 0);
      sha.hash(numInBytes);
      byte[] digestOfNumInBytes = new byte[sha.digestSize()];
      sha.digest(digestOfNumInBytes);
      String digestOfNum = Hex.toString(digestOfNumInBytes);
      //System.out.println(Hex.toString(value) + "  " + digestOfNum);
      if (digestOfNum.substring(digestOfNum.length() - digest.length()).equals(digest)) {
        System.out.println(Hex.toString(value));
        count++;
      }
    }
  }

  /**
   * Print a usage message and exit.
   */
  private void usageMsg() {
    System.err.println("Usage: java pj2 PreimageSeq <N> <digest>");
    System.err.println("where <N> is a number of type int such that 1 <= N <= 63, and");
    System.err.println("      <digest> is a truncated digest value consisting of sixteen hexadecimal digits (0-9, A-F, a-f).");
    System.err.flush();
    throw new IllegalArgumentException();
  }
}
