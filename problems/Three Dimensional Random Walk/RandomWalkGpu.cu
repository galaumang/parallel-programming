#include "Random.cu"

// Number of threads per block.
#define noOfThreads 1024

// Variable in global memory to store total distance of all particles.
__device__ double totalDistance;

// Per-thread variables in shared memory.
__shared__ long long int x [noOfThreads];
__shared__ long long int y [noOfThreads];
__shared__ long long int z [noOfThreads];

/**
 * Atomically set double variable v to the sum of itself and value.
 *
 * @param  v      Pointer to double variable.
 * @param  value  Value.
 */
__device__ void atomicAdd(double *v, double value) {
	double oldval, newval;
	do {
		oldval = *v;
		newval = oldval + value;
	} while (atomicCAS((unsigned long long int *)v,__double_as_longlong (oldval), __double_as_longlong (newval)) != __double_as_longlong (oldval));
}

/**
 * Device kernel to compute total distance of particles.
 * <P>
 * Called with a one-dimensional grid of one-dimensional blocks.
 *
 * @param  noOfSteps	Number of steps.
 * @param  seed			Pseudorandom number generator seed.
 *
 * @author Umang Jethalal Gala
 * @version 11-Nov-2014
 */
extern "C" __global__ void calculateDistance(unsigned long long int noOfSteps, long long int seed) {
	int thrdIndex;
	int rank;
	prng_t prng;
	long long int thrdX = 0;
	long long int thrdY = 0;
	long long int thrdZ = 0;
	
	// Determine the current thread Index.
	thrdIndex = threadIdx.x;
	rank = blockIdx.x * noOfThreads + thrdIndex;
	
	// Initialize per-thread prng.
	prngSetSeed (&prng, seed + rank);
	
	// Compute steps for particles
	for (unsigned long long int step = thrdIndex; step < noOfSteps; step += noOfThreads) {
		int choice = prngNextInt (&prng, 6);
		if (choice == 0) {
			thrdX -= 1;
		} else if (choice == 1) {
			thrdX += 1;
		} else if (choice == 2) {
			thrdY -= 1;
		} else if (choice == 3) {
			thrdY += 1;
		} else if (choice == 4) {
			thrdZ -= 1;
		} else if (choice == 5) {
			thrdZ += 1;
		}
	}
	
	// Shared memory parallel reduction within thread block.
	x[thrdIndex] = thrdX;
	y[thrdIndex] = thrdY;
	z[thrdIndex] = thrdZ;
	__syncthreads();
	for (int th = noOfThreads/2; th > 0; th >>=1) {
		if (thrdIndex < th) {
			x[thrdIndex] += x[thrdIndex + th];
			y[thrdIndex] += y[thrdIndex + th];
			z[thrdIndex] += z[thrdIndex + th];
		}
		__syncthreads();
	}
	
	// Calculate distance for each particle and perform atomic addition to determine total distance.
	if (thrdIndex == 0) {
		double distance = sqrt((double)(x[0]*x[0]) + (y[0]*y[0]) + (z[0]*z[0]));
		atomicAdd (&totalDistance, distance);
	}
}
