package data_filtering;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class mydatat {
	 public static class Map extends
	 
     Mapper<LongWritable, Text, LongWritable, Text> {
static long hh=1;
 @Override
 public void map(LongWritable key, Text value, Context context)
         throws IOException, InterruptedException {
	 //System.out.println(value);
	 LongWritable ln1=new LongWritable(hh);
     context.write(ln1, value);
     hh++;
 }
}

	 
	 
public static class Map2 extends
     Mapper<LongWritable, Text, LongWritable, Text> {
	static int v=1;
	static long hh2=1;
 @Override
 public void map(LongWritable key, Text value, Context context)
         throws IOException, InterruptedException {
	 String line= value.toString();
	 	
	 	if(v>1){
	 		
	 	
		LongWritable ln=new LongWritable(hh2);	
		
		String[] hh=line.split(",");
		String pk="";
		
			if(Integer.parseInt(hh[60])>100){
  				pk=new String(pk.concat("Y,"));
  				
  				
  			}
  			else {
  				pk=new String(pk.concat("N,"));
  				
  			}
  			if(Integer.parseInt(hh[60])<50){
  				pk=new String(pk.concat("Y,"));
  				
  			}
  			else {
  				pk=new String(pk.concat("N,"));
  				
  			}
  			if(Integer.parseInt(hh[62])<94){
  				pk=new String(pk.concat("Y,"));
  				
  			}
  			else {
  				pk=new String(pk.concat("N,"));
  				
  			}
  			if(Integer.parseInt(hh[63])<90 && Integer.parseInt(hh[63])<60){
  				pk=new String(pk.concat("Y,"));
  				
  			}
  			else {
  				pk=new String(pk.concat("N,"));
  				
  			}
  			if(Integer.parseInt(hh[64])>120 && Integer.parseInt(hh[64])>80){
  				pk=new String(pk.concat("Y"));
  				
  			}
  			else {
  				pk=new String(pk.concat("N"));
  				
  			}
  			Text ky=new Text(pk);
		context.write(ln, ky);
hh2++;
	 	}
	 	v++;
	 	//System.out.println("jjjjjjjjjjjjjjjjjjjjjj"+v+"-"+line);
 }
}

public static class Reduce extends
     Reducer<LongWritable, Text, LongWritable, Text> {

 public void reduce(LongWritable key, Iterable <Text> values,
         Context context) throws IOException, InterruptedException {
     String[] lines = new String[2];
  
     for (Text text : values) {
    	 
    	 String gh = text.toString();
    	 if(gh.charAt(0)=='N' || gh.charAt(0)=='Y'){
    		 lines[1]=gh;
    	 }
    	 else{
    		 lines[0]=gh;
    	 }
      
     }
     
   
     Text vl=new Text(lines[0]+","+lines[1]);
     context.write(key, vl);
    

 }

}

@SuppressWarnings("deprecation")
public static void main(String[] args) throws Exception {

 Configuration conf = new Configuration();
 
	 conf.set("fs.defaultFS", "file:///");
 //conf.set("fs.default.name", "hdfs://localhost:8020");
 Job job = new Job(conf);
 job.setJarByClass(mydatat.class);
 job.setJobName("Compare Two Files and Identify the Difference");
 String b="/home/raghav/myMTPproject/trainingdata/outpoy/d-20_10_test";
 Path output = new Path(b);
 
	FileSystem fs = output.getFileSystem((Configuration)conf);
 
	if (fs.exists(output)) {
     fs.delete(output);
 }
 FileOutputFormat.setOutputPath(job, new Path(b));
 job.setReducerClass(Reduce.class);
 job.setOutputKeyClass(LongWritable.class);
 job.setOutputValueClass(Text.class);
 MultipleInputs.addInputPath(job, new Path("/home/raghav/myMTPproject/trainingdata/d2_case21-32(testing).csv"),TextInputFormat.class, Map.class);
 
MultipleInputs.addInputPath(job, new Path("/home/raghav/myMTPproject/trainingdata/d2_case21-32(testing)2.csv"),TextInputFormat.class, Map2.class);
 job.waitForCompletion(true);

}
}
