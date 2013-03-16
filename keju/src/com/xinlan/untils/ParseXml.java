package com.xinlan.untils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.xinlan.data.ItemsXmlParseHandler;
import com.xinlan.model.Item;

/**
 * 解析xml文件
 * @author Geass
 *
 */
public class ParseXml 
{
	public static ArrayList<Item> parseXmlToItemList(String content)
	{
		ArrayList<Item> list=new ArrayList<Item>();
		SAXParserFactory factory=SAXParserFactory.newInstance();//产生一个SAXParser工厂
		try
		{
			XMLReader reader=factory.newSAXParser().getXMLReader();
			reader.setContentHandler(new ItemsXmlParseHandler(list));//设置内容处理器
			try
			{
				reader.parse(new InputSource(new StringReader(content)));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		} 
		catch (SAXException e)
		{
			e.printStackTrace();
		} 
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		return list;
	}
	
	
}
