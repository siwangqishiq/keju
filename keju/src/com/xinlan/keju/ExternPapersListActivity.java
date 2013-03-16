package com.xinlan.keju;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import com.xinlan.data.ExternPaperXmlParseHandler;
import com.xinlan.data.SQLiteService;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.xinlan.model.ExternPaper;
import com.xinlan.model.Item;
import com.xinlan.model.Paper;
import com.xinlan.untils.FileUtils;
import com.xinlan.untils.ParseXml;
/**
 * 试卷列表
 * 
 * @author Geass
 * 
 */
public class ExternPapersListActivity extends ListActivity
{
	private SQLiteService sqlService;
	private ArrayList<ExternPaper> externPaperList=null;
	private Handler handler;
	private ProgressDialog loadDialog;
	private String tempFileName;
	private int tempPaperId;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.externpaperlist);
		setTitle("点击载入试卷");
		init();
	}
	
	/**
	 * 显示未载入试卷列表
	 */
	protected void init()
	{
		sqlService=new SQLiteService(this);
		if(sqlService.queryExternPaperNum()<=0)//第一次启动，需要从配置文件中载入数据
		{
			externPaperList=new ArrayList<ExternPaper>();
			loadExternPaperListFromXml();
			saveListToDatabase();
		}
		externPaperList=sqlService.selectAllExternPapers();
		ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
		for(int i=0;i<externPaperList.size();i++)
		{
			ExternPaper externPaper=externPaperList.get(i);
			HashMap<String,String> map=new HashMap<String,String>();
			map.put("title", externPaper.getTitle());
	        map.put("num","题目总数:"+externPaper.getSubjectNum()+"题");
	        map.put("status","");
	        list.add(map);
		}//end for
		SimpleAdapter listAdapter=new SimpleAdapter(this,list,R.layout.paperitemlayout,new String[]{"title","num","status"},
				new int[]{R.id.paper_title,R.id.paper_subject_num,R.id.paper_status});
		setListAdapter(listAdapter);
	}
	
	/**
	 * 将List中的数据存入数据库中
	 */
	private void saveListToDatabase()
	{
		for(int i=0;i<externPaperList.size();i++)
		{
			sqlService.insertExternPaper(externPaperList.get(i));
		}
	}
	
	/**
	 * 从配置文件中载入额外试卷列表
	 */
	private void loadExternPaperListFromXml()
	{
		AssetManager assetManager=this.getAssets();
		BufferedReader buffer=null;
		String fileName="externpro"+File.separator+"paperProp.xml";//额外试卷列表配置xml文件
		StringBuffer sb=new StringBuffer();
		try
		{
			buffer = new BufferedReader(new InputStreamReader(assetManager.open(fileName),"utf-8"));
			String line=null;
			while ((line = buffer.readLine()) != null)
			{
				sb.append(line);
			}//end while
			buffer.close();
			String content=sb.toString();//读出文件内容
			parseXmlToList(content);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(buffer!=null)
			{
				try
				{
					buffer.close();
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	private void parseXmlToList(String content)
	{
		SAXParserFactory factory=SAXParserFactory.newInstance();//产生一个SAXParser工厂
		try
		{
			XMLReader reader=factory.newSAXParser().getXMLReader();
			reader.setContentHandler(new ExternPaperXmlParseHandler(externPaperList));//设置内容处理器
			reader.parse(new InputSource(new StringReader(content)));
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		} 
	}
	
	/**
	 * 选择载入的试卷
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		ExternPaper externpaper=externPaperList.get(position); 
		//loadExternPaperInfo(externpaper.getPaperId(),externpaper.getFilePath(),externpaper.getTitle());
		loadExternPaperInfo(externpaper);
	}
	 
	/**
	 * 载入选中的试卷到数据库中
	 * @param paperId
	 * @param fileName
	 * @param paperTitle
	 * @return
	 */
	private boolean loadExternPaperInfo(final ExternPaper externPaper)
	{
		tempFileName=externPaper.getFilePath();//fileName;
		tempPaperId=externPaper.getPaperId();//paperId;
		handler=new Handler(){
			 @Override
			 public void handleMessage(Message msg)
			 {
				 Paper newpaper=new Paper();
				 newpaper.setPaperId(externPaper.getPaperId());
				 newpaper.setTitle(externPaper.getTitle());
				 newpaper.setSubjectNum(externPaper.getSubjectNum());
				 newpaper.setStatus(1);
				 newpaper.setFlag(1);
				 sqlService.insertPapers(newpaper);//在试卷表中插入选择的试卷
				 Toast.makeText(ExternPapersListActivity.this,"试卷导入完成!\n您之后可以在试题库中选择查看了", Toast.LENGTH_LONG).show();
				 //打开该试卷
				 Intent intent=new Intent();
				 intent.putExtra("paperId",newpaper.getPaperId());
				 intent.setClass(ExternPapersListActivity.this,ItemActivity.class);
				 ExternPapersListActivity.this.startActivity(intent);
				 ExternPapersListActivity.this.finish();
			 }
		};
		loadDialog = ProgressDialog.show(this,"载入新试卷数据","正在载入"+externPaper.getTitle()+"请稍等...", true, true);
		//开启一个新线程导入数据
		new Thread(new Runnable(){
			public void run() 
			{
				//String fileName="papers"+File.separator+fileNames[i];
				String filepath="externpapers"+File.separator+tempFileName+".xml";
				String content=FileUtils.readAssertFileContent(filepath,ExternPapersListActivity.this);
				//System.out.println("XXX"+content);
				ArrayList<Item> list=ParseXml.parseXmlToItemList(content);
				for(int i=0;i<list.size();i++)
				{
					sqlService.insertItems(list.get(i));
				}//end for 插入数据库完成
				sqlService.updateExternPaperIsLoad(tempPaperId);
				loadDialog.dismiss();
				handler.sendMessage(new Message());//导入数据完成 给主线程发送一条消息
			}
		}).start();
		return true;
	}
}// end class
