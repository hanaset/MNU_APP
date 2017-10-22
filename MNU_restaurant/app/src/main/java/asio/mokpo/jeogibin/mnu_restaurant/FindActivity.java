package asio.mokpo.jeogibin.mnu_restaurant;

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

public class FindActivity extends AppCompatActivity {

    EditText id, phone;
    TextView pass;
    Button button;
    String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        id = (EditText)findViewById(R.id.F_id_edit);
        phone = (EditText)findViewById(R.id.F_phone_edit);
        pass = (TextView)findViewById(R.id.F_text);
        button = (Button)findViewById(R.id.F_find_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id.getText().toString().isEmpty() || phone.getText().toString().isEmpty()){
                    Toast.makeText(getApplication(), "모두 입력하세요.", Toast.LENGTH_SHORT).show();
                }else{
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            try{
                                PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/restaurant_find.php");
                                String result = request.PhP_Pass_find(id.getText().toString(), phone.getText().toString());

                                password = result;
                                handler.sendEmptyMessage(0);
                            }catch (MalformedURLException e){
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(password.equals("failed")){
                pass.setText("다시 정보를 확인하세요.");
            }else{
                pass.setText("비밀번호 : " + password);
            }
        }
    };
}
