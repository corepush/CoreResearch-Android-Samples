/**
 * TokenUnregisterActivity
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
 * トークン削除画面のアクティビティ
 */
public class TokenUnregisterActivity extends Activity {

    // トークン削除用のボタン
    private Button tokenUnregisterButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_unregister);

        // 登録ボタン押下時のトークン削除処理
        tokenUnregisterButton = (Button) findViewById(R.id.tokenUnregisterButton);
        tokenUnregisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CorePushManager manager = CorePushManager.getInstance();

                //CORE PUSHサーバからデバイストークンを削除
                manager.removeToken(TokenUnregisterActivity.this);
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