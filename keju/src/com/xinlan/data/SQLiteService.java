package com.xinlan.data;


import java.util.ArrayList;

import com.xinlan.model.ExternPaper;
import com.xinlan.model.Item;
import com.xinlan.model.Paper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 提供对数据库的操作
 * @author Geass
 *
 */
public class SQLiteService 
{
	private SQLiteOpen sqliteOpen;
	
	public SQLiteService(Context context)
	{
		sqliteOpen=new SQLiteOpen(context,"kejudb", null,1);
		getReadableDatabase();
	}
	
	public SQLiteDatabase getWritableDatabase()
	{
		return  sqliteOpen.getWritableDatabase();
	}
	
	public SQLiteDatabase getReadableDatabase()
	{
		return sqliteOpen.getReadableDatabase();
	}
	
	/**
	 *   根据paperId以及numberId查询指定试题
	 * @param paperId
	 * @param numberId
	 * @return
	 */
	public Item queryItemByPaperAndNumber(int paperId,int numberId)
	{
		Item item=new Item();
		SQLiteDatabase db=getReadableDatabase();//读取数据库
		Cursor cursor=db.query("ITEMS",new String[]{"ID","ITEM_TYPE","ITEM_NUMBER","ITEM_TITLE",
				"SECTIONA","SECTIONB","SECTIONC","SECTIOND","ANSWER","FLAG","ANSWERDESC","EXTERN"}, 
				"ITEM_TYPE=? and ITEM_NUMBER=?",new String[]{paperId+"",numberId+""}, null, null,"1");
		cursor.moveToNext();
		item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
		item.setItemType(cursor.getInt(cursor.getColumnIndex("ITEM_TYPE")));
		item.setItemNumber(cursor.getInt(cursor.getColumnIndex("ITEM_NUMBER")));
		item.setItemTitle(cursor.getString(cursor.getColumnIndex("ITEM_TITLE")));
		item.setSectionA(cursor.getString(cursor.getColumnIndex("SECTIONA")));
		item.setSectionB(cursor.getString(cursor.getColumnIndex("SECTIONB")));
		item.setSectionC(cursor.getString(cursor.getColumnIndex("SECTIONC")));
		item.setSectionD(cursor.getString(cursor.getColumnIndex("SECTIOND")));
		item.setAnswer(cursor.getString(cursor.getColumnIndex("ANSWER")));
		item.setFlag(cursor.getInt(cursor.getColumnIndex("FLAG")));
		item.setAnswerDesc(cursor.getString(cursor.getColumnIndex("ANSWERDESC")));
		item.setExtern(cursor.getString(cursor.getColumnIndex("EXTERN")));
		cursor.close();
		db.close();
		return item;
	}
	
	public int getPapersCount()//查询试卷表中项目的条数
	{
		SQLiteDatabase db=getReadableDatabase();
		Cursor cursor=db.query("PAPERS",new String[]{"ID"}, null, null, null, null, null);
		int ret=cursor.getCount();
		cursor.close();
		db.close();
		return ret;
	}
	
	/**
	 * 获取指定试卷下题目总数
	 * @param paperId
	 * @return
	 */
	public int getPaperTotalItem(int paperId)
	{
		SQLiteDatabase db=getReadableDatabase();
		Cursor cursor=db.query("ITEMS",new String[]{"count(ID)"},"ITEM_TYPE=?",new String[]{paperId+""}, null, null,"1");
		cursor.moveToNext();
		int ret=cursor.getInt(cursor.getColumnIndex("count(ID)"));
		cursor.close();
		db.close();
		return ret;
	}
	
	/**
	 * 根据id查询试卷
	 * @param paperId
	 * @return
	 */
	public Paper queryPaperByPaperId(int paperId)
	{
		Paper paper=new Paper();
		SQLiteDatabase db=sqliteOpen.getReadableDatabase();
		Cursor cursor=db.query("PAPERS",new String[]{"ID","PAPER_ID","TITLE","SUBJECT_NUM","FLAG","STATUS","EXTERN"}, 
				"PAPER_ID=?", new String[]{paperId+""}, null, null,null,"1");
		while(cursor.moveToNext())
		{
			paper.setId(cursor.getInt(cursor.getColumnIndex("ID")));
			paper.setPaperId(cursor.getInt(cursor.getColumnIndex("PAPER_ID")));
			paper.setTitle(cursor.getString(cursor.getColumnIndex("TITLE")));
			paper.setSubjectNum(cursor.getInt(cursor.getColumnIndex("SUBJECT_NUM")));
			paper.setFlag(cursor.getInt(cursor.getColumnIndex("FLAG")));
			paper.setStatus(cursor.getInt(cursor.getColumnIndex("STATUS")));
			paper.setExtern(cursor.getString(cursor.getColumnIndex("EXTERN")));
		}//end while
		cursor.close();
		db.close();
		return paper;
	}
	
	public long insertPapers(Paper paper)//插入新的试卷
	{
		SQLiteDatabase db=sqliteOpen.getWritableDatabase();//写入数据库
		ContentValues values=new ContentValues();
		values.put("PAPER_ID", paper.getPaperId());
		values.put("TITLE", paper.getTitle());
		values.put("SUBJECT_NUM", paper.getSubjectNum());
		values.put("FLAG", paper.getFlag());
		values.put("STATUS", paper.getStatus());
		values.put("EXTERN", paper.getExtern());
		long ret=db.insert("PAPERS",null,values);
		db.close();
		return ret;
	}
	
	/**
	 * 插入试题
	 * @param item
	 * @return
	 */
	public long insertItems(Item item)
	{
		SQLiteDatabase db=sqliteOpen.getWritableDatabase();//写入数据库
		ContentValues values=new ContentValues();
		values.put("ITEM_TYPE",item.getItemType());
		values.put("ITEM_NUMBER",item.getItemNumber());
		values.put("ITEM_TITLE",item.getItemTitle());
		values.put("SECTIONA",item.getSectionA());
		values.put("SECTIONB",item.getSectionB());
		values.put("SECTIONC",item.getSectionC());
		values.put("SECTIOND",item.getSectionD());
		values.put("ANSWER",item.getAnswer());
		values.put("FLAG",item.getFlag());
		values.put("ANSWERDESC",item.getAnswerDesc());
		values.put("EXTERN",item.getExtern());
		long ret=db.insert("ITEMS",null,values);
		db.close();
		return ret;
	}
	
	public String queryPaperById()
	{
		StringBuffer sb=new StringBuffer();
		SQLiteDatabase db=sqliteOpen.getReadableDatabase();
		Cursor cursor=db.query("PAPERS", new String[]{"TITLE"},"PAPER_ID=1",null,null,null,null);
		cursor.moveToNext();
		sb.append(cursor.getString(cursor.getColumnIndex("TITLE")));
		cursor.close();
		db.close();
		return sb.toString();
	}
	
	
	/**
	 * 获取数据库中所有试卷列表
	 * @return
	 */
	public ArrayList<Paper> getAllPapersList()
	{
		ArrayList<Paper> list=new ArrayList<Paper>();
		SQLiteDatabase db=sqliteOpen.getReadableDatabase();
		Cursor cursor=db.query("PAPERS",new String[]{"PAPER_ID","TITLE","SUBJECT_NUM","STATUS"}, null,null,null,null,"PAPER_ID desc");
		while(cursor.moveToNext())
		{
			Paper paper=new Paper();
			paper.setPaperId(cursor.getInt(cursor.getColumnIndex("PAPER_ID")));
			paper.setTitle(cursor.getString(cursor.getColumnIndex("TITLE")));
			paper.setSubjectNum(cursor.getInt(cursor.getColumnIndex("SUBJECT_NUM")));
			paper.setStatus(cursor.getInt(cursor.getColumnIndex("STATUS")));
			list.add(paper);
		}//end while
		cursor.close();
		db.close();
		return list;
	}
	
	
	public int queryExternPaperNum()
	{
		SQLiteDatabase db=getReadableDatabase();
		Cursor cursor=db.query("LOADPAPERS",new String[]{"ID"}, null, null, null, null, null);
		int ret=cursor.getCount();
		cursor.close();
		db.close();
		return ret;
	}
	
	/**
	 * 插入额外试卷到LOADPAPERS
	 * @param externPaper
	 * @return
	 */
	public long insertExternPaper(ExternPaper externPaper)
	{
		SQLiteDatabase db=sqliteOpen.getWritableDatabase();//写入数据库
		ContentValues values=new ContentValues();
		values.put("PAPER_ID",externPaper.getPaperId());
		values.put("TITLE",externPaper.getTitle());
		values.put("SUBJECT_NUM",externPaper.getSubjectNum());
		values.put("ISLOAD",externPaper.getIsLoad());
		values.put("FILEPATH",externPaper.getFilePath());
		long ret=db.insert("LOADPAPERS",null,values);
		db.close();
		return ret;
	}
	
	/**
	 * 得到额外试卷列表(未载入)
	 * @return
	 */
	public ArrayList<ExternPaper> selectAllExternPapers()
	{
		ArrayList<ExternPaper> list=new ArrayList<ExternPaper>();
		SQLiteDatabase db=sqliteOpen.getReadableDatabase();
		Cursor cursor=db.query("LOADPAPERS",new String[]{"PAPER_ID","TITLE",
				"SUBJECT_NUM","ISLOAD","FILEPATH"}, 
		"ISLOAD=0",null,null,null,"PAPER_ID desc");
		while(cursor.moveToNext())
		{
			ExternPaper externPaper=new ExternPaper();
			externPaper.setPaperId(cursor.getInt(cursor.getColumnIndex("PAPER_ID")));
			externPaper.setTitle(cursor.getString(cursor.getColumnIndex("TITLE")));
			externPaper.setSubjectNum(cursor.getInt(cursor.getColumnIndex("SUBJECT_NUM")));
			externPaper.setIsLoad(cursor.getInt(cursor.getColumnIndex("ISLOAD")));
			externPaper.setFilePath(cursor.getString(cursor.getColumnIndex("FILEPATH")));
			list.add(externPaper);
		}//end while
		cursor.close();
		return list;
	}
	
	/**
	 * 置额外试卷表标志位为1
	 * @param externPaperId
	 * @return
	 */
	public int updateExternPaperIsLoad(int externPaperId)
	{
		SQLiteDatabase db=sqliteOpen.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("ISLOAD","1");
		int ret=db.update("LOADPAPERS", values,"PAPER_ID=?",new String[]{externPaperId+""});
		db.close();
		return ret;
	}
}//end class











