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
