package com.xinlan.keju;

import cn.domob.android.ads.DomobAdView;

import com.xinlan.data.SQLiteService;
import com.xinlan.data.Settings;
import com.xinlan.model.Item;
import com.xinlan.model.Paper;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ItemActivity extends Activity implements OnGestureListener
{	
	private int paperId;//显示试卷的ID
	private int itemNumber;//题号
	private String title;//标题
	private Item currentItem;//当前显示的试题
	private int paperTotalItems;//试卷总题目数
	private Paper currentPaper;//当前试卷信息
	private GestureDetector gestureDetector;
	
	//页面控件
	private TextView totalView;//标题题目总数
	private TextView textTitle;//题目
	private RadioGroup radioGroup;
	private RadioButton rbSelectionA,rbSelectionB,rbSelectionC,rbSelectionD,rbSelectionE;
	private ImageButton submitBtn;//提交答案按钮
	private ImageButton preBtn;//上一题
	private ImageButton nextBtn;//下一题
	private ImageView resultView;//回答结果
	
	private SQLiteService sqlService;//读写数据库
	
	private String selectItems="none";
	
	//资源
	private Drawable rightDrawble;
	private Drawable wrongDrawble;
	private Settings setting;
	
	RelativeLayout mAdContainer;
	DomobAdView domobView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items);
        gestureDetector = new GestureDetector(this);  
        /*------------------------广告-------------------------------------*/
        mAdContainer=(RelativeLayout)findViewById(R.id.adcontainer1);
        domobView=new DomobAdView(this,"56OJz1H4uM3aOxesPu",DomobAdView.INLINE_SIZE_320X50);
        mAdContainer.addView(domobView);
        /*---------------------------------end-----------------------------*/
        init();
        Toast.makeText(this,"在空白处滑动也可切换题目哦!^^",Toast.LENGTH_LONG).show();
        initCompoint();
        addEventListener();
        setContent();
    }
	
	private void init()
	{
		setting=new Settings(this);
		sqlService=new SQLiteService(this);
		Intent intent=getIntent();
		paperId=intent.getIntExtra("paperId",1);
		currentPaper=sqlService.queryPaperByPaperId(paperId);
		title=currentPaper.getTitle();
		itemNumber=intent.getIntExtra("itemNumber",1);
		paperTotalItems=currentPaper.getSubjectNum();
		currentItem=sqlService.queryItemByPaperAndNumber(paperId,itemNumber);//查询出该试卷下第一题
		Resources res = getResources();
		rightDrawble=res.getDrawable(R.drawable.gou);
		wrongDrawble=res.getDrawable(R.drawable.cha);
	}
	
	/**
	 * 初始化控件
	 */
	private void initCompoint()
	{
		totalView=(TextView)findViewById(R.id.itemstotal);
		textTitle=(TextView)findViewById(R.id.itemtitle);
		radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
		rbSelectionA=(RadioButton)findViewById(R.id.itemSectionA);
		rbSelectionB=(RadioButton)findViewById(R.id.itemSectionB);
		rbSelectionC=(RadioButton)findViewById(R.id.itemSectionC);
		rbSelectionD=(RadioButton)findViewById(R.id.itemSectionD);
		rbSelectionE=(RadioButton)findViewById(R.id.itemSectionE);
		submitBtn=(ImageButton)findViewById(R.id.btnsubmitAnswer);
		preBtn=(ImageButton)findViewById(R.id.btnPreItem);
		nextBtn=(ImageButton)findViewById(R.id.btnNextItem);
		resultView=(ImageView)findViewById(R.id.rightorwrong);
		
		totalView.setText(currentPaper.getTitle()+"\n题目总数:"+paperTotalItems);
	}
	
	/**
	 * 设置控件内容
	 */
	private void setContent()
	{
		resultView.setVisibility(ImageView.INVISIBLE);
		resultView.setEnabled(false);
		
		rbSelectionE.setChecked(true);
		
		textTitle.setText(currentItem.getItemNumber()+"."+currentItem.getItemTitle());
		rbSelectionA.setText("A ."+currentItem.getSectionA());
		rbSelectionB.setText("B ."+currentItem.getSectionB());
		rbSelectionC.setText("C ."+currentItem.getSectionC());
		rbSelectionD.setText("D ."+currentItem.getSectionD());
		selectItems="none";
		
		setting.setLastPosition(paperId,currentItem.getItemNumber());
	}
	
	/**
	 * 添加事件响应
	 */
	private void addEventListener()
	{
		//选择
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				if(rbSelectionA.getId()==checkedId)
				{
					selectItems="a";
				}
				else if(rbSelectionB.getId()==checkedId)
				{
					selectItems="b";
				}
				else if(rbSelectionC.getId()==checkedId)
				{
					selectItems="c";
				}
				else if(rbSelectionD.getId()==checkedId)
				{
					selectItems="d";
				}
				else
				{
					selectItems="none";
				}//end if
			}
		});
		
		//提交答案
		submitBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if(currentItem.getAnswer().toLowerCase().equals(selectItems))//回答正确
				{
					//Toast.makeText(ItemActivity.this,"回答正确",Toast.LENGTH_LONG).show();
					resultView.setBackgroundDrawable(rightDrawble);
				}
				else//错误
				{
					//Toast.makeText(ItemActivity.this,"回答错误",Toast.LENGTH_LONG).show();
					resultView.setBackgroundDrawable(wrongDrawble);
				}
				resultView.setVisibility(ImageView.VISIBLE);
				AnimationSet as=new AnimationSet(true);
				ScaleAnimation scaleAnmation=new ScaleAnimation(0f,1,0f,1,Animation.RELATIVE_TO_SELF,
						0.5f,Animation.RELATIVE_TO_SELF,0.5f);
				scaleAnmation.setDuration(500);
				as.addAnimation(scaleAnmation);
				resultView.startAnimation(as);
				resultView.setEnabled(true);
			}
		});
		
		//选择前一题
		preBtn.setOnClickListener(new OnClickListener(){
			public void onClick(View v)
			{
				showPreItem();
			}
		});
		
		//选择后一题
		nextBtn.setOnClickListener(new OnClickListener(){
			public void onClick(View v)
			{
				showNextItem();
			}
		});
		
		//点击结果图片 
		resultView.setOnClickListener(new OnClickListener(){
			public void onClick(View v)
			{
				AnimationSet as=new AnimationSet(true);
				ScaleAnimation scaleAnmation=new ScaleAnimation(1f,0f,1f,0f,Animation.RELATIVE_TO_SELF,
						0.5f,Animation.RELATIVE_TO_SELF,0.5f);
				scaleAnmation.setDuration(500);
				as.addAnimation(scaleAnmation);
				resultView.startAnimation(as);
				resultView.setEnabled(true);
				resultView.setVisibility(ImageView.INVISIBLE);
				resultView.setEnabled(false);
			}
		});
		
	}//end addEventListener
	
	/**
	 * 显示下一题
	 */
	private void showNextItem()
	{
		if(currentItem!=null)
		{
			int nextNum=currentItem.getItemNumber()+1;
			if(nextNum>paperTotalItems)//
			{
				Toast.makeText(ItemActivity.this,"已经是最后一题",Toast.LENGTH_SHORT).show();
				return;
			}
			else
			{
				currentItem=sqlService.queryItemByPaperAndNumber(paperId,nextNum);//查询
				setContent();//更新内容
			}
		}
	}
	
	/**
	 * 显示前一题
	 */
	private void showPreItem()
	{
		if(currentItem!=null)
		{
			int nextNum=currentItem.getItemNumber()-1;
			if(nextNum<=0)
			{
				Toast.makeText(ItemActivity.this,"已经是第一题",Toast.LENGTH_SHORT).show();
				return;
			}
			else
			{
				currentItem=sqlService.queryItemByPaperAndNumber(paperId,nextNum);//查询
				setContent();//更新内容
			}
		}//end if
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{  
	        if (gestureDetector.onTouchEvent(event))  
	            return true;  
	        else  
	            return false;  
	}  

	public boolean onDown(MotionEvent arg0)
	{
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) 
	{
		if (e1.getX() - e2.getX() > 120) 
		{
			showPreItem();
            return true;
        }
		else if (e1.getX() - e2.getX() < -120) 
        {
			showNextItem();
            return true;
        }
        
		return false;
	}

	public void onLongPress(MotionEvent e) 
	{
		
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) 
	{
		return false;
	}

	public void onShowPress(MotionEvent e) 
	{
	}

	public boolean onSingleTapUp(MotionEvent e) 
	{
		return false;
	}
	
}//end class
