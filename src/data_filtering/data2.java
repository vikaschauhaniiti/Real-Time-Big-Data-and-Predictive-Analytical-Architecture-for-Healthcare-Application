package data_filtering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class data2 {
static int b=0;
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String dir="/home/raghav/myMTPproject/trainingdata/d2_(20-10)test.csv";
		  File fs =new File(dir);
			 if (!fs.exists()){fs.createNewFile();}
			
			 FileWriter fw =new FileWriter(fs.getAbsoluteFile(),true);
			 
	
			String h="/home/raghav/myMTPproject/trainingdata/d1_case21-32(testing).csv";
			

	   	 BufferedReader reader = null;
	    	 
	  
	    	    
	    	   
	    		String line;
	    	
	    		reader = new BufferedReader(new FileReader(h));
	   			
	    		while ((line = reader.readLine()) != null) {
	    			//System.out.println(ii);
	    	
	    				
	    		try{
	    		String[] hh=line.split(",");
	    		int x=hh.length;
	    		if(x>b){
	    			b=x;
	    		}
	    			
	    		if(x==b){
    			fw.write(line);
    			
    			fw.write("\n");
	    		}
	    		}catch(Exception e){
	    			System.out.println(line);
	    		}}
	    		reader.close();
	    		System.out.println("done");
	    		
	   			

	}

}
