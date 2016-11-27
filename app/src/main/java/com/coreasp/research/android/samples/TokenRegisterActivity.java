/**
 * TokenRegisterActivity
 * CorePush-API-Android-Samples
 *
 * Copyright © 2016年 株式会社ブレスサービス. All rights reserved.
 */

package com.coreasp.research.android.samples;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coreasp.CorePushManager;

/**
 * トークン登録画面のアクティビティ
 */
public class TokenRegisterActivity extends Activity {

    // トークン登録用ボタン
    private Button tokenRegisterButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_register);

        // 登録ボタン押下時のトークン登録処理
        tokenRegisterButton = (Button) findViewById(R.id.tokenRegisterButton);
        tokenRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CorePushManager manager = CorePushManager.getInstance();

                //CORE PUSHサーバにデバイストークンを登録
                manager.registToken(TokenRegisterActivity.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}