package com.mokpo.jeongbin.mnu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by JeongBin on 2016-11-23.
 */

public class LunchGradeActivity extends AppCompatActivity {

    TextView date_text, menu_text, grade_text, location_text;
    RatingBar ratingBar;
    Button button;
    EditText content_edit;

    String date;


    ArrayList<HashMap<String,String>> gradeList;

    ListView list;

    JSONArray grade;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_grade);

        Intent intent = getIntent();
        date = intent.getExtras().getString("date");
        final String day = intent.getExtras().getString("day");

        String menu = intent.getExtras().getString("menu");
        final String location = intent.getExtras().getString("location");

        ratingBar = (RatingBar)findViewById(R.id.LG_ratingbar);

        ratingBar.setMax(5);
        ratingBar.setStepSize((float) 1.0);
        ratingBar.setIsIndicator(false);
        ratingBar.setStepSize((float)0.5);



        date_text = (TextView)findViewById(R.id.LG_title_text);
        menu_text = (TextView)findViewById(R.id.LG_menu_text);
        grade_text = (TextView)findViewById(R.id.LG_rating_text);
        location_text = (TextView)findViewById(R.id.LG_location);

        date_text.setText(date);

        location_text.setText("["+location+"]");
        menu_text.setText(menu);

        button = (Button)findViewById(R.id.LG_confirm_btn);
        content_edit = (EditText)findViewById(R.id.LG_grade_edit);

        grade_text.setText(String.valueOf(ratingBar.getRating()));

        gradeList = new ArrayList<>();


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                grade_text.setText(rating+"");
            }
        });


        try{
            PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/grade_output.php");


            String result = request.PhPgrade_output(date, location, day);

            if(result.equals("-1")){
                Toast.makeText(getApplication(),"게시판에 오류가 발생했습니다.",Toast.LENGTH_SHORT).show();
                finish();

            }else {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    grade = jsonObject.getJSONArray("result");

                    for (int i = 0; i < grade.length(); i++) {
                        JSONObject c = grade.getJSONObject(i);

                        HashMap<String, String> grades = new HashMap<String, String>();

                        grades.put("score", c.getString("score"));
                        grades.put("content", c.getString("content"));

                        gradeList.add(grades);
                    }

                    if(grade.length() > 0) {
                        ListAdapter adapter = new SimpleAdapter(LunchGradeActivity.this, gradeList, R.layout.grade_item,
                                new String[]{"content", "score"},
                                new int[]{R.id.GI_item_content, R.id.GI_item_score});
                        list = (ListView) findViewById(R.id.LG_grade_list);
                        list.setAdapter(adapter);
                    }else{
                        Toast.makeText(getApplication(),"한줄 평가가 하나도 없습니다.",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }catch(MalformedURLException e){
            e.printStackTrace();
        }


        ////////////////////////////////////////////////////////////////////////////////////////////


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/grade_input.php");
                    if(content_edit.length() > 0){

                        String result = request.PhPgrade_input(date, location, content_edit.getText().toString(), String.valueOf(ratingBar.getRating()),day);

                        if(result.equals("1")){
                            Toast.makeText(getApplication(),"성공적으로 평점이 입력되었습니다.",Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(getApplication(),"평점 입력에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(getApplication(),"한줄 평가를 입력하세요.",Toast.LENGTH_SHORT).show();
                    }
                }catch(MalformedURLException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
