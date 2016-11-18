package com.example.jeongbin.mnu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JeongBin on 2016-11-18.
 */

public class WeatherMainActivity extends AppCompatActivity {

    String mokpo_url = "http://weather.naver.com/rgn/cityWetrCity.nhn?cityRgnCd=CT011009";
    String muan_url = "http://weather.naver.com/rgn/cityWetrCity.nhn?cityRgnCd=CT011010";
    String Gangju_url = "http://weather.naver.com/rgn/cityWetrCity.nhn?cityRgnCd=CT011005";

    String[] Mokpo_temp = new String[2];
    String[] Muan_temp = new String[2];
    String[] Gangju_temp = new String[2];

    String[] Mokpo_info = new String[2];
    String[] Muan_info = new String[2];
    String[] Gangju_info = new String[2];

    ImageView[] imageViews = new ImageView[6];

    TextView[] textViews = new TextView[12];

    TextView date_text;

    public static Handler handler = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_main);



        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                setting();
            }
        };


        WeatherMainActivity.JsoupAsyncTask jsoupAsyncTask = new WeatherMainActivity.JsoupAsyncTask();
        jsoupAsyncTask.execute();
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
                Elements links = doc.select("[class=info]");

                for(int i = 0; i<2; i++) {
                    Mokpo_info[i] = links.get(i).text();
                }

                links = doc.select("[class=nm]");

                for(int i = 0; i<2; i++) {
                    Mokpo_temp[i] = links.get(i).text();
                }

                /////////

                doc = Jsoup.connect(muan_url).get();
                links = doc.select("[class=info]");

                for(int i = 0; i<2; i++) {
                    Muan_info[i] = links.get(i).text();
                }

                links = doc.select("[class=nm]");

                for(int i = 0; i<2; i++) {
                    Muan_temp[i] = links.get(i).text();
                }

                //////////


                doc = Jsoup.connect(Gangju_url).get();
                links = doc.select("[class=info]");

                for(int i = 0; i<2; i++) {
                    Gangju_info[i] = links.get(i).text();
                }

                links = doc.select("[class=nm]");

                for(int i = 0; i<2; i++) {
                    Gangju_temp[i] = links.get(i).text();
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

    private  void setting(){

        Date d = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월 dd일");

        textViews[0] = (TextView)findViewById(R.id.WM_MPamtemp);
        textViews[1] = (TextView)findViewById(R.id.WM_MPpmtemp);
        textViews[2] = (TextView)findViewById(R.id.WM_MPamhan);
        textViews[3] = (TextView)findViewById(R.id.WM_MPpmhan);

        textViews[4] = (TextView)findViewById(R.id.WM_MAamtemp);
        textViews[5] = (TextView)findViewById(R.id.WM_MApmtemp);
        textViews[6] = (TextView)findViewById(R.id.WM_MAamhan);
        textViews[7] = (TextView)findViewById(R.id.WM_MApmhan);

        textViews[8] = (TextView)findViewById(R.id.WM_GJamtemp);
        textViews[9] = (TextView)findViewById(R.id.WM_GJpmtemp);
        textViews[10] = (TextView)findViewById(R.id.WM_GJamhan);
        textViews[11] = (TextView)findViewById(R.id.WM_GJpmhan);

        imageViews[0] = (ImageView)findViewById(R.id.WM_MPamimage);
        imageViews[1] = (ImageView)findViewById(R.id.WM_MPpmimage);

        imageViews[2] = (ImageView)findViewById(R.id.WM_MAamimage);
        imageViews[3] = (ImageView)findViewById(R.id.WM_MApmimage);

        imageViews[4] = (ImageView)findViewById(R.id.WM_GJamimage);
        imageViews[5] = (ImageView)findViewById(R.id.WM_GJpmimage);

        ////////////////////////////////////////////////
        textViews[0].setText(Mokpo_temp[0]);
        textViews[1].setText(Mokpo_temp[1]);
        textViews[2].setText(Mokpo_info[0]);
        textViews[3].setText(Mokpo_info[1]);

        imageCheck(imageViews[0], Mokpo_info[0]);
        imageCheck(imageViews[1], Mokpo_info[1]);

        textViews[4].setText(Muan_temp[0]);
        textViews[5].setText(Muan_temp[1]);
        textViews[6].setText(Muan_info[0]);
        textViews[7].setText(Muan_info[1]);

        imageCheck(imageViews[2], Muan_info[0]);
        imageCheck(imageViews[3], Muan_info[1]);

        textViews[8].setText(Gangju_temp[0]);
        textViews[9].setText(Gangju_temp[1]);
        textViews[10].setText(Gangju_info[0]);
        textViews[11].setText(Gangju_info[1]);

        imageCheck(imageViews[4], Gangju_info[0]);
        imageCheck(imageViews[5], Gangju_info[1]);

        date_text = (TextView)findViewById(R.id.WM_today_text);
        date_text.setText(simpleDateFormat.format(d));

    }

    public void imageCheck(ImageView imageView, String text){
        if(text.contains("비")){
            imageView.setImageResource(R.drawable.w_l4);
        }else if(text.contains("구름")) {
            imageView.setImageResource(R.drawable.w_l3);
        }else if(text.contains("맑음")){
            imageView.setImageResource(R.drawable.w_t01);
        }else if(text.contains("흐림")){
            imageView.setImageResource(R.drawable.w_l3);
        }

    }
}
