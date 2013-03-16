package com.xinlan.keju;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class UpdateActivity extends Activity
{
	private final static String updateUrl="http://dl.dbank.com/c0b88jw8i2";
	private WebView webView=null;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.updatelayout);
	    init();
	}
	
	protected void init()
	{
		webView=(WebView)this.findViewById(R.id.updateView);
		webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(false);
        //webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setAllowFileAccess(true);
        webView.loadUrl(updateUrl);
	}
}
