package asio.mokpo.jeogibin.mnu_restaurant;

import android.Manifest;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by b1835 on 2017-10-22.
 */

public class InfoEditActivity extends AppCompatActivity {


    private FileInputStream mFileInputStream = null;
    private URL connectUrl = null;

    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";
    String upload;
    Bitmap bitmap;

    String server = "http://114.70.93.130/mnu/";


    TextView name_text, id_text;
    EditText pass_edit, phone_edit, time_edit, delivery_edit;
    Button button;
    String name, id, pass, phone, time, delivery;
    ImageButton imageButton;
    String image_uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infoedit);
        Intent intent = getIntent();

        id = intent.getExtras().getString("id");

        name_text = (TextView)findViewById(R.id.info_name_text);
        id_text = (TextView)findViewById(R.id.Info_id_text);
        pass_edit = (EditText) findViewById(R.id.info_pass_edit);
        phone_edit = (EditText)findViewById(R.id.Info_phone_edit);
        time_edit = (EditText)findViewById(R.id.info_time_edit);
        delivery_edit = (EditText)findViewById(R.id.Info_delivery_edit);
        imageButton = (ImageButton) findViewById(R.id.info_image);

        button = (Button)findViewById(R.id.Info_edit_btn);

        info_Call(id);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(server + image_uri);
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

        if(!image_uri.equals("null")) {
            t.start();
            try {
                t.join();
                imageButton.setImageBitmap(bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info_edit(id, pass_edit.getText().toString(), phone_edit.getText().toString(), time_edit.getText().toString(), delivery_edit.getText().toString(), image_uri);

                if(imageButton != null){
                    try {
                        DoFileUpload(image_uri, id, "http://114.70.93.130/mnu/image_upload.php");
                        if(upload.equals("11")){
                            Toast.makeText(getApplication(),"수정이 완료 되었습니다.",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplication(),"수정에 실패 하였습니다.",Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                image_uri = null;
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                ActivityCompat.requestPermissions(InfoEditActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                startActivityForResult(intent, 1);
            }
        });
    }


    void info_edit(final String id, final String pass, final String phone, final String time, final String delivery, final String image_uri){
        try{
            PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/restaurant_info_edit.php");
            String result = request.PhP_Edit(id, pass, phone, time, delivery, image_uri);

            if(result.equals("1")){
                Toast.makeText(getApplication(),"성공적으로 변경되었습니다.",Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(getApplication(),"변경하는데 실패하였습니다.",Toast.LENGTH_SHORT).show();
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

    }

    void info_Call(final String id){

        try{
            PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/restaurant_info.php");
            String result = request.PhP_ID_search(id);
            Message msg = Message.obtain();

            if(result.isEmpty() || result.equals("failed")){
                Toast.makeText(getApplication(),"수정이 불가능합니다.",Toast.LENGTH_SHORT).show();
                finish();
            }else{
                String[] arr = result.split(",");
                name = arr[0];
                phone = arr[1];
                time = arr[2];
                pass = arr[3];
                delivery = arr[4];
                image_uri = arr[5];

                name_text.setText(name);
                pass_edit.setText(pass);
                time_edit.setText(time);
                phone_edit.setText(phone);
                id_text.setText("ID : "+ id);
                delivery_edit.setText(delivery);
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            imageButton.setImageURI(data.getData());
            image_uri = getPath(data.getData());
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
