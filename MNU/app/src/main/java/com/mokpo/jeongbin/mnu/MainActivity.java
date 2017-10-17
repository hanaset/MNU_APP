package com.mokpo.jeongbin.mnu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ImageButton lunch_btn, weather_btn, bus_btn, book_btn,intranet_btn,qa_btn;
    TextView weather_text ;
    String tmp;

    public static Handler handler = null;

    public static ProgressDialog progressDialog;

    String url = "http://weather.naver.com/rgn/townWetr.nhn?naverRgnCd=12840330";

    Toolbar toolbar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(MainActivity.this, StartActivity.class));

        NetworkUtil.setNetworkPolicy();

        toolbar = (Toolbar)findViewById(R.id.M_toolbar);

        lunch_btn = (ImageButton)findViewById(R.id.M_lunch_btn);
        weather_btn = (ImageButton)findViewById(R.id.M_weather_btn);
        bus_btn = (ImageButton)findViewById(R.id.M_bus_btn);
        book_btn = (ImageButton)findViewById(R.id.M_book_btn);
        intranet_btn = (ImageButton)findViewById(R.id.M_intranet_btn);
        qa_btn = (ImageButton)findViewById(R.id.M_QA_Btn);

        weather_text = (TextView)findViewById(R.id.M_weather);


        final Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Nanum.ttf");

        progressDialog = ProgressDialog.show(MainActivity.this, "로딩 중", "잠시 기달려주세요.",true);

        MainActivity.JsoupAsyncTask jsoupAsyncTask = new MainActivity.JsoupAsyncTask();
        jsoupAsyncTask.execute();


        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                progressDialog.dismiss();

                ((TextView)findViewById(R.id.M_bus_text)).setTypeface(typeface);
                ((TextView)findViewById(R.id.M_lunch_text)).setTypeface(typeface);
                ((TextView)findViewById(R.id.M_book_text)).setTypeface(typeface);
                ((TextView)findViewById(R.id.M_weather_text)).setTypeface(typeface);
                ((TextView)findViewById(R.id.M_intranet_text)).setTypeface(typeface);
                ((TextView)findViewById(R.id.M_weather)).setTypeface(typeface);

                weather_text.setText(tmp);

                lunch_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, SchoolLunchActivity.class);
                        startActivity(intent);
                    }
                });

                weather_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                        startActivity(intent);
                    }
                });

                book_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, BookMainActivity.class);
                        startActivity(intent);
                    }
                });

                intranet_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, IntranetActivity.class);
                        startActivity(intent);
                    }
                });

                qa_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, QAinfoActivity.class);
                        startActivity(intent);
                    }
                });

                bus_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, BusActivity.class);
                        startActivity(intent);
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
                Document doc = Jsoup.connect(url).get();
                Elements links = doc.select("[class=w_now2]");

                String test  = links.text();
                String[] test2 = test.split(" ");

                tmp = test2[1] +" "+ test2[2] + " " + test2[3];

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

}
