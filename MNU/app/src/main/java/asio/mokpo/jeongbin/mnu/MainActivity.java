package asio.mokpo.jeongbin.mnu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ImageButton lunch_btn, bus_btn, book_btn, backdoor_btn;
    TextView weather_text ;
    String tmp;
    Toolbar toolbar;
    DrawerLayout dlDrawer;
    ActionBarDrawerToggle dtToggle;
    ListView listView;
    String[] ListItem = {"오늘의 날씨", "학교 인트라넷", "기숙사 인트라넷", "학교 지도", "중앙도서관 검색", "문의하기"};
    final int Weather = 0, School_intranet = 1, Intranet = 2, School_map = 3, Book_search = 4, Qa = 5;

    public static Handler handler = null;

    public static ProgressDialog progressDialog;

    String url = "http://weather.naver.com/rgn/townWetr.nhn?naverRgnCd=12840330";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(MainActivity.this, StartActivity.class));

        NetworkUtil.setNetworkPolicy();

        listView = (ListView) findViewById(R.id.main_drawer);
        toolbar = (Toolbar) findViewById(R.id.M_toolbar2);
        dlDrawer = (DrawerLayout) findViewById(R.id.main_draw_layout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ListItem));

        dtToggle = new ActionBarDrawerToggle(this, dlDrawer, 0, 0);
        dlDrawer.setDrawerListener(dtToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        lunch_btn = (ImageButton)findViewById(R.id.M_lunch_btn);
        bus_btn = (ImageButton)findViewById(R.id.M_bus_btn);
        book_btn = (ImageButton)findViewById(R.id.M_book_btn);
        backdoor_btn = (ImageButton)findViewById(R.id.M__backdoor_btn);

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
                ((TextView)findViewById(R.id.M_backdoor_text)).setTypeface(typeface);
                ((TextView)findViewById(R.id.M_weather)).setTypeface(typeface);

                weather_text.setText(tmp);

                listView.setOnItemClickListener(new DrawerItemClickListener());

                lunch_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, SchoolLunchActivity.class);
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

                backdoor_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                   //     Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://intra.mokpo.ac.kr:7777/mobile/Login.htm"));
                   //     startActivity(intent);
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


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        dtToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dtToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (dtToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position){
        Intent intent;
        switch(position){
            case Weather: // 오늘의 날씨
                intent = new Intent(MainActivity.this, WeatherActivity.class);
                startActivity(intent);
                break;
            case School_intranet:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://intra.mokpo.ac.kr:7777/mobile/Login.htm"));
                startActivity(intent);
                break;
            case Intranet: // 기숙사 인트라넷
                intent = new Intent(MainActivity.this, IntranetActivity.class);
                startActivity(intent);
                break;
            case School_map: // 학교 지도
                intent = new Intent(MainActivity.this, SchoolMapActivity.class);
                startActivity(intent);
                break;
            case Book_search: // 중앙 도서관 책 검색
                intent = new Intent(MainActivity.this, BookMainActivity.class);
                startActivity(intent);
                break;
            case Qa: // 문의하기
                intent = new Intent(MainActivity.this, QAinfoActivity.class);
                startActivity(intent);
                break;
        }
    }
}


