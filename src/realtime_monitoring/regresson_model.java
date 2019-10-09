package realtime_monitoring;


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



public class regresson_model {
	 
	public static	OnlineLogisticRegression olr = new OnlineLogisticRegression(2, 11, new L2(1));
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
		double[] d = new double[11];
		for(int i=0;i<11;i++){
			try{
			d[i] = Double.parseDouble(vl[i+4]);
			}
			catch(Exception r){
				d[i]=0;
			}
		}
		
		try{
			String []ops=vl[1].split(":");
			String []ops2=ops[2].split("_");
			
			if((Integer.parseInt(ops2[0])%60)==0 && ops2[1].equals("000")){
				
				
			System.out.println(vl[0]+"--"+vl[1]+"--"+vl[15]);
			if(vl[15].equals("0")){
				one=new IntWritable(0);
				p=0;
			}
			else{
				one=new IntWritable(1);
				p=1;
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
			catch(Exception r){
				System.out.println("error");
			}
		}
	
			
	} 
	
	
	
			//Main function
			public static void main(String args[])throws Exception
			{	long st=System.currentTimeMillis();
			regresson_model obj = new regresson_model();
			obj.makemodel(); //make-model
			
		//	double[] input = {13.2,17.43,84.13,541.6,0.07215,0.04524,0.04336,0.01105,0.1487,0.05635,0.163,1.601,0.873,13.56,0.006261,0.01569,0.03079,0.005383,0.01962,0.00225,13.94,27.82,88.28,602,0.1101,0.1508,0.2298,0.0497,0.2767,0.07198};
		//	obj.testmodel(input); // test model
		
			long end =System.currentTimeMillis();
			
			System.out.println("total execution time ="+(end-st)+"ms");
			}
		
			@SuppressWarnings("deprecation")
			public void makemodel() throws IOException{
				String a="/home/raghav/myMTPproject/trainingdata/case1_5.csv";
				String b="/home/raghav/myMTPproject/regression_model/1-5/out";
				
				
				JobConf conf = new JobConf(regresson_model.class);
				Path output = new Path(b);
		        
				FileSystem fs = output.getFileSystem((Configuration)conf);
		        
				if (fs.exists(output)) {
		            fs.delete(output);
		        }
				
				conf.setJobName("regression_model");
				
				conf.setOutputKeyClass(IntWritable.class);
				conf.setOutputValueClass(VectorWritable.class);
				conf.setMapperClass(wordcountMapper.class);
				FileInputFormat.addInputPath(conf, new Path(a));
				FileOutputFormat.setOutputPath(conf, new Path(b));
				JobClient.runJob(conf);
				System.out.format(" Learning rate: %2.4f, Accuracy: %2.4f\n", olr.currentLearningRate(), eval.auc());
				ModelSerializer.writeBinary("/home/raghav/myMTPproject/regression_model/1-5/my.model", olr);;
					
			}
			
			public void testmodel(double[] dw) throws FileNotFoundException, IOException{
				OnlineLogisticRegression classifier =
				ModelSerializer.readBinary(new FileInputStream("/home/raghav/myMTPproject/regression_model/my.model"), OnlineLogisticRegression.class);
				Vector veca = new RandomAccessSparseVector(dw.length);
		        veca.assign(dw); 
				Vector result = classifier.classifyFull(veca);
				System.out.println("------------- Testing -------------");
				System.out.format("Probability of Normal = %.3f\n",result.get(0));
				System.out.format("Probability of Alert= %.3f\n",result.get(1));
				
			}
		
}

