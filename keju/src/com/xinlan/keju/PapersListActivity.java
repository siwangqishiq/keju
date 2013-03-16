package com.xinlan.keju;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.xinlan.data.ItemsXmlParseHandler;
import com.xinlan.data.SQLiteService;
import com.xinlan.data.PaperXmlParseHandler;
import com.xinlan.model.Item;
import com.xinlan.model.Paper;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * �Ծ��б�
 * @author Geass
 *
 */
public class PapersListActivity extends ListActivity
{
	private static final int INITPAPERSNUM=2;
	private SQLiteService sqlService;
	private ArrayList<Paper> paperList;
	private ArrayList<Item> itemsList;
	private Button loadExternBtn;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		setTitle("����ְҵ���������Ծ�");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paperslist);
        init();
    }
	
	public void init()
	{
		loadExternBtn=(Button)findViewById(R.id.addExternBtn);
		sqlService=new SQLiteService(this);
		if(sqlService.getPapersCount()<INITPAPERSNUM)//��һ�����У����ݿ��л�û������
		{
			
			loadInitDefaultPapers();
			Toast.makeText(this,"�Ծ����ݵ������!", Toast.LENGTH_SHORT).show();
		}
		
		paperList=sqlService.getAllPapersList();
		showPapersList();
		//����������Ծ�ť
		loadExternBtn.setOnClickListener(new OnClickListener(){
			public void onClick(View v)
			{
				Intent intent=new Intent();
				intent.setClass(PapersListActivity.this,ExternPapersListActivity.class);
				PapersListActivity.this.startActivity(intent);
				PapersListActivity.this.finish();
			}
		});
	}
	
	private void loadInitDefaultPapers()//�����ʼʱ���Ծ���Ϣ
	{
		AssetManager assetManager=this.getAssets();
		paperList=new ArrayList<Paper>();//�������Ծ�List�ṹ
		try
		{
			String[] fileNames=assetManager.list("papers");
			for(int i=0;i<fileNames.length;i++)
			{
				StringBuffer sb=new StringBuffer();
				BufferedReader buffer=null;
				String line=null;
				String fileName="papers"+File.separator+fileNames[i];
				try
				{
					buffer = new BufferedReader(new InputStreamReader(assetManager.open(fileName),"utf-8"));
					while ((line = buffer.readLine()) != null)
					{
						sb.append(line);
					}//end while
					buffer.close();
					String content=sb.toString();
					addPaperParseXml(content);
					loadDefaultItems(content);
				} 
				finally
				{
					if(buffer!=null)
					{
						buffer.close();
					}
				}
			}//end for
			insertPapersIntoDatabase();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * ����Ĭ������
	 */
	private void loadDefaultItems(String content)
	{
		SAXParserFactory factory=SAXParserFactory.newInstance();//����һ��SAXParser����
		itemsList=new ArrayList<Item>();
		try
		{
			XMLReader reader=factory.newSAXParser().getXMLReader();
			reader.setContentHandler(new ItemsXmlParseHandler(itemsList));//�������ݴ�����
			try
			{
				reader.parse(new InputSource(new StringReader(content)));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		} 
		catch (SAXException e)
		{
			e.printStackTrace();
		} 
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		insertDefaultItems();
	}
	
	/**
	 * �����ݿ��д�����Ϣ
	 */
	private void insertDefaultItems()
	{
		for(int i=0;i<itemsList.size();i++)
		{
			sqlService.insertItems(itemsList.get(i));
		}//end for
	}
	
	private void insertPapersIntoDatabase()//������������Ϣ�������ݿ���
	{
		for(int i=0;i<paperList.size();i++)
		{
			sqlService.insertPapers(paperList.get(i));
		}//end for
	}

	private void addPaperParseXml(String content)//����xml�ļ������Ծ���Ϣ����List��
	{
		SAXParserFactory factory=SAXParserFactory.newInstance();//����һ��SAXParser����
		try
		{
			XMLReader reader=factory.newSAXParser().getXMLReader();
			reader.setContentHandler(new PaperXmlParseHandler(paperList));//�������ݴ�����
			try
			{
				reader.parse(new InputSource(new StringReader(content)));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		} 
		catch (SAXException e)
		{
			e.printStackTrace();
		} 
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
	}//end addPaperParseXml
	
	private void showPapersList()
	{
		ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
		for(int i=0;i<paperList.size();i++)
		{
			Paper paper=paperList.get(i);
			HashMap<String,String> map=new HashMap<String,String>();
			map.put("title", paper.getTitle());
	        map.put("num","��Ŀ����:"+paper.getSubjectNum()+"��");
	        map.put("status", "");
	        list.add(map);
		}//end for
		SimpleAdapter listAdapter=new SimpleAdapter(this,list,R.layout.paperitemlayout,new String[]{"title","num","status"},
				new int[]{R.id.paper_title,R.id.paper_subject_num,R.id.paper_status});
		setListAdapter(listAdapter);
	}
	
	/**
	 * ѡ���Ծ�
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		Paper paper=paperList.get(position);//��ȡ���ѡ����Ծ�
		//Toast.makeText(this,paper.toString(),Toast.LENGTH_LONG).show();
		Intent intent=new Intent();
		intent.putExtra("itemNumber",paper.getTitle());
		intent.putExtra("paperId",paper.getPaperId());
		intent.setClass(this,ItemActivity.class);
		startActivity(intent);
	}

}//end class
