package com.example.jeongbin.mnu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by JeongBin on 2016-11-19.
 */

public class BookInfoActivity extends AppCompatActivity {

    public static Handler handler = null;
    String url;
    String name;
    String info;
    int list_check = 0;
    String image_url;

    TextView name_text, info_text;

    ArrayList<HashMap<String, String>> bookList;
    SimpleAdapter adapter;
    ListView list;

    public static ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        Intent intent = getIntent();
        url = intent.getExtras().getString("url");
        name = intent.getExtras().getString("name");
        info = intent.getExtras().getString("info");

        progressDialog = ProgressDialog.show(BookInfoActivity.this, "로딩 중", "잠시 기달려주세요.",true);

        BookInfoActivity.JsoupAsyncTask jsoupAsyncTask = new BookInfoActivity.JsoupAsyncTask();
        jsoupAsyncTask.execute();


        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                name_text = (TextView)findViewById(R.id.BIF_book_name);
                info_text = (TextView)findViewById(R.id.BIF_info);

                name_text.setText(name);

                info = info.replace("/","\n\n");
                info_text.setText(info);

                if(list_check == 1){
                    list = (ListView)findViewById(R.id.BIF_list);
                    list.setAdapter(adapter);
                }

               new DownloadImageTask((ImageView)findViewById(R.id.BIF_book_image)).execute(image_url);

                progressDialog.dismiss();

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
                Elements image = doc.select("[name=CoreImg]");
                Elements location = doc.select("[class=stock_location]");
                Elements callnumber = doc.select("[class=stock_callnumber]");
                Elements stat = doc.select("[class=stock_stat]");

                image_url = image.attr("src");

                doc = Jsoup.connect(image_url).get();

                image = doc.select("img");

                image_url = image.attr("src");

                bookList = new ArrayList<>();

                for(int i = 1; i<location.size();i++){

                    HashMap<String, String> books = new HashMap<String, String>();

                    books.put("location",location.get(i).text());
                    books.put("callnumber",callnumber.get(i).text());
                    books.put("stat",stat.get(i).text().replace("(","\n("));

                    bookList.add(books);

                }

                if(location.size() <= 1 || bookList.size() == 0){
                    Toast.makeText(getApplication(),"보유중인 책이 없습니다.",Toast.LENGTH_SHORT).show();
                    list_check = 0;
                }else{
                    adapter = new SimpleAdapter(BookInfoActivity.this, bookList, R.layout.book_info_item,
                            new String[]{"location","callnumber","stat"},
                            new int[]{R.id.BIFI_position,R.id.BIFI_num,R.id.BIFI_state});
                    list_check = 1;
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


    ////////////////////////////////////////////////////////////////////////////////////////
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
