package heart_diseases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class data_collect {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String dir="/home/raghav/Desktop/heart1";
		 File fs =new File("/home/raghav/Desktop/hear_don1.csv");
			 if (!fs.exists()){fs.createNewFile();}
			 FileWriter fw =new FileWriter(fs.getAbsoluteFile(),true);
			 
		  BufferedReader reader = new BufferedReader(new FileReader(dir));
		  String line;
		  while ((line = reader.readLine()) != null) {
		    	String[] s1=line.split(" ");
		    	int i=0;
		    	fw.write(line);
		    	fw.write(" ");
		    	System.out.println(line);
		    	try{
		    	while(s1[i]!=null){
		    		if(s1[i].equals("name")){
		    			fw.write("\n");
		    		}
		    		i++;
		    	}
		    	}catch(Exception e){
		    		
		    	}
		    	
		
		    	}
		  reader.close();
		  fw.close();
	}

}
