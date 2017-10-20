package asio.mokpo.jeongbin.mnu;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by b1835 on 2017-10-20.
 */

public class RestaurantActivity extends AppCompatActivity {

    ArrayList<HashMap<String, String>> name_list;
    ListView listView;
    JSONArray nameArray = null;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        final ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("전체 보기");
        arrayList.add("배달 가능");

        progressDialog = ProgressDialog.show(RestaurantActivity.this, "로딩중 ", "잠시만 기다려주세요.", true);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayList);
        name_list = new ArrayList<>();

        final Spinner spinner = (Spinner)findViewById(R.id.RS_spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            int delivery;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                name_list = new ArrayList<>();

                switch (position){
                    case 0:
                        delivery = 0;
                        break;
                    case 1:
                        delivery = 1;
                        break;
                }
                try{
                    PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/restaurant.php");
                    String result = request.PhPrestaurant_search(delivery);
                    try{
                        JSONObject jsonObject = new JSONObject(result);
                        nameArray = jsonObject.getJSONArray("result");

                        for(int i = 0 ;i<nameArray.length();i++){
                            HashMap<String, String> name = new HashMap<String, String>();

                            JSONObject c = nameArray.getJSONObject(i);

                            name.put("name",c.getString("name"));
                            name.put("notice", c.getString("notice"));

                            name_list.add(name);
                        }

                        if(name_list.size() == 0){
                            Toast.makeText(getApplication(), "가게 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            ListAdapter adapter = new SimpleAdapter(RestaurantActivity.this, name_list, R.layout.restaurant_item,
                                    new String[] {"name", "notice"},
                                    new int[] {R.id.RS_item_name_text, R.id.RS_item_notice_text});

                            listView = (ListView)findViewById(R.id.RS_list);
                            listView.setAdapter(adapter);


                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                     Intent intent = new Intent(RestaurantActivity.this, RestaurantdetailActivity.class);
                                     intent.putExtra("name", name_list.get(position).get("name"));
                                    startActivity(intent);
                                }
                            });

                        }
                        progressDialog.dismiss();

                    }catch (JSONException e){
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                }catch (MalformedURLException e){
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


}
