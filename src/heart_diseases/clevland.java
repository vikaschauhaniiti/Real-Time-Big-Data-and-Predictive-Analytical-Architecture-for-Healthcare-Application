package heart_diseases;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.mahout.classifier.evaluation.Auc;
import org.apache.mahout.classifier.sgd.L2;
import org.apache.mahout.classifier.sgd.ModelSerializer;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;



public class clevland {
	 
	public static	OnlineLogisticRegression olr = new OnlineLogisticRegression(2, 13, new L2(1));
	public static Auc eval = new Auc(0.5);
	//Mapper class
	public static class wordcountMapper extends MapReduceBase implements
	Mapper<LongWritable ,/*Input key Type */
	Text, /*Input value Type*/
	IntWritable, /*Output key Type*/
	VectorWritable> /*Output value Type*/
	{
	//	private final static IntWritable one= new IntWritable(1);
	//	private Text word=new Text();
	@Override
	public void map(LongWritable key, Text value, OutputCollector<IntWritable, VectorWritable> output, Reporter reporter)
			throws IOException {
		// TODO Auto-generated method stub
		
		IntWritable one;
		int p;
		String line = value.toString();
		String[] vl=line.split(",");
		double[] d = new double[13];
		for(int i=0;i<13;i++){
			if(vl[i].equals("?"))
				vl[i]="-9";
			d[i] = Double.parseDouble(vl[i]);
			
		}
		if(Integer.parseInt(vl[13])>=1){
			one=new IntWritable(1);
			p=1;
		}
		else{
			one=new IntWritable(0);
			p=0;
		}
		 Vector vec = new RandomAccessSparseVector(d.length);
         vec.assign(d);      // Assign a double array to vector
         VectorWritable writable = new VectorWritable();
         writable.set(vec);
         olr.train(p, vec);
         output.collect(one,writable);
         eval.add(p,olr.classifyScalar(vec));
			
	} 
	
	}
	
			//Main function
			public static void main(String args[])throws Exception
			{	long st=System.currentTimeMillis();
			clevland obj = new clevland();
			obj.makemodel(); //make-model
			
			double[] input = {52,1,4,160,331,0,0,94,1,2.5,-9,-9,-9};
			obj.testmodel(input); // test model
		
			long end =System.currentTimeMillis();
			
			System.out.println("total execution time ="+(end-st)+"ms");
			}
		
			@SuppressWarnings("deprecation")
			public void makemodel() throws IOException{
				String a="/home/raghav/myMTPproject/heart/cleveland.csv";
				String b="/home/raghav/myMTPproject/heart/out/cleveland";
				JobConf conf = new JobConf(clevland.class);
				Path output = new Path(b);
		        
				FileSystem fs = output.getFileSystem((Configuration)conf);
		        
				if (fs.exists(output)) {
		            fs.delete(output);
		        }
				conf.setJobName("word count");
				conf.setOutputKeyClass(IntWritable.class);
				conf.setOutputValueClass(VectorWritable.class);
				conf.setMapperClass(wordcountMapper.class);
				FileInputFormat.addInputPath(conf, new Path(a));
				FileOutputFormat.setOutputPath(conf, new Path(b));
				JobClient.runJob(conf);
				System.out.format(" Learning rate: %2.4f, Accuracy: %2.4f\n", olr.currentLearningRate(), eval.auc());
				ModelSerializer.writeBinary("/home/raghav/mahout-model/logistical_regression/my.model", olr);;
					
			}
			
			public void testmodel(double[] dw) throws FileNotFoundException, IOException{
				OnlineLogisticRegression classifier =
				ModelSerializer.readBinary(new FileInputStream("/home/raghav/mahout-model/logistical_regression/my.model"), OnlineLogisticRegression.class);
				Vector veca = new RandomAccessSparseVector(dw.length);
		        veca.assign(dw); 
				Vector result = classifier.classifyFull(veca);
				System.out.println("------------- Testing (heart diseases)-------------");
				System.out.format("Probability of < 50 diameter narrowing = %f\n",result.get(0));
				System.out.format("Probability of > 50 diameter narrowing = %f\n",result.get(1));
				
			}
		
}
