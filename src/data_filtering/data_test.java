package data_filtering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;



public class data_test {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String dir="/home/raghav/myMTPproject/trainingdata/d1_case21-32(testing).csv";
		  File fs =new File(dir);
			 if (fs.exists()){fs.delete();}
			 fs.createNewFile();
			 FileWriter fw =new FileWriter(fs.getAbsoluteFile(),true);
			 
	
			String h="/home/raghav/myMTPproject/trainingdata/case21-32(testing).csv";
			

	   	 BufferedReader reader = null;
	   
	    		String line;
	    	int oo=1;
	   			
	   				int ii=1;
	   				reader = new BufferedReader(new FileReader(h));
	    		while ((line = reader.readLine()) != null) {
	    			try{
	    			//System.out.println(ii);
	    			
	    				//String[] hh=line.split(",");
    	    		//	String kk=hh[0]+","+hh[1]+","+hh[2]+","+hh[5]+","+hh[6]+","+hh[9];
    	    			fw.write(line);
    	    			fw.write(",");
    	    			System.out.println(line);
	    			 
    	    			if(ii%20 == 0){
	    				System.out.println("ok");
		    		   	fw.write("\n");
    	    			}
	    			
	    			ii++;
	    			}
	    			
	    			
	    			  catch(Exception e){
	    				  
	    			  }
	   		    	}
	    			
	    		reader.close();
	    		fw.write("\n");
	    		System.out.println("00====="+oo);

	    		oo++;
	   			
	  
	   			
	    	   }
		
		
	

}
