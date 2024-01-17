import edu.rit.pjmr.PjmrJob;
import edu.rit.pjmr.TextDirectorySource;
import edu.rit.pjmr.TextId;
import java.io.File;

/**
 * Class MaxTemp is the main program for a PJMR map-reduce job that calculate
 * the average of all the maximum temperatures recorded by all the weather
 * stations on all the days in a specified range of years.
 * <P>
 * Usage: <TT>java pj2 [threads=<I>t</I>] MaxTemp <I>directory</I> <I>lb</I>
 * <I>ub</I> <I>node</I> [<I>node</I> ...]</TT>
 * <P>
 * The program runs a separate mapper task on each of the given nodes. Each
 * mapper task has one source and <I>t</I> mappers (default: one mapper).
 * <P>
 * The program prints on each line, the year, a tab character, and the average
 * maximum temperature for that year printed with one digit after the decimal
 * point; the lines are printed in ascending order of year.
 *
 * @author  Umang Gala
 * @version 02-Dec-2014
 */
public class MaxTemp extends PjmrJob<TextId, String, String, AverageVbl> {

	/**
	 * PJMR job main program.
	 *
	 * @param  args  Command line arguments.
	 */
	public void main (String[] args) {
		if ( args.length < 4 ) usage();
		if ( !args[1].matches("^\\d{4}$") || !args[2].matches("^\\d{4}$") ) {
			System.err.println ("Lower bound year or upper bound year is not a number");
			usage();
		}
		
		int noOfThreads = Math.max (threads(), 1);

		for (int node = 3; node < args.length; node++)
			mapperTask(args[node])
				.source (new TextDirectorySource (args[0]))
				.mapper (noOfThreads, MaxTempMapper.class, args[1], args[2]);

		reducerTask()
			.runInJobProcess()
			.customizer (MaxTempCustomizer.class)
			.reducer (MaxTempReducer.class);

		startJob();
	}

   /**
    * Print a usage message and exit.
    */
   private static void usage()
      {
      System.err.println ("Usage: java pj2 [threads=<t>] MaxTemp <directory> <lb> <ub> <node> [<node> ...] ");
      throw new IllegalArgumentException();
      }
}