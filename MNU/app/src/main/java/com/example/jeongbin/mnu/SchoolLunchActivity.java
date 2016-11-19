package com.example.jeongbin.mnu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JeongBin on 2016-11-20.
 */

public class SchoolLunchActivity extends AppCompatActivity {

    TextView m_text, l_text, d_text, bm_text, bl_text, bd_text, date_text;

    TextView mon, tur, wed, thu, fri;

    String Tmon, Ttur, Twed, Tthu, Tfri;

    String url2 = "http://www.mokpo.ac.kr/index.9is?contentUid=402848ef42b36e680142bbed26d102f7";



    String[] morning = new String[7];
    String[] lunch = new String[7];
    String[] dinner = new String[7];

    String[] btl_morning = new String[7];
    String[] btl_lunch = new String[7];
    String[] btl_dinner = new String[7];

    int page = 0;

    String[] day = new String[7];
    String[] date = new String[7];

    String url = "http://dormi.mokpo.ac.kr/www/bbs/board.php?bo_table=food";
    String btl_url = "http://dormi.mokpo.ac.kr/www/bbs/board.php?bo_table=food_btl";

    public static Handler handler = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_lunch);
        setTitle("오늘 급식은 어떠니?");

        TabHost tabHost = (TabHost)findViewById(R.id.SL_tabhost);
        tabHost.setup();

        TabHost.TabSpec first = tabHost.newTabSpec("first");
        first.setIndicator("기숙사 식당");
        first.setContent(R.id.SL_tab1);
        tabHost.addTab(first);


        TabHost.TabSpec second = tabHost.newTabSpec("second");
        second.setIndicator("학식 및 교수식당");
        second.setContent(R.id.SL_tab2);
        tabHost.addTab(second);

        tabHost.setCurrentTab(0);

        SchoolLunchActivity.JsoupAsyncTask jsoupAsyncTask = new SchoolLunchActivity.JsoupAsyncTask();
        jsoupAsyncTask.execute();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Date d = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월 dd일");

                for(int i = 0; i<7 ;i++){
                    if(day[i].equals(simpleDateFormat.format(d))){
                        page = i;
                        break;
                    }
                }

                setting_menu();
                setting();
            }
        };

        Button back_btn, next_btn;

        back_btn = (Button)findViewById(R.id.SL_prev_btn);
        next_btn = (Button)findViewById(R.id.SL_next_btn);


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                --page;
                setting_menu();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++page;
                setting_menu();
            }
        });


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
                Elements links = doc.select("[width=28%]");

                //morning = links.text();

                for(int i = 6 ; i <27; i+=3){
                    morning[(i-6)/3] = links.get(i).text();
                    morning[(i-6)/3] = morning[(i-6)/3].replace(" ","\n");
                    lunch[(i-6)/3] = links.get(i+1).text();
                    lunch[(i-6)/3] = lunch[(i-6)/3].replace(" ","\n");
                    dinner[(i-6)/3] = links.get(i+2).text();
                    dinner[(i-6)/3] = dinner[(i-6)/3].replace(" ","\n");
                }

                doc = Jsoup.connect(btl_url).get();
                links = doc.select("[width=28%]");

                for(int i = 6 ; i <27; i+=3){
                    btl_morning[(i-6)/3] = links.get(i).text();
                    btl_morning[(i-6)/3] = btl_morning[(i-6)/3].replace(" ","\n");
                    btl_lunch[(i-6)/3] = links.get(i+1).text();
                    btl_lunch[(i-6)/3] = btl_lunch[(i-6)/3].replace(" ","\n");
                    btl_dinner[(i-6)/3] = links.get(i+2).text();
                    btl_dinner[(i-6)/3] = btl_dinner[(i-6)/3].replace(" ","\n");
                }

                links = doc.select("[class=day7]");

                for(int i = 0; i<14; i+=2){
                    day[i/2] = links.get(i).text();
                    date[i/2] = links.get(i+1).text();
                }

                doc = Jsoup.connect(url2).get();
                links = doc.select("[class=list]");

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

    private void setting_menu(){

        m_text = (TextView)findViewById(R.id.SL_morning_menu);
        l_text = (TextView)findViewById(R.id.SL_lunch_menu);
        d_text = (TextView)findViewById(R.id.SL_dinner_menu);

        bm_text = (TextView)findViewById(R.id.SL_bmorning_menu);
        bl_text = (TextView)findViewById(R.id.SL_blunch_menu);
        bd_text = (TextView)findViewById(R.id.SL_bdinner_menu);

        date_text = (TextView)findViewById(R.id.SL_date_text);


        if(page < 0){
            Toast.makeText(getApplication(),"더 이상 식단표가 없습니다.",Toast.LENGTH_SHORT).show();
            page = 0;
            return;
        }else if(page > 6){
            Toast.makeText(getApplication(),"더 이상 식단표가 없습니다.",Toast.LENGTH_SHORT).show();
            page = 6;
            return;
        }


        m_text.setText(morning[page]);
        l_text.setText(lunch[page]);
        d_text.setText(dinner[page]);

        bm_text.setText(btl_morning[page]);
        bl_text.setText(btl_lunch[page]);
        bd_text.setText(btl_dinner[page]);

        date_text.setText(day[page] + " [" +date[page]+"]");

    }

    private void setting(){
        mon = (TextView)findViewById(R.id.SL2_mon_menu);
        tur = (TextView)findViewById(R.id.SL2_tur_menu);
        wed = (TextView)findViewById(R.id.SL2_wed_menu);
        thu = (TextView)findViewById(R.id.SL2_thu_menu);
        fri = (TextView)findViewById(R.id.SL2_fri_menu);


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
