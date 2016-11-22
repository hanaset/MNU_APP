package com.mokpo.jeongbin.mnu;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by JeongBin on 2016-11-22.
 */

public class WeatherActivity extends AppCompatActivity {

    TextView[] dayViews = new TextView[7];
    TextView[] infoViews = new TextView[14];

    String[] MP_day = new String[7];
    String[] MP_info = new String[14];

    String[] GJ_day = new String[7];
    String[] GJ_info = new String[14];

    String[] MA_day = new String[7];
    String[] MA_info = new String[14];

    Button mokpo_btn, gangju_btn, muan_btn;

    public static ProgressDialog progressDialog;

    String mokpo_url = "http://weather.naver.com/rgn/cityWetrCity.nhn?cityRgnCd=CT011009";
    String muan_url = "http://weather.naver.com/rgn/cityWetrCity.nhn?cityRgnCd=CT011010";
    String gangju_url = "http://weather.naver.com/rgn/cityWetrCity.nhn?cityRgnCd=CT011005";


    public static Handler handler = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        progressDialog = ProgressDialog.show(WeatherActivity.this, "로딩 중", "잠시 기달려주세요.",true);

        WeatherActivity.JsoupAsyncTask jsoupAsyncTask = new WeatherActivity.JsoupAsyncTask();
        jsoupAsyncTask.execute();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                setting();
                text_setting(MP_day,MP_info);

                progressDialog.dismiss();

                final TextView textView = (TextView) findViewById(R.id.W_main_text);
                textView.setText("주간 예보 : 목포");

                mokpo_btn = (Button)findViewById(R.id.W_mp_btn);
                gangju_btn = (Button)findViewById(R.id.W_gj_btn);
                muan_btn = (Button)findViewById(R.id.W_ma_btn);

                mokpo_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.setText("주간 예보 : 목포");

                        text_setting(MP_day,MP_info);

                    }
                });

                gangju_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.setText("주간 예보 : 광주");

                        text_setting(GJ_day, GJ_info);

                    }
                });

                muan_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.setText("주간 예보 : 무안");

                        text_setting(MA_day, MA_info);

                    }
                });



            }
        };
    }


    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(mokpo_url).get();
                Elements links = doc.select("[scope]");

                for(int i = 0 ; i < 7 ; i++){

                    if(i>=2){
                        MP_day[i] = "\n"+links.get(i).text();
                        MP_day[i].replace(" ","\n");
                    }else
                    {
                        MP_day[i] = links.get(i).text();
                    }
                }

                links = doc.select("[class=cell]");

                for(int i = 0 ; i < 14; i++){
                    MP_info[i] = "\n" + links.get(i).text();

                    MP_info[i].replace(" 강","\n강");
                }
///////////////////////////////////////////////////////
                doc = Jsoup.connect(gangju_url).get();
                links = doc.select("[scope]");

                for(int i = 0 ; i < 7 ; i++){

                    if(i>=2){
                        GJ_day[i] = "\n"+links.get(i).text();
                        GJ_day[i].replace(" ","\n");
                    }else
                    {
                        GJ_day[i] = links.get(i).text();
                    }
                }

                links = doc.select("[class=cell]");

                for(int i = 0 ; i < 14; i++){
                    GJ_info[i] = "\n" + links.get(i).text();

                    GJ_info[i].replace(" 강","\n강");
                }
                ///////////////////////////////////////////////////////

                doc = Jsoup.connect(muan_url).get();
                links = doc.select("[scope]");

                for(int i = 0 ; i < 7 ; i++){

                    if(i>=2){
                        MA_day[i] = "\n"+links.get(i).text();
                        MA_day[i].replace(" ","\n");
                    }else
                    {
                        MA_day[i] = links.get(i).text();
                    }
                }

                links = doc.select("[class=cell]");

                for(int i = 0 ; i < 14; i++){
                    MA_info[i] = "\n" + links.get(i).text();

                    MA_info[i].replace(" 강","\n강");
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            handler.sendEmptyMessage(0);
        }
    }

    private void setting(){

        infoViews[0] = (TextView)findViewById(R.id.W_today_a);
        infoViews[1] = (TextView)findViewById(R.id.W_today_p);
        infoViews[2] = (TextView)findViewById(R.id.W_tomorrow_a);
        infoViews[3] = (TextView)findViewById(R.id.W_tomorrow_p);

        infoViews[4] = (TextView)findViewById(R.id.W_day1_a);
        infoViews[5] = (TextView)findViewById(R.id.W_day1_p);

        infoViews[6] = (TextView)findViewById(R.id.W_day2_a);
        infoViews[7] = (TextView)findViewById(R.id.W_day2_p);

        infoViews[8] = (TextView)findViewById(R.id.W_day3_a);
        infoViews[9] = (TextView)findViewById(R.id.W_day3_p);

        infoViews[10] = (TextView)findViewById(R.id.W_day4_a);
        infoViews[11] = (TextView)findViewById(R.id.W_day4_p);

        infoViews[12] = (TextView)findViewById(R.id.W_day5_a);
        infoViews[13] = (TextView)findViewById(R.id.W_day5_p);

        dayViews[0] = (TextView)findViewById(R.id.W_today_text);
        dayViews[1] = (TextView)findViewById(R.id.W_tomorrow_text);

        dayViews[2] = (TextView)findViewById(R.id.W_day1_text);
        dayViews[3] = (TextView)findViewById(R.id.W_day2_text);
        dayViews[4] = (TextView)findViewById(R.id.W_day3_text);
        dayViews[5] = (TextView)findViewById(R.id.W_day4_text);
        dayViews[6] = (TextView)findViewById(R.id.W_day5_text);
    }

    private void text_setting(String[] day, String[] info){
        for(int i=0;i<7;i++){
            dayViews[i].setText(day[i]);
        }

        for(int i=0;i<14;i++){
            infoViews[i].setText(info[i]);
        }
    }

}
