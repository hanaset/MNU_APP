package asio.mokpo.jeogibin.mnu_restaurant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by b1835 on 2017-10-21.
 */

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        android.os.Handler hd = new android.os.Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },1000);
    }
}
