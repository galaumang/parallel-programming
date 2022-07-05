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
