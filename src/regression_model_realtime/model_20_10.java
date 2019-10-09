package regression_model_realtime;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
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



public class model_20_10 {
	 
	public static	OnlineLogisticRegression olr = new OnlineLogisticRegression(2, 120, new L2(1));
	public static Auc eval = new Auc(0.5);
	public static int y=1,zz=1,cc=1,cn=1;
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
		try{
		String[] vl=line.split(",");
		double[] d = new double[120];
		for(int i=0;i<120;i++){
			if(vl[i].equals("?"))
				vl[i]="-9";
			
			d[i] = Double.parseDouble(vl[i]);
			
		}
		
		if(vl[122].equals("Y")){
			one=new IntWritable(1);
			p=1;
			Vector vec = new RandomAccessSparseVector(d.length);
	        vec.assign(d);      // Assign a double array to vector
	        VectorWritable writable = new VectorWritable();
	        writable.set(vec);
	       // System.out.println(p+"--("+y+")"+vec);
	        y++;
	        olr.train(p, vec);
	        output.collect(one,writable);
	        eval.add(p,olr.classifyScalar(vec));
	        cc++;
			
			 		}
		else{
			
			one=new IntWritable(0);
			p=0;
			if(cn >0){
				Vector vec = new RandomAccessSparseVector(d.length);
		        vec.assign(d);      // Assign a double array to vector
		        VectorWritable writable = new VectorWritable();
		        writable.set(vec);
		     //  System.out.println(p+"--("+y+")"+vec);
		        y++;
		        olr.train(p, vec);
		        output.collect(one,writable);
		        eval.add(p,olr.classifyScalar(vec));
		        cn++;
			}
			
		}
		
		
		}catch(Exception e){}
			
	} 
	
	}
	
			//Main function
			public static void main(String args[])throws Exception
			{	long st=System.currentTimeMillis();
			model_20_10 obj = new model_20_10();
			obj.makemodel(); //make-model
			
			//double[] input = {56,55,98,104,56,12,55,55,98,104,56,13,56,56,98,104,56,12,56,57,98,104,56,13,56,56,99,105,60,13};
			obj.testmodel(); // test model
		
			long end =System.currentTimeMillis();
			
			System.out.println("total execution time ="+(end-st)+"ms");
			}
		
			@SuppressWarnings("deprecation")
			public void makemodel() throws IOException{
				String a="/home/raghav/myMTPproject/trainingdata/20-10/20-10.csv";
				String b="/home/raghav/myMTPproject/trainingdata/out/20-10";
				JobConf conf = new JobConf(model_20_10.class);
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
				System.out.format(" Learning rate: %2.4f, AUC: %2.4f\n", olr.currentLearningRate(), eval.auc());
				ModelSerializer.writeBinary("/home/raghav/myMTPproject/trainingdata/20-10/20-10.model", olr);;
					
			}
			
			public void testmodel() throws FileNotFoundException, IOException{
				String h="/home/raghav/myMTPproject/trainingdata/20-10/20-10_test.csv";
				
				BufferedReader reader = null;
				reader = new BufferedReader(new FileReader(h));
				System.out.println("------------- Testing -------------");
				String line;
				double tp=0,tn=0,fp=0,fn=0,tot=0;
				while ((line = reader.readLine()) != null) {
	    			//System.out.println(ii);
					OnlineLogisticRegression classifier =
							ModelSerializer.readBinary(new FileInputStream("/home/raghav/myMTPproject/trainingdata/20-10/20-10.model"), OnlineLogisticRegression.class);
							int nn=120;
							
							
					String[] p=line.split(",");
					 //System.out.println(line);
					
					double[] dw1=new double[nn];
					for(int op=0;op<dw1.length;op++){
						dw1[op]=Double.parseDouble(p[op]);
						
					}
					Vector veca = new RandomAccessSparseVector(dw1.length);
			        veca.assign(dw1); 
			        Vector result = classifier.classifyFull(veca);
			        //double r1=result.get(0);
			        double r2=result.get(1);
			       
			    // System.out.println( result.logNormalize());
			        String ck;
			        if(r2>0.9){
			        	ck= new String("Y");
			        	 System.out.println(r2);
			        	
			        }
			        else{
			        	ck= new String("N");
			        	 
			        //	 System.out.println(ck+"--"+line);
			        }
			        
			        
			        if(ck.equals("Y") && p[122].equals("Y")){
			        	tp++;
			        }
			       
			        if(ck.equals("Y") && p[122].equals("N")){
			        	fp++;
			        }
			        if(ck.equals("N") && p[122].equals("N")){
			        	tn++;
			        }
			       
			        if(ck.equals("N") && p[122].equals("Y")){
			        	fn++;
			        }
			        tot++;
			       //System.out.format("Probability of N %f\n",result.get(0));
					//System.out.format("Probability of Y %f\n",result.get(1));
	    			}
				reader.close();
				System.out.println("tp= "+tp);
				System.out.println("fp= "+fp);
				System.out.println("tn= "+tn);
				System.out.println("fn= "+fn);
				
				double dd=((tp+tn)/tot)*100;
				
				System.out.format("Accuracy = %.2f\n",dd);
				System.out.print("%");
				
			}
		
}
