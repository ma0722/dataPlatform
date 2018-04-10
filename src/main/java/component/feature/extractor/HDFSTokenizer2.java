package component.feature.extractor;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.spark.sql.Dataset;
import util.HDFSFileUtil;
import util.SparkUtil;

import java.io.IOException;

public class HDFSTokenizer2 extends Configured implements Tool {

    static int index = -1;
    static int columnsNum = -1;

    public void submit (String[] args) throws Exception{
        index = Integer.parseInt(args[2]);
        columnsNum = Integer.parseInt(args[3]);
        while (HDFSFileUtil.checkFile(args[1]))
            HDFSFileUtil.delFile(args[1], false);
        int res = ToolRunner.run(new Configuration(), new HDFSTokenizer2(), args);
    }

    public static class Map extends Mapper<LongWritable,Text,Text,Text> {

        //Map程序
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
            String line = value.toString();
            if (line.trim().equals("")) return;
            String[] lineSplit = line.split(",");

//            System.out.println("=================");
//            System.out.println("整行文本： " + line);
//            System.out.println("列数" + lineSplit.length);

            if (lineSplit.length != columnsNum) return;
            String text = lineSplit[index];
//            System.out.println("分词文本： " + text);

//            System.out.println("分词列： " + String.valueOf(index));
            //分词
            StringBuilder sb = new StringBuilder();
            Result result = ToAnalysis.parse(text);
            for (Term term : result.getTerms()) {
                sb.append(term.getName());
                sb.append(" ");
            }
//            System.out.println("分词结果： " + sb.toString());

            context.write(new Text(" "), new Text(line +  "," + sb.toString()));
        }
    }

    //配置、提交任务
    public int run(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = new Job(conf, "Tokenizer");//任务名
        job.setJarByClass(HDFSTokenizer2.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        job.setMapperClass(Map.class);
        job.setOutputFormatClass(org.apache.hadoop.mapreduce.lib.output.TextOutputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.waitForCompletion(true);

        return job.isSuccessful()?0:1;
    }
}
