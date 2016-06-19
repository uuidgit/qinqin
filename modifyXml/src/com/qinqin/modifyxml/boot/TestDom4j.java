package com.qinqin.modifyxml.boot;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class TestDom4j {
	 public static void main(String[] args) throws Exception{
	        SAXReader saxReader = new SAXReader();
//	        Document document = saxReader.read(new File("D:" + File.separator + "Vtree-scan2.xml"));
	        Document document = saxReader.read(new File("build.xml"));
	        Element root=document.getRootElement();
	        list(root);
	    }
	    public static void list(Element node) {
	        List<Attribute> list = node.attributes();
	        for (Attribute attribute : list) {
	            System.out.println(attribute.getText());
	        }
//	        Iterator<Element> it = node.elementIterator();
//	        while (it.hasNext()) {
//	            Element e = it.next();
//	            System.out.println(e.getStringValue());
//	            list(e);
//	        }
	    }

	
}