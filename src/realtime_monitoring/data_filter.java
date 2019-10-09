package realtime_monitoring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;



public class data_filter {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String[]uu=new String[66];
		for(int k=10;k<=32;k++){
			String h="/home/raghav/pateint_dataset/case"+k;
			String dir2="/home/raghav/newdata/case"+k;
			  File fs2 =new File(dir2);
			     fs2.mkdir();
			
		  File folder = new File(h);
	    	File[] listOfFiles = folder.listFiles();
	    	Arrays.sort(listOfFiles);

	   	 BufferedReader reader = null;
	   	 
	   	for (int i =0; i<listOfFiles.length; i++) {
	   		int cont=0;
	    	    File file = listOfFiles[i];
	    	    String s=file.getAbsolutePath();
	    	    String uup=file.getName();
	    	    reader = new BufferedReader(new FileReader(s));
	    		String line;
	    		String dir="/home/raghav/newdata/case"+k+"/"+uup;
	  		  File fs =new File(dir);
	  			 if (!fs.exists()){fs.createNewFile();}
	  			 FileWriter fw =new FileWriter(fs.getAbsoluteFile(),true);
	  			 
	   			
	   				 
	    		while ((line = reader.readLine()) != null) {
	    	
	    			if(cont != 0){
	    			String x ="";
	    			String[] r=line.split(",",-1);
	    			
	    			
	    			
	    			
	    			for(int z=0;z<r.length;z++){
	    				
	    				if(r[z].equals("")){
	    					r[z]="00";
	    				}
	    				
	    				
	    			x=x.concat(",").concat(r[z]);
	    		
	    			}
	    		
	    			String ot="case"+k+x;
	    		String[] nn=ot.split(",");
	    		
	    		
	    		for(int km=0;km<66;km++){
	    			
	    			if(!nn[km].equals("00")){
	    			uu[km]=nn[km];
	    			}
	    		}
	    		
	    		
	    			System.out.println(uu[0]+","+uu[1]+","+uu[2]+","+uu[3]+","+uu[4]+","+uu[6]+","+uu[7]+","
	    			+uu[9]+","+uu[10]+","+uu[12]+","+uu[13]+","+uu[29]+","+uu[30]+","+uu[37]+","+uu[52]+","+uu[63]);
	    			
	    			
	    			
	    			
	   			  
	   			     
	   			     
	   				
	   		       	 fw.write(uu[0]+","+uu[1]+","+uu[2]+","+uu[3]+","+uu[4]+","+uu[6]+","+uu[7]+","
	   		    			+uu[9]+","+uu[10]+","+uu[12]+","+uu[13]+","+uu[29]+","+uu[30]+","+uu[37]+","+uu[52]+","+uu[63]);
	   		       	 
	   		       	 fw.write("\n");
	   		  
	   		       	 
	   		       	 
	   		       	 
	   		       	 
	    			}	
	    			
	    			cont++;
	    			
	    		}
	    	fw.close();
	    	}
	}
	
	
	}
}
