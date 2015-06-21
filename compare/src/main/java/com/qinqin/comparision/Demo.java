package com.qinqin.comparision;

import java.util.logging.Logger;


public class Demo {
	 
    /**
     * @param args
     */
    public static void main(String[] args) {
 
        if(args==null) {
            Logger.getLogger(Demo.class.getName()).info("Arguments error!!!");
            return;
             
        }
         
        if (args.length != 4) {
            for (int i = 0; i < args.length; i++) {
              System.out.println("arg" + i + ":" + args[i]);
            }
            Logger.getLogger(Demo.class.getName()).info("args has been too many!!!");
            return;
          }
          try
          {
            DirectoryComparison.compare(args[0], args[1], args[2], args[3]);
          } catch (Exception e) {
            e.printStackTrace();
          }
         
    }
 
}