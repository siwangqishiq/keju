package com.xinlan.keju;

import cn.domob.android.ads.DomobAdView;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.RelativeLayout;

public class DescActivity extends Activity
{
	RelativeLayout mAdContainer;
	DomobAdView domobView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.desclayout);
	    
	    /*------------------------¹ã¸æ-------------------------------------*/
        mAdContainer=(RelativeLayout)findViewById(R.id.adcontainer2);
        domobView=new DomobAdView(this,"56OJz1H4uM3aOxesPu",DomobAdView.INLINE_SIZE_320X50);
        mAdContainer.addView(domobView);
        /*---------------------------------end-----------------------------*/
	}
}
