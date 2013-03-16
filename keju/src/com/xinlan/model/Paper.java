package com.xinlan.model;

/** 
 * 试卷类
 * @author Geass
 *
 */
public class Paper
{
	@Override
	public String toString()
	{
		return "Paper [id=" + id + ", paperId=" + paperId + ", title=" + title
				+ ", subjectNum=" + subjectNum + ", flag=" + flag + ", status="
				+ status + ", extern=" + extern + "]";
	}
	private int id;
	private int paperId;//试卷编号
	private String title;//试卷标题
	private int subjectNum;//试卷题目总数
	private int flag;//标志位
	private int status;//当前状态
	private String extern;//额外标志
	
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
	public int getFlag()
	{
		return flag;
	}
	public void setFlag(int flag)
	{
		this.flag = flag;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public String getExtern()
	{
		return extern;
	}
	public void setExtern(String extern)
	{
		this.extern = extern;
	}
	
}//end class
