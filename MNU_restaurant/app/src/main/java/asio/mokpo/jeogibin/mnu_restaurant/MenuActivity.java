package asio.mokpo.jeogibin.mnu_restaurant;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by b1835 on 2017-10-22.
 */

public class MenuActivity extends AppCompatActivity {

    public static ProgressDialog progressDialog;
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            progressDialog.dismiss();
        }
    };

    String id, num;
    String server = "http://114.70.93.130/mnu/";
    String image_url;
    EditText food_name_edit, price_eidt;
    ImageButton imageButton;

    ArrayList<HashMap<String, String>> food_list;
    ListView listView;
    JSONArray foodArray = null;

    Button add_btn, del_btn, edit_btn;

    private FileInputStream mFileInputStream = null;
    private URL connectUrl = null;

    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";
    String upload;
    Bitmap bitmap;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        food_name_edit = (EditText)findViewById(R.id.Menu_food_edit);
        price_eidt = (EditText)findViewById(R.id.Menu_price_edit);
        del_btn = (Button)findViewById(R.id.Menu_del_btn);
        add_btn = (Button)findViewById(R.id.Menu_add_btn);
        edit_btn = (Button)findViewById(R.id.Menu_edit_btn);
        imageButton = (ImageButton)findViewById(R.id.Menu_image);

        final Intent intent = getIntent();
        id = intent.getExtras().getString("id");

        list_setting();

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date date = new Date();

                num = id + simpleDateFormat.format(date);

                if (food_name_edit.getText().toString().isEmpty() || price_eidt.getText().toString().isEmpty()) {
                    Toast.makeText(getApplication(), "모두 입력하세요.",Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/restaurant_menu_add.php");
                        String result = request.PhP_food_menu_edit(id, food_name_edit.getText().toString(), price_eidt.getText().toString(), num);

                        if (result.equals("1")) {
                            list_setting();
                            Toast.makeText(getApplication(), "메뉴 추가에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplication(), "메뉴 추가에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }catch (MalformedURLException e){
                        e.printStackTrace();
                    }
                    if (image_url != null) {
                        try {
                            DoFileUpload(image_url, num, "http://114.70.93.130/mnu/image_edit.php");
                            if (upload.equals("11")) {
                                list_setting();
                                Toast.makeText(getApplication(), "메뉴 추가에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplication(), "메뉴 추가에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplication(), "사진 업로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                    imageButton.setImageResource(R.drawable.icon1);
                    food_name_edit.setText("");
                    price_eidt.setText("");
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
                        String result = request.PhP_food_menu_edit(num);

                        if(result.equals("1")){
                            list_setting();
                            imageButton.setImageResource(R.drawable.icon1);
                            Toast.makeText(getApplication(),"메뉴 삭제에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplication(),"메뉴 삭제에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    food_name_edit.setText("");
                    price_eidt.setText("");
                }
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (food_name_edit.getText().toString().isEmpty() || price_eidt.getText().toString().isEmpty()) {
                    Toast.makeText(getApplication(), "모두 입력하세요.",Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/restaurant_menu_edit.php");
                        String result = request.PhP_food_menu_edit(food_name_edit.getText().toString(), price_eidt.getText().toString(), num);

                        if (result.equals("1")) {
                            list_setting();
                            Toast.makeText(getApplication(), "메뉴 수정에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplication(), "메뉴 수정에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    if (image_url != null) {
                        try {
                            DoFileUpload(image_url, num, "http://114.70.93.130/mnu/image_edit.php");
                            if (upload.equals("11")) {
                                list_setting();

                                Toast.makeText(getApplication(), "메뉴 수정에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplication(), "메뉴 수정에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplication(), "사진 업로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                    food_name_edit.setText("");
                    price_eidt.setText("");
                }
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                image_url = null;
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                ActivityCompat.requestPermissions(MenuActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                startActivityForResult(intent, 1);
            }
        });

    }

    void list_setting(){
        try{
            PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/restaurant_menu.php");
            String result = request.PhP_food_menu(id);

            try{
                food_list = new ArrayList<>();

                if(!result.isEmpty()) {
                    JSONObject jsonObject = new JSONObject(result);
                    foodArray = jsonObject.getJSONArray("result");

                    for (int i = 0; i < foodArray.length(); i++) {
                        JSONObject c = foodArray.getJSONObject(i);
                        HashMap<String, String> food = new HashMap<String, String>();
                        food.put("food_name", c.getString("food_name"));
                        food.put("price", c.getString("price") + "원");
                        food.put("num", c.getString("num"));
                        food.put("image",c.getString("image"));
                        food_list.add(food);
                    }
                }else{
                    HashMap<String, String> food = new HashMap<String, String>();
                    food.put("food_name", "메뉴를 추가해주세요.");
                    food.put("price", "");
                    food.put("num", "");
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
                        public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                            progressDialog = ProgressDialog.show(MenuActivity.this, "로딩 중", "잠시 기달려주세요.",true);

                            food_name_edit.setText(food_list.get(i).get("food_name"));
                            price_eidt.setText(food_list.get(i).get("price").toString().replace("원",""));
                            num = food_list.get(i).get("num");

                            Thread t = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        URL url = new URL(server + food_list.get(i).get("image"));
                                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                                        conn.setDoInput(true);
                                        conn.connect();

                                        InputStream inputStream = conn.getInputStream();
                                        bitmap = BitmapFactory.decodeStream(inputStream);
                                    }catch (MalformedURLException e){
                                        e.printStackTrace();
                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }
                                }
                            });

                            if(!food_list.get(i).get("image").isEmpty()) {
                                t.start();
                                try {
                                    t.join();
                                    imageButton.setImageBitmap(bitmap);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                imageButton.setImageResource(R.drawable.icon1);
                            }

                            handler.sendEmptyMessage(0);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            imageButton.setImageURI(data.getData());
            image_url = getPath(data.getData());
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

    private void DoFileUpload(String filePath, String name, String url) throws IOException {
        Log.d("Test" , "file path = " + filePath);
        HttpFileUpload(url, "", filePath, name);
    }

    private void HttpFileUpload(String urlString, String params, String fileName, String name) {
        try {

            mFileInputStream = new FileInputStream(fileName);
            connectUrl = new URL(urlString);
            Log.d("Test", "mFileInputStream  is " + mFileInputStream);

            // open connection
            HttpURLConnection conn = (HttpURLConnection)connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            // write data
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + fileName +"." + name + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            int bytesAvailable = mFileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            byte[] buffer = new byte[bufferSize];
            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

            Log.d("Test", "image byte is " + bytesRead);

            // read image
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = mFileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            Log.e("Test" , "File is written");
            mFileInputStream.close();
            dos.flush(); // finish upload...

            // get response
            int ch;
            InputStream is = conn.getInputStream();
            StringBuffer b =new StringBuffer();
            while( ( ch = is.read() ) != -1 ){
                b.append( (char)ch );
            }
            upload = b.toString();
            dos.close();

        } catch (Exception e) {
            Log.d("Test", "exception " + e.getMessage());
            // TODO: handle exception
        }
    }
}
