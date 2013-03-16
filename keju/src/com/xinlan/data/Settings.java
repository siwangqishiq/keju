package com.xinlan.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Settings
{
	private final static String NAME_SPACE="com.xinlan.keju";
	private final static String LAST_POSITION="lastposition";
	
	private Context context;
	private SharedPreferences settings;
	
	public Settings(Context context)
	{
		this.context=context;
		settings = context.getSharedPreferences(NAME_SPACE,Context.MODE_PRIVATE);
	}
	
	public void setLastPosition(int paper,int item)
	{
		String last=paper+","+item;
		Editor editor=settings.edit();
		editor.putString(LAST_POSITION,last);
		editor.commit();
	}
	
	public Integer[] getLastPosition()
	{
		Integer[] retInt=new Integer[2];
		String str=settings.getString(LAST_POSITION,"1,1");
		String[] strs=str.split(",");
		for(int i=0;i<strs.length;i++)
		{
			retInt[i]=Integer.parseInt(strs[i]);
		}//end for
		return retInt;
	}
}
