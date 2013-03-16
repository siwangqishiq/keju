package com.xinlan.model;

/**
 * @author Geass
 *
 */
public class Item
{
	private int id;
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public int getItemType()
	{
		return itemType;
	}
	public void setItemType(int itemType)
	{
		this.itemType = itemType;
	}
	@Override
	public String toString()
	{
		return "Item [id=" + id + ", itemType=" + itemType + ", itemNumber="
				+ itemNumber + ", itemTitle=" + itemTitle + ", sectionA="
				+ sectionA + ", sectionB=" + sectionB + ", sectionC="
				+ sectionC + ", sectionD=" + sectionD + ", answer=" + answer
				+ ", answerDesc=" + answerDesc + ", flag=" + flag + ", extern="
				+ extern + "]";
	}
	public int getItemNumber()
	{
		return itemNumber;
	}
	public void setItemNumber(int itemNumber)
	{
		this.itemNumber = itemNumber;
	}
	public String getItemTitle()
	{
		return itemTitle;
	}
	public void setItemTitle(String itemTitle)
	{
		this.itemTitle = itemTitle;
	}
	public String getSectionA()
	{
		return sectionA;
	}
	public void setSectionA(String sectionA)
	{
		this.sectionA = sectionA;
	}
	public String getSectionB()
	{
		return sectionB;
	}
	public void setSectionB(String sectionB)
	{
		this.sectionB = sectionB;
	}
	public String getSectionC()
	{
		return sectionC;
	}
	public void setSectionC(String sectionC)
	{
		this.sectionC = sectionC;
	}
	public String getSectionD()
	{
		return sectionD;
	}
	public void setSectionD(String sectionD)
	{
		this.sectionD = sectionD;
	}
	public String getAnswer()
	{
		return answer;
	}
	public void setAnswer(String answer)
	{
		this.answer = answer;
	}
	public String getAnswerDesc()
	{
		return answerDesc;
	}
	public void setAnswerDesc(String answerDesc)
	{
		this.answerDesc = answerDesc;
	}
	public int getFlag()
	{
		return flag;
	}
	public void setFlag(int flag)
	{
		this.flag = flag;
	}
	public String getExtern()
	{
		return extern;
	}
	public void setExtern(String extern)
	{
		this.extern = extern;
	}
	private int itemType;
	private int itemNumber;
	private String itemTitle;
	private String sectionA;
	private String sectionB;
	private String sectionC;
	private String sectionD;
	private String answer;
	private String answerDesc;
	private int flag;
	private String extern;
}
