package edu.ncit.reducesidejoin.optimized.plaintext;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @author Hikmat Dhamee
 * @email me.hemant.available@gmail.com
 */
public class DriverProgram {

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Reduce-Side(Repartition) Join");
        job.setJarByClass(ReduceSidePlainTextJoin.class);

        // Input parameters
        Path donationsPath = new Path(args[0]);
        Path projectsPath = new Path(args[1]);
        Path outputPath = new Path(args[2]);

        // Mappers configuration
        MultipleInputs.addInputPath(job, donationsPath, TextInputFormat.class, ReduceSidePlainTextJoin.DonationsPlainTextMapper.class);
        MultipleInputs.addInputPath(job, projectsPath, TextInputFormat.class, ReduceSidePlainTextJoin.ProjectsPlainTextMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        // Reducer configuration
        job.setNumReduceTasks(3);
        job.setReducerClass(ReduceSidePlainTextJoin.PlainTextJoinReducer.class);

        FileOutputFormat.setOutputPath(job, outputPath);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
