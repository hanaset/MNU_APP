package asio.mokpo.jeogibin.mnu_restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;

/**
 * Created by b1835 on 2017-10-22.
 */

public class InfoEditActivity extends AppCompatActivity {

    TextView name_text, id_text;
    EditText pass_edit, phone_edit, time_edit, delivery_edit;
    Button button;
    String name, id, pass, phone, time, delivery;

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

        button = (Button)findViewById(R.id.Info_edit_btn);

        info_Call(id);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info_edit(id, pass_edit.getText().toString(), phone_edit.getText().toString(), time_edit.getText().toString(), delivery_edit.getText().toString());
            }
        });
    }


    void info_edit(final String id, final String pass, final String phone, final String time, final String delivery){
        try{
            PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/restaurant_info_edit.php");
            String result = request.PhP_Edit(id, pass, phone, time, delivery);

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
                String[] arr = msg.obj.toString().split(",");
                name = arr[0];
                phone = arr[1];
                time = arr[2];
                pass = arr[3];
                delivery = arr[4];

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
}
