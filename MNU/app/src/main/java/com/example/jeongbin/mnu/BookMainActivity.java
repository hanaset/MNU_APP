package com.example.jeongbin.mnu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by JeongBin on 2016-11-18.
 */

public class BookMainActivity extends AppCompatActivity {

    ArrayList<HashMap<String, String>> bookList;
    ListView list;

    String prev_url = "http://library.mokpo.ac.kr/DLiWeb25/comp/search/Results.aspx?m_var=505&srv=95&method=0&field=";
    String next_url = "&sType2=PUBN&keyword=";

    String url = null;
    int check_error = 0;

    ListAdapter adapter2;

    public static Handler handler = null;

    String subject_text, subject, name;

    Button search_btn;
    EditText editText;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_main);

        final ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("책 이름");
        arrayList.add("저자");
        arrayList.add("출판사");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayList);
        bookList = new ArrayList<>();

        final Spinner spinner = (Spinner)findViewById(R.id.BM_spinner);
        spinner.setAdapter(adapter);

        search_btn = (Button) findViewById(R.id.BM_search_btn);
        editText = (EditText)findViewById(R.id.BM_search_edit);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                progressDialog.dismiss();

                if(check_error == 1) {

                    list = (ListView) findViewById(R.id.BM_book_list);
                    list.setAdapter(adapter2);



                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent(BookMainActivity.this, BookInfoActivity.class);
                            HashMap<String, String> books =  bookList.get(position);
                            intent.putExtra("url","http://library.mokpo.ac.kr"+books.get("url"));
                            intent.putExtra("info",books.get("info"));
                            intent.putExtra("name",books.get("name"));
                            startActivity(intent);
                        }
                    });


                }else{
                    Toast.makeText(getApplication(), "검색 내용이 없습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        };

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().length() <= 0) {
                    return;
                } else {
                    subject_text = spinner.getSelectedItem().toString();

                    if(subject_text.equals("책 이름")){
                        subject = "TITL";
                    }else if(subject_text.equals("저자")){
                        subject = "AUTH";
                    }else if(subject_text.equals("출판사")){
                        subject = "PUBN";
                    }
                    name = editText.getText().toString();

                    url = prev_url + subject + next_url + name;

                    progressDialog = ProgressDialog.show(BookMainActivity.this, "로딩 중", "잠시 기달려주세요.",true);

                    BookMainActivity.JsoupAsyncTask jsoupAsyncTask = new BookMainActivity.JsoupAsyncTask();
                    jsoupAsyncTask.execute();
                }
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
                Elements title = doc.select("[class=title]");
                Elements info = doc.select("[class=advance_bibliography]");

                bookList = new ArrayList<>();

                for(int i = 0; i<title.size();i++){

                    HashMap<String, String> books = new HashMap<String, String>();

                    books.put("name", title.get(i).text());
                    books.put("info", info.get(i).text());
                    books.put("url",  title.get(i).attr("href"));

                    bookList.add(books);

                }

                if(title.size() <= 1 || bookList.size() == 0){

                    check_error = 0;
                }else{
                    adapter2 = new SimpleAdapter(BookMainActivity.this, bookList, R.layout.book_item,
                            new String[]{"name","info","url"},
                            new int[]{R.id.BI_book_name, R.id.BI_book_info});

                    check_error = 1;
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

}
