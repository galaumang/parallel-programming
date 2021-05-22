import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;
import edu.rit.pj2.vbl.IntVbl;

/**
 * FourSquaresSmp
 * FourSquaresSmp is a parallel program that finds the lexicographically
 * largest four numbers such that the sum of squares of those number is the
 * natural number which is given input.
 * It prints the lexicographically largest four expression for number on the
 * first line and the number of different expressions for number on second line.
 * Example: (for number = 10)
 * 10 = 1^2 + 1^2 + 2^2 + 2^2
 * 2
 *
 * <p>
 * Usage: <TT>java pj2 threads=<K> FourSquaresSmp <I>number</I> </TT>
 *
 * @author Umang G
 */
public class FourSquaresSmp extends Task {
  /**
   * To count the different four square expressions.
   */
  private IntVbl count;
  /**
   * The largest four square expression.
   */
  private FourSquareExpression expr;

  /**
   * Main program.
   *
   * @param arguments input arguments
   * @throws Exception IllegalArgumentException or NumberFormatException
   */
  @Override
  public void main(String[] arguments) throws Exception {
    // Validate command line arguments.
    if (arguments.length != 1)
      usage();

    // Convert the command line argument into int.
    int number = 0;
    try {
      number = Integer.parseInt(arguments[0]);
    } catch (NumberFormatException e) {
      System.err.println("Argument is not an Integer.");
      usage();
    }

    // Validate number in non negative.
    if (number < 0)
      usage();

    System.out.println(number + " = " + findFourSqExp(number));
    System.out.println(count);
  }

  /**
   * Finds the lexicographically largest four numbers such that the sum of
   * squares of those number is the natural number which is given input.
   *
   * @param number Input number to find four square expression.
   * @return a FourSquareExpression object which represents the expression
   * of given number.
   */
  private FourSquareExpression findFourSqExp(final int number) {
    final int sqrtOfNumber = (int) Math.sqrt(number);
    count = new IntVbl.Sum(0);
    expr = new FourSquareExpression();

    parallelFor(0, sqrtOfNumber).schedule(dynamic).exec(new Loop() {
      FourSquareExpression s;
      IntVbl c;
      int firstSq, secondSq, thirdSq, fourthSq;

      public void start() {
        s = threadLocal(expr);
        c = threadLocal(count);
      }

      public void run(int first) {
        firstSq = first * first;
        secondSq = -1;
        for (int second = first; second <= sqrtOfNumber && secondSq < number; second++) {
          secondSq = firstSq + second * second;
          thirdSq = -1;
          for (int third = second; third <= sqrtOfNumber && thirdSq < number; third++) {
            thirdSq = secondSq + third * third;
            int fourth = (int) Math.sqrt(number - thirdSq);
            fourthSq = thirdSq + fourth * fourth;
            if (fourth >= third)
              if (fourthSq == number) {
                s.set(first, second, third, fourth);
                ++c.item;
              }
          }
        }
      }
    });
    return expr;
  }

  /**
   * Print a usage message and exit.
   */
  private void usage() {
    System.err.println("Usage: java pj2 FourSquaresSeq <number>");
    System.err.println("where <number is a number of type int >= 0.");
    System.err.flush();
    throw new IllegalArgumentException();
  }
}
