import edu.rit.pj2.Vbl;

/**
 * Class FourSquareExpression provides the expression and evaluation to find
 * lexicographically largest expression for four-square theorem.
 *
 * @author Umang G
 */
public class FourSquareExpression implements Vbl, Comparable<FourSquareExpression> {

  private int firstNo;
  private int secondNo;
  private int thirdNo;
  private int fourthNo;

  /**
   * Construct a new four-square expression. It is initialize to 0.
   */
  public FourSquareExpression() {
    firstNo = secondNo = thirdNo = fourthNo = 0;
  }

  /**
   * Construct a new four-square expression. It is initialize to given values.
   *
   * @param first  First value of expression.
   * @param second Second value of expression.
   * @param third  Third value of expression.
   * @param fourth Fourth value of expression.
   */
  public FourSquareExpression(int first, int second, int third, int fourth) {
    firstNo = first;
    secondNo = second;
    thirdNo = third;
    fourthNo = fourth;
  }

  /**
   * Construct a new four-square expression that is a copy of the given
   * expression.
   *
   * @param expr Expression to copy.
   */
  public FourSquareExpression(FourSquareExpression expr) {
    this.firstNo = expr.firstNo;
    this.secondNo = expr.secondNo;
    this.thirdNo = expr.thirdNo;
    this.fourthNo = expr.fourthNo;
  }

  /**
   * Clone this expression. It creates the new expression, copy of this
   * expression.
   *
   * @return A new expression that is a copy of this expression.
   */
  @Override
  public Object clone() {
    return new FourSquareExpression(this);
  }

  /**
   * Set this expression to be a copy of the given expression.
   *
   * @param vbl FourSquareExpression (reduction variable) to copy.
   */
  @Override
  public void set(Vbl vbl) {
    FourSquareExpression expr = (FourSquareExpression) vbl;
    this.firstNo = expr.firstNo;
    this.secondNo = expr.secondNo;
    this.thirdNo = expr.thirdNo;
    this.fourthNo = expr.fourthNo;
  }

  /**
   * Reduce the given expression together with this expression.
   * After that, this expression is whichever of the two is better
   * (lexicographically largest).
   *
   * @param vbl FourSquareExpression (reduction variable) to reduce.
   */
  @Override
  public void reduce(Vbl vbl) {
    FourSquareExpression expr = (FourSquareExpression) vbl;
    if (this.compareTo(expr) < 0)
      this.set(expr);
  }

  /**
   * Compare this expression to the given expression. Of the two expression
   * being compared, the one which has larger first number in expression is
   * better. If both expression have same first number, than they are
   * compared with second and so on. If all four numbers are same than they
   * are equal.
   *
   * @param expr Expression to compare.
   * @return A negative, zero, or positive number if this expression is
   * worse than, equal to , or better than the given expression,
   * respectively.
   */
  @Override
  public int compareTo(FourSquareExpression expr) {
    if (this.firstNo > expr.firstNo)
      return 1;
    else if (this.firstNo < expr.firstNo)
      return -1;
    else if (this.secondNo > expr.secondNo)
      return 1;
    else if (this.secondNo < expr.secondNo)
      return -1;
    else if (this.thirdNo > expr.thirdNo)
      return 1;
    else if (this.thirdNo < expr.thirdNo)
      return -1;
    else if (this.fourthNo > expr.fourthNo)
      return 1;
    else if (this.fourthNo < expr.fourthNo)
      return -1;
    else
      return 0;
  }

  /**
   * Returns a String object representing the four-square expression.
   *
   * @return a string representation of this expression.
   */
  public String toString() {
    return firstNo + "^2 + " + secondNo + "^2 + " + thirdNo + "^2 + " +
        fourthNo + "^2";
  }

  /**
   * Sets the given values to the expression. It overrides the current values
   * to the provided new values.
   *
   * @param first  First value of expression.
   * @param second Second value of expression.
   * @param third  Third value of expression.
   * @param fourth Fourth value of expression.
   */
  public void set(int first, int second, int third, int fourth) {
    firstNo = first;
    secondNo = second;
    thirdNo = third;
    fourthNo = fourth;
  }
}
