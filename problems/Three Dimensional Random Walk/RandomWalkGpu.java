import edu.rit.gpu.Gpu;
import edu.rit.gpu.GpuDoubleVbl;
import edu.rit.gpu.Module;
import edu.rit.gpu.Kernel;
import edu.rit.pj2.Task;
import java.nio.ByteBuffer;

/**
 * Class RandomWalkGpu is a GPU parallel program to compute the average final
 * distance from the origin over all the particles.
 * <P>
 * Usage: <TT>java pj2 RandomWalkGpu <I>N</I> <I>T</I> <I>Seed</I> </TT>
 * <BR><TT><I>N</I></TT> = Number of Particles
 * <BR><TT><I>T</I></TT> = Number of Steps
 * <BR><TT><I>Seed</I></TT> = Random seed
 *
 * @author Umang Jethalal Gala
 * @version 11-Nov-2014
 */
public class RandomWalkGpu extends Task {

	/**
	 * Kernel function interface.
	 */
	private static interface RandomWalkKernel extends Kernel {
		/**
		 * Device kernel function to compute total distance of particles.
		 *
		 * @param  noOfSteps	Number of steps.
		 * @param  seed			Pseudorandom number generator seed.
		 */
		public void calculateDistance(long noOfSteps, long seed);
	}
	
	/**
	 * Task main program.
	 */
	public void main(String[] args) throws Exception {
		// Validate number of command line arguments.
		if ( args.length != 3 ) usageMsg();
		
		// Convert the first command line argument (No. of particles) into int.
		int noOfParticles = 0;
		try {
			noOfParticles = Integer.parseInt( args[0] );
		} catch( NumberFormatException e ) {
			System.err.println ("First argument " + 
								"(No. of particles) is not an Integer.");
			usageMsg();
		}
		
		// Validate number is non negative and non zero.
		if ( noOfParticles < 1 ){
			System.err.println ("First argument " + 
								"(No. of particles) is negative or zero");
			usageMsg();
		}
		
		// Convert the second command line argument (Steps in each walk) into
		// long.
		long noOfSteps = 0L;
		try {
			noOfSteps = Long.parseLong( args[1] );
		} catch( NumberFormatException e ) {
			System.err.println ("Second argument " + 
								"(Steps in each walk) is not an Long.");
			usageMsg();
		}
		// Validate number is non negative and non zero.
		if ( noOfSteps < 1L ){
			System.err.println ("Second argument " + 
								"(Steps in each walk) is negative or zero");
			usageMsg();
		}
		
		// Convert the third command line argument (Random seed) into long.
		long seed = 0L;
		try {
			seed = Long.parseLong( args[2] );
		} catch( NumberFormatException e ) {
			System.err.println ("Third argument " + 
								"(Random seed) is not an Long.");
			usageMsg();
		}
		
		// System.out.println ("Number of Particles: " + noOfParticles);
		// System.out.println ("Number of steps for each particle: " 
		//						+ noOfSteps);
		// System.out.println ("Random seed: " + seed);
		
		// Initialize GPU.
		Gpu gpu = Gpu.gpu();
		gpu.ensureComputeCapability (2, 0);
		
		// Set up GPU counter variable.
		Module module = gpu.getModule ("RandomWalkGpu.cubin");
		GpuDoubleVbl totalDistance = module.getDoubleVbl ("totalDistance");
		totalDistance.item = 0.0;
		// Send the variable value from host to device.
		totalDistance.hostToDev();
		
		// Create kernel object.
		RandomWalkKernel kernel = module.getKernel (RandomWalkKernel.class);
		// Set the grid dimension for kernel.
		kernel.setGridDim (noOfParticles);
		// Set the block dimension for each grid cell.
		kernel.setBlockDim (1024);
		// Call the kernel function.
		kernel.calculateDistance (noOfSteps, seed);
		
		// Receive the variable value from device to host.
		totalDistance.devToHost();
		// System.out.println ("Total distance: " + totalDistance.item);
		
		// Print the average distance.
		System.out.println (String.format("%.5f", 
							totalDistance.item / noOfParticles));
	}
	
	/**
	 * Print a usage message and exit.
	 */
	private static void usageMsg() {
		System.err.println ("Usage: java pj2 RandomWalkGpu <N> <T> <seed>");
		System.err.println ("<N> = Number of Particles (type int >= 1)");
		System.err.println ("<T> = Number of Steps (type long >= 1)");
		System.err.println ("<seed> = Random seed (type long)");
		throw new IllegalArgumentException();
	}
	 
	/**
	 * Specify that this task requires one core.
	 */
	protected static int coresRequired() {
		return 1;
	}
	
	/**
	 * Specify that this task requires one GPU accelerator.
	 */
	protected static int gpusRequired() {
		return 1;
	}
}
