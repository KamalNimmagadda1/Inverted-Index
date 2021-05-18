# Inverted-Index

## Objectives:
● Creating an Inverted Index of words occurring in a set of web pages using GCP App Engine.\

We’ll be using a subset of 74 files from a total of 408 files (text extracted from HTML tags) derived from the
Stanford WebBase project that is available here. It was obtained from a web crawl done in February 2007. It
is one of the largest collections totaling more than 100 million web pages from more than 50,000 websites.
This version has been cleaned for the purpose of this assignment.\

These files will be placed in a bucket on your Google cloud storage and the Hadoop job will be instructed to
read the input from this bucket.\
1. Uploading the input data into the bucket\
a. Get the data files from either of the links below\
http://csci572.com/2020Fall/hw3/DATA.zip \

## Inverted Index Implementation using Map-Reduce
Now that you have the cluster and the files in place, you need to write the actual code for the job. As of
now, Google Cloud allows us to submit jobs via the UI, only if they are packaged as a jar file. The following steps
are focused on submitting a job written in Java via the Cloud console UI.\
Refer to the examples below and write a Map-Reduce job in Java that creates an Inverted Index given a
collection of text files. You can very easily tweak a word-count example to create an inverted index instead
(Hint: Change the mapper to output word docID instead of word count and in the reducer use a HashMap). \

Here are some helpful examples of Map-Reduce Jobs\
1. https://developer.yahoo.com/hadoop/tutorial/module4.html#wordcount \
2. https://hadoop.apache.org/docs/stable/hadoop-mapreduce-client/hadoop-mapreduce-clientcore/MapReduceTutorial.html \

You can then refer to the code present in the folder [Inverted Index](Inverted Index). Run it in the Hadoop Cluster to get output file.

Once the job executes copy all the log entries that were generated to a text file called log.txt. You
need to submit this log along with the Java code. You need to do this only for the job you run on the
full data. No need to submit the logs for the dev_data.\
The output files will be stored in the output folder on the bucket. If you open this folder, you’ll notice
that the inverted index is in several segments. (Delete the _SUCCESS file in the folder before merging
all the output files)\
To merge the output files, run the following command in the master nodes command line (SSH)\
○ hadoop fs -getmerge gs://dataproc-69070458-bbe2-.../output
./output.txt\
○ hadoop fs -copyFromLocal ./output.txt\
○ hadoop fs -cp ./output.txt gs://dataproc-69070458-bbe2-.../output.txt\

The output.txt file in the bucket contains the full Inverted Index for all the files.\
1. Sort your output.txt file using the command\
sort -o output_sorted.txt output.txt\
2. Use grep to search for the words mentioned in the submissions section. Using grep is the
fastest way to get the entries associated with the words. For example, to search for “string” use\
grep -w ‘^string ’ output_sorted.txt\
or\
grep -w ‘^string’ output_sorted.txt\

## Inverted Index of Bigrams using Map-Reduce
Now that you are familiar with setting up and running Hadoop jobs on GCP, you will now modify your
InvertedIndexJob.java script to generate an inverted index of bigrams (instead of unigrams).\
Your existing Mapper class emits (word, docID) pairs which are then aggregated in the Reducer class. You
will have to modify your Mapper class to emit (“word1 word2”, docID) pairs instead. The reducer remains
unchanged. 
