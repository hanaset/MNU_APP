package com.example.jeongbin.mnu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton lunch_btn, weather_btn, bus_btn, book_btn,intranet_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkUtil.setNetworkPolicy();

        lunch_btn = (ImageButton)findViewById(R.id.M_lunch_btn);
        weather_btn = (ImageButton)findViewById(R.id.M_weather_btn);
        bus_btn = (ImageButton)findViewById(R.id.M_bus_btn);
        book_btn = (ImageButton)findViewById(R.id.M_book_btn);
        intranet_btn = (ImageButton)findViewById(R.id.M_intranet_btn);


        lunch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LunchMainActivity.class);
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

    }
}
