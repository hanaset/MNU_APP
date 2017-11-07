package asio.mokpo.jeongbin.mnu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
    Bitmap bitmap;
    String image_url;
    String server = "http://114.70.93.130/mnu/";

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
        String id = intent.getExtras().getString("id");

        food_list = new ArrayList<>();

        name_text.setText(name);

        try{
            PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/restaurant_count.php");
            String result = request.PhPfood_name(id);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        try{
            PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/restaurant_detail.php");
            String result = request.PhPrestaurant_detail(id);
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray nameAarry = jsonObject.getJSONArray("result");

                JSONObject c = nameAarry.getJSONObject(0);

                phone_text.setText("연락처 : " + c.getString("phone"));
                time_text.setText("영업시간 : " + c.getString("time"));
                notice_text.setText("알림 : " + c.getString("notice"));
                image_url = c.getString("image");

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            URL url = new URL(server + image_url);
                            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                            connection.setDoInput(true);
                            connection.connect();
                            InputStream inputStream = connection.getInputStream();
                            bitmap = BitmapFactory.decodeStream(inputStream);
                        }catch (MalformedURLException e){
                            e.printStackTrace();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                });
                if (!image_url.toString().isEmpty()){
                    t.start();
                    try{
                        t.join();
                        image.setImageBitmap(bitmap);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        try{
            PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/food_menu.php");
            String result = request.PhPfood_name(id);

            try{
                JSONObject jsonObject = new JSONObject(result);
                food_array = jsonObject.getJSONArray("result");

                for(int i = 0; i<food_array.length();i++){
                    HashMap<String, String> food = new HashMap<String, String>();

                    JSONObject c = food_array.getJSONObject(i);

                    food.put("food_name",c.getString("food_name"));
                    food.put("price",c.getString("price")+"원");
                    food.put("image",c.getString("image"));

                    food_list.add(food);
                }


                if(food_list.size() == 0){
                    Toast.makeText(getApplication(), "메뉴를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    listView = (ListView)findViewById(R.id.RSD_list);
//                    ListAdapter adapter = new SimpleAdapter(RestaurantdetailActivity.this, food_list, R.layout.restaurant_detail_item, new String[] {"food_name","price"},
//                            new int[] { R.id.RSD_item_name_text, R.id.RSD_item_price_text});
//
//                    listView.setAdapter(adapter);

                    CustomAdapter customAdapter = new CustomAdapter(RestaurantdetailActivity.this, 0, food_list);
                    listView.setAdapter(customAdapter);
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

    private class CustomAdapter extends ArrayAdapter<HashMap<String, String>> {
        private ArrayList<HashMap<String, String>> items;

        public CustomAdapter(Context context, int textViewResourceid, ArrayList<HashMap<String, String>> objects){
            super(context, textViewResourceid, objects);
            this.items = objects;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if(v == null){
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.restaurant_detail_item, null);
            }

            ImageView imageView = (ImageView)v.findViewById(R.id.RSD_item_image);
            TextView nameText, priceText;
            nameText = (TextView)v.findViewById(R.id.RSD_item_name_text);
            priceText = (TextView)v.findViewById(R.id.RSD_item_price_text);

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        URL url = new URL(server + items.get(position).get("image"));
                        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();

                        InputStream inputStream = connection.getInputStream();
                        bitmap = BitmapFactory.decodeStream(inputStream);
                    }catch (MalformedURLException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });

            if (!items.get(position).get("image").isEmpty()){
                t.start();
                try{
                    t.join();
                    imageView.setImageBitmap(bitmap);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            nameText.setText(items.get(position).get("food_name"));
            priceText.setText(items.get(position).get("price"));

            return v;
        }
    }
}
