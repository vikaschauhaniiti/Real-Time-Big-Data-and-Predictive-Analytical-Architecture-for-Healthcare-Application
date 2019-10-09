package heart_diseases;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;

import org.apache.mahout.classifier.evaluation.Auc;
import org.apache.mahout.classifier.sgd.L2;
import org.apache.mahout.classifier.sgd.ModelSerializer;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;

public class simple_regression {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		OnlineLogisticRegression olr = new OnlineLogisticRegression(2, 13, new L2(1));
		Auc eval = new Auc(0.5);
		
		
		
		
		String dir="/home/raghav/myMTPproject/heart/cleveland.csv";
		 
		  BufferedReader reader = new BufferedReader(new FileReader(dir));
		  String line;
		  int p;
		  while ((line = reader.readLine()) != null) {
			  String[] vl=line.split(",");
				double[] d = new double[13];
				for(int i=0;i<13;i++){
					if(vl[i].equals("?"))
						vl[i]="-9";
					d[i] = Double.parseDouble(vl[i]);
					
				}
				if(Integer.parseInt(vl[13])>=1){
					
					p=1;
				}
				else{
				
					p=0;
				}
				
				 Vector vec = new RandomAccessSparseVector(d.length);
		         vec.assign(d);
		         olr.train(p, vec);
		         eval.add(p,olr.classifyScalar(vec));
				}
		  reader.close();
		  System.out.format(" Learning rate: %2.4f, Accuracy: %2.4f\n", olr.currentLearningRate(), eval.auc());
		  ModelSerializer.writeBinary("/home/raghav/mahout-model/logistical_regression/my2.model", olr);
		  
		  OnlineLogisticRegression classifier =
					ModelSerializer.readBinary(new FileInputStream("/home/raghav/mahout-model/logistical_regression/my2.model"), OnlineLogisticRegression.class);
					
		  double[] dw = {52,1,4,160,331,0,0,94,1,2.5,-9,-9,-9};
		  Vector veca = new RandomAccessSparseVector(dw.length);
			        veca.assign(dw); 
					Vector result = classifier.classifyFull(veca);
					System.out.println("------------- Testing (Wisconsin Diagnostic Breast Cancer (WDBC))-------------");
					System.out.format("Probability of < 50 diameter narrowing = %f\n",result.get(0));
					System.out.format("Probability of > 50 diameter narrowing = %f\n",result.get(1));
					
			
		 
	}

}
