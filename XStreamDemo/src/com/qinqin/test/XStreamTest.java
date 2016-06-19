package com.qinqin.test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jettison.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.qinqin.entity.Student;
import com.qinqin.utls.Classes;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

public class XStreamTest {
    private XStream xstream=null;
    private ObjectInputStream inStream=null;
    private ObjectOutputStream outStream=null;
    private Student student=null;
    
    /**
     * 初始化
     */
    @Before
    public void init(){
        xstream=new XStream();
        student=new Student();
        student.setAddress("china");
        student.setEmail("434525082@qq.com");
        student.setId(1);
        student.setName("qinqin");
        student.setBirthday("1991-12-10");
    }
    
    /**
     * 释放资源
     */
    @After
    public void destroy() {
        xstream = null;
        student = null;
        try {
            if (inStream != null) {
                inStream.close();
            }
            if(outStream!=null){
                outStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * java对象转换为xml对象
     */
    @Test
    public void beanToXml(){
        try {
            info("java->xml");
            // <com.ljq.test.Student>
            //    <id>1</id>
            //    <name>ljq</name>
            //    <email>416501600@qq.com</email>
            //    <address>china</address>
            //    <birthday>1988-04-04</birthday>
            // </com.ljq.test.Student>
            info(xstream.toXML(student));
            info("重命名后的xml");
            //包重命名
            xstream.aliasPackage("com", "com.ljq.test"); //包名com.ljq.test记得写正确
            //<com.Student>
            //    <id>1</id>
            //    <name>ljq</name>
            //    <email>416501600@qq.com</email>
            //    <address>china</address>
            //    <birthday>1988-04-04</birthday>
            //</com.Student>
            info(xstream.toXML(student));
            //类重命名
            xstream.alias("Student", Student.class);
            //属性重命名
            xstream.aliasField("birth", Student.class, "birthday");
            //<Student>
            //    <id>1</id>
            //    <name>ljq</name>
            //    <email>416501600@qq.com</email>
            //    <address>china</address>
            //    <birth>1988-04-04</birth>
            //</Student>
            info(xstream.toXML(student));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * List集合对象转换成xml对象
     */
    @Test
    public void listToXml(){
        try{
            List<Student> students=new ArrayList<Student>();
            students.add(new Student(1, "zhangsan1", "zhangsan1@126.com", "china", "1988-04-04"));
            students.add(new Student(2, "zhangsan2", "zhangsan2@126.com", "china", "1988-04-04"));
            //id引用
            //xstream.setMode(XStream.ID_REFERENCES);
            //类重命名
            xstream.alias("Student", Student.class);
            //根元素重命名
            xstream.alias("beans", List.class);
            //将id、name设置为父类(Student)的元素的属性
            xstream.useAttributeFor(Student.class, "id");
            xstream.useAttributeFor(Student.class, "name");
            //<beans>
            //<Student id="1" name="zhangsan1">
            //    <email>zhangsan1@126.com</email>
            //    <address>china</address>
            //    <birthday>1988-04-04</birthday>
            //</Student>
            //<Student id="2" name="zhangsan2">
            //    <email>zhangsan2@126.com</email>
            //    <address>china</address>
            //    <birthday>1988-04-04</birthday>
            // </Student>
            //</beans>
            info(xstream.toXML(students));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 通过注解把java对象转换为xml对象
     */
    @Test
    public void javaToXmlByAnnotation() {
        Student student = new Student();
        student.setName("jack");
        Classes classes = new Classes("初中1班", student, Calendar.getInstance());
        classes.setNumber(2);
        //对指定的类使用Annotation
        xstream.processAnnotations(Classes.class);
        //启用Annotation
        xstream.autodetectAnnotations(true);
        xstream.alias("student", Student.class);
        xstream.alias("classes", Classes.class);
        //<classes 名称="初中1班">
        //   <Students>
        //      <id>0</id>
        //      <name>jack</name>
        //   </Students>
        //   <created>1329880816089</created>
        //</classes>
        info(xstream.toXML(classes));
    }

    /**
     * Map集合转换xml文档 
     */
    @Test
    public void mapToXml() {
        Map<String, Student> mapStudent=new HashMap<String, Student>();
        Student student=new Student();
        mapStudent.put("No.1", student);
        
        student = new Student();
        student.setAddress("china");
        student.setEmail("tom@125.com");
        student.setId(2);
        student.setName("tom");
        student.setBirthday("1988-04-04");
        mapStudent.put("No.2", student);

        student = new Student();
        student.setName("jack");
        mapStudent.put("No.3", student);
        
        xstream.alias("student", Student.class);
        xstream.alias("key", String.class);
        xstream.useAttributeFor(Student.class, "id");
        xstream.useAttributeFor("birthday", String.class);
        //<map>
        //<entry>
        //    <key>No.1</key>
        //    <student id="0"/>
        //</entry>
        //<entry>
        //    <key>No.3</key>
        //    <student id="0">
        //       <name>jack</name>
        //    </student>
        //</entry>
        //<entry>
        // <key>No.2</key>
        //    <student id="2" birthday="1988-04-04">
        //       <name>tom</name>
        //       <email>tom@125.com</email>
        //       <address>china</address>
        //    </student>
        //</entry>
        //</map>
        info(xstream.toXML(mapStudent));
    }

    /**
     * 用OutStream输出流写XML
     */
    @Test
    public void writeXMLByOutStream() {
        try {
            //<object-stream>
            //  <com.ljq.test.Student>
            //      <id>0</id>
            //      <name>jack</name>
            //  </com.ljq.test.Student>
            //  <byte>22</byte>
            //  <boolean>true</boolean>
            //  <float>22.0</float>
            //  <string>hello</string>
            //</object-stream>
            //把结果输出到控制台
            outStream = xstream.createObjectOutputStream(System.out);
            Student stu = new Student();
            stu.setName("jack");
            Classes classes = new Classes("初中1班", student, Calendar.getInstance());
            classes.setNumber(2);
            outStream.writeObject(stu);
            outStream.write(22);//byte
            outStream.writeBoolean(true);
            outStream.writeFloat(22.f);
            outStream.writeUTF("hello");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用InputStream将XML文档转换成java对象
     */
    @Test
    public void readXmlByInputStream() {
        try {
            String s = 
                "<object-stream>" +
                    "<com.qinqin.entity.Student>" +
                        "<id>0</id>" +
                        "<name>jack</name>"+ 
                    "</com.qinqin.entity.Student>"+ 
                    "<byte>22</byte>" +
                    "<boolean>true</boolean>" +
                    "<float>22.0</float>"+ 
                    "<string>hello</string>" +
               "</object-stream>";
            StringReader reader = new StringReader(s);
            inStream = xstream.createObjectInputStream(reader);
            Student stu = (Student) inStream.readObject();
            byte i = inStream.readByte();
            boolean bo = inStream.readBoolean();
            float f = inStream.readFloat();
            String str = inStream.readUTF();
            System.out.println(stu);
            System.out.println(i);
            System.out.println(bo);
            System.out.println(f);
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 将xml文档转换成Java对象 
     */
    @Test
    public void xmlByJava() {
        info("------xml>>java------");
        Student stu = (Student) xstream.fromXML(xstream.toXML(student));
        //ljq#1#china#1988-04-04#416501600@qq.com
        info(stu.toString());

        List<Student> list = new ArrayList<Student>();
        list.add(stu);
        list.add(student);
        
        info("------xml>>List------");
        List<Student> studetns = (List<Student>) xstream.fromXML(xstream.toXML(list));
        info("size:" + studetns.size());
        //ljq#1#china#1988-04-04#416501600@qq.com
        //ljq#1#china#1988-04-04#416501600@qq.com
        for (Student s : studetns) {
            info(s.toString());
        }
        
        Map<String, Student> map = new HashMap<String, Student>();
        map.put("No.1", student);

        student = new Student();
        student.setAddress("china");
        student.setEmail("tom@125.com");
        student.setId(2);
        student.setName("tom");
        student.setBirthday("1988-04-04");
        list.add(student);
        map.put("No.2", student);
        
        student = new Student();
        student.setName("jack");
        list.add(student);
        map.put("No.3", student);
        
        info("------xml>>Map------");
        Map<String, Student> maps = (Map<String, Student>) xstream.fromXML(xstream.toXML(map));
        info("size:" + maps.size());
        Set<String> key = maps.keySet();
        Iterator<String> iter = key.iterator();
        //No.1:ljq#1#china#1988-04-04#416501600@qq.com
        //No.3:jack#0#null#null#null
        //No.2:tom#2#china#1988-04-04#tom@125.com
        while (iter.hasNext()) {
            String k = iter.next();
            info(k + ":" + map.get(k));
        }
    }
    
    /**
     * 用JettisonMappedXmlDriver完成Java对象到JSON的转换
     */
    @Test
    public void javaToJsonByJettisonMappedXmlDriver(){
        xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("student", Student.class);
        //{"student":{"id":"1","name":"ljq","email":"416501600@qq.com","address":"china","birthday":"1988-04-04"}}
        info(xstream.toXML(student));
    }
    
    /**
     * JsonHierarchicalStreamDriver完成Java对象到JSON的转换 
     */
    @Test
    public void javaToJsonByJsonHierarchicalStreamDriver(){
        xstream = new XStream(new JsonHierarchicalStreamDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("student", Student.class);
        //{"student": {
        //      "id": 1,
        //      "name": "ljq",
        //      "email": "416501600@qq.com",
        //      "address": "china",
        //      "birthday": "1988-04-04"
        //    }
        //}
        info(xstream.toXML(student));
        
        //删除根节点
        xstream = new XStream(new JsonHierarchicalStreamDriver() {
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new JsonWriter(out, JsonWriter.DROP_ROOT_MODE);
            }
        });
        xstream.alias("student", Student.class);
        //{
        //      "id": 1,
        //      "name": "ljq",
        //      "email": "416501600@qq.com",
        //      "address": "china",
        //      "birthday": "1988-04-04"
        //    }
        info(xstream.toXML(student));
    }
    
    /**
     * 将List集合转换成JSON字符串
     */
    @Test
    public void list2JSON() {
        JsonHierarchicalStreamDriver driver = new JsonHierarchicalStreamDriver();
        xstream = new XStream(driver);
        //xstream = new XStream(new JettisonMappedXmlDriver());//转换错误
        //xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("student", Student.class);
        
        List<Student> list = new ArrayList<Student>();
        list.add(student);
        
        student = new Student();
        student.setAddress("china");
        student.setEmail("tom@125.com");
        student.setId(2);
        student.setName("tom");
        student.setBirthday("1988-04-04");
        list.add(student);
        
        student = new Student();
        student.setName("jack");
        list.add(student);
        //{"list": [
        //          {
        //            "id": 1,
        //            "name": "ljq",
        //            "email": "416501600@qq.com",
        //            "address": "china",
        //            "birthday": "1988-04-04"
        //          },
        //          {
        //            "id": 2,
        //            "name": "tom",
        //            "email": "tom@125.com",
        //            "address": "china",
        //            "birthday": "1988-04-04"
        //          },
        //          {
        //            "id": 0,
        //           "name": "jack"
        //          }
        //        ]}
        info(xstream.toXML(list));
        
        info("---------------------");
        //删除根节点
        xstream = new XStream(new JsonHierarchicalStreamDriver() {
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new JsonWriter(out, JsonWriter.DROP_ROOT_MODE);
            }
        });
        xstream.alias("student", Student.class);
        //[
        // {
        //   "id": 1,
        //   "name": "ljq",
        //   "email": "416501600@qq.com",
        //   "address": "china",
        //   "birthday": "1988-04-04"
        // },
        // {
        //   "id": 2,
        //   "name": "tom",
        //   "email": "tom@125.com",
        //   "address": "china",
        //   "birthday": "1988-04-04"
        // },
        //{
        //   "id": 0,
        //   "name": "jack"
        //}
        //]
        info(xstream.toXML(list));
    }
    
    /**
     * Map转换json 
     */
    @Test
    public void map2JSON() {
        xstream = new XStream(new JsonHierarchicalStreamDriver());
        //xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.alias("student", Student.class);
        
        Map<String, Student> map = new HashMap<String, Student>();
        map.put("No.1", student);
        
        student = new Student();
        student.setAddress("china");
        student.setEmail("tom@125.com");
        student.setId(2);
        student.setName("tom");
        student.setBirthday("2010-11-21");
        map.put("No.2", student);
        
        student = new Student();
        student.setName("jack");
        map.put("No.3", student);
//        {"map": [
//                 [
//                   "No.1",
//                   {
//                     "id": 1,
//                     "name": "ljq",
//                     "email": "416501600@qq.com",
//                     "address": "china",
//                     "birthday": "1988-04-04"
//                   }
//                 ],
//                 [
//                   "No.3",
//                   {
//                     "id": 0,
//                     "name": "jack"
//                   }
//                 ],
//                 [
//                   "No.2",
//                   {
//                     "id": 2,
//                     "name": "tom",
//                     "email": "tom@125.com",
//                     "address": "china",
//                     "birthday": "2010-11-21"
//                   }
//                 ]
//               ]}
        info(xstream.toXML(map));
        info("---------------------");
        
        //删除根节点
        xstream = new XStream(new JsonHierarchicalStreamDriver() {
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new JsonWriter(out, JsonWriter.DROP_ROOT_MODE);
            }
        });
        
        xstream.alias("student", Student.class);
//        [
//         [
//           "No.1",
//           {
//             "id": 1,
//             "name": "ljq",
//             "email": "416501600@qq.com",
//             "address": "china",
//             "birthday": "1988-04-04"
//           }
//         ],
//         [
//           "No.3",
//           {
//             "id": 0,
//             "name": "jack"
//           }
//         ],
//         [
//           "No.2",
//           {
//             "id": 2,
//             "name": "tom",
//             "email": "tom@125.com",
//             "address": "china",
//             "birthday": "2010-11-21"
//           }
//         ]
//       ]
        info(xstream.toXML(map));
    }

    /**
     * 将JSON转换java对象 
     * 
     * @throws JSONException
     */
    @Test
    public void jsonToObject(){
        String json = 
        "{\"student\": " +
           "{" + "\"id\": 1," 
               + "\"name\": \"haha\","
               + "\"email\": \"email\","
               + "\"address\": \"address\","
               + "\"birthday\": \"2010-11-22\"" + "}"+ 
           "}" +
        "}";

        //JsonHierarchicalStreamDriver读取JSON字符串到java对象出错，但JettisonMappedXmlDriver可以
        xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.alias("student", Student.class);
        info(xstream.fromXML(json).toString());
        
        //JettisonMappedXmlDriver转换List集合出错，但JsonHierarchicalStreamDriver可以转换正确
        json = "{\"list\": ["+
          "{"+
            "\"id\": 1,"+
            "\"name\": \"ljq\","+
            "\"email\": \"416501600@qq.com\","+
            "\"address\": \"china\","+
            "\"birthday\": \"1988-04-04\""+
          "},"+
          "{"+
            "\"id\": 2,"+
            "\"name\": \"tom\","+
            "\"email\": \"tom@125.com\","+
            "\"address\": \"china\","+
            "\"birthday\": \"1988-04-04\""+
          "},"+
          "{"+
            "\"id\": 0,"+
            "\"name\": \"jack\""+
          "}"+
        "]}";
        //xstream = new XStream(new JsonHierarchicalStreamDriver()); //会报错
        List<Student> list = (List<Student>) xstream.fromXML(json);
        System.out.println(list.size());//0好像转换失败

    }
    
    public final void info(String string) {
        System.out.println(string);
    }
    
    public final void error(String string) {
        System.err.println(string);
    }

}
