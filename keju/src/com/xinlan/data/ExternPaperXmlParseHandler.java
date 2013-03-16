package com.xinlan.data;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.xinlan.model.ExternPaper;
import com.xinlan.model.Paper;

public class ExternPaperXmlParseHandler extends DefaultHandler
{
	private ArrayList<ExternPaper> list;
	private String tagName;
	private ExternPaper paper;
	
	public ExternPaperXmlParseHandler(ArrayList<ExternPaper> list)
	{
		this.list=list;
		tagName="";
		paper=null;
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		String str=new String(ch,start,length);
		if(tagName.equals("id"))
		{
			paper.setPaperId(Integer.parseInt(str));
		}
		else if(tagName.equals("title"))
		{
			paper.setTitle(str);
		}
		else if(tagName.equals("num"))
		{
			paper.setSubjectNum(Integer.parseInt(str));
		}
		else if(tagName.equals("filename"))
		{
			paper.setFilePath(str);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException
	{
		tagName="";
		if("paper".equals(localName))
		{
			paper.setIsLoad(0);
			list.add(paper);//∑≈»ÎList÷–
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		tagName=localName;
		if("paper".equals(localName))
		{
			paper=new ExternPaper();
		}
	}
}
