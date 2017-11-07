package asio.mokpo.jeongbin.mnu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import android.widget.ArrayAdapter;
/**
 * Created by b1835 on 2017-10-20.
 */

public class RestaurantActivity extends AppCompatActivity {

    String server = "http://114.70.93.130/mnu/";

    ArrayList<HashMap<String, String>> name_list;
    ListView listView;
    JSONArray nameArray = null;
    Bitmap bitmap;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        final ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("전체 보기");
        arrayList.add("배달 가능 매장");

        progressDialog = ProgressDialog.show(RestaurantActivity.this, "로딩중 ", "잠시만 기다려주세요.", true);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayList);
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
                            name.put("id",c.getString("id"));
                            name.put("image",c.getString("image"));

                            name_list.add(name);
                        }

                        if(name_list.size() == 0){
                            Toast.makeText(getApplication(), "가게 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                        }else{
                        //   ListAdapter adapter = new SimpleAdapter(RestaurantActivity.this, name_list, R.layout.restaurant_item,
                        //           new String[] {"name", "notice", "image"},
                        //           new int[] {R.id.RS_item_name_text, R.id.RS_item_notice_text, R.id.RS_item_imageVIew});

                           listView = (ListView)findViewById(R.id.RS_list);
                        //   listView.setAdapter(adapter);
                            CustomAdapter customAdapter = new CustomAdapter(RestaurantActivity.this, 0, name_list);
                            listView.setAdapter(customAdapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(RestaurantActivity.this, RestaurantdetailActivity.class);
                                    intent.putExtra("id", name_list.get(position).get("id"));
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

    private class CustomAdapter extends ArrayAdapter<HashMap<String, String>>{
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
                v = vi.inflate(R.layout.restaurant_item, null);
            }

            ImageView imageView = (ImageView)v.findViewById(R.id.RS_item_imageVIew);
            TextView nameText, noticeText;
            nameText = (TextView)v.findViewById(R.id.RS_item_name_text);
            noticeText = (TextView)v.findViewById(R.id.RS_item_notice_text);

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

            if (!items.get(position).get("image").equals("null")){
                t.start();
                try{
                    t.join();
                    imageView.setImageBitmap(bitmap);
                    imageView.setImageAlpha(200);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }

            nameText.setText(items.get(position).get("name"));
            SpannableString content = new SpannableString(items.get(position).get("notice"));
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            noticeText.setText(content);

            return v;
        }
    }
}
