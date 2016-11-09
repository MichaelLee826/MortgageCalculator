package michaellee.mortgagecalculator;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import net.youmi.android.AdManager;

/**
 * Created by MichaelLee826 on 2016-10-21-0021.
 */
public class Activity_Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //友盟统计服务
        //MobclickAgent.setDebugMode(true);                                                               //集成测试模式
        MobclickAgent.setScenarioType(Activity_Splash.this, MobclickAgent.EScenarioType.E_UM_NORMAL);     //场景
        MobclickAgent.enableEncrypt(true);                                                                //加密发送数据

        //开启友盟推送服务
        PushAgent mPushAgent = PushAgent.getInstance(Activity_Splash.this);
        mPushAgent.onAppStart();

        //有米广告：发布ID  应用密钥  测试模式  日志输出
        AdManager.getInstance(this).init("d05e3439db391fcb", "aea2844b236a0eea", false, true);

        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 移除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //首次启动显示欢迎页面
        setContentView(R.layout.activity_splash);

        System.out.println("启动");

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

        //延时2秒跳转
        new Handler().postDelayed(new Runnable(){
            public void run() {
                Intent intent = new Intent();
                intent.setClass(Activity_Splash.this, Activity_Splash_Ad.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.anim_splash_fade_in, R.anim.anim_splash_fade_out);
                finish();
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        //友盟数据统计
        MobclickAgent.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        //友盟数据统计
        MobclickAgent.onPause(this);
        super.onPause();
    }
}
