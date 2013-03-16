package com.xinlan.keju;

import java.util.ArrayList;

import com.xinlan.data.XinlanFileService;
import com.xinlan.keju.handler.ImportHandler;
import com.xinlan.untils.FileUtils;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class ImportActivity extends Activity 
{
	private ProgressDialog loadDialog;
	private ImportHandler handler;
	private XinlanFileService xinlanService;
	private Button backBtn;
	private Button importBtn;
	private LinearLayout linearLayout;
	private ArrayList<CheckBox> checkBoxList;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.importpaperlist);
        init();
        addBtn();
    }
    
    /**
     * ��ť�¼���Ӧ
     */
    protected void addBtn()
    {
    	backBtn=(Button)this.findViewById(R.id.importCancel);
    	backBtn.setOnClickListener(new OnClickListener(){
			public void onClick(View v) 
			{
				ImportActivity.this.finish();//�رյ�ǰҳ��
			}}
    	);
    	importBtn=(Button)findViewById(R.id.importDoBtn);
    	importBtn.setOnClickListener(new OnClickListener(){
			public void onClick(View v) 
			{
				ArrayList<Integer> ids=new ArrayList<Integer>();
				for(int i=0;i<checkBoxList.size();i++)
				{
					if(checkBoxList.get(i).isChecked())//��ѡ��
					{
						ids.add(i);
					}//end for
				}//end for
				if(ids.size()==0)
				{
					Toast.makeText(ImportActivity.this,"����δѡ����Ҫ�����lan��Դ�ļ�!",
							Toast.LENGTH_SHORT).show();
					return;
				}
				loadDialog=ProgressDialog.show(ImportActivity.this,"����","����lan��Դ�ļ�...", true, true);
				handler.setDialog(loadDialog);
				for(int i=0;i<checkBoxList.size();i++)
				{
					//����ѡ���ļ�
					if(checkBoxList.get(i).isChecked())//��ѡ��
					{
						xinlanService.importFileToDatabase(xinlanService.getFilePathList().get(i));
					}//end for
					ImportActivity.this.finish();
				}//end for
				if(xinlanService!=null)
				{
					xinlanService.sendImportEndMsg(ids);
				}
			}}
    	);
    	
    }
    
    protected void init()
    {
    	loadDialog=ProgressDialog.show(this,"����lan�ļ�","�����������ֻ�SD���ϵ���Դ�ļ� \n���Ե�...", true, true);
    	linearLayout=(LinearLayout)findViewById(R.id.importCheckBoxes);
    	checkBoxList=new ArrayList<CheckBox>();
    	handler=new ImportHandler(this,loadDialog,linearLayout,checkBoxList);
    	xinlanService=new XinlanFileService(this,handler);
    	xinlanService.searchLanFile();//��SD����Ѱ��lan��ʽ���ļ�
    	xinlanService.sendSearchEndMsg();
    }
}//end class