package com.mokpo.jeongbin.mnu;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageButton lunch_btn, weather_btn, bus_btn, book_btn,intranet_btn,qa_btn;

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


        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Nanum.ttf");

        ((TextView)findViewById(R.id.M_bus_text)).setTypeface(typeface);
        ((TextView)findViewById(R.id.M_lunch_text)).setTypeface(typeface);
        ((TextView)findViewById(R.id.M_book_text)).setTypeface(typeface);
        ((TextView)findViewById(R.id.M_weather_text)).setTypeface(typeface);
        ((TextView)findViewById(R.id.M_intranet_text)).setTypeface(typeface);




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
                Intent intent = new Intent(MainActivity.this, WeatherMainActivity.class);
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

}
