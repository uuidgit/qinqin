package com.qinqin.comparision;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DirectoryComparison {
	 
    public static void compare(String src,String dest,String incre, String del) throws Exception {
        File srcFile = new File(src);
        File destFile = new File(dest);
        List<FileEntity> srcList = new ArrayList<FileEntity>();
        List<FileEntity> destList = new ArrayList<FileEntity>();
        Map<Integer,List<FileEntity>> map = null;
         
        if(!srcFile.exists() || !destFile.exists()) {
             return; 
        }
         
        FileHelper.traversingDir(srcFile, srcList);
        FileHelper.traversingDir(destFile, destList);
        FileHelper.setLevels(srcList, src);
        FileHelper.setLevels(destList, dest);
        //FileHelper.output(srcList);
        //FileHelper.output(destList);
        //map = FileHelper.doCompare(srcList, destList, src, dest);
        map = FileHelper.doCompare(srcList, destList, src, dest);
        System.out.println("----------------INCRE:-----------------");
        
        for (Entry<Integer,List<FileEntity>> entry : map.entrySet()) {
             
            System.out.println("########  Level:" + entry.getKey() +"   ###################\n");

            //FileHelper.output(entry.getValue());
            FileHelper.output(entry.getValue(),dest,incre);
            
            System.out.println("#######################################");
        }
        map = FileHelper.doDelCompare(srcList, destList, src, dest);
        System.out.println("----------------DEL:-----------------");
        for (Entry<Integer,List<FileEntity>> entry : map.entrySet()) {
            
            System.out.println("########  Level:" + entry.getKey() +"   ###################\n");

            //FileHelper.output(entry.getValue());
            FileHelper.output(entry.getValue(),src,del);
            
            System.out.println("#######################################");
        }
    }
}
