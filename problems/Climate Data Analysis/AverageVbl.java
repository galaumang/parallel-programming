import edu.rit.pj2.Vbl;
import java.io.Serializable;

/**
 * Class AverageVbl provides a double variable which is a average of long
 * integer variable shared by multiple threads.
 *
 * @author  Umang Gala
 * @version 02-Dec-2014
 */
class AverageVbl implements Vbl, Serializable {

	/**
	 * The shared average (double) item.
	 */
	public double item;
	
	/**
	 * The shared integer which store the number of elements used for
	 * calculating average.
	 */
	private int noOfElements;

	/**
	 * Construct a new shared average (double) variable. The item's initial
	 * value is 0.0.
	 */
	public AverageVbl() {
	
	}
	
	/**
	 * Construct a new shared average (double) variable with the given initial
	 * value.
	 *
	 * @param  value  Initial value.
	 */
	public AverageVbl(long value) {
		this.item = value;
		noOfElements++;
	}
	
	/**
	 * Construct a new shared average (double) variable that is a copy of the
	 * given variable.
	 *
	 * @param  vbl  Variable to copy.
	 */
	public AverageVbl(AverageVbl vbl) {
		this.item = vbl.item;
		this.noOfElements = vbl.noOfElements;
	}
	
	/**
	 * Returns the average (double) value of this shared variable.
	 *
	 * @return  double average value.
	 */
	public double avgValue() {
		return item;
	}
	
	/**
	 * Set this shared variable to the given shared variable. This variable
	 * must be set to a deep copy of the given variable.
	 *
	 * @param  vbl  Shared variable.
	 *
	 * @exception  ClassCastException
	 *     (unchecked exception) Thrown if the class of <TT>vbl</TT> is not
	 *     compatible with the class of this shared variable.
	 */
	public void set(Vbl vbl) {
		AverageVbl avgVbl = (AverageVbl) vbl;
		this.item = avgVbl.item;
		this.noOfElements = avgVbl.noOfElements;
	}
	
	/**
	 * Reduce the given shared variable into this shared variable. The two
	 * items are combined together such that this variable stores the average
	 * of all the elements in both the variable.
	 *
	 * @param  vbl  Shared variable.
	 *
	 * @exception  ClassCastException
	 *     (unchecked exception) Thrown if the class of <TT>vbl</TT> is not
	 *     compatible with the class of this shared variable.
	 */
	public void reduce(Vbl vbl) {
		AverageVbl avgVbl = (AverageVbl) vbl;
		double total = this.item * this.noOfElements + avgVbl.item * avgVbl.noOfElements;
		this.noOfElements = this.noOfElements + avgVbl.noOfElements;
		this.item = total / this.noOfElements;
	}
	
	/**
	 * Clone this variable. It creates the new variable, copy of this variable.
	 *
	 * @return  A new variable that is a copy of this variable.
	 */
	public Object clone() {
		return new AverageVbl (this);
	}
	
	/**
	 * Returns a string version of this shared variable.
	 *
	 * @return  String version.
	 */
	public String toString() {
		return "" + avgValue() + "      " + noOfElements;
	}
}