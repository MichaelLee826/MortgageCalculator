package michaellee.mortgagecalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;

import net.youmi.android.normal.banner.BannerManager;

public class Activity_Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        //有米广告：发布ID  应用密钥  测试模式  日志输出
        //AdManager.getInstance(this).init("d05e3439db391fcb", "aea2844b236a0eea", false, false);

        //获取要嵌入广告条的布局
        LinearLayout bannerLayout = (LinearLayout)findViewById(R.id.help_ll_banner);

        //获取广告条
        View bannerView = BannerManager.getInstance(Activity_Help.this).getBannerView(new net.youmi.android.normal.banner.BannerViewListener() {
            @Override
            public void onRequestSuccess() {
                System.out.println("请求广告成功");
            }

            @Override
            public void onSwitchBanner() {
                System.out.println("切换广告条");
            }

            @Override
            public void onRequestFailed() {
                System.out.println("请求广告失败");
            }
        });

        //将广告条加入到布局中
        bannerLayout.addView(bannerView);
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
