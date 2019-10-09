package testcases;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.mapred.TextOutputFormat;

import kafka.etl.KafkaETLInputFormat;
import kafka.etl.KafkaETLJob;
import kafka.etl.KafkaETLKey;
import kafka.etl.KafkaETLUtils;
import kafka.etl.Props;
import kafka.message.Message;


public class hdf {

	  public static class mapper1 implements Mapper<KafkaETLKey, BytesWritable, LongWritable, Text> {
	        protected long _count = 0;

	        protected Text getData(Message message) throws IOException {
	            ByteBuffer buf = message.payload();
	            if (buf == null) {
	                return new Text();
	            }
	            byte[] array = new byte[buf.limit()];
	            buf.get(array);
	            Text text = new Text(new String(array, "UTF8"));
	            return text;
	        }

	        public void map(KafkaETLKey key, BytesWritable val, OutputCollector<LongWritable, Text> collector, Reporter reporter) throws IOException {
	            byte[] bytes = KafkaETLUtils.getBytes(val);
	            Message message = new Message(ByteBuffer.wrap(bytes));
	            long checksum = key.getChecksum();
	            if (checksum != message.checksum()) {
	                throw new IOException("Invalid message checksum " + message.checksum() + ". Expected " + key + ".");
	            }
	            Text data = this.getData(message);
	            ++this._count;
	            collector.collect(new LongWritable (_count), data);
	            
	        }

	        public void configure(JobConf arg0) {
	        }

	        public void close() throws IOException {
	        }
	    }
	  
	
	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	public void myhdfs(String a) throws Exception {
		Props props = new Props("/home/raghav/myMTPproject/test2.properties");
	//	String _name = "HDFS CONSUMER";
		Props _props = props;
		String _input = _props.getProperty("input")+a;
		String _output =_props.getProperty("output")+a;
		String _topic = a;
        
		JobConf jobConf = KafkaETLJob.createJobConf("mapper1", _topic, _props, getClass());
        
		jobConf.setMapperClass((Class)mapper1.class);
        
		KafkaETLInputFormat.setInputPaths((JobConf)jobConf, (Path[])new Path[]{new Path(_input)});
        
		jobConf.setOutputKeyClass((Class)LongWritable.class);
        
		jobConf.setOutputValueClass((Class)Text.class);
        
		jobConf.setOutputFormat((Class)TextOutputFormat.class);
        
		TextOutputFormat.setCompressOutput((JobConf)jobConf, (boolean)false);
        
		Path output = new Path(_output);
        
		FileSystem fs = output.getFileSystem((Configuration)jobConf);
        
		if (fs.exists(output)) {
            fs.delete(output);
        }
        
		TextOutputFormat.setOutputPath((JobConf)jobConf, (Path)output);
        
		jobConf.setNumReduceTasks(0);
       

        RunningJob runningJob = new JobClient(jobConf).submitJob(jobConf);
        String id = runningJob.getJobID();
        System.out.println("Hadoop job id=" + id);
        runningJob.waitForCompletion();
        if (!runningJob.isSuccessful()) {
            throw new Exception("Hadoop ETL job failed! Please check status on http://" + jobConf.get("mapred.job.tracker") + "/jobdetails.jsp?jobid=" + id);
        }
		
		
	}
	
	
}
