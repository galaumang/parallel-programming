import edu.rit.pjmr.Reducer;

/**
 * Class MaxTempReducer specifies the reduce operation.
 *
 * @author  Umang Gala
 * @version 02-Dec-2014
 */
class MaxTempReducer extends Reducer<String, AverageVbl> {
	public void reduce (String key,  AverageVbl value) {
		System.out.printf ("%s\t%.1f%n", key, value.avgValue()/10);
		System.out.flush();
	}
}