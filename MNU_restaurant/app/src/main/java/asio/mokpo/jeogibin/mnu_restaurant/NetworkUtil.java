package asio.mokpo.jeogibin.mnu_restaurant;

import android.annotation.SuppressLint;
import android.os.StrictMode;
/**
 * Created by JeongBin on 2016-11-02.
 */

public class NetworkUtil {
    @SuppressLint("NewApi")
    static public void setNetworkPolicy() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
}
