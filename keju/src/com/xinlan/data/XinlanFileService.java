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
 * 对指定lan文件，试卷资源进行操作
 * @author Geass
 *
 */
public class XinlanFileService 
{
	private final static int KEY=39;//解密密钥 新兰科技lan生日39
	private final static String lastName=".lan";//后缀名
	
	private Context context;
	private String SDPATH;//sd卡路径
	private ArrayList<String> filePathList;//找到的文件路径
	private ArrayList<String> fileNameList;//文件名列表
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
	 * 发送搜寻文件完毕消息
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
	 * 搜索手机中所有lan为后缀名的文件
	 */
	public void searchLanFile()
	{
		doFileSearch(SDPATH);
		//decodeLanFile();//解码
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
	 * 解码 解析指定文件
	 * @param filepath
	 */
	public void importFileToDatabase(String filepath)
	{
		byte[] filedata=FileUtils.readFileToBye(filepath);//读取文件为二进制数据
		for(int i=0;i<filedata.length;i++)//解码文件
		{
			filedata[i]=(byte)(filedata[i]^KEY);
		}//end for
		FileUtils.writeByteDataToFile(filepath,filedata);//解码过的文件写入
		String fileContent=FileUtils.readTextFile(filepath);//读取出文件的内容
		//System.out.println("--->"+fileContent);
		ArrayList<Paper> paperList=new ArrayList<Paper>();//试卷列表
		ArrayList<Item> itemList=new ArrayList<Item>();//题目列表
		if(parseXmlToList(fileContent,paperList,itemList))
		{
			//解析xml文件内容
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
			Toast.makeText(context,"文件解析完成!\n可在试卷列表中查看导入试卷了^_^.",Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(context,"文件解析失败!",Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/**
	 * 解析文件
	 * @param fileContent
	 * @param paperList
	 * @param itemList
	 * @return
	 */
	private boolean parseXmlToList(String fileContent,ArrayList<Paper> paperList,ArrayList<Item> itemList)
	{
		SAXParserFactory factory=SAXParserFactory.newInstance();//产生一个SAXParser工厂
		try
		{
			XMLReader reader=factory.newSAXParser().getXMLReader();
			reader.setContentHandler(new ImportsXmlParseHandler(paperList,itemList));//设置内容处理器
			try
			{
				reader.parse(new InputSource(new StringReader(fileContent)));//解析指定内容
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












