package data_filtering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;



public class data {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String dir="/home/raghav/myMTPproject/trainingdata/d1_(20-10)test.csv";
		  File fs =new File(dir);
			 if (fs.exists()){fs.delete();}
			 fs.createNewFile();
			 FileWriter fw =new FileWriter(fs.getAbsoluteFile(),true);
			 
	
			String h="/home/raghav/myMTPproject/trainingdata/case1-20.csv";
			

	   	 BufferedReader reader = null;
	   	BufferedReader reader2 = null;
	   	 
	  
	    	    
	    	    reader2 = new BufferedReader(new FileReader(h));
	    		String line;
	    	
	   			
	   			int oo=1;
	   			while((reader2.readLine()) != null)
	   			{ 
	   				int ii=1,ii2=1;
	   				reader = new BufferedReader(new FileReader(h));
	    		while ((line = reader.readLine()) != null) {
	    			try{
	    			//System.out.println(ii);
	    			if(ii2>=oo){
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
	    			ii2++;
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
		
		
	

}
