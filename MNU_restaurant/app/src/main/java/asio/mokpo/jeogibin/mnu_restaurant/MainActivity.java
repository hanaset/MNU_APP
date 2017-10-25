package asio.mokpo.jeogibin.mnu_restaurant;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity {

    EditText id_edit, pass_edit;
    Button login_btn, join_btn, find_btn;
    int login = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(MainActivity.this, StartActivity.class));

        NetworkUtil.setNetworkPolicy();

        id_edit = (EditText)findViewById(R.id.M_id_edit);
        pass_edit = (EditText)findViewById(R.id.M_password_edit);
        login_btn = (Button)findViewById(R.id.M_login_btn);
        join_btn = (Button)findViewById(R.id.M_join_btn);
        find_btn = (Button)findViewById(R.id.M_find_btn);

        find_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MainActivity.this, FindActivity.class);
                Intent intent = new Intent(MainActivity.this, testActivity.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(id_edit.getText().toString().isEmpty() || pass_edit.getText().toString().isEmpty()){
                    Toast.makeText(getApplication(), "아이디와 비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                }

            }
        });

        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    PHPRequest request = new PHPRequest("http://114.70.93.130/mnu/restaurant_login.php");
                    String result = request.PhP_login(id_edit.getText().toString(), pass_edit.getText().toString());
                    if(result.equals("failed")){
                        login = 0;
                    }else{
                        login = 1;
                    }
                    if(login == 1){
                        login = 0;
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.putExtra("id",id_edit.getText().toString());
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplication(),"아이디와 비밀번호를 다시 확인하세요.",Toast.LENGTH_SHORT).show();
                    }
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }
            }
        });
    }

}
