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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;

/**
 * Created by b1835 on 2017-10-22.
 */

public class LoginActivity extends AppCompatActivity {

    TextView name_text, notice_text;
    EditText notice_edit;
    Button button;
    ImageButton menu_add, menu_edit;
    ImageButton mem_edit, mem_delete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name_text = (TextView)findViewById(R.id.L_name_text);
        notice_text = (TextView)findViewById(R.id.L_notice_text);
        notice_edit = (EditText)findViewById(R.id.L_notice_eidt);
        button = (Button)findViewById(R.id.L_notice_edit_btn);

        menu_add = (ImageButton)findViewById(R.id.L_menu_add_btn);
        menu_edit = (ImageButton)findViewById(R.id.L_mem_edit_btn);
        mem_edit = (ImageButton)findViewById(R.id.L_mem_edit_btn);
        mem_delete = (ImageButton)findViewById(R.id.L_mem_delete_btn);

        Intent intent = getIntent();
        final String id = intent.getExtras().getString("id");

        info_call(id);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notice_change(id, notice_edit.getText().toString());
            }
        });

        menu_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mem_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mem_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mem_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_delete(id);
            }
        });

    }

    void id_delete(final String id){
        new Thread(){
            @Override
            public void run() {
                super.run();

                try{
                    PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/restaurant_delete.php");
                    String result = request.PhP_ID_search(id);
                    Message message = Message.obtain();

                    if(result.equals("1")){
                        message.arg1 = 1;
                    }else{
                        message.arg1 = 2;
                    }
                    delete_handler.sendMessage(message);

                }catch (MalformedURLException e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler delete_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.arg1 == 1)
            {
                Toast.makeText(getApplication(),"탈퇴에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(getApplication(),"탈퇴에 실패하였습니다.",Toast.LENGTH_SHORT).show();
            }
        }
    };

    void notice_change(final String id, final String notice){
        new Thread(){
            @Override
            public void run() {
                super.run();

                try{
                    PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/restaurant_login_notice_edit.php");
                    String result  = request.PhP_notice_edit(id, notice);
                    Message message = Message.obtain();
                    message.obj = notice;
                    if(result.equals("1")) {
                        handler.sendMessage(message);
                    }else{
                        handler.sendEmptyMessage(0);
                    }
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }

            }
        }.start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.toString().isEmpty()){
                notice_text.setText("알림 : 오류 발생");
            }else{
                notice_text.setText("알림 : " + msg.obj.toString());
            }
        }
    };

    void info_call(final String id){
        new Thread(){
            @Override
            public void run() {
                super.run();

                try{
                    PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/restaurant_login_output.php");
                    String result  = request.PhP_ID_search(id);

                    Message message = Message.obtain();
                    message.obj = result;

                    info_handler.sendMessage(message);

                }catch (MalformedURLException e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler info_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String[] arr = msg.obj.toString().split(",");
            name_text.setText(arr[0]);
            notice_text.setText("알림 : " + arr[1]);
        }
    };

}
