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
 * �Ծ��б�
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
		setTitle("��������Ծ�");
		init();
	}
	
	/**
	 * ��ʾδ�����Ծ��б�
	 */
	protected void init()
	{
		sqlService=new SQLiteService(this);
		if(sqlService.queryExternPaperNum()<=0)//��һ����������Ҫ�������ļ�����������
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
	        map.put("num","��Ŀ����:"+externPaper.getSubjectNum()+"��");
	        map.put("status","");
	        list.add(map);
		}//end for
		SimpleAdapter listAdapter=new SimpleAdapter(this,list,R.layout.paperitemlayout,new String[]{"title","num","status"},
				new int[]{R.id.paper_title,R.id.paper_subject_num,R.id.paper_status});
		setListAdapter(listAdapter);
	}
	
	/**
	 * ��List�е����ݴ������ݿ���
	 */
	private void saveListToDatabase()
	{
		for(int i=0;i<externPaperList.size();i++)
		{
			sqlService.insertExternPaper(externPaperList.get(i));
		}
	}
	
	/**
	 * �������ļ�����������Ծ��б�
	 */
	private void loadExternPaperListFromXml()
	{
		AssetManager assetManager=this.getAssets();
		BufferedReader buffer=null;
		String fileName="externpro"+File.separator+"paperProp.xml";//�����Ծ��б�����xml�ļ�
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
			String content=sb.toString();//�����ļ�����
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
		SAXParserFactory factory=SAXParserFactory.newInstance();//����һ��SAXParser����
		try
		{
			XMLReader reader=factory.newSAXParser().getXMLReader();
			reader.setContentHandler(new ExternPaperXmlParseHandler(externPaperList));//�������ݴ�����
			reader.parse(new InputSource(new StringReader(content)));
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		} 
	}
	
	/**
	 * ѡ��������Ծ�
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		ExternPaper externpaper=externPaperList.get(position); 
		//loadExternPaperInfo(externpaper.getPaperId(),externpaper.getFilePath(),externpaper.getTitle());
		loadExternPaperInfo(externpaper);
	}
	 
	/**
	 * ����ѡ�е��Ծ����ݿ���
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
				 sqlService.insertPapers(newpaper);//���Ծ���в���ѡ����Ծ�
				 Toast.makeText(ExternPapersListActivity.this,"�Ծ������!\n��֮��������������ѡ��鿴��", Toast.LENGTH_LONG).show();
				 //�򿪸��Ծ�
				 Intent intent=new Intent();
				 intent.putExtra("paperId",newpaper.getPaperId());
				 intent.setClass(ExternPapersListActivity.this,ItemActivity.class);
				 ExternPapersListActivity.this.startActivity(intent);
				 ExternPapersListActivity.this.finish();
			 }
		};
		loadDialog = ProgressDialog.show(this,"�������Ծ�����","��������"+externPaper.getTitle()+"���Ե�...", true, true);
		//����һ�����̵߳�������
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
				}//end for �������ݿ����
				sqlService.updateExternPaperIsLoad(tempPaperId);
				loadDialog.dismiss();
				handler.sendMessage(new Message());//����������� �����̷߳���һ����Ϣ
			}
		}).start();
		return true;
	}
}// end class
