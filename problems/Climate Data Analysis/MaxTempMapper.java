import edu.rit.pjmr.Combiner;
import edu.rit.pjmr.Mapper;
import edu.rit.pjmr.TextId;

/**
 * Class MaxTempMapper specifies the map operation.
 *
 * @author  Umang Gala
 * @version 02-Dec-2014
 */
class MaxTempMapper extends Mapper<TextId, String, String, AverageVbl> {
	private AverageVbl maxTemp;
	private Integer lb, ub;

	public void start (String[] args, Combiner<String, AverageVbl> combiner) {
		lb = Integer.parseInt(args[0]);
		ub = Integer.parseInt(args[1]);
	}

	public void map (TextId inKey, String inValue, Combiner<String, AverageVbl> combiner) {
		Integer year = Integer.parseInt(inValue.substring(11,15));
		String element = inValue.substring(17,21);
		
		if (year < lb || year > ub || !element.equals("TMAX"))
			return;

		for (int index = 21; index < inValue.length(); index += 8) {
			Long value = Long.parseLong(inValue.substring(index, index + 5).trim());
			String qualityFlag = inValue.substring(index + 6, index + 7);
			if (value != -9999 && qualityFlag.equals(" ")) {
				maxTemp = new AverageVbl (value);
				combiner.add (year.toString(), maxTemp);
			}
		}
	}
}