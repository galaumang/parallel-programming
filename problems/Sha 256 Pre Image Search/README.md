# SHA-256 Pre-Image Search

## Overview
Write a sequential program and a parallel program in Java using the Parallel Java 2 Library. 
Run the programs on a cluster parallel computer to learn about a cluster parallel programming and weak scaling.

## SHA-256 Pre-Image Search
The SHA-256 cryptographic hash function takes an input message (byte sequence) of arbitrary length and 
produces an output digest of fixed length, namely 256 bits (32 bytes). For this project we will work with 
messages of length 64 bits (8 bytes). Also, we will work with truncated digests. To compute a truncated 
digest of length D bits, compute the full 256-bit digest, then discard all but the D the least significant bits 
of the digest. We will work with truncated digests where D is a multiple of 4 bits in the range 4 <= D <= 64. 
Here is an example:
```
Message = 0000000000000000
Digest	= af5570f5a1810b7af78caf4bc70a660f0df51e42baf91d4de5b2328de0e83dfc
16-bit truncated digest = 3dfc
```
You will write a sequential program, and a parallel program to find pre-images of a given truncated digest. 
A pre-image is an input message whose truncated digest is the given value. It is not possible to run a hash 
function backwards and compute the input, given the output. The only generic way to find a pre-image is to 
try different messages, compute their truncated digests, and repeat until the desired digest value is found.

The program is given a number N, and a truncated digest value. The program computes the truncated digest for 
all 64-bit message values from 0 through 2^N-1. The program reports each such a message that is a preimage of 
the given truncated digest value. The program also reports the number of pre-images found.

Here is a tiny example. Suppose N = 5, and the truncated digest value is 75 hexadecimal (8 bits). 
Here are the messages the program would examine, the full digests, and the truncated digests, all in hexadecimal:

```
0000000000000000  af5570f5a1810b7af78caf4bc70a660f0df51e42baf91d4de5b2328de0e83dfc  fc
0000000000000001  cd2662154e6d76b2b2b92e70c0cac3ccf534f9b74eb5b89819ec509083d00a50  50
0000000000000002  cd04a4754498e06db5a13c5f371f1f04ff6d2470f24aa9bd886540e5dce77f70  70
0000000000000003  d5688a52d55a02ec4aea5ec1eadfffe1c9e0ee6a4ddbe2377f98326d42dfc975  75  <==
0000000000000004  8005f02d43fa06e7d0585fb64c961d57e318b27a145c857bcd3a6bdb413ff7fc  fc
0000000000000005  5dee4dd60ff8d0ba9900fe91e90e0dcf65f0570d42c431f727d0300dd70dc431  31
0000000000000006  14ac577cdb2ef6d986078b4054cc9893a9a14a16dbb0d8f37b89167c1f1aacdf  df
0000000000000007  a3eb8db89fc5123ccfd49585059f292bc40a1c0d550b860f24f84efb4760fbf2  f2
0000000000000008  4c0e071832d527694adea57b50dd7b2164c2a47c02940dcf26fa07c44d6d222a  2a
0000000000000009  5924513516a5993435ec4a240610304aca7d4acf1f2de5ce6812a8c43610c6e6  e6
000000000000000a  8d85f8467240628a94819b26bee26e3a9b2804334c63482deacec8d64ab4e1e7  e7
000000000000000b  0b5000b73a53f0916c93c68f4b9b6ba8af5a10978634ae4f2237e1f3fbe324fa  fa
000000000000000c  d27a8d9a8d38c7a37c922450a7a1961f138abfa25f5da3df2b972820715fa5ae  ae
000000000000000d  1f5edc6f1efb165d45a654798d4baaa50e3b4d24182913aef5110a15580ebaad  ad
000000000000000e  76b1ff1a6cb1b23738647eb1ea40d8f14b037285e457214aab335874feb6e79e  9e
000000000000000f  e66c57014a6156061ae669809ec5d735e484e8fcfd540e110c9b04f84c0b4504  04
0000000000000010  998e907bfbb34f71c66b6dc6c40fe98ca6d2d5a29755bc5a04824c36082a61d1  d1
0000000000000011  a348621a527709a7d3a71eec18de7bb281a01af97f06fe463c87d0de5c437f75  75  <==
0000000000000012  5bc67471c189d78c76461dcab6141a733bdab3799d1d69e0c419119c92e82b3d  3d
0000000000000013  1b8d0103e3a8d9ce8bda3bff71225be4b5bb18830466ae94f517321b7ecc6f94  94
0000000000000014  22a264ee63bc826a6df778800a62ca8f7033d50f14c7c738ece23b505f2bf3c4  c4
0000000000000015  e85f440b865d705e30c4e50635ffb8880ca03b3c54f294deb577b800bbd96de9  e9
0000000000000016  7a42e3892368f826928202014a6ca95a3d8d846df25088da80018663edf96b1c  1c
0000000000000017  aed2b8245fdc8acc45eda51abc7d07e612c25f05cadd1579f3474f0bf1f6bdc6  c6
0000000000000018  b16efe34e810d8f791443efa2519fea699857cb8bcbf93f69f6eea4163753333  33
0000000000000019  561f627b4213258dc8863498bb9b07c904c3c65a78c1a36bca329154d1ded213  13
000000000000001a  1209fe3bc3497e47376dfbd9df0600a17c63384c85f859671956d8289e5a0be8  e8
000000000000001b  42f28a46039f894d3a0179d090851ba795ef081ae128cf54ee4e496d3453244d  4d
000000000000001c  1d7968ffc995d6a495071668f4a957bed35c6ef912f62c667e5cb535cb2bbb1e  1e
000000000000001d  539b4c4c41a13f1f0452f5a35a6743c4212946f7f32ded28a7d9fdb17672cb54  54
000000000000001e  48a97e421546f8d4cae1cf88c51a459a8c10a88442eed63643dd263cef880c1c  1c
000000000000001f  1664a6e0ea12d234b4911d011800bb0f8c1101a0f9a49a91ee6e2493e34d8e7b  7b

```
In this case the program found two pre-images, 0000000000000003 and 0000000000000011.

Note that there is an implementation of SHA-256 in the Parallel Java 2 Library, class edu.rit.crypto.SHA256. 
Use that class. Don't try to implement SHA-256 yourself. You may also find class edu.rit.util.Packing useful.

## Program Input and Output
The pre-image program's command line arguments are N, and a truncated digest value in hexadecimal. The truncated 
digest size in bits is the number of hexadecimal digits times 4. The pre-image program's output is a list of 
all the pre-images found, one per line, in hexadecimal. The pre-images are printed in ascending numerical order. 
The pre-image program's final output line is the number of pre-images found. Here is an example:
```
$ java pj2 PreimageSeq 5 75
0000000000000003
0000000000000011
2
```

## Software Requirements
* The sequential version of the program must be run by typing this command line:
```
java pj2 PreimageSeq <n> <digest>
	<n> is a number of type int in the range 1 <= N <= 63.
	<digest> is a truncated digest value consisting of one through sixteen hexadecimal digits (0–9, A–F, a–f).
```
**Note:** This means that the program's class must be named PreimageSeq, this class must not be in a package, and this class must extend class edu.rit.pj2.Task.
* The cluster parallel version of the program must be run by typing this command line:
```
java pj2 workers=<K> PreimageClu <N> <digest>
	<n> is a number of type int in the range 1 <= N <= 63.
	<digest> is a truncated digest value consisting of one through sixteen hexadecimal digits (0–9, A–F, a–f).
```
**Note:** This means that the program's class must be named PreimageClu, this class must not be in a package, and this class must extend class edu.rit.pj2.Job.
* If the command line does not have the required number of arguments, or if any argument is erroneous, the program must print an error message on the standard error and must exit. The error message must describe what the problem is. The wording of the error message is up to you.
* The program must print on the standard output the pre-images of the given truncated digest value, each on a separate line, and must print the number of pre-images found as the last line. Each preimage must be printed as a hexadecimal number of 16 upper-case digits (0–9, A–F). The pre-images must be printed in ascending numerical order.

**Note:** Your program's output should conform exactly Software Requirements 1 through 4.

## Software Design Criteria
* The programs must follow the cluster parallel programming patterns studied in class.
* The PreimageClu program must be a hybrid parallel program that runs in multiple tasks, each task running on all the cores in the node.
* The programs must be designed using object oriented design principles as appropriate.
* The programs must make use of reusable software components as appropriate.
* Each class or interface must include a Javadoc comment describing the overall class or interface.
* Each method within each class or interface must include a Javadoc comment describing the overall method, the arguments if any, the return value if any, and the exceptions thrown if any.

**Note:** Your program's design should conform Software Design Criteria 1 through 6.

## Test Cases
If the program was designed properly, each program run should have taken at most 16 seconds. 120-second time-out on each program run is imposed. The program's class files were in a JAR file named Preimage.jar (for tardis cluster).
```
$ java pj2 debug=none timelimit=120 jar=Preimage.jar PreimageSeq 20 ffff
$ java pj2 debug=none timelimit=120 jar=Preimage.jar workers=2 PreimageClu 20 ffff
000000000001762D
000000000001E99A
0000000000020677
0000000000028767
0000000000028C42
0000000000031D7B
000000000003E8AF
00000000000411A5
000000000005849E
00000000000628CF
000000000006DA7B
0000000000071F69
00000000000935C6
00000000000A78DA
00000000000B41DA
00000000000C537D
00000000000D7EE4
00000000000DF62F
00000000000EC117
00000000000ED713
20
```
```
$ java pj2 debug=none timelimit=120 jar=Preimage.jar PreimageSeq 20 fffff
$ java pj2 debug=none timelimit=120 jar=Preimage.jar workers=2 PreimageClu 20 fffff
00000000000935C6
1
```
```
$ java pj2 debug=none timelimit=120 jar=Preimage.jar PreimageSeq 21 342f
$ java pj2 debug=none timelimit=120 jar=Preimage.jar workers=2 PreimageClu 21 342f
000000000001E3D3
0000000000029B77
000000000002F86D
00000000000369CF
000000000004C8BA
000000000005396F
0000000000056408
0000000000067A3A
0000000000077C45
0000000000083A3F
0000000000084D3F
000000000009989F
000000000009B452
000000000009B6FF
000000000009E1C6
00000000000BDA82
00000000000C9E3B
00000000000CE101
00000000000EA2E8
00000000000EB9EF
00000000000F0440
000000000010D010
000000000010F272
00000000001118C6
000000000016041F
000000000016FE58
00000000001740E2
000000000019E673
00000000001A196B
00000000001A6635
00000000001C0EF6
00000000001DDAE2
00000000001E30F4
33
```
```
$ java pj2 debug=none timelimit=120 jar=Preimage.jar PreimageSeq 21 0342f
$ java pj2 debug=none timelimit=120 jar=Preimage.jar workers=2 PreimageClu 21 0342f
00000000000C9E3B
00000000001A6635
2
```
```
$ java pj2 debug=none timelimit=120 jar=Preimage.jar PreimageSeq 22 48fa
$ java pj2 debug=none timelimit=120 jar=Preimage.jar workers=2 PreimageClu 22 48fa
0000000000026FC7
000000000002ECF8
000000000003AA24
00000000000517B7
000000000005A3D9
000000000005F790
0000000000060A6D
00000000000BCCA5
00000000000BE07B
00000000000CC69F
00000000000CF2F5
00000000000DB68E
000000000010BE7F
000000000013DEB8
000000000014AC7E
000000000014D80D
00000000001612D5
000000000016FD75
000000000017F015
0000000000180DCC
000000000019CEDE
000000000019EC33
00000000001A1FC0
00000000001AC8B0
00000000001B66A6
00000000001BF3DA
00000000001C5241
00000000001E0518
00000000001E54E7
00000000001F2B28
0000000000201905
0000000000210A73
0000000000214490
00000000002241A2
000000000022C942
000000000022F4CC
0000000000234C57
000000000023C19B
0000000000246373
000000000024F468
000000000026CCC0
000000000027870A
000000000028FFF3
000000000029DD2D
00000000002A0BE5
00000000002E079F
00000000002E67EF
00000000002F6E53
00000000003031CF
0000000000323A16
00000000003386C2
0000000000365717
00000000003A41CD
00000000003AEF00
00000000003B06A9
00000000003BE404
00000000003C116D
00000000003C747F
00000000003E1BD8
00000000003EA4AC
00000000003F883E
61
```
```
$ java pj2 debug=none timelimit=120 jar=Preimage.jar PreimageSeq 22 b48fa
$ java pj2 debug=none timelimit=120 jar=Preimage.jar workers=2 PreimageClu 22 b48fa
0000000000026FC7
000000000002ECF8
00000000000CC69F
000000000026CCC0
000000000029DD2D
00000000003E1BD8
6
```
```
$ java pj2 debug=none timelimit=120 jar=Preimage.jar PreimageSeq 23 0c59a
$ java pj2 debug=none timelimit=120 jar=Preimage.jar workers=2 PreimageClu 23 0c59a
00000000000389B9
000000000010761C
00000000001380A7
00000000001A8DA4
0000000000218040
00000000003C4638
0000000000467973
000000000063E96D
8
```
```
$ java pj2 debug=none timelimit=120 jar=Preimage.jar PreimageSeq 23 00c59a
$ java pj2 debug=none timelimit=120 jar=Preimage.jar workers=2 PreimageClu 23 00c59a
0
```
```
$ java pj2 debug=none timelimit=120 jar=Preimage.jar PreimageSeq 24 3ff6f
$ java pj2 debug=none timelimit=120 jar=Preimage.jar workers=2 PreimageClu 24 3ff6f
000000000004AC03
0000000000186339
0000000000395EF1
000000000044FC1B
00000000004EFF8E
0000000000563C45
0000000000664387
00000000006657B7
000000000072AD68
00000000007A4EBD
0000000000891B89
000000000091462A
000000000095702F
0000000000A8530C
0000000000B25BA4
0000000000C82BD6
0000000000ED38B9
0000000000FD0613
18
```
```
$ java pj2 debug=none timelimit=120 jar=Preimage.jar PreimageSeq 24 53ff6f
$ java pj2 debug=none timelimit=120 jar=Preimage.jar workers=2 PreimageClu 24 53ff6f
000000000044FC1B
1
```

**Weak Scaling Performance**
```
$ java pj2 jar=Preimage.jar debug=makespan workers=1 PreimageClu 26 012345
	Running times (msec) on one node:
	41378 41423 41606
$ java pj2 jar=Preimage.jar debug=makespan workers=4 PreimageClu 28 012345
	Running times (msec) on four nodes:
	41655 41957 42239
Efficiency = 0.993
# Efficiency will be different for every different execution.
```

## Grading Criteria
I will grade your project by:
* (10 points) Evaluating the design of your sequential and  cluster parallel programs, as documented in the Javadoc and as implemented in the source code.
    * All of the Software Design Criteria are fully met: 10 points.
    * Some of the Software Design Criteria are not fully met: 0 points.
* (10 points) Running your sequential and parallel programs. There will be ten test cases, each worth 1 point. For each test case, if both programs run using the command lines in Requirements 1 and 2 and both programs produce the correct output, the test case will get 1 point, otherwise the test case will get 0 points. "Correct output" means "output fulfils all the Software Requirements exactly."
* (10 points) Weak scaling performance. I will run your parallel program on the tardis cluster, with a certain N and a certain truncated digest value, with workers=1, three times, and take the smallest running time T1. I will run your parallel program on the tardis cluster, with N' = N+2 and the same truncated digest value, with workers=4, three times, and take the smallest running time T2. I will compute Eff = T1/T2.
```
Eff = 0.9: 10 points.
Eff = 0.8: 8 points.
Eff = 0.7: 6 points.
Eff = 0.6: 4 points.
Eff = 0.5: 2 points.
Otherwise: 0 points.
```
* (30 points) Total.

I will grade the test cases based solely on whether your program produces the correct output as specified in the above Software Requirements. Any deviation from the requirements will result in a grade of 0 for the test case. This includes errors in the formatting (such as extra spaces), output lines not terminated with a newline, and extraneous output not called for in the requirements. The requirements state exactly what the output is supposed to be, and there is no excuse for outputting anything different.

If there is a defect in your program and that same defect causes multiple test cases to fail, I will deduct points for every failing test case. The number of points deducted does not depend on the size of the defect; I will deduct the same number of points whether the defect is 1 line, 10 lines, 100 lines, or whatever.

## Compiling and Running Your Program
*(Specific for the following computers listed below)*

Your Java main program must be in a file named PreimageSeq.java and PreimageSmp.java. To compile and run your program:
* Log into the tardis.cs.rit.edu computer.
* Set the CLASSPATH, PATH, and LD_LIBRARY_PATH variables as follows.
```
$ export CLASSPATH=.:/var/tmp/parajava/pj2/pj2.jar
$ export PATH=/usr/local/dcs/versions/jdk1.7.0_11_x64/bin:$PATH
```
* Compile the Java main program using this command:
```
$ javac PreimageSeq.java
$ javac PreimageSmp.java
```
* Create a jar to run a program on tardis cluster computer.
```
$ jar cvf Preimage.jar *.class
```
* Run the program using this command (substituting the proper command line arguments) on tardis (all class files must be in FourSquares.jar):
```
$ java pj2 jar=Preimage.jar PreimageSeq <N> <digest>
$ java pj2 jar=Preimage.jar workers=<K> PreimageClu <N> <digest>
```