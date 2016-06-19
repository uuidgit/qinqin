package com.qinqin.modifyxml.boot;

import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * dom4j框架学习： 读取并解析xml
 * 
 * 
 */
public class Dom4JTest2 {
	public static void main(String[] args) throws Exception {
		SAXReader saxReader = new SAXReader();

		Document document = saxReader.read(new File("build.xml"));

		// 获取根元素
		Element root = document.getRootElement();
		System.out.println("Root: " + root.getName());

		// 获取所有子元素
		List<Element> childList = root.elements();
		System.out.println("total child count: " + childList.size());

		// 获取特定名称的子元素
		List<Element> childList2 = root.elements("userId");
		System.out.println("hello child: " + childList2.size());

		// 获取名字为指定名称的第一个子元素
		Element firstWorldElement = root.element("world");
		// 输出其属性
		// System.out.println("first World Attr: "
		// + firstWorldElement.attribute(0).getName() + "="
		// + firstWorldElement.attributeValue("name"));

		System.out.println("迭代输出-----------------------");
		Iterator iter = root.elementIterator();
		doRead(childList);
		// 迭代输出
		// for (Iterator iter = root.elementIterator(); iter.hasNext();)
		// {
		// Element e = (Element) iter.next();
		// Iterator it = e.elementIterator();
		//
		// if () {
		// System.out.println(e.getName()+":"+e.getStringValue());
		// }
		//
		// // iter = e.elementIterator();
		// }

		System.out.println("用DOMReader-----------------------");
		// DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// DocumentBuilder db = dbf.newDocumentBuilder();
		// // 注意要用完整类名
		// org.w3c.dom.Document document2 = db.parse(new File("build.xml "));
		//
		// DOMReader domReader = new DOMReader();
		//
		// // 将JAXP的Document转换为dom4j的Document
		// Document document3 = domReader.read(document2);
		//
		// Element rootElement = document3.getRootElement();
		//
		// System.out.println("Root: " + rootElement.getName());

	}

	public static void doRead(List<Element> list) {

		for (Element e : list) {
			List<Element> childs = e.elements();
			if (childs == null || childs.isEmpty()) {
				String value = e.getStringValue();
				if (null != value && !value.trim().equals("")) {
					if (e.getName().equals("userId")) {
						System.out.println(e.getName() + ":"
								+ e.getStringValue());
					}
					if (e.getName().equals("url")) {
						System.out.println(e.getName() + ":"
								+ e.getStringValue());
					}
					if (e.getName().equals("revision")) {
						System.out.println(e.getName() + ":"
								+ e.getStringValue());
					}
					if (e.getName().equals("queueId")) {
						System.out.println(e.getName() + ":"
								+ e.getStringValue());
					}
					if (e.getName().equals("timestamp")) {
						DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
						Timestamp ts = new Timestamp(Long.parseLong(e.getStringValue()));
						System.out.println(e.getName() + ":"
								+ sdf.format(ts));
					}
					if (e.getName().equals("startTime")) {
						DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
						Timestamp ts = new Timestamp(Long.parseLong(e.getStringValue()));
						System.out.println(e.getName() + ":"
								+ sdf.format(ts));
					}
					
					if (e.getName().equals("description")) {
						System.out.println(e.getName() + ":"
								+ e.getStringValue());
					}
					if (e.getName().equals("duration")) {
						System.out.println(e.getName() + ":"
								+ e.getStringValue());
					}
					if (e.getName().equals("keepLog")) {
						System.out.println(e.getName() + ":"
								+ e.getStringValue());
					}
					if (e.getName().equals("workspace")) {
						System.out.println(e.getName() + ":"
								+ e.getStringValue());
					}
				}
			} else {
				if (e.getName().equals("culprits")) {
					List<Element> culprits = e.elements();
					for (Element i : culprits) {
						if (childs != null && !childs.isEmpty()) {
							System.out.println(i.getName()+"==="+i.getStringValue());
						}
					}
				}
				doRead(childs);
			}
		}

	}
}
