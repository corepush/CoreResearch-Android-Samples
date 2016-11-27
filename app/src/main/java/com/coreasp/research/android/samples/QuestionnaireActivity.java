/**
 * MainActivity
 * CorePush-API-Android-Samples
 *
 * Copyright © 2016年 株式会社ブレスサービス. All rights reserved.
 */

package com.coreasp.research.android.samples;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.coreasp.CorePushManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * メイン画面のアクティビティ
 */
public class QuestionnaireActivity extends Activity {

    // 一覧のアクティビティ情報の配列
    List<Map<String, Object>> activityMapList = new ArrayList<>();

    private String url;
    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
//        setTheme(android.R.style.Theme_Translucent);


        // 通知から起動時に通知パラメータを取得
        handleNotificationLaunchParameter(getIntent());

        webView = (WebView)findViewById(R.id.questionnaire_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        webView.loadUrl(this.url);
        ImageView close = (ImageView)findViewById(R.id.cr_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    private void handleNotificationLaunchParameter(Intent intent) {
        // 通知から起動時に通知パラメータを取得
        CorePushManager manager = CorePushManager.getInstance();
        String date = manager.getDate(intent);
        String title = manager.getTitle(intent);
        String message = manager.getMessage(intent);
        String url = manager.getUrl(intent);
        this.url = url + "&pf=android&cruuid=" + manager.getUuid(this);
        Log.d("reseach_url", this.url);
    }
}