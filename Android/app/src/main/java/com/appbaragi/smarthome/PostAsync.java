package com.appbaragi.smarthome;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by kim on 2017-02-04.
 */

public class PostAsync extends AsyncTask<String, Integer, String> {
    Context context;
    String result;
    NetworkDialog netwrokDialog;
    String targetURL = String.format("http://192.168.43.104:3001/message");

    public PostAsync(Context context,String result) {
        this.context = context;
        this.result =result;
        netwrokDialog = new NetworkDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        netwrokDialog.show();
    }


    @Override
    protected String doInBackground(
            String... params) {
        Response response = null;
        try {
            //OKHttp3사용ㄴ
            OkHttpClient toServer = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "message=" + result);

            Request request = new Request.Builder()
                    .url(targetURL)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .post(body)
                    .build();
            //동기 방식
            response = toServer.newCall(request).execute();
            ResponseBody responseBody = response.body();
            // responseBody.string();
            boolean flag = response.isSuccessful();
            //응답 코드 200등등
            int responseCode = response.code();
            if (flag) {
                Log.e("넘어간값",result);
            }
        } catch (UnknownHostException une) {
            e("UnknownHost", une.toString());
        } catch (UnsupportedEncodingException uee) {
            e("UnsupportedEncoding", uee.toString());
        } catch (Exception e) {
            e("Exception", e.toString());
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        netwrokDialog.dismiss();

    }
}
