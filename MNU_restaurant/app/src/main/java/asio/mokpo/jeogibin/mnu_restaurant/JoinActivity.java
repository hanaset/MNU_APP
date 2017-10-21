package asio.mokpo.jeogibin.mnu_restaurant;

import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;
import android.os.Handler;

import java.net.MalformedURLException;
import java.util.logging.LogRecord;

/**
 * Created by b1835 on 2017-10-21.
 */

public class JoinActivity extends AppCompatActivity {

    EditText id_edit, pass_edit, pass2_edit, name_edit, time_edit, delivery_edit, phone_edit;
    Button check_button, join_btn;
    int check = 0;
    int join_check = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        id_edit = (EditText)findViewById(R.id.Join_id_edit);
        pass_edit = (EditText)findViewById(R.id.Join_pass_edit);
        pass2_edit = (EditText)findViewById(R.id.Join_pass2_Edit);
        name_edit = (EditText)findViewById(R.id.Join_name_edit);
        delivery_edit = (EditText)findViewById(R.id.Join_delivery_edit);
        time_edit = (EditText)findViewById(R.id.Join_time_edit);
        phone_edit = (EditText)findViewById(R.id.Join_phone_edit);

        check_button = (Button)findViewById(R.id.Join_check_btn);
        join_btn = (Button)findViewById(R.id.Join_btn);

        check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (id_edit.getText().toString().isEmpty()) {
                    Toast.makeText(getApplication(), "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    final String id = id_edit.getText().toString();

                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/restaurant_id_check.php");
                                String result = request.PhP_ID_search(id);

                                if(result.equals("failed")){
                                    check = 1;
                                }else{
                                    check = 2; // 불가
                                }
                                handler.sendEmptyMessage(0);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            }
        });

        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(id_edit.getText().toString().isEmpty() || pass_edit.getText().toString().isEmpty() || pass2_edit.getText().toString().isEmpty() || name_edit.getText().toString().isEmpty() ||
                        phone_edit.getText().toString().isEmpty() || time_edit.getText().toString().isEmpty() || delivery_edit.getText().toString().isEmpty()){
                    Toast.makeText(getApplication(), "모두 입력하세요.",Toast.LENGTH_SHORT).show();
                }else{
                    if(pass_edit.getText().toString().equals(pass2_edit.getText().toString())){
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();

                                try {
                                    PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/restaurant_join.php");
                                    String result = request.PhP_Join(id_edit.getText().toString(),pass_edit.getText().toString(),phone_edit.getText().toString(),
                                            name_edit.getText().toString(),time_edit.getText().toString(),delivery_edit.getText().toString());

                                    if(result.equals("succesed")){
                                        join_check = 1; // 사용가능
                                    }else{
                                        join_check = 2; // 불가
                                    }
                                    join_handler.sendEmptyMessage(0);
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }else{
                        Toast.makeText(getApplication(), "비밀번호를 확인하세요.",Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

    }
    Handler join_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(join_check == 1 && check == 1){
                Toast.makeText(getApplication(), "회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(getApplication(), "회원가입이 실패하였습니다.",Toast.LENGTH_SHORT).show();
            }
        }
    };

    Handler handler = new Handler() {

        public void handleMessage(Message msg){
            if(check == 1){
                Toast.makeText(getApplication(), "사용 가능한 아이디입니다.",Toast.LENGTH_SHORT).show();
            }else if(check == 2){
                Toast.makeText(getApplication(), "사용 불가능한 아이디입니다.",Toast.LENGTH_SHORT).show();
            }
        }
    };
}
