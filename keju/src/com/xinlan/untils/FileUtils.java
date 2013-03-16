package com.xinlan.untils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;

/**
 * �ļ�������ع�����
 * @author Geass
 *
 */
public class FileUtils 
{  
	/**
	 * ��ȡ�ı��ļ�����
	 * @param filepath
	 * @return
	 */
	public static String readTextFile(String filepath)
	{
		StringBuffer sb=new StringBuffer();
		BufferedReader buffer=null;
		String line=null;
		try
		{
			buffer = new BufferedReader(new InputStreamReader(new FileInputStream(filepath),"utf-8"));
			while ((line = buffer.readLine()) != null)
			{
				sb.append(line);
			}//end while
		} 
		catch(Exception e)
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
		return sb.toString();
	}
 	/**
	 * ��ָ���ļ���д�����������
	 * @param filepath
	 * @param filedata
	 */
	public static void writeByteDataToFile(String filepath,byte[] filedata)
	{
		File file=new File(filepath);
		if(file.exists())
		{
			FileOutputStream fos=null;
			try
			{
				fos=new FileOutputStream(file);
				fos.write(filedata);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				if(fos!=null)
				{
					try 
					{
						fos.close();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
			}
		}
	}
	/**
	 * ��ȡָ���ļ�����������
	 * @param filepath
	 * @return
	 */
	public static byte[] readFileToBye(String filepath)
	{
		File file=new File(filepath);
		ByteArrayOutputStream byteArray=new ByteArrayOutputStream();
		try 
		{
			if(file.exists())
			{
				FileInputStream fis=new FileInputStream(file);
				byte[] buffer=new byte[1024];
				int len;
				while((len=fis.read(buffer))!=-1)
				{
					byteArray.write(buffer,0,len);
				}//end while
				fis.close();
			}
			byteArray.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return byteArray.toByteArray();
	}
	/**
	 * ��ȡAssertĿ¼�µ�ָ���ļ�
	 */
	public static String readAssertFileContent(String filePath,Context context)
	{
		BufferedReader buffer=null;
		StringBuffer sb=new StringBuffer();
		String line=null;
		try {
			buffer = new BufferedReader(new InputStreamReader(context.getAssets().open(filePath),"utf-8"));
			while ((line = buffer.readLine()) != null)
			{
				sb.append(line);
			}//end while
		} 
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
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
		return sb.toString();
	}
	
	
}//end class


















