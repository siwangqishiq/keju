package com.xinlan.model;

/** 
 * �Ծ���
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
	private int paperId;//�Ծ���
	private String title;//�Ծ����
	private int subjectNum;//�Ծ���Ŀ����
	private int flag;//��־λ
	private int status;//��ǰ״̬
	private String extern;//�����־
	
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
