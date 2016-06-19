package com.qinqin.entity;

import java.io.Serializable;

/**
 * 新建Student实体类
 * @author qinqin11460
 *
 */

@SuppressWarnings("serial")
public class Student implements Serializable {
    private int id;
    private String name;
    private String email;
    private String address;
    private String birthday;

    public Student() {
    }
    
    public Student(int id, String name, String email, String address,
            String birthday) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.birthday = birthday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String toString() {
        return this.name + "#" + this.id + "#" + this.address + "#"
                + this.birthday + "#" + this.email;
    }

}
