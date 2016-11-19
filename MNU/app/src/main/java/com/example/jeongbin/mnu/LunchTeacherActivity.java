package com.example.jeongbin.mnu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by JeongBin on 2016-11-20.
 */

public class LunchTeacherActivity extends AppCompatActivity {

    TextView mon, tur, wed, thu, fri;

    String Tmon, Ttur, Twed, Tthu, Tfri;

    public static Handler handler = null;

    String url = "http://www.mokpo.ac.kr/index.9is?contentUid=402848ef42b36e680142bbed26d102f7";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_teacher_lunch);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                setting();
            }
        };

        LunchTeacherActivity.JsoupAsyncTask jsoupAsyncTask = new LunchTeacherActivity.JsoupAsyncTask();
        jsoupAsyncTask.execute();
    }

    /////////////////////////////////////////////////////////////////////////////

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements links = doc.select("[class=list]");

                Tmon = links.get(0).text();
                Tmon = check(Tmon);
                Ttur = links.get(1).text();
                Ttur = check(Ttur);
                Twed = links.get(2).text();
                Twed = check(Twed);
                Tthu = links.get(3).text();
                Tthu = check(Tthu);
                Tfri = links.get(4).text();
                Tfri = check(Tfri);

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
        mon = (TextView)findViewById(R.id.TL_mon_menu);
        tur = (TextView)findViewById(R.id.TL_tur_menu);
        wed = (TextView)findViewById(R.id.TL_wed_menu);
        thu = (TextView)findViewById(R.id.TL_thu_menu);
        fri = (TextView)findViewById(R.id.TL_fri_menu);


        mon.setText(Tmon);
        tur.setText(Ttur);
        wed.setText(Twed);
        thu.setText(Tthu);
        fri.setText(Tfri);
    }

    private String check(String str){

        str = str.replace(" ","\n");
        str = str.replace(",","");
        return str;
    }

}
