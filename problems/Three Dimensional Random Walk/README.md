# Three-Dimensional Random Walk

## Overview
Write a GPU parallel program, with the main program in Java using the Parallel Java 2 Library and the computational 
kernel in C using CUDA. Run the program on a GPU accelerated computer to learn about GPU parallel programming.

## Three-Dimensional Random Walk
A three-dimensional random walk is defined as follows. A particle is initially positioned at (0, 0, 0) in the X–Y–Z 
coordinate space. The particle does a sequence of T steps. At each step, the particle chooses one of the six directions 
left, right, ahead, back, up, or down at random, and then moves one unit in that direction. Specifically, if the 
particle is at (x, y, z):

With probability 1/6 the particle moves left to (x−1, y, z).\
With probability 1/6 the particle moves right to (x+1, y, z).\
With probability 1/6 the particle moves back to (x, y−1, z).\
With probability 1/6 the particle moves ahead to (x, y+1, z).\
With probability 1/6 the particle moves down to (x, y, z−1).\
With probability 1/6 the particle moves up to (x, y, z+1).\

After T steps, the particle is at a certain final distance d from the origin, where d = sqrt(x^2 + y^2 + z^2) is the 
standard Euclidean distance.

Suppose N particles each do a random walk of T steps as described above. We want to compute the average final distance 
from the origin over all the particles. It can be shown that for a large number of particles, the average final 
distance is approximately sqrt(T).

It's clear that different particles random walks can be done in parallel. At first glance, it appears that the steps 
within one random walk have to be performed sequentially. However, it is in fact possible to do the steps within one 
random walk in parallel. (Keep in mind that all we care about is the particle's final (x, y, z) position.)

You will write a GPU computational kernel to do, in parallel, N random walks of T steps each. You will need a 
pseudo-random number generator (PRNG) that runs on the GPU; use Random.cu. You will also need to calculate squares and 
square roots on the GPU; use code like this:

```
double a = ...;
double a_squared = a*a;
double sqrt_a = sqrt(a);
```

## Program Input and Output
The random walk program's command line arguments are N, T, and a random seed. The random walk program's output is the 
average final distance, printed with five digits after the decimal point. Here is an example:
```
$ java pj2 RandomWalkGpu 1000 1000000 142857
930.05215
```

## Software Requirements
* The program must be run by typing this command line:
```
java pj2 RandomWalkGpu <N> <T> <seed>
	<N> is a number of type int ≥ 1 and ≤ 65535 giving the number of particles.
	<T> is a number of type long ≥ 1 giving the number of steps in each random walk.
	<seed> is a number of type long giving the random seed.
```
**Note:** This means that the program's class must be named RandomWalkGpu, this class must not be in a package, and this class must extend class edu.rit.pj2.Task.
* If the command line does not have the required number of arguments, or if any argument is erroneous, the program must print an error message on the standard error and must exit. The error message must describe what the problem is. The wording of the error message is up to you.
* The program must print on the standard output the average final distance from the origin over all the particles. The answer must be printed with five digits after the decimal point.

**Note:** Your program's output should conform exactly Software Requirements 1 through 3.

## Software Design Criteria
* The program must follow the GPU parallel programming patterns studied in class.
* The GPU kernel must compute the random walks in parallel; must compute the steps of each random walk in parallel; and must output a single number that is used to determine the average final distance.
* The program must be designed using object oriented design principles as appropriate.
* The program must make use of reusable software components as appropriate.
* Each class or interface must include a Javadoc comment describing the overall class or interface.
* Each method within each class or interface must include a Javadoc comment describing the overall method, the arguments if any, the return value if any, and the exceptions thrown if any.

**Note:** Your program's design should conform Software Design Criteria 1 through 6.

## Test Cases
```
java pj2 RandomWalkGpu 1000 10000 142857 
92.88639
# Output should be 90..110
```
```
java pj2 RandomWalkGpu 5000 10000 142858 
92.19888
# Output should be 90..110
```
```
java pj2 RandomWalkGpu 1000 40000 142859 
181.35092
# Output should be 180..220
```
```
java pj2 RandomWalkGpu 5000 40000 142860 
185.31274
# Output should be 180..220
```
```
java pj2 RandomWalkGpu 1000 160000 142861 
360.15162
# Output should be 360..440
```
```
java pj2 RandomWalkGpu 5000 160000 142862 
368.59621
# Output should be 360..440
```
```
java pj2 RandomWalkGpu 1000 640000 142863 
742.08882
# Output should be 720..880
```
```
java pj2 RandomWalkGpu 5000 640000 142864 
738.49677
# Output should be 720..880
```
```
java pj2 RandomWalkGpu 1000 2560000 142865 
1477.43493
# Output should be 1440..1760
```
```
java pj2 RandomWalkGpu 5000 2560000 142866 
1484.11007
# Output should be 1440..1760
```

## Grading Criteria
I will grade your project by:
* (10 points) Evaluating the design of your GPU parallel programs, as documented in the Javadoc and as implemented in the source code.
    * All of the Software Design Criteria are fully met: 10 points.
    * Some of the Software Design Criteria are not fully met: 0 points.
* (10 points) Running your GPU parallel program. There will be ten test cases, each worth 1 point. For each test case, if the program runs using the command line in Requirement 1 and the program produces the correct output, the test case will get 1 point, otherwise the test case will get 0 points. "Correct output" means "output fulfils all the Software Requirements exactly, and the printed answer is within ±10 percent of the theoretical value sqrt(T)."
* (20 points) Total.

I will grade the test cases based solely on whether your program produces the correct output as specified in the above Software Requirements. Any deviation from the requirements will result in a grade of 0 for the test case. This includes errors in the formatting (such as extra spaces), output lines not terminated with a newline, and extraneous output not called for in the requirements. The requirements state exactly what the output is supposed to be, and there is no excuse for outputting anything different.

If there is a defect in your program and that same defect causes multiple test cases to fail, I will deduct points for every failing test case. The number of points deducted does not depend on the size of the defect; I will deduct the same number of points whether the defect is 1 line, 10 lines, 100 lines, or whatever.

## Compiling and Running Your Program
*(Specific for the following computers listed below)*

Your Java main program must be in a file named RandomWalkGpu.java. Your CUDA kernel must be in a file named RandomWalkGpu.cu. To compile and run your program:
* Log into the nessie.cs.rit.edu or champ.cs.rit.edu computer
* Set the CLASSPATH, PATH, and LD_LIBRARY_PATH variables as follows.
```
$ export CLASSPATH=.:/var/tmp/parajava/pj2/pj2.jar
$ export PATH=/usr/local/dcs/versions/jdk1.7.0_11_x64/bin:/usr/local/cuda/bin:$PATH
$ export LD_LIBRARY_PATH=/usr/local/cuda/lib:/usr/local/cuda/lib64:/var/tmp/parajava
```
* Compile the Java main program using this command:
```
$ javac RandomWalkGpu.java
```
* Compile the CUDA kernel using this command:
```
$ nvcc -cubin -arch compute_20 -code sm_20 --ptxas-options="-v" -o RandomWalkGpu.cubin RandomWalkGpu.cu
```
* Run the program using this command (substituting the proper command line arguments) on nessie or champ:
```
$ java pj2 RandomWalkGpu <N> <T> <seed>
```