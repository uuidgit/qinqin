package com.qinqin.comparision;

import java.util.Date;

public class FileEntity {
    
    private String name;
    private String path;
    private Date lastModified;
    private String size;
    private boolean isHidden;
    private boolean isFile;
    private boolean isDirectory;
    /**
     * 代表目录或者文件所在的层级，其实目录为0
     */
    private int level;
     
    public FileEntity() {
    }
 
    public FileEntity(String name, String path, Date lastModified,
            String size, boolean isHidden, boolean isFile,boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
        this.isFile = isFile;
        this.isHidden = isHidden;
        this.lastModified = lastModified;
        this.path = path;
        this.size = size;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public String getPath() {
        return path;
    }
 
    public void setPath(String path) {
        this.path = path;
    }
 
    public Date getLastModified() {
        return lastModified;
    }
 
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
 
    public String getSize() {
        return size;
    }
 
    public void setSize(String size) {
        this.size = size;
    }
 
    public boolean isHidden() {
        return isHidden;
    }
 
    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }
 
    public boolean isFile() {
        return isFile;
    }
 
    public void setFile(boolean isFile) {
        this.isFile = isFile;
    }
 
    public boolean isDirectory() {
        return isDirectory;
    }
 
    public void setDirectory(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }
 
    public void setLevel(int level) {
        this.level = level;
    }
 
    public int getLevel() {
        return level;
    }
 
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name:" + name + ",\n");
        sb.append("Path:" + path + ",\n");
        sb.append("IsFile:" + isFile + ",\n");
        sb.append("Hidden:" + isHidden + ",\n");
        sb.append("IsDirectory:" + isDirectory + ",\n");
        sb.append("Size:" + size + ",\n");
        sb.append("Level:"+level+"\n");
        sb.append("LastModified:"+
        DateHelper.format(lastModified, "yyyy-MM-dd HH:mm:ss\n"));
        return sb.toString();
    }
 
    public boolean equals(FileEntity o) {
        if ( o == null ) {
            return false;
        } else {
            if(this.isDirectory() && o.isDirectory()) {
                return o.getLastModified().getTime() == this.getLastModified().getTime();
             
            } else if(this.isFile() && o.isFile()) {
                return  this.getSize().equals(o.getSize()) &&
                        this.getLastModified().getTime() == o.getLastModified().getTime();
            }
             
            return false;
        }
    }
 

}
