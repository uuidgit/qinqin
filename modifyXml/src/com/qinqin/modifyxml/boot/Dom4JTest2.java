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
 * dom4j���ѧϰ�� ��ȡ������xml
 * 
 * 
 */
public class Dom4JTest2 {
	public static void main(String[] args) throws Exception {
		SAXReader saxReader = new SAXReader();

		Document document = saxReader.read(new File("build.xml"));

		// ��ȡ��Ԫ��
		Element root = document.getRootElement();
		System.out.println("Root: " + root.getName());

		// ��ȡ������Ԫ��
		List<Element> childList = root.elements();
		System.out.println("total child count: " + childList.size());

		// ��ȡ�ض����Ƶ���Ԫ��
		List<Element> childList2 = root.elements("userId");
		System.out.println("hello child: " + childList2.size());

		// ��ȡ����Ϊָ�����Ƶĵ�һ����Ԫ��
		Element firstWorldElement = root.element("world");
		// ���������
		// System.out.println("first World Attr: "
		// + firstWorldElement.attribute(0).getName() + "="
		// + firstWorldElement.attributeValue("name"));

		System.out.println("�������-----------------------");
		Iterator iter = root.elementIterator();
		doRead(childList);
		// �������
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

		System.out.println("��DOMReader-----------------------");
		// DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// DocumentBuilder db = dbf.newDocumentBuilder();
		// // ע��Ҫ����������
		// org.w3c.dom.Document document2 = db.parse(new File("build.xml "));
		//
		// DOMReader domReader = new DOMReader();
		//
		// // ��JAXP��Documentת��Ϊdom4j��Document
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
