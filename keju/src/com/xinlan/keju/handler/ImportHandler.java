package com.xinlan.keju.handler;

import java.util.ArrayList;
import java.util.HashMap;

import com.xinlan.keju.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ImportHandler extends Handler 
{
	private Context context;
	private ProgressDialog dialog;
	
	private final static int SEARCH_END=1;
	private final static int LOADPAPER_END=2;
	
	private ArrayList<String> itemData;
	private ArrayList<String> itemNameData;
	private ArrayList<CheckBox> checkBoxsList;
	private LinearLayout layout;
	
	public ArrayList<String> getItemData() 
	{
		return itemData;
	}
	public void setItemData(ArrayList<String> itemData) 
	{
		this.itemData = itemData;
	}
	public ArrayList<String> getItemNameData() 
	{
		return itemNameData;
	}
	public void setItemNameData(ArrayList<String> itemNameData) 
	{
		this.itemNameData = itemNameData;
	}

	public ImportHandler(Context context,ProgressDialog dialog,
			LinearLayout layout,ArrayList<CheckBox> checkBoxsList)
	{
		this.context=context;
		this.dialog=dialog;
		this.checkBoxsList=checkBoxsList;
		this.layout=layout;
	}
	/**
	 * 处理消息
	 */
	 @Override
	 public void handleMessage(Message msg)
	 {
		 switch(msg.arg1)
		 {
		 case SEARCH_END:
			 doSearchEnd();
			 break;
		 case LOADPAPER_END:
			 ArrayList<Integer> ids=(ArrayList<Integer>)msg.obj;
			 doLoadEnd(ids);
			 break;
		 }//end switch
	 }
	 
	 /**
	  * 寻找结束操作
	  */
	 private void doSearchEnd()
	 {
		 dialog.dismiss();
		 Toast.makeText(context,"查找操作完成!",Toast.LENGTH_SHORT).show();
		 LayoutParams layoutParams=new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				 LinearLayout.LayoutParams.WRAP_CONTENT);//布局参数
		 for(int i=0;i<itemNameData.size();i++)
		 {
			 CheckBox checkBox=new CheckBox(context);
			 checkBox.setText(itemNameData.get(i));
			 layout.addView(checkBox,layoutParams);
			 checkBoxsList.add(checkBox);
		 }//end for
	 }
	 
	 /**
	  * 载入结束
	  */
	 private void doLoadEnd(ArrayList<Integer> ids)
	 {
		 dialog.dismiss();
	 }
	public ProgressDialog getDialog() 
	{
		return dialog;
	}
	public void setDialog(ProgressDialog dialog) 
	{
		this.dialog = dialog;
	}
}//end class
