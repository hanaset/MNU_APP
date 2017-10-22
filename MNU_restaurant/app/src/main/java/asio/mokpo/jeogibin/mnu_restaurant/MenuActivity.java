package asio.mokpo.jeogibin.mnu_restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by b1835 on 2017-10-22.
 */

public class MenuActivity extends AppCompatActivity {

    String name, food_name, price;
    EditText food_name_edit, price_eidt;

    ArrayList<HashMap<String, String>> food_list;
    ListView listView;
    JSONArray foodArray = null;

    Button add_btn, del_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        food_name_edit = (EditText)findViewById(R.id.Menu_food_edit);
        price_eidt = (EditText)findViewById(R.id.Menu_price_edit);
        del_btn = (Button)findViewById(R.id.Menu_del_btn);
        add_btn = (Button)findViewById(R.id.Menu_add_btn);

        Intent intent = getIntent();
        name = intent.getExtras().getString("name");

        list_setting();

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (food_name_edit.getText().toString().isEmpty() || price_eidt.getText().toString().isEmpty()) {
                    Toast.makeText(getApplication(), "모두 입력하세요.",Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/restaurant_menu_add.php");
                        String result = request.PhP_food_menu_edit(name,food_name_edit.getText().toString(), price_eidt.getText().toString().replace("원",""));

                        if(result.equals("1")){
                            list_setting();
                            Toast.makeText(getApplication(),"메뉴 추가에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplication(),"메뉴 추가에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (food_name_edit.getText().toString().isEmpty() || price_eidt.getText().toString().isEmpty()) {
                    Toast.makeText(getApplication(), "모두 입력하세요.",Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/restaurant_menu_del.php");
                        String result = request.PhP_food_menu_edit(name,food_name_edit.getText().toString(), price_eidt.getText().toString().replace("원",""));

                        if(result.equals("1")){
                            Toast.makeText(getApplication(),"메뉴 삭제에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                            list_setting();
                        }else{
                            Toast.makeText(getApplication(),"메뉴 삭제에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                }
            }
        });



    }

    void list_setting(){
        try{
            PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/restaurant_menu.php");
            String result = request.PhP_food_menu(name);

            try{
                JSONObject jsonObject = new JSONObject(result);
                foodArray = jsonObject.getJSONArray("result");

                food_list = new ArrayList<>();

                for(int i = 0; i<foodArray.length(); i++){
                    HashMap<String, String> food = new HashMap<String, String>();
                    JSONObject c = foodArray.getJSONObject(i);

                    food.put("food_name",c.getString("food_name"));
                    food.put("price",c.getString("price") + "원");
                    food_list.add(food);
                }

                if(food_list.size() == 0){
                    Toast.makeText(getApplication(), "메뉴 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    ListAdapter adapter = new SimpleAdapter(MenuActivity.this, food_list, R.layout.item_menu,
                            new String[] {"food_name","price"},
                            new int[] {R.id.Menu_item_name_text, R.id.Menu_item_price_text});

                    listView = (ListView)findViewById(R.id.Menu_list);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            food_name_edit.setText(food_list.get(i).get("food_name"));
                            price_eidt.setText(food_list.get(i).get("price"));
                        }
                    });
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

    }

}
