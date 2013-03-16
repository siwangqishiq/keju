package com.xinlan.data;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.xinlan.model.Item;

public class ItemsXmlParseHandler extends DefaultHandler
{
	private ArrayList<Item> list;
	private String tagName;
	private Item item;
	private int itemType;
	
	public ItemsXmlParseHandler(ArrayList<Item> list)
	{
		this.list=list;
		tagName="";
		item=null;
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		String str=new String(ch,start,length);
		if(tagName.equals("id"))
		{
			itemType=Integer.parseInt(str);
		}
		else if(tagName.equals("itemNumber"))
		{
			item.setItemNumber(Integer.parseInt(str));
		}
		else if(tagName.equals("itemTitle"))
		{
			item.setItemTitle(str);
		}
		else if(tagName.equals("sectionA"))
		{
			item.setSectionA(str);
		}
		else if(tagName.equals("sectionB"))
		{
			item.setSectionB(str);
		}
		else if(tagName.equals("sectionC"))
		{
			item.setSectionC(str);
		}
		else if(tagName.equals("sectionD"))
		{
			item.setSectionD(str);
		}
		else if(tagName.equals("answer"))
		{
			item.setAnswer(str);
		}
		else if(tagName.equals("extern"))
		{
			item.setExtern(str);
		}
		else if(tagName.equals("answerDesc"))
		{
			item.setAnswerDesc(str);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException
	{
		tagName="";
		if("item".equals(localName))
		{
			item.setItemType(itemType);
			item.setFlag(1);
			list.add(item);
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		tagName=localName;
		if("item".equals(localName))
		{
			item=new Item();
		}
	}
	
}//end class
