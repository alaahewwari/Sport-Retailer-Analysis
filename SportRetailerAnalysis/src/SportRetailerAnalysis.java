import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.TreeMap;

public class SportRetailerAnalysis {

    public static class SalesMapper
            extends Mapper<Object, Text, Text, DoubleWritable> {

        private Text retailerCity = new Text();
        private DoubleWritable salesAmount = new DoubleWritable();

        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
            String[] fields = value.toString().split(",");
            if (fields.length == 6 ) {
                String retailer = fields[0].trim();
                String city = fields[2].trim();
                double price = Double.parseDouble(fields[4].replace("$", "").trim());
                int unitsSold = Integer.parseInt(fields[5].trim().replace(",", ""));
                double totalSales = price * unitsSold;
                
                retailerCity.set(retailer + "," + city);
                salesAmount.set(totalSales);
                context.write(retailerCity, salesAmount);
            }
        }
    }

    public static class SalesReducer
            extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {

        private TreeMap<Double, Text> sortedMap = new TreeMap<>();

        public void reduce(Text key, Iterable<DoubleWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            double sum = 0;
            for (DoubleWritable val : values) {
                sum += val.get();
            }
            sortedMap.put(-sum, new Text(key));  // Negative sum for descending order
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            for (Entry<Double, Text> entry : sortedMap.entrySet())
            {
                context.write(entry.getValue(), new DoubleWritable(-entry.getKey()));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length < 2) {
            System.err.println("Usage: SportsRetailerAnalysis <in> <out>");
            System.exit(2);
        }
        Job job = Job.getInstance(conf, "Sport Retailer Analysis");
        job.setJarByClass(SportRetailerAnalysis.class);
        job.setMapperClass(SalesMapper.class);
        job.setReducerClass(SalesReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);
        FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}