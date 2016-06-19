package com.qinqin.test;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class Log {
	  public static void main(String[] args) {
	   File file = new File("E:\\JENKINS_HOME\\jobs");
	   File [] fs = file.listFiles();
	   Arrays.sort(fs, new Log.CompratorByLastModified());
	   for (int i = 1; i < fs.length; i++) {
		   if(fs[i].isDirectory()) {
			   System.out.println(new Date(fs[i].lastModified()).toLocaleString()+"==="+fs[i]);
		   }
	   }
	  }
	  static class CompratorByLastModified implements Comparator<File>
	  {
	   public int compare(File f1, File f2) {
		long diff = f1.lastModified()-f2.lastModified();
			if(diff>0)
			  return 1;
			else if(diff==0)
	  		  return 0;
			else
			  return -1;
			}
	  public boolean equals(Object obj){
		return true;
	  }
	  }
	}
