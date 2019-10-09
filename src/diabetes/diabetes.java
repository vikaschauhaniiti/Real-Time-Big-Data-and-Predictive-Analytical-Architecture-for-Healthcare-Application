package diabetes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
import org.apache.mahout.classifier.sgd.AdaptiveLogisticRegression;
import org.apache.mahout.classifier.sgd.CrossFoldLearner;
import org.apache.mahout.classifier.sgd.L2;
import org.apache.mahout.classifier.sgd.ModelSerializer;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;



public class diabetes {
	public static AdaptiveLogisticRegression olr = new AdaptiveLogisticRegression(3, 43, new L2(1));
	//public static	OnlineLogisticRegression olr = new OnlineLogisticRegression(2, 43, new L2(1));
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
		double[] d = new double[43];
		System.out.println();
		
		for(int i=0;i<43;i++){
			
			if(vl[i].equals("?"))
				vl[i]="0";
			if(vl[i].equals("Up"))
				vl[i]="1";
			if(vl[i].equals("Down"))
				vl[i]="2";
			if(vl[i].equals("Steady"))
				vl[i]="3";
			if(vl[i].equals("No"))
				vl[i]="4";
			if(vl[i].equals("Yes"))
				vl[i]="5";
			if(vl[i].equals("Ch"))
				vl[i]="6";
			
			////////////////////////////
			
			if(vl[0].equals("Caucasian"))
				vl[0]="1";
			else if(vl[0].equals("Asian"))
				vl[0]="2";
			else if(vl[0].equals("African"))
				vl[0]="3";
			else if(vl[0].equals("American"))
				vl[0]="4";
			else if(vl[0].equals("Hispanic"))
				vl[0]="5";
			else {
				vl[0]="0";}
		//////////////////////////////////	
			if(vl[1].equals("Male"))
				vl[1]="1";
			else if(vl[1].equals("Female"))
				vl[1]="2";
			else {
				vl[1]="0";}
		////////////////////////////////////
			
		vl[12]=	vl[12].replace("V", "10");
		vl[13]=	vl[13].replace("V", "10");
		vl[14]=	vl[14].replace("V", "10");
		vl[12]=	vl[12].replace("E", "11");
		vl[13]=	vl[13].replace("E", "11");
		vl[14]=	vl[14].replace("E", "11");
			
			if(vl[12].equals("?"))
				vl[12]="0";
			if(vl[13].equals("?"))
				vl[13]="0";
			if(vl[14].equals("?"))
				vl[14]="0";
		/////////////////////////////////////
			if(vl[16].equals("None"))
				vl[16]="1";
			else if(vl[16].equals(">200"))
				vl[16]="2";
			else if(vl[16].equals(">300"))
				vl[16]="3";
			else {
				vl[16]="0";}
	   //////////////////////////////////////
			if(vl[17].equals("None"))
				vl[17]="1";
			else if(vl[17].equals(">7"))
				vl[17]="2";
			else if(vl[17].equals(">8"))
				vl[17]="3";
			else {
				vl[17]="0";}
			/////////////////////////////////
			System.out.print(i+"--"+vl[i]+" ");
			
		d[i] = Double.parseDouble(vl[i]);
		
		
			
		}
		if(vl[43].equals(">30")){
			one=new IntWritable(1);
			p=1;
		}
		else if(vl[43].equals("<30")){
			one=new IntWritable(2);
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
         System.out.println();
         System.out.println(vec);
         olr.train(p, vec);
        
         output.collect(one,writable);
         
         System.out.print(p);
         
         System.out.print("  ok.. ");
        // State<AdaptiveLogisticRegression.Wrapper, CrossFoldLearner> best = olr.getBest();
         
         //System.out.println(olr.classifyScalarNoLink(vec));
      // eval.add(p,olr.classifyScalar(vec));
         
        
         
	} 
	
	}
	
			//Main function
			public static void main(String args[])throws Exception
			{	long st=System.currentTimeMillis();
			diabetes obj = new diabetes();
			obj.makemodel(); //make-model
			
			//double[] input = {52,1,4,160,331,0,0,94,1,2.5,-9,-9,-9};
			//obj.testmodel(input); // test model
		
			long end =System.currentTimeMillis();
			
			System.out.println("total execution time ="+(end-st)+"ms");
			}
		
			@SuppressWarnings("deprecation")
			public void makemodel() throws IOException{
				String a="/home/raghav/dataset/dataset_diabetes/diabetic_data.csv";
				String b="/home/raghav/dataset/dataset_diabetes/out";
				JobConf conf = new JobConf(diabetes.class);
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
				System.out.println("done");
				olr.close();
				ModelSerializer.writeBinary("/home/raghav/dataset/dataset_diabetes/diabetes.model",olr.getBest().getPayload().getLearner());
				
				

				 InputStream in=new FileInputStream("/home/raghav/dataset/dataset_diabetes/diabetes.model");
				 CrossFoldLearner best=ModelSerializer.readBinary(in,CrossFoldLearner.class);
				 System.out.println(" auc="+best.auc()+"\n percentCorrect="+best.percentCorrect()+"\n LogLikelihood="+best.getLogLikelihood());
			
				
				//System.out.println(" auc="+best.auc()+"\n percentCorrect="+best.percentCorrect()+"\n LogLikelihood="+best.getLogLikelihood());
				
				//System.out.format("Accuracy: %2.4f\n", eval.auc());
				//ModelSerializer.writeBinary("/home/raghav/mahout-model/logistical_regression/my.model", olr);;
					
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
