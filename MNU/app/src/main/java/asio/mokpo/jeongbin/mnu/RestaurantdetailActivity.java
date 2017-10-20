package asio.mokpo.jeongbin.mnu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by b1835 on 2017-10-20.
 */

public class RestaurantdetailActivity extends AppCompatActivity {

    TextView name_text, phone_text, time_text, notice_text;
    ImageView image;
    ListView listView;
    JSONArray food_array = null;
    ArrayList<HashMap<String,String>> food_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        name_text = (TextView)findViewById(R.id.RSD_name_text);
        phone_text = (TextView)findViewById(R.id.RSD_phone_text);
        time_text = (TextView)findViewById(R.id.RSD_time_text);
        notice_text = (TextView)findViewById(R.id.RSD_notice_text);
        image = (ImageView)findViewById(R.id.RSD_image);

        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");

        food_list = new ArrayList<>();

        name_text.setText(name);

        try{
            PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/restaurant_detail.php");
            String result = request.PhPrestaurant_detail(name);
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray nameAarry = jsonObject.getJSONArray("result");

                JSONObject c = nameAarry.getJSONObject(0);

                phone_text.setText(c.getString("phone"));
                // Linkify.addLinks(phone_text, Linkify.PHONE_NUMBERS);
                time_text.setText(c.getString("time"));
                notice_text.setText(c.getString("notice"));
                String url = c.getString("image");

                if(!url.isEmpty()) {
                    Picasso.with(getApplicationContext()).load(c.getString("image")).into(image);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        try{
            PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/food_menu.php");
            String result = request.PhPfood_name(name);

            try{
                JSONObject jsonObject = new JSONObject(result);
                food_array = jsonObject.getJSONArray("result");

                for(int i = 0; i<food_array.length();i++){
                    HashMap<String, String> food = new HashMap<String, String>();

                    JSONObject c = food_array.getJSONObject(i);

                    food.put("food_name",c.getString("food_name"));
                    food.put("price",c.getString("price"));

                    food_list.add(food);
                }


                if(food_list.size() == 0){
                    Toast.makeText(getApplication(), "메뉴를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    ListAdapter adapter = new SimpleAdapter(RestaurantdetailActivity.this, food_list, R.layout.restaurant_detail_item, new String[] {"food_name","price"},
                            new int[] { R.id.RSD_item_name_text, R.id.RSD_item_price_text});

                    listView = (ListView)findViewById(R.id.RSD_list);
                    listView.setAdapter(adapter);
                }
            }catch (JSONException e){
                Toast.makeText(getApplication(), "메뉴를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }catch (MalformedURLException e){
            Toast.makeText(getApplication(), "메뉴를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
