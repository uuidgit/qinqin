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
     * ��xmlת��Ϊjava����
     */
    public static void xmlToJavaBean() {
        XStream stream = new XStream(new DomDriver()); 
        String xml = "book.xml";
        try {
            //���ýڵ��Ӧ��ʵ����
            stream.alias("book", Book.class);
            stream.alias("books", Books.class);     
            // ����XML���������Ķ�����Books���󣬸��ڵ�books��ӦBooks�࣬book�ڵ��ӦBook�࣬�ж��book�ڵ㣬������ת����������
            stream.addImplicitCollection(Books.class, "books");
            //��XML������Books����
            Books books = (Books) stream.fromXML(new FileReader(new File(xml)));
            ArrayList<Book> bookList = books.getBooks();
            for (int i = 0; i < bookList.size(); i++) {
                Book book = (Book) bookList.get(i);
            //��ӡʵ����
            System.out.println("name��" + book.getName() + "��" + "author:"+ book.getAuthor() + "��" + "date:" + book.getDate());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
