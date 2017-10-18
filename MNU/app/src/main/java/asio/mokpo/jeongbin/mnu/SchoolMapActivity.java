package asio.mokpo.jeongbin.mnu;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

/**
 * Created by b1835 on 2017-10-18.
 */

public class SchoolMapActivity extends AppCompatActivity {
    ImageView Map, Map_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_map);



        Map = (ImageView) findViewById(R.id.Map_Map);
        Map_name = (ImageView) findViewById(R.id.Map_map_name);

        Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = new AlphaAnimation(0, 1); // 천천히 나타나는 애니메이션 (리얼 혁명)
                animation.setDuration(1000);
                Map_name.setVisibility(View.VISIBLE);
                Map_name.setAnimation(animation);
            }
        });

        Map_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = new AlphaAnimation(1, 0); // 천천히 나타나는 애니메이션 (리얼 혁명) (0과 1)의 자리를 바꿔줘야하는 듯
                animation.setDuration(1000);
                Map_name.setVisibility(View.INVISIBLE);
                Map_name.setAnimation(animation);
            }
        });

    }
}
