package com.example.jeongbin.mnu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton lunch_btn, weather_btn, bus_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkUtil.setNetworkPolicy();

        lunch_btn = (ImageButton)findViewById(R.id.M_lunch_btn);
        weather_btn = (ImageButton)findViewById(R.id.M_weather_btn);
        bus_btn = (ImageButton)findViewById(R.id.M_bus_btn);


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

        bus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
