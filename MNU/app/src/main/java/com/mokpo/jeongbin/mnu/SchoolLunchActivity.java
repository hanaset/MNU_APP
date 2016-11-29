package com.mokpo.jeongbin.mnu;

import android.app.ProgressDialog;
import android.content.Intent;
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
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JeongBin on 2016-11-20.
 */

public class SchoolLunchActivity extends AppCompatActivity {

    TextView m_text, l_text, d_text, bm_text, bl_text, bd_text, date_text;

    TextView m_grade, l_grade, d_grade, bm_grade, bl_grade, bd_grade;

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

    private ProgressDialog progressDialog;


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

        progressDialog = ProgressDialog.show(SchoolLunchActivity.this, "로딩 중", "잠시 기달려주세요.",true);

        SchoolLunchActivity.JsoupAsyncTask jsoupAsyncTask = new SchoolLunchActivity.JsoupAsyncTask();
        jsoupAsyncTask.execute();

        progressDialog.dismiss();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);



                Date d = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월 dd일");

                for(int i = 0; i<7 ;i++){
                    if(date[i].equals(simpleDateFormat.format(d))){
                        page = i;
                        break;
                    }
                }



                setting_menu();
                setting();
                grade_setting();
                grade_day_setting();

                bm_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SchoolLunchActivity.this, LunchGradeActivity.class);
                        intent.putExtra("menu",bm_text.getText().toString());
                        intent.putExtra("location","BTL");
                        intent.putExtra("date", date_text.getText().toString());
                        intent.putExtra("day","오전");
                        startActivity(intent);
                    }
                });
                bl_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SchoolLunchActivity.this, LunchGradeActivity.class);
                        intent.putExtra("menu",bl_text.getText().toString());
                        intent.putExtra("location","BTL");
                        intent.putExtra("date", date_text.getText().toString());
                        intent.putExtra("day","오후");
                        startActivity(intent);
                    }
                });
                bd_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SchoolLunchActivity.this, LunchGradeActivity.class);
                        intent.putExtra("menu",bd_text.getText().toString());
                        intent.putExtra("location","BTL");
                        intent.putExtra("date", date_text.getText().toString());
                        intent.putExtra("day","저녁");
                        startActivity(intent);
                    }
                });
                m_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SchoolLunchActivity.this, LunchGradeActivity.class);
                        intent.putExtra("menu",m_text.getText().toString());
                        intent.putExtra("location","한울관");
                        intent.putExtra("date", date_text.getText().toString());
                        intent.putExtra("day","오전");
                        startActivity(intent);
                    }
                });
                l_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SchoolLunchActivity.this, LunchGradeActivity.class);
                        intent.putExtra("menu",l_text.getText().toString());
                        intent.putExtra("location","한울관");
                        intent.putExtra("date", date_text.getText().toString());
                        intent.putExtra("day","오후");
                        startActivity(intent);
                    }
                });
                d_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SchoolLunchActivity.this, LunchGradeActivity.class);
                        intent.putExtra("menu",d_text.getText().toString());
                        intent.putExtra("location","한울관");
                        intent.putExtra("date", date_text.getText().toString());
                        intent.putExtra("day","저녁");
                        startActivity(intent);
                    }
                });
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
                grade_day_setting();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++page;
                setting_menu();
                grade_day_setting();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

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

        progressDialog = ProgressDialog.show(SchoolLunchActivity.this, "로딩 중", "잠시 기달려주세요.",true);

        SchoolLunchActivity.JsoupAsyncTask jsoupAsyncTask = new SchoolLunchActivity.JsoupAsyncTask();
        jsoupAsyncTask.execute();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                progressDialog.dismiss();

                Date d = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월 dd일");

                for(int i = 0; i<7 ;i++){
                    if(date[i].equals(simpleDateFormat.format(d))){
                        page = i;
                        break;
                    }
                }



                setting_menu();
                setting();
                grade_setting();
                grade_day_setting();

                bm_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SchoolLunchActivity.this, LunchGradeActivity.class);
                        intent.putExtra("menu",bm_text.getText().toString());
                        intent.putExtra("location","BTL");
                        intent.putExtra("date", date_text.getText().toString());
                        intent.putExtra("day","오전");
                        startActivity(intent);
                    }
                });
                bl_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SchoolLunchActivity.this, LunchGradeActivity.class);
                        intent.putExtra("menu",bl_text.getText().toString());
                        intent.putExtra("location","BTL");
                        intent.putExtra("date", date_text.getText().toString());
                        intent.putExtra("day","오후");
                        startActivity(intent);
                    }
                });
                bd_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SchoolLunchActivity.this, LunchGradeActivity.class);
                        intent.putExtra("menu",bd_text.getText().toString());
                        intent.putExtra("location","BTL");
                        intent.putExtra("date", date_text.getText().toString());
                        intent.putExtra("day","저녁");
                        startActivity(intent);
                    }
                });
                m_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SchoolLunchActivity.this, LunchGradeActivity.class);
                        intent.putExtra("menu",m_text.getText().toString());
                        intent.putExtra("location","한울관");
                        intent.putExtra("date", date_text.getText().toString());
                        intent.putExtra("day","오전");
                        startActivity(intent);
                    }
                });
                l_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SchoolLunchActivity.this, LunchGradeActivity.class);
                        intent.putExtra("menu",l_text.getText().toString());
                        intent.putExtra("location","한울관");
                        intent.putExtra("date", date_text.getText().toString());
                        intent.putExtra("day","오후");
                        startActivity(intent);
                    }
                });
                d_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SchoolLunchActivity.this, LunchGradeActivity.class);
                        intent.putExtra("menu",d_text.getText().toString());
                        intent.putExtra("location","한울관");
                        intent.putExtra("date", date_text.getText().toString());
                        intent.putExtra("day","오후");
                        startActivity(intent);
                    }
                });
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
                grade_day_setting();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++page;
                setting_menu();
                grade_day_setting();
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
                    date[i/2] = links.get(i).text();
                    day[i/2] = links.get(i+1).text();
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

        date_text.setText(date[page] + " [" +day[page]+"]");

    }

    private void grade_day_setting(){

        try {
            PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/menu_grade_output.php");
            String m_result = request.PhPgrade_output(date[page] + " ["+day[page]+"]","오전");
            String[] m_string = m_result.split(",");

            bm_grade.setText(check_grade_text(m_string[0]));
            m_grade.setText(check_grade_text(m_string[1]));

            String l_result = request.PhPgrade_output(date[page] + " ["+day[page]+"]","오후");
            String[] l_string = l_result.split(",");

            bl_grade.setText(check_grade_text(l_string[0]));
            l_grade.setText(check_grade_text(l_string[1]));

            String d_result = request.PhPgrade_output(date[page] + " ["+day[page]+"]","저녁");
            String[] d_string = d_result.split(",");

            bd_grade.setText(check_grade_text(d_string[0]));
            d_grade.setText(check_grade_text(d_string[1]));


        }catch (MalformedURLException e){
            e.printStackTrace();
        }

    }

    String check_grade_text(String text){
        if(text.equals("-1")){
           return "평점 : X";
        }else{
            return "평점 : "+text;
        }
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


    private void grade_setting() {
        m_grade = (TextView) findViewById(R.id.SL_m_grade);
        l_grade = (TextView) findViewById(R.id.SL_l_grade);
        d_grade = (TextView) findViewById(R.id.SL_d_grade);
        bm_grade = (TextView) findViewById(R.id.SL_bm_grade);
        bl_grade = (TextView) findViewById(R.id.SL_bl_grade);
        bd_grade = (TextView) findViewById(R.id.SL_bd_grade);
    }
}
