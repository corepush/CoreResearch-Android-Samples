/**
 * HistoryActivity
 * CorePush-API-Android-Samples
 *
 * Copyright © 2016年 株式会社ブレスサービス. All rights reserved.
 */

package com.coreasp.research.android.samples;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.coreasp.CorePushManager;

import java.util.ArrayList;

/**
 * 　通知履歴を表示するアクティビティ
 */
public class HistoryActivity extends ListActivity implements CoreResearchHistoryManagerListener {

    private ArrayList<CoreResearchHistoryModel> historyModelList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setListAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, new ArrayList<String>()));
    }

    public void onResume() {
        super.onResume();

        CoreResearchHistoryManager manager = new CoreResearchHistoryManager(this);

        //コールバックリスナーを設定
        manager.setListener(this);

        //プログレスダイアログを表示
        manager.setProgressDialog(true);

        //通知履歴取得リクエストの実行
        manager.execute();
    }

    public void onPause() {
        super.onPause();
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        //リストアイテムをタップされた時の動作を定義
        TextView t = (TextView) v;
        CoreResearchHistoryModel model = historyModelList.get(position);
        String url = model.getUrl();
        Intent i = new Intent(this,QuestionnaireActivity.class);

        i.putExtra("cp_date", model.getRegDate());
        i.putExtra("cp_title", model.getTitle());
        i.putExtra("cp_message", model.getMessage());
        i.putExtra("cp_url", model.getUrl()+"&pf=android&cruuid="+ CorePushManager.getInstance().getUuid(this));
//        i.putExtra("cp_push_id", pushId);
        startActivity(i);

        super.onListItemClick(l, v, position, id);
    }

    /**
     * 通知履歴の取得に成功した場合
     */
    @Override
    public void historyManagerSuccess() {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListView().getAdapter();
        adapter.clear();

        this.historyModelList = CoreResearchHistoryManager.getNotificationHistoryModelList();
        for (int i = 0; i < historyModelList.size(); i++) {
            CoreResearchHistoryModel historyModel = historyModelList.get(i);
            String title = historyModel.getTitle();
            String message = historyModel.getMessage();
            String regDate = historyModel.getRegDate();

            // 「通知タイトル : 通知メッセージ : 通知日時」の形式のテキストを設定
            String text = title + " : " + message + " : " + regDate;
            adapter.add(text);
            ;
        }

        adapter.notifyDataSetChanged();
    }

    /**
     * 通知履歴の取得に失敗した場合
     */
    @Override
    public void historyManagerFail() {

    }
}