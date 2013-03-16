package com.xinlan.data;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.xinlan.model.Item;
import com.xinlan.model.Paper;

/**
 * Panyi潘易
 * @author Administrator
 *
 */
public class ImportsXmlParseHandler extends DefaultHandler {
	private ArrayList<Paper> paperList;
	private ArrayList<Item> itemList;
	private String tagName;
	private Paper paper;
	private Item item;
	private int itemType;

	public ImportsXmlParseHandler(ArrayList<Paper> paperList,
			ArrayList<Item> itemList) {
		this.paperList = paperList;
		this.itemList = itemList;
		tagName = "";
		item = null;
		paper = null;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String str = new String(ch, start, length);
		if (tagName.equals("id")) {
			paper.setPaperId(Integer.parseInt(str));
		} else if (tagName.equals("title")) {
			paper.setTitle(str);
		} else if (tagName.equals("num")) {
			paper.setSubjectNum(Integer.parseInt(str));
		} else if (tagName.equals("id")) {
			itemType = Integer.parseInt(str);
			item.setItemType(itemType);
		} else if (tagName.equals("itemNumber")) {
			item.setItemNumber(Integer.parseInt(str));
		} else if (tagName.equals("itemTitle")) {
			item.setItemTitle(str);
		} else if (tagName.equals("sectionA")) {
			item.setSectionA(str);
		} else if (tagName.equals("sectionB")) {
			item.setSectionB(str);
		} else if (tagName.equals("sectionC")) {
			item.setSectionC(str);
		} else if (tagName.equals("sectionD")) {
			item.setSectionD(str);
		} else if (tagName.equals("answer")) {
			item.setAnswer(str);
		} else if (tagName.equals("extern")) {
			item.setExtern(str);
		} else if (tagName.equals("answerDesc")) {
			item.setAnswerDesc(str);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		tagName = "";
		if ("item".equals(localName)) {
			item.setFlag(1);
			itemList.add(item);
		} else if ("paper".equals(localName)) {
			paper.setExtern("");
			paper.setFlag(1);
			paper.setStatus(1);
			paperList.add(paper);
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		tagName = localName;
		if ("item".equals(localName)) {
			item = new Item();
		} else if ("paper".equals(localName)) {
			paper = new Paper();
		}
	}

}// end class
