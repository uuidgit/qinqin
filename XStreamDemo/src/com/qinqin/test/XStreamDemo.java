package com.qinqin.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import com.qinqin.entity.Book;
import com.qinqin.entity.Books;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamDemo {
    public static void main(String[] args) {
        xmlToJavaBean();
    }

    /**
     * 把xml转化为java对象
     */
    public static void xmlToJavaBean() {
        XStream stream = new XStream(new DomDriver()); 
        String xml = "book.xml";
        try {
            //设置节点对应的实体类
            stream.alias("book", Book.class);
            stream.alias("books", Books.class);     
            // 设置XML解析出来的对象是Books对象，根节点books对应Books类，book节点对应Book类，有多个book节点，这里需转换到集合中
            stream.addImplicitCollection(Books.class, "books");
            //从XML解析出Books对象
            Books books = (Books) stream.fromXML(new FileReader(new File(xml)));
            ArrayList<Book> bookList = books.getBooks();
            for (int i = 0; i < bookList.size(); i++) {
                Book book = (Book) bookList.get(i);
            //打印实体类
            System.out.println("name：" + book.getName() + "，" + "author:"+ book.getAuthor() + "，" + "date:" + book.getDate());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
