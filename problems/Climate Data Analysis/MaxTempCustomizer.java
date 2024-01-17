import edu.rit.pjmr.Customizer;

/**
 * Class MaxTempCustomizer customize the mapper output before giving to
 * reducer.
 *
 * @author  Umang Gala
 * @version 02-Dec-2014
 */
class MaxTempCustomizer extends Customizer<String, AverageVbl> {
	public boolean comesBefore (String key_1, AverageVbl value_1, String key_2, AverageVbl value_2) {
		return key_1.compareTo (key_2) < 0;
	}
}