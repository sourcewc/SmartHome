package com.appbaragi.smarthome;

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appbaragi.smarthome.util.AudioWriterPCM;
import com.naver.speech.clientapi.SpeechRecognitionResult;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;


public class SpeechFragment extends Fragment {

    private static final String TAG = SpeechFragment.class.getSimpleName();
    private static final String CLIENT_ID = "BdLMLYP5tL_MYFzS_ufJ";

    String temperature = null;
    String cds=null;
    TextView temperatureText;
    TextView cdsText;

    public MediaPlayer mp;

    static MainTab owner;

    // 1. "내 애플리케이션"에서 Client ID를 확인해서 이곳에 적어주세요.
    // 2. build.gradle (Module:app)에서 패키지명을 실제 개발자센터 애플리케이션 설정의 '안드로이드 앱 패키지 이름'으로 바꿔 주세요

    private RecognitionHandler handler;
    private NaverRecognizer naverRecognizer;

    private TextView txtResult;
    private ImageView btnStart;
    private String mResult;

    private AudioWriterPCM writer;

    // Handle speech recognition Messages.
    private void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady:
                // Now an user can speak.
                txtResult.setText("Connected");
                writer = new AudioWriterPCM(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/NaverSpeechTest");
                writer.open("Test");
                break;

            case R.id.audioRecording:
                writer.write((short[]) msg.obj);
                break;

            case R.id.partialResult:
                // Extract obj property typed with String.
                mResult = (String) (msg.obj);
                txtResult.setText(mResult);
                break;

            case R.id.finalResult:
                // Extract obj property typed with String array.
                // The first element is recognition result for speech.
                SpeechRecognitionResult speechRecognitionResult = (SpeechRecognitionResult) msg.obj;
                List<String> results = speechRecognitionResult.getResults();
                List<String> uniqueItems = new ArrayList<String>(new HashSet<String>(results));
                StringBuilder strBuf = new StringBuilder();
                for(String result : uniqueItems) {
                    strBuf.append(result);
                    Log.e("음성 메시지",result);

                    switch(result) {
                        case "에어컨 켜":
                            new PostAsync(owner,"에어컨 켜").execute();
                            mp.start();
                            break;

                        case "에어컨 꺼":
                            new PostAsync(owner,"에어컨 꺼").execute();
                            mp.start();
                            break;

                        case "목욕물 틀어줘":
                            new PostAsync(owner,"목욕물 켜").execute();
                            mp.start();
                            break;

                        case "불켜":
                            new PostAsync(owner,"불 켜").execute();
                            mp.start();
                            break;

                        case "불꺼":
                            new PostAsync(owner,"불 꺼").execute();
                            mp.start();
                            break;

                        default:
                            Log.e("인식결과값 없음",result);
//                            if(result)
                            break;
                    }
                    strBuf.append("\n");
                }
                mResult = strBuf.toString();
                txtResult.setText(mResult);
                break;

            case R.id.recognitionError:
                if (writer != null) {
                    writer.close();
                }

                mResult = "Error code : " + msg.obj.toString();
                txtResult.setText(mResult);
                btnStart.setImageResource(R.drawable.microphone);
                btnStart.setEnabled(true);
                break;

            case R.id.clientInactive:
                if (writer != null) {
                    writer.close();
                }

                btnStart.setImageResource(R.drawable.microphone);
                btnStart.setEnabled(true);
                break;
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main,container,false);

        owner = (MainTab) getActivity();
        txtResult = (TextView) view.findViewById(R.id.txt_result);
        btnStart = (ImageView) view.findViewById(R.id.btn_start);
        temperatureText =(TextView)view.findViewById(R.id.temperature_text);
        cdsText=(TextView)view.findViewById(R.id.cds_text);
        new TempGetAsync(owner).execute();
        mp = MediaPlayer.create(owner, R.raw.effectsound);
        handler = new RecognitionHandler(this);
        naverRecognizer = new NaverRecognizer(owner, handler, CLIENT_ID);

        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!naverRecognizer.getSpeechRecognizer().isRunning()) {
                    // Start button is pushed when SpeechRecognizer's state is inactive.
                    // Run SpeechRecongizer by calling recognize().
                    mResult = "";
                    txtResult.setText("Connecting...");
                    btnStart.setImageResource(R.drawable.microphone_on);
                    naverRecognizer.recognize();
                } else {
                    Log.d(TAG, "stop and wait Final Result");
                    btnStart.setEnabled(false);

                    naverRecognizer.getSpeechRecognizer().stop();
                }
            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        // NOTE : initialize() must be called on start time.
        naverRecognizer.getSpeechRecognizer().initialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        mResult = "";
        txtResult.setText("");
        btnStart.setImageResource(R.drawable.microphone);
        btnStart.setEnabled(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        // NOTE : release() must be called on stop time.
        naverRecognizer.getSpeechRecognizer().release();
    }

    // Declare handler for handling SpeechRecognizer thread's Messages.
    static class RecognitionHandler extends Handler {
        private final WeakReference<SpeechFragment> mActivity;

        RecognitionHandler(SpeechFragment activity) {
            mActivity = new WeakReference<SpeechFragment>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SpeechFragment activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }

    class TempGetAsync extends AsyncTask<String, Integer, String> {
        Context context;
        NetworkDialog networkDialog;

        String temper ;
        String cds ;

        public TempGetAsync(Context context) {
            this.context = context;
            networkDialog = new NetworkDialog(context);
        }


        String targetURL = String.format("http://192.168.43.104:3001/data");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            networkDialog.show();
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

                Request request = new Request.Builder()
                        .url(targetURL)
                        .build();
                //동기 방식
                response = toServer.newCall(request).execute();
                ResponseBody responseBody = response.body();
                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                if (flag) {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String returnMessage = jsonObject.optString("returnMessage");

                    temper = jsonObject.optString("temper");
                    cds =jsonObject.optString("cds");

                    Log.e("returnMessage",returnMessage);
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
            networkDialog.dismiss();

            temperatureText.setText(getString(R.string.tempurature,temper));
            cdsText.setText(getString(R.string.cdstext,cds));

            float checkTemp =Float.parseFloat(temper);

            if(checkTemp<=12) {
                showHitterDialog();
            } else if(checkTemp>=32) {
                showAirconDialog();
            }
        }

    }

    private void showAirconDialog() {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(owner);
        alt_bld.setMessage("에어컨을 켜시겠습니까 ?").setCancelable(
                false).setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'Yes' Button

                    }
                }).setNegativeButton("no",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    private void showHitterDialog() {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(owner);
        alt_bld.setMessage("히터를 켜시겠습니까 ?").setCancelable(
                false).setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'Yes' Button

                    }
                }).setNegativeButton("no",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }



}

