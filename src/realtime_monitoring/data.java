package realtime_monitoring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;



public class data {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String dir="/home/raghav/myMTPproject/trainingdata/case21-32(testing).csv";
		  File fs =new File(dir);
			 if (!fs.exists()){fs.createNewFile();}
			 FileWriter fw =new FileWriter(fs.getAbsoluteFile(),true);
			 String h;
		for(int k=21;k<=32;k++){
			if(k<10){
			 h="/home/raghav/myMTPproject/dataset/case0"+k;
			}
			else{
				 h="/home/raghav/myMTPproject/dataset/case"+k;
			}
			     
			
		  File folder = new File(h);
	    	File[] listOfFiles = folder.listFiles();
	    	Arrays.sort(listOfFiles);

	   	 BufferedReader reader = null;
	   	 
	   	for (int i =0; i<listOfFiles.length; i++) {
	   		
	    	    File file = listOfFiles[i];
	    	    String s=file.getAbsolutePath();
	    	    reader = new BufferedReader(new FileReader(s));
	    		String line;
	    	
	   			
	   				 
	    		while ((line = reader.readLine()) != null) {
	    	
	    			try{
	    			
	    			String[] vl=line.split(",");
	    			String []ops=vl[1].split(":");
	    			String []ops2=ops[2].split("_");
	    			
	    			if((Integer.parseInt(ops2[0])%60)==0 && ops2[1].equals("000")){
	    				System.out.println(line);
	    			fw.write(line);
	    		   	 fw.write("\n");
	    		   	
	    			}
	    			 }
	    			catch(Exception e){}
	   		    	}
	    	   }
		}
		
	}

}
