package com.chat.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * login : 1
 * client message : 2
 * server message : 3
 * user list: 4
 * close client window: 5
 * close server window: 6
 * close client window confirmation: 7
 * login result: 8
 *
 */


public class XMLUtil
{
	private static Document constructDocument()
	{
		Document document = DocumentHelper.createDocument();
		Element root = DocumentHelper.createElement("message");
		
		document.setRootElement(root);
		
		return document;
	}
	
	
	/**
	 * 客户端登录时向服务器端发送的XML数据
	 * @return
	 */
	//构造类型type为“1”的  ，“user”为username的document
	public static String constructLoginXML(String username)
	{
		Document document = constructDocument();
		
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("1");
		
		Element user = root.addElement("user");
		user.setText(username);
		
		return document.asXML();    //返回格式为XML的字符串信息

	}
	
	/**
	 * 从客户端登录所发送的XML数据中解析出用户名（username）
	 */
	public static String extractUsername(String xml)
	{
		try
		{
			SAXReader saxReader = new SAXReader();
			
			Document document = saxReader.read(new StringReader(xml));
			
			Element user = document.getRootElement().element("user");
			
			return user.getText();
		}
		catch(Exception ex)
		{
			
		}
		
		return null;
	}
	
	/**
	 * 构造向客户端发送的在线用户列表xml数据
	 */
	
	public static String constructUserList(Set<String> users)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("4");
		
		for(String user : users)
		{
			Element e = root.addElement("user");
			e.setText(user);
		}
		
		return document.asXML();
	}
	
	/**
	 * 从XML信息中提取出所有的在线用户列表信息
	 */
	public static List<String> extractUserList(String xml)
	{
		List<String> list = new ArrayList<String>();  						//泛型，new一个Arraylist
		
		try
		{
			SAXReader saxReader = new SAXReader();
			
			Document document = saxReader.read(new StringReader(xml));
			
			for(Iterator iter = document.getRootElement().elementIterator("user"); iter.hasNext();)    		//从根节点遍历XML    document.getrootElement()
			{
				Element e = (Element)iter.next();
				
				list.add(e.getText());                                                                          //值添加到list中去
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return list;                                                                                           //返回list
	}
	
	/**
	 *  从xml消息中解析出类型值
	 */
	public static String extractType(String xml)
	{
		try
		{
			SAXReader saxReader = new SAXReader();
			
			Document document = saxReader.read(new StringReader(xml));
			
			Element typeElement = document.getRootElement().element("type");
			
			return typeElement.getText();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 
	 * 构造客户端向服务器端发送的聊天数据xml
	 * 
	 * @param username
	 * @param message
	 * @return
	 */
	public static String constructMessageXML(String username, String message)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("2");
		
		Element user = root.addElement("user");
		user.setText(username);
		
		Element content = root.addElement("content");
		content.setText(message);
		
		return document.asXML();
	}
	
	/**
	 * 构造服务器端向所有客户端发送的XML聊天数据
	 *
	 */
    public static String constructServerMessageXML(String message)
    {
        Document document = constructDocument();
        Element root = document.getRootElement();

        Element type = root.addElement("type");
        type.setText("3");

        Element content = root.addElement("content");
        content.setText(message);

        return document.asXML();
    }




    /**
	 * 从客户端向服务器端发送的XML聊天数据中解析出聊天内容
	 */
	public static String extractContent(String xml)
	{
		try
		{
			SAXReader saxReader = new SAXReader();
			
			Document document = saxReader.read(new StringReader(xml));
			
			Element contentElement = document.getRootElement().element("content");                   //返回content元素的值
			
			return contentElement.getText();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 构造服务器端窗口关闭的XML数据
	 * 
	 */
	public static String constructCloseServerWindowXML()
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("6");
		
		return document.asXML();
	}
	
	/**
	 * 构造客户端窗口关闭的XML数据
	 */
	public static String constructCloseClientWindowXML(String username)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("5");
		
		Element user = root.addElement("user");
		user.setText(username);
		
		return document.asXML();
	}
	
	/**
	 * 构造服务器端确认客户端关闭的XML信息
	 */
	
	public static String constructCloseClientWindowConfirmationXML()
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("7");
		
		return document.asXML();
	}
	
	/**
	 * 构造向客户端返回的登录结果XML
	 */
	public static String constructLoginResultXML(String result)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element typeElement = root.addElement("type");
		typeElement.setText("8");
		
		Element resultElement = root.addElement("result");
		resultElement.setText(result);
		
		return document.asXML();
	}
	
	/**
	 * 从xml数据中解析出登录结果
	 */
	
	public static String extractLoginResult(String xml)
	{
		String result = null;
		
		try
		{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			
			Element root = document.getRootElement();      //获得根节点
			
			result = root.element("result").getText();     //返回子节点“result”的值
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return result;
	}
	
}