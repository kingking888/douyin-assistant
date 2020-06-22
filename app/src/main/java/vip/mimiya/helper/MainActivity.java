package vip.mimiya.helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ComponentName componetName = new ComponentName(
                //这个是另外一个应用程序的包名
                "com.ss.android.ugc.aweme",
                //这个参数是要启动的Activity
                "com.ss.android.ugc.aweme.main.MainActivity");

        try {
            Intent intent = new Intent();
            intent.setComponent(componetName);
            startActivity(intent);
        } catch (Exception e) {
        }
    }
}
