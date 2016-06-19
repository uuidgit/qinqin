package com.qinqin.xmlParse;

import java.io.File;
import java.util.Scanner;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * xml增刪改查操作
 * 
 * @author qinqin11460
 *
 */

public class XmlManager {
	// 定义需要解析xml文件路径
	private static String xmlPath = "test.xml";

	/**
	 * @param nodeParam
	 *            获取指定节点下的节点
	 */
	public static void getAllNodes(String nodeParam, String nodeAttr) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setIgnoringElementContentWhitespace(true);
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xmlPath); // 使用dom解析xml文件

			NodeList nodelist = doc.getElementsByTagName(nodeParam);

			for (int i = 0; i < nodelist.getLength(); i++) // 循环处理对象
			{
				Element son = (Element) nodelist.item(i);
				;

				for (Node node = son.getFirstChild(); node != null; node = node
						.getNextSibling()) {
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						String name = node.getNodeName();
						String value = node.getFirstChild().getNodeValue();
						if (name.equals(nodeAttr)) {
							System.out.println(name + " : " + value);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param nodeParam
	 *            节点参数
	 * @param attr
	 *            需要修改节点值的节点属性
	 * @param value
	 *            设置修改后的节点值
	 */
	public static void modifyNodeValue(String express, String attr, String value) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setIgnoringElementContentWhitespace(true);
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();
			Document xmldoc = db.parse(xmlPath);

			Element root = xmldoc.getDocumentElement();

			Element per = (Element) selectSingleNode(express, root);
			per.getElementsByTagName(attr).item(0).setTextContent(value);

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer former = factory.newTransformer();
			former.transform(new DOMSource(xmldoc), new StreamResult(new File(
					xmlPath)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param nodeParam
	 *            指定刪除node
	 */
	public static void discardNode(String express) {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setIgnoringElementContentWhitespace(true);

		try {

			DocumentBuilder db = dbf.newDocumentBuilder();
			Document xmldoc = db.parse(xmlPath);

			Element root = xmldoc.getDocumentElement();

			Element node = (Element) selectSingleNode(express, root);
			root.removeChild(node);

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer former = factory.newTransformer();
			former.transform(new DOMSource(xmldoc), new StreamResult(new File(
					xmlPath)));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createNode(String nodeString, String att1,
			String value1, String att2, String value2) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setIgnoringElementContentWhitespace(false);

		try {

			DocumentBuilder db = dbf.newDocumentBuilder();
			Document xmldoc = db.parse(xmlPath);

			Element root = xmldoc.getDocumentElement();

			// 删除指定节点

			Element node = xmldoc.createElement(nodeString);
			// son.setAttribute("id", "004");

			Element attribute1 = xmldoc.createElement(att1);
			attribute1.setTextContent(value1);
			node.appendChild(attribute1);

			Element attribute2 = xmldoc.createElement(att2);
			attribute2.setTextContent(value2);
			node.appendChild(attribute2);

			root.appendChild(node);
			// 保存
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer former = factory.newTransformer();
			former.transform(new DOMSource(xmldoc), new StreamResult(new File(
					xmlPath)));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param express
	 *            "/father/son[@id='001']"
	 * @param source
	 * @return
	 */
	public static Node selectSingleNode(String express, Element source) {
		Node result = null;
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		try {
			result = (Node) xpath
					.evaluate(express, source, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static void main(String[] args) {

//		if (args == null) {
//			Logger.getLogger(XmlManager.class.getName())
//					.info("Arguments errors,Please input xmlpath , express,nodename,nodevalue!");
//			return;
//		}
//
//		if (args.length != 5) {
//			for (int i = 0; i < args.length; i++) {
//				System.out.println("arg" + i + ":" + args[i]);
//			}
//			Logger.getLogger(XmlManager.class.getName()).info(
//					"args has been too many!!!");
//			return;
//		}

//		xmlPath = args[0];
//		String parent_node = args[1];
//		String child_node = args[2];
//		String node_express = args[3];
//		String node_value = args[4];
		
		String parent_node = "modules";
		String child_node = "module";
		String node_express = "/project/modules";
		String node_value = "hs-yun-service-online"; 

		for (int i = 0; i < args.length; i++) {
			System.out.println("arg" + i + ":" + args[i]);
		}

//		Scanner sc = new Scanner(System.in);
//		String ss = sc.nextLine();
//
//		while (true) {
//			sc = new Scanner(System.in);
//			ss = sc.nextLine();
//			break;
//		}

		getAllNodes(parent_node, child_node);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		modifyNodeValue(node_express, child_node, node_value);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		getAllNodes(parent_node, child_node);
		// System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		// discardSon();
		// System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		// getFamilyMemebers();
		// System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		// createSon();
		// System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		// getFamilyMemebers();
	}
}
