# Four Square Theorem

## Overview
Write a sequential program and a parallel program in Java using the Parallel Java 2 Library.
Run the programs on a multi-core parallel computer to learn about multi-core parallel
programming and strong scaling.

## Four Square Theorem
French mathematician Joseph Louis Lagrange (1736–1813) proved this theorem in 1770: Any natural
number is the sum of four squares. Specifically, any integer n >= 0 can be expressed as
```
n = a^2 + b^2 + c^2 + d^2
```
where a, b, c, and d are integers and, without loss of generality, 0 <= a <= b <= c <= d. 
Here are some examples:
```
0 = 0^2 + 0^2 + 0^2 + 0^2
```
```
10 = 0^2 + 0^2 + 1^2 + 3^2
   = 1^2 + 1^2 + 2^2 + 2^2
```
```
100 = 0^2 + 0^2 + 0^2 + 10^2
    = 0^2 + 0^2 + 6^2 + 8^2
    = 1^2 + 1^2 + 7^2 + 7^2
    = 1^2 + 3^2 + 3^2 + 9^2
    = 1^2 + 5^2 + 5^2 + 7^2
    = 2^2 + 4^2 + 4^2 + 8^2
    = 5^2 + 5^2 + 5^2 + 5^2
```
You will write sequential and multi-core parallel versions of a program that, given n, does two things:
* Count the number of different ways n can be expressed as the sum of four squares.
* Determine the lexicographically largest such expression.

When comparing two different expressions, the expression with the larger a value is the lexicographically
largest expression. If both expressions' a values are the same, the expression with the larger b value
is the lexicographically largest expression. If both expressions' a and b values are the same, the expression
with the larger c value is the lexicographically largest expression. If both expressions' a, b, and c values
are the same, the expression with the larger d value is the lexicographically largest expression. In the
above examples, the expressions are listed in ascending lexicographic order.

## Program Input and Output
The four squares program's command line argument is n, a number of type int >= 0. The four squares program's
output is the lexicographically largest expression for n on the first line and the number of different
expressions for n on the second line, in the format shown by these examples:
```
$ java pj2 FourSquaresSeq 0
0 = 0^2 + 0^2 + 0^2 + 0^2
1

$ java pj2 FourSquaresSeq 10
10 = 1^2 + 1^2 + 2^2 + 2^2
2

$ java pj2 FourSquaresSeq 100
100 = 5^2 + 5^2 + 5^2 + 5^2
7
```

## Software Requirements
* The sequential version of the program must be run by typing this command line:
```
java pj2 FourSquaresSeq <n>
	<n> is a number of type int >= 0.
```
**Note:** This means that the program's class must be named FourSquaresSeq, this class must not be in a package, and this class must extend class edu.rit.pj2.Task.
* The parallel version of the program must be run by typing this command line:
```
java pj2 threads=<K> FourSquaresSmp <n>
	<n> is a number of type int >= 0.
	<K> is the number of threads in the parallel thread team (optional).
```
**Note:** This means that the program's class must be named FourSquaresSmp, this class must not be in a package, and this class must extend class edu.rit.pj2.Task.
* If the command line does not have the required number of arguments, or if any argument is erroneous, the program must print a usage message on the standard error and must exit. The wording of the usage message is up to you.
* The program must print on the standard output the lexicographically largest expression and the number of different expressions in the format specified above under Program Input and Output.

**Note:** Your program's output should conform exactly Software Requirements 1 through 4.

## Software Design Criteria
* The programs must follow the multi-core parallel programming patterns studied in class.
* The programs must be designed using object oriented design principles as appropriate.
* The programs must make use of reusable software components as appropriate.
* Each class or interface must include a Javadoc comment describing the overall class or interface.
* Each method within each class or interface must include a Javadoc comment describing the overall method, the arguments if any, the return value if any, and the exceptions thrown if any.

**Note:** Your program's design should conform Software Design Criteria 1 through 5.

## Test Cases
If the program is designed properly, each program run should have taken a fraction of a second. 120-second time-out on each program run is imposed. The program's class files were in a JAR file named FourSquares.jar (for tardis cluster).

```
$ java pj2 timelimit=120 jar=FourSquares.jar FourSquaresSeq 35593
35593 = 92^2 + 92^2 + 92^2 + 101^2
793
$ java pj2 timelimit=120 jar=FourSquares.jar FourSquaresSmp 35593
35593 = 92^2 + 92^2 + 92^2 + 101^2
793
```
```
$ java pj2 timelimit=120 jar=FourSquares.jar FourSquaresSeq 60628
60628 = 113^2 + 113^2 + 121^2 + 143^2
1060
$ java pj2 timelimit=120 jar=FourSquares.jar FourSquaresSmp 60628
60628 = 113^2 + 113^2 + 121^2 + 143^2
1060
```
```
$ java pj2 timelimit=120 jar=FourSquares.jar FourSquaresSeq 49161
49161 = 106^2 + 108^2 + 110^2 + 119^2
1608
$ java pj2 timelimit=120 jar=FourSquares.jar FourSquaresSmp 49161
49161 = 106^2 + 108^2 + 110^2 + 119^2
1608
```
```
$ java pj2 timelimit=120 jar=FourSquares.jar FourSquaresSeq 76098
76098 = 128^2 + 133^2 + 133^2 + 156^2
3531
$ java pj2 timelimit=120 jar=FourSquares.jar FourSquaresSmp 76098
76098 = 128^2 + 133^2 + 133^2 + 156^2
3531
```
```
$ java pj2 timelimit=120 jar=FourSquares.jar FourSquaresSeq 64517
64517 = 120^2 + 121^2 + 126^2 + 140^2
1421
$ java pj2 timelimit=120 jar=FourSquares.jar FourSquaresSmp 64517
64517 = 120^2 + 121^2 + 126^2 + 140^2
1421
```
```
$ java pj2 timelimit=120 jar=FourSquares.jar FourSquaresSeq 21456
21456 = 66^2 + 70^2 + 74^2 + 82^2
139
$ java pj2 timelimit=120 jar=FourSquares.jar FourSquaresSmp 21456
21456 = 66^2 + 70^2 + 74^2 + 82^2
139
```
```
$ java pj2 timelimit=120 jar=FourSquares.jar FourSquaresSeq 1789
1789 = 14^2 + 22^2 + 22^2 + 25^2
46
$ java pj2 timelimit=120 jar=FourSquares.jar FourSquaresSmp 1789
1789 = 14^2 + 22^2 + 22^2 + 25^2
46
```
```
$ java pj2 timelimit=120 jar=FourSquares.jar FourSquaresSeq 16849
16849 = 60^2 + 63^2 + 64^2 + 72^2
447
$ java pj2 timelimit=120 jar=FourSquares.jar FourSquaresSmp 16849
16849 = 60^2 + 63^2 + 64^2 + 72^2
447
```
```
$ java pj2 timelimit=120 jar=FourSquares.jar FourSquaresSeq 36964
36964 = 91^2 + 91^2 + 101^2 + 101^2
632
$ java pj2 timelimit=120 jar=FourSquares.jar FourSquaresSmp 36964
36964 = 91^2 + 91^2 + 101^2 + 101^2
632
```
```
$ java pj2 timelimit=120 jar=FourSquares.jar FourSquaresSeq 35083
35083 = 87^2 + 87^2 + 91^2 + 108^2
781
$ java pj2 timelimit=120 jar=FourSquares.jar FourSquaresSmp 35083
35083 = 87^2 + 87^2 + 91^2 + 108^2
781
```

**Strong Scaling Performance**
```
$ java pj2 threads=1 debug=makespan jar=FourSquares.jar FourSquaresSmp 10000000
	Running times (msec) on one core, n = 10000000:
	39911 39918 39919
$ java pj2 threads=4 debug=makespan jar=FourSquares.jar FourSquaresSmp 10000000
	Running times (msec) on four cores, n = 10000000:
	9786 10160 10172
Efficiency = 1.020
# Efficiency will be different for every different execution.
```

## Grading Criteria
I will grade your project by:
* (10 points) Evaluating the design of your sequential and parallel programs, as documented in the Javadoc and as implemented in the source code.
    * All of the Software Design Criteria are fully met: 10 points.
    * Some of the Software Design Criteria are not fully met: 0 points.
* (10 points) Running your sequential and parallel programs. There will be ten test cases, each worth 1 point. For each test case, if both programs run using the command lines in Requirements 1 and 2 and both programs produce the correct output, the test case will get 1 point, otherwise the test case will get 0 points. "Correct output" means "output fulfills all the Software Requirements exactly."
* (10 points) Strong scaling performance. I will run your parallel program on one node of the tardis cluster, on a certain test case, with K = 1 thread, three times, and take the smallest running time T1. I will run the same test case, with K = 4 threads, three times, and take the smallest running time T2. I will compute Eff = T1/(4*T2).
```
Eff >= 0.9: 10 points.
Eff >= 0.8: 8 points.
Eff >= 0.7: 6 points.
Eff >= 0.6: 4 points.
Eff >= 0.5: 2 point.
Otherwise: 0 points.
```
* (30 points) Total.

## Compiling and Running Your Program
*(Specific for the following computers listed below)*

Your Java main program must be in a file named FourSquaresSeq.java and FourSquaresSmp.java. To compile and run your program:
* Log into the nessie.cs.rit.edu, kraken.cs.rit.edu, champ.cs.rit.edu or tardis.cs.rit.edu computer.
* Set the CLASSPATH, PATH, and LD_LIBRARY_PATH variables as follows.
```
$ export CLASSPATH=.:/var/tmp/parajava/pj2/pj2.jar
$ export PATH=/usr/local/dcs/versions/jdk1.7.0_11_x64/bin:$PATH
```
* Compile the Java main program using this command:
```
$ javac FourSquaresSeq.java
$ javac FourSquaresSmp.java
```
* Create a jar to run a program on tardis cluster computer.
```
$ jar cvf FourSquares.jar *.class
```
* Run the program using this command (substituting the proper command line arguments) on nessie, kraken or champ:
```
$ java pj2 FourSquaresSeq <n>
$ java pj2 threads=<K> FourSquaresSmp <n>
```
* Run the program using this command (substituting the proper command line arguments) on tardis (all class files must be in FourSquares.jar):
```
$ java pj2 jar=FourSquares.jar FourSquaresSeq <n>
$ java pj2 jar=FourSquares.jar threads=<K> FourSquaresSmp <n>
```