package com.xinlan.model;

/**
 * ∂ÓÕ‚ ‘æÌ¡–±Ì
 * @author Geass
 *
 */
public class ExternPaper
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
	public int getPaperId()
	{
		return paperId;
	}
	public void setPaperId(int paperId)
	{
		this.paperId = paperId;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public int getSubjectNum()
	{
		return subjectNum;
	}
	public void setSubjectNum(int subjectNum)
	{
		this.subjectNum = subjectNum;
	}
	public int getIsLoad()
	{
		return isLoad;
	}
	public void setIsLoad(int isLoad)
	{
		this.isLoad = isLoad;
	}
	public String getFilePath()
	{
		return filePath;
	}
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}
	private int paperId;
	private String title;
	private int subjectNum;
	private int isLoad;
	private String filePath;
}//end class
