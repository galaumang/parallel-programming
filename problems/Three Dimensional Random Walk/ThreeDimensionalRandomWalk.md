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
