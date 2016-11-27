/**
 * CorePushHistoryManager
 * CorePush-API-Android-Samples
 *
 * Copyright © 2016年 株式会社ブレスサービス. All rights reserved.
 */

package com.coreasp.research.android.samples;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;

import com.coreasp.CorePushManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * 通知履歴取得の通信マネジャークラス。
 * AsyncTaskを継承。
 */
public class CoreResearchHistoryManager extends AsyncTask<Void, Integer, Boolean> implements OnCancelListener {

    private static final String LOG_TAG = "CORERESEARCH";
    private Context mContext;
    private CoreResearchHistoryManagerListener listener;
    private ProgressDialog dialog;
    private boolean isProgressDialog = false;
    private String progressMessage = "読み込み中...";
    private static ArrayList<CoreResearchHistoryModel> notificationHistoryModelList = new ArrayList<CoreResearchHistoryModel>();
    private static final String CORE_RESEARCH_NOTIFY_HISTORY_API = "http://stage.core-asp.com/corepush/api/research_history.php";

    /**
     * コンストラクタ
     * @param context コンストラクタ
     */
    public CoreResearchHistoryManager(Context context) {
        mContext = context;
    }

    /**
     * CorePushHistoryManagerListerを実装したクラスを設定する。
     * 通知履歴の取得が成功した場合は CorePushHistoryManagerListener#notificationHistoryManagerSuccess が呼ばれる。
     * 通知履歴の取得が失敗した場合は CorePushHistoryManagerListener#notificationHistoryManagerFailが呼ばれる。
     * @param listener CorePushHistoryManagerListerを実装したクラス
     */
    public void setListener(CoreResearchHistoryManagerListener listener) {
        this.listener = listener;
    }

    /**
     * プログレスダイアログのメッセージを設定する。メッセージを設定しない場合は 「読み込み中...」がデフォルトメッセージとして設定される。
     * @param progressMessage プログレスダイアログのメッセージ
     */
    public void setProgressMessage(String progressMessage) {
        this.progressMessage = progressMessage;
    }

    /**
     * 通知履歴の配列を取得する。配列は CorePushNotificationHistoryModelの形式で格納される。
     * @return notificationHistoryModel 履歴モデル
     */
    public static ArrayList<CoreResearchHistoryModel> getNotificationHistoryModelList() {
        return notificationHistoryModelList;
    }

    @Override
    protected void onPreExecute() {

        if (isProgressDialog) {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage(this.progressMessage);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(true);
            dialog.setOnCancelListener(this);
            dialog.show();
        }
    }

    /**
     * プログレスダイアログを表示するか判定する
     * @return プログレスダイアログの表示有無
     */
    public boolean isProgressDialog() {
        return this.isProgressDialog;
    }

    /**
     * プログレスダイアログの表示を設定する。
     * プログレスダイアログを表示する場合は true を設定し、表示しない場合は false を設定する。
     * @param isProgressDialog プログレスダイアログの表示有無
     */
    public void setProgressDialog(boolean isProgressDialog) {
        this.isProgressDialog = isProgressDialog;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        HashMap<String, String> postDataParams = new HashMap<>();
        postDataParams.put("config_type", "2");
        CorePushManager manager = CorePushManager.getInstance();
        postDataParams.put("config_key", manager.getConfigKey());

        String jsonText = "";
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(CORE_RESEARCH_NOTIFY_HISTORY_API);
            connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setReadTimeout(30000);
            connection.setConnectTimeout(30000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(CorePushUtil.getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                Log.d(LOG_TAG, "OK");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    jsonText += line;
                }
            } else {
                Log.d(LOG_TAG, "NG");
                jsonText = "";
            }

            Log.d(LOG_TAG, jsonText);
        } catch (UnsupportedEncodingException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
                ;
            }
        }

        // jsonのテキストからJSONオブジェクトを生成
        try {
            JSONObject jsonObject = new JSONObject(jsonText);
            String status = jsonObject.getString("status");

            if (status.equalsIgnoreCase("0")) {
                notificationHistoryModelList.clear();
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject result = jsonArray.getJSONObject(i);
                    String historyId = result.getString("history_id");
                    String title = result.getString("title");
                    String message = result.getString("body");
                    String urlStr = result.getString("url");
                    if (urlStr != null && urlStr.equals("null")) {
                        urlStr = null;
                    }

                    String regDate = result.getString("reg_date");

                    CoreResearchHistoryModel model = new CoreResearchHistoryModel();
                    model.setHistoryId(historyId);
                    model.setTitle(title);
                    model.setMessage(message);
                    model.setUrl(urlStr);
                    model.setRegDate(regDate);
                    notificationHistoryModelList.add(model);
                }

                return true;
            } else {
                return false;
            }

        } catch (JSONException e) {

            Log.d(LOG_TAG, e.getMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {

        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
            }
        }

        if (success) {
            if (this.listener != null) {
                this.listener.historyManagerSuccess();
            }
        } else {
            if (this.listener != null) {
                this.listener.historyManagerFail();
            }
        }
    }

    @Override
    protected void onCancelled() {
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onCancel(DialogInterface arg0) {
        this.cancel(true);
    }

}