package com.mokpo.jeongbin.mnu;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by JeongBin on 2016-11-20.
 */

public class BusActivity extends AppCompatActivity {

    Spinner go_spinner1, go_spinner2;
    Spinner come_spinner1, come_spinner2;

    private SQLiteDatabase db;
    String result = null;
    JSONArray array = null;

    Cursor c;
    ArrayList<HashMap<String,String>> busList;

    ListView list1, list2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);

        TabHost tabHost = (TabHost)findViewById(R.id.B_tabhost);
        tabHost.setup();

        TabHost.TabSpec first = tabHost.newTabSpec("first");
        first.setIndicator("등교 시간표");
        first.setContent(R.id.B_go_tab);
        tabHost.addTab(first);


        TabHost.TabSpec second = tabHost.newTabSpec("second");
        second.setIndicator("하교 시간표");
        second.setContent(R.id.B_come_tab);
        tabHost.addTab(second);

        tabHost.setCurrentTab(0);

        final BusDB Busdb = new BusDB(getApplicationContext(),"MNU.db",null, 1);

        try{
            db = Busdb.getReadableDatabase();

            spinner_setting1();

        }catch (SQLiteException e){
            e.printStackTrace();

            Busdb.onCreate(db);
        }

    }

    ////////////////////////////////////////////////////

    public void spinner_setting1(){
        ArrayList<String> GoList1 = new ArrayList<String>();
        ArrayList<String> ComeList1 = new ArrayList<String>();

        c = db.rawQuery("select * from bus where go_come = '등교' ORDER BY location ASC",null);
        while(c.moveToNext()){
            GoList1.add(c.getString(c.getColumnIndex("location")));
        }

        HashSet hs = new HashSet(GoList1);
        GoList1 = new ArrayList<>(hs);


        c = db.rawQuery("select * from bus where go_come = '하교' ORDER BY location ASC",null);
        while(c.moveToNext()){
            ComeList1.add(c.getString(c.getColumnIndex("location")));
        }

        hs = new HashSet(ComeList1);
        ComeList1 = new ArrayList<>(hs);

        go_spinner1 = (Spinner)this.findViewById(R.id.B_go_spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, GoList1);
        go_spinner1.setAdapter(adapter);

        come_spinner1 = (Spinner)this.findViewById(R.id.B_come_spinner1);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ComeList1);
        come_spinner1.setAdapter(adapter);

        go_spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                busList = new ArrayList<>();

                String string = go_spinner1.getSelectedItem().toString();
                Cursor c = db.rawQuery("select * from bus where go_come = '등교' and location = '"+ string +"' ORDER BY time ASC",null);

                while(c.moveToNext()){

                    HashMap<String, String> buses = new HashMap<String, String>();

                    buses.put("time",c.getString(c.getColumnIndex("time")));
                    buses.put("start", c.getString(c.getColumnIndex("start")));
                    String route = c.getString(c.getColumnIndex("route"));

                    route = route.replace("/","->\n");
                    route = route.replace("\r","");
                    buses.put("route",route);

                    busList.add(buses);
                }

                if(busList.size() == 0){
                    Toast.makeText(getApplication(),"스쿨 시간표를 불러오지 못했습니다.",Toast.LENGTH_SHORT).show();
                }else{
                    ListAdapter adapter = new SimpleAdapter(BusActivity.this, busList, R.layout.bus_item,
                            new String[]{"time","start","route"},
                            new int[]{R.id.Bitem_time, R.id.Bitem_start,R.id.Bitem_route});

                    list1 = (ListView)findViewById(R.id.B_go_list);
                    list1.setAdapter(adapter);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        come_spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                busList = new ArrayList<>();

                String string = come_spinner1.getSelectedItem().toString();
                Cursor c = db.rawQuery("select * from bus where go_come = '하교' and location = '"+ string +"' ORDER BY time ASC",null);

                while(c.moveToNext()){

                    HashMap<String, String> buses = new HashMap<String, String>();

                    buses.put("time",c.getString(c.getColumnIndex("time")));
                    buses.put("start", c.getString(c.getColumnIndex("start"))+"홈");
                    String route = c.getString(c.getColumnIndex("route"));

                    route = route.replace("/","->\n");
                    buses.put("route",route);

                    busList.add(buses);
                }

                if(busList.size() == 0){
                    Toast.makeText(getApplication(),"스쿨 시간표를 불러오지 못했습니다.",Toast.LENGTH_SHORT).show();
                }else{
                    ListAdapter adapter = new SimpleAdapter(BusActivity.this, busList, R.layout.bus_item,
                            new String[]{"time","start","route"},
                            new int[]{R.id.Bitem_time, R.id.Bitem_start,R.id.Bitem_route});

                    list1 = (ListView)findViewById(R.id.B_come_list);
                    list1.setAdapter(adapter);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }
}
