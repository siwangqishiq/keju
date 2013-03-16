package com.xinlan.data;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.xinlan.model.Paper;

public class PaperXmlParseHandler extends DefaultHandler
{
	private ArrayList<Paper> list;
	private String tagName;
	private Paper paper;
	
	public PaperXmlParseHandler(ArrayList<Paper> list)
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
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException
	{
		tagName="";
		if("paper".equals(localName))
		{
			paper.setExtern("");
			paper.setFlag(1);
			paper.setStatus(1);
			list.add(paper);
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		tagName=localName;
		if("paper".equals(localName))
		{
			paper=new Paper();
		}
	}
}
