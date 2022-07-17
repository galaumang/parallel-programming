# Climate Data Analysis

## Overview
Write a map-reduce parallel program using the Parallel Java Map Reduce (PJMR) framework
in the Parallel Java 2 Library. Run the program on a cluster parallel computer to learn
about big data and map-reduce parallel programming.

# Climate Data Analysis
The National Climatic Data Center (NCDC), part of the U.S. National Oceanic and Atmospheric Administration
(NOAA), has several publicly accessible climate datasets. One of them is the Global Historical Climatology 
Network (GHCN) dataset, "an integrated database of climate summaries from land surface stations across the 
globe that have been subjected to a common suite of quality assurance reviews. The data are obtained from 
more than 20 sources. Some data are more than 175 years old while others are less than an hour old."

I have downloaded a portion of the GHCN dataset (version 3.12-upd-2014110506) and installed it on the local 
hard disks of the dr00 through dr09 nodes of the tardis cluster. Each node has multiple files stored in the 
directory /var/tmp/ark/weather/. Each file contains weather data from a particular weather station for 
particular months and years. The files on each node total about 100 megabytes, and the dataset totals about 
one gigabyte. (The complete GHCN dataset is about 24 gigabytes.)

Here is an example of one of the files: ACW00011604.dly

Here is a description of the data in each of the files (see Section III, Format of Data Files): readme.txt

For this project, you will analyze the maximum temperatures recorded in the GHCN dataset. You will develop a 
PJMR job to calculate the average of all the maximum temperatures recorded by all the weather stations on all 
the days in a specified range of years.

Note that each reading has a "quality flag" associated with it. If a reading's quality flag indicates that 
the reading failed a quality assurance check, that reading must be omitted from the analysis. A reading of 
−9999 means "missing"; such readings must also be omitted from the analysis.

# Program Input and Output
The maximum temperature program's command line arguments are the name of the directory containing the weather 
data files, the year lower bound, the year upper bound, and one or more cluster node names. The maximum 
temperature program prints a series of lines; each line consists of the year, a tab character, and the 
average maximum temperature for that year printed with one digit after the decimal point; the lines are 
printed in ascending order of year. Here is an example running on the tardis cluster:
```
$ java pj2 jar=MaxTemp.jar MaxTemp /var/tmp/ark/weather/ 1950 1959 dr00 dr01 dr02 dr03 dr04 dr05 dr06 dr07 dr08 dr09      
Job 3 launched Fri Nov 07 10:26:25 EST 2014
Job 3 started Fri Nov 07 10:26:25 EST 2014
1950    25.0
1951    24.7
1952    24.6
1953    24.3
1954    24.4
1955    24.2
1956    23.7
1957    25.2
1958    25.0
1959    25.0
Job 3 finished Fri Nov 07 10:26:34 EST 2014 time 8996 msec
```
This output says that the average of all the maximum temperatures recorded at all the weather stations on all the days in 1950 was 25.0 Celsius; in 1951, 24.7 Celsius; and so on.

A PJMR job is a cluster parallel program. This means that, when running on the tardis cluster, you must include the jar= option on the pj2 command. The JAR file must contain all the Java class files (.class files) in your project.