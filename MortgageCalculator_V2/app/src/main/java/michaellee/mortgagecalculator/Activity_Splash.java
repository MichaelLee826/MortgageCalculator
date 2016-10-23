package michaellee.mortgagecalculator;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by MichaelLee826 on 2016-10-21-0021.
 */
public class Activity_Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 移除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        System.out.println("首次启动");

        //首次启动显示欢迎页面
        setContentView(R.layout.activity_splash);

        //显示版本号
        String version = "";
        TextView versionTextView = (TextView)findViewById(R.id.Splash_Version_TextView);
        PackageManager packageManager = getPackageManager();
        try {
            version = packageManager.getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionTextView.setText("V" + version);

        //延时3秒跳转
        new Handler().postDelayed(new Runnable(){
            public void run() {
                Intent intent = new Intent();
                intent.setClass(Activity_Splash.this, Activity_Main.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_splash_fade_in, R.anim.anim_splash_fade_out);
                finish();
            }
        }, 3000);
    }
}
