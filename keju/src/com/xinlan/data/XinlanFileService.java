package com.xinlan.data;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.xinlan.keju.handler.ImportHandler;
import com.xinlan.model.Item;
import com.xinlan.model.Paper;
import com.xinlan.untils.FileUtils;

import android.content.Context;
import android.os.Environment;
import android.os.Message;
import android.widget.Toast;

/**
 * ��ָ��lan�ļ����Ծ���Դ���в���
 * @author Geass
 *
 */
public class XinlanFileService 
{
	private final static int KEY=39;//������Կ �����Ƽ�lan����39
	private final static String lastName=".lan";//��׺��
	
	private Context context;
	private String SDPATH;//sd��·��
	private ArrayList<String> filePathList;//�ҵ����ļ�·��
	private ArrayList<String> fileNameList;//�ļ����б�
	private ImportHandler handler;
	private SQLiteService sqlService;
	
	private final static int SEARCH_END=1;
	private final static int LOADPAPER_END=2;
	
	public ArrayList<String> getFilePathList() 
	{
		return filePathList;
	}

	public XinlanFileService(Context context,ImportHandler handler)
	{
		this.context=context;
		this.handler=handler;
		filePathList=new ArrayList<String>();
		fileNameList=new ArrayList<String>();
		sqlService=new SQLiteService(context);
		SDPATH=Environment.getExternalStorageDirectory()+"";
		
	}
	
	/**
	 * ������Ѱ�ļ������Ϣ
	 */
	public void sendSearchEndMsg()
	{
		Message msg=new Message();
		msg.arg1=SEARCH_END;
		handler.setItemNameData(fileNameList);
		handler.setItemData(filePathList);
		handler.sendMessage(msg);
	}
	
	public void sendImportEndMsg(ArrayList<Integer> ids)
	{
		Message msg=new Message();
		msg.arg1=LOADPAPER_END;
		msg.obj=ids;
		handler.sendMessage(msg);
	}
	
	/**
	 * �����ֻ�������lanΪ��׺�����ļ�
	 */
	public void searchLanFile()
	{
		doFileSearch(SDPATH);
		//decodeLanFile();//����
	}
	
	public void doFileSearch(String filePath)
	{
		File file=new File(filePath);
		File[] files=file.listFiles();
		if(files==null)
		{
			return;
		}
		for(File eachFile:files)
		{
			if(eachFile!=null)
			{
				if(eachFile.isDirectory())
				{
					doFileSearch(eachFile.getAbsolutePath());
				}
				else if(eachFile.isFile())
				{
					if(eachFile.getName().endsWith(lastName))
					{
						filePathList.add(eachFile.getAbsolutePath());
						String name=eachFile.getName();
						fileNameList.add(name.substring(0,name.length()-4));
					}
				}//end if
				
			}//end if
		}//end for
		
	}//doFileSearch
	
	public ArrayList<String> getFileNameList() {
		return fileNameList;
	}
	
	/**
	 * ���� ����ָ���ļ�
	 * @param filepath
	 */
	public void importFileToDatabase(String filepath)
	{
		byte[] filedata=FileUtils.readFileToBye(filepath);//��ȡ�ļ�Ϊ����������
		for(int i=0;i<filedata.length;i++)//�����ļ�
		{
			filedata[i]=(byte)(filedata[i]^KEY);
		}//end for
		FileUtils.writeByteDataToFile(filepath,filedata);//��������ļ�д��
		String fileContent=FileUtils.readTextFile(filepath);//��ȡ���ļ�������
		//System.out.println("--->"+fileContent);
		ArrayList<Paper> paperList=new ArrayList<Paper>();//�Ծ��б�
		ArrayList<Item> itemList=new ArrayList<Item>();//��Ŀ�б�
		if(parseXmlToList(fileContent,paperList,itemList))
		{
			//����xml�ļ�����
			for(int i=0;i<paperList.size();i++)
			{
				sqlService.insertPapers(paperList.get(i));
			}//end for
			for(int i=0;i<itemList.size();i++)
			{
				sqlService.insertItems(itemList.get(i));
			}//end for
			File curFile=new File(filepath);
			if(curFile.exists())
			{
				curFile.delete();
			}
			Toast.makeText(context,"�ļ��������!\n�����Ծ��б��в鿴�����Ծ���^_^.",Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(context,"�ļ�����ʧ��!",Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/**
	 * �����ļ�
	 * @param fileContent
	 * @param paperList
	 * @param itemList
	 * @return
	 */
	private boolean parseXmlToList(String fileContent,ArrayList<Paper> paperList,ArrayList<Item> itemList)
	{
		SAXParserFactory factory=SAXParserFactory.newInstance();//����һ��SAXParser����
		try
		{
			XMLReader reader=factory.newSAXParser().getXMLReader();
			reader.setContentHandler(new ImportsXmlParseHandler(paperList,itemList));//�������ݴ�����
			try
			{
				reader.parse(new InputSource(new StringReader(fileContent)));//����ָ������
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return false;
			}
		} 
		catch (SAXException e)
		{
			e.printStackTrace();
			return false;
		} 
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
}//end class












