package com.pernix.menus;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class AyudaActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        WebView webView = (WebView) findViewById(R.id.ayudaActWebView);
        webView.loadUrl("http://www.adn.fm");
	}
}
