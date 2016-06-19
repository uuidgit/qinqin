package com.qinqin.comparision;

import java.io.File;
import java.util.logging.Logger;


public class DemoByList {
	 
    /**
     * @param args
     */
    public static void main(String[] args) {
 
        if(args==null) {
            Logger.getLogger(DemoByList.class.getName()).info("Arguments error!!!");
            return;
             
        }
         
        if (args.length != 3) {
            for (int i = 0; i < args.length; i++) {
              System.out.println("arg" + i + ":" + args[i]);
            }
            Logger.getLogger(DemoByList.class.getName()).info("args has been too many!!!");
            return;
          }
          try
          {
//            DirectoryComparison.compare(args[0], args[1], args[2], args[3]);
        	//组装增量或者删除文件前，先清空相应目录
  			File inc = new File(args[2]);

  			if (inc.exists()) {
  				FileHelper.deleteDir(inc);
  			}
  			System.out.println("-----------------INC:--------------------------\n");
        	DirectoryComparison.getIncByList(args[0], args[1], args[2]);
          } catch (Exception e) {
            e.printStackTrace();
          }
         
    }
 
}