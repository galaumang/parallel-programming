import edu.rit.pj2.Task;

/**
 * FourSquareSeq
 * FourSquaresSeq is a sequential program that finds the lexicographically
 * largest four numbers such that the sum of squares of those number is the
 * natural number which is given input.
 * It prints the lexicographically largest four expression for number on the
 * first line and the number of different expressions for number on second line.
 * Example: (for number = 10)
 * 10 = 1^2 + 1^2 + 2^2 + 2^2
 * 2
 *
 * <p>
 * Usage: <TT>java pj2 FourSquaresSeq <I>number</I> </TT>
 *
 * @author Umang G
 */
public class FourSquaresSeq extends Task {
  /**
   * To count the different four square expressions.
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
  private FourSquareExpression findFourSqExp(int number) {
    int sqrtOfNumber = (int) Math.sqrt(number);
    count = 0;
    FourSquareExpression expr = new FourSquareExpression();
    int firstSq, secondSq, thirdSq, fourthSq;

    for (int first = 0; first <= sqrtOfNumber; first++) {
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
              expr = new FourSquareExpression(first, second, third, fourth);
              count++;
            }
        }
      }
    }
    return expr;
  }

  /**
   * Print a usage message and exit.
   */
  private void usage() {
    System.err.println("Usage: java pj2 FourSquaresSeq <number>");
    System.err.println("where <number> is a number of type int >= 0.");
    System.err.flush();
    throw new IllegalArgumentException();
  }
}
