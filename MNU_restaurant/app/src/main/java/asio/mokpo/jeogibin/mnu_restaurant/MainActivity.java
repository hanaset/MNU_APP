package asio.mokpo.jeogibin.mnu_restaurant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText id_edit, pass_edit;
    Button login_btn, join_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(MainActivity.this, StartActivity.class));

        id_edit = (EditText)findViewById(R.id.M_id_edit);
        pass_edit = (EditText)findViewById(R.id.M_password_edit);
        login_btn = (Button)findViewById(R.id.M_login_btn);
        join_btn = (Button)findViewById(R.id.M_join_btn);

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



    }
}
