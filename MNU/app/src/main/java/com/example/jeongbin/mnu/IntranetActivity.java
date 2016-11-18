package com.example.jeongbin.mnu;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/*
* by ggomi 11_18
 */

public class IntranetActivity extends Activity{

    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intranet_login);// layout연결

        setLayout();

        // 웹뷰에서 자바스크립트실행 true
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true); //확대축소 버튼
        mWebView.getSettings().setSupportZoom(true); //확대축소 지원


        // Url 설정
        mWebView.loadUrl("http://dormsys.mokpo.ac.kr/system/user/login.php");

        // WebViewClient 지정
        mWebView.setWebViewClient(new WebViewClientClass());

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private void setLayout(){
        mWebView = (WebView) findViewById(R.id.webview); //레이아웃 설정
    }
}
