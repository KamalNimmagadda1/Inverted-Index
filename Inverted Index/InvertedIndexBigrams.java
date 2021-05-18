import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class InvertedIndexBigrams {
    public static class Tokenizer1 extends Mapper<Object, Text, Text, Text> {
        private Text bigrams = new Text();
        private Text docID = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] document = value.toString().split("\t", 2);
            String text = document[1].toLowerCase();
            text = text.replaceAll("[^a-z\\s]", " ").replaceAll("\\s+", " ");

            docID.set(document[0]);
            StringTokenizer tokenizer = new StringTokenizer(text);
            String previous = tokenizer.nextToken();
            while (tokenizer.hasMoreTokens()) {
                String current = tokenizer.nextToken();
                bigrams.set(previous + " " + current);
                context.write(bigrams, docID);
                previous = current;
            }
        }
    }

    public static class Indexer1 extends Reducer<Text, Text, Text, Text> {
        private Text result = new Text();

        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            HashMap<String, Integer> count = new HashMap<>();
            for (Text val : values) {
                String docID = val.toString();
                count.put(docID, count.getOrDefault(docID, 0) + 1);
            }

            StringBuilder invIdx = new StringBuilder();
            for (String s : count.keySet())
                invIdx.append(s).append(":").append(count.get(s)).append("\t");

            result.set(invIdx.substring(0, invIdx.length() - 1));
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Bigrams Inverted Index");
        job.setJarByClass(InvertedIndexBigrams.class);
        job.setMapperClass(Tokenizer1.class);
        job.setReducerClass(Indexer1.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}