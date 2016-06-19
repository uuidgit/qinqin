package com.qinqin.modifyxml.boot;

import java.io.File;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Demo {
	public static void main(String[] args) throws Exception {
		SAXReader reader = new SAXReader();
		File file = new File("build.xml");
		Document document = reader.read(file);
		Element root = document.getRootElement();
//		System.out.println(root.getStringValue());
		List<Element> childElements = root.elements();
		for (Element child : childElements) {
			System.out.println(child.getName()+":"+child.getStringValue());
			
			
			// δ֪�����������
		/*	List<Attribute> attributeList = child.attributes();
			for (Attribute attr : attributeList) {
				System.out.println(attr.getName() + ": " + attr.getValue());
				System.out.println();
			}
*/
			// ��֪�����������
			// System.out.println("id: " + child.attributeValue("id"));

			// δ֪��Ԫ���������
			/*
			 * List<Element> elementList = child.elements(); for (Element ele :
			 * elementList) { System.out.println(ele.getName() + ": " +
			 * ele.getText()); } System.out.println();
			 */

			// ��֪��Ԫ�����������
			// System.out.println("title" + child.elementText("title"));
			// System.out.println("author" + child.elementText("author"));
			// ������Ϊ�˸�ʽ�����۶�����
			System.out.println();
		}
	}
}