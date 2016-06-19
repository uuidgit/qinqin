package com.qinqin.utls;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.qinqin.entity.Student;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 使用注解进行设置
 * @author qinqin11460
 *
 */
@SuppressWarnings("serial")
public class Classes implements Serializable {
	
	/**
	 * 设置属性显示
	 */
	@XStreamAsAttribute
	@XStreamAlias("名称")
	private String name;
	
	/**
	 * 忽略
	 */
	@XStreamOmitField
	private int number;
	
	 @XStreamImplicit(itemFieldName = "Students")
	    private List<Student> students;

	    @XStreamConverter(SingleValueCalendarConverter.class)
	    private Calendar created = new GregorianCalendar();

	    public Classes() {
	    }

	    public Classes(String name, Student student, Calendar created) {
	        super();
	        this.name = name;
	        this.students = Arrays.asList(student);
	        this.created = created;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public int getNumber() {
	        return number;
	    }

	    public void setNumber(int number) {
	        this.number = number;
	    }

	    public List<Student> getStudents() {
	        return students;
	    }

	    public void setStudents(List<Student> students) {
	        this.students = students;
	    }

	    public Calendar getCreated() {
	        return created;
	    }

	    public void setCreated(Calendar created) {
	        this.created = created;
	    };
	
}
