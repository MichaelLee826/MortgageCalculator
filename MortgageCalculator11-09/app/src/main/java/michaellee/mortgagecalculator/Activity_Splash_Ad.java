package michaellee.mortgagecalculator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;

import net.youmi.android.normal.common.ErrorCode;
import net.youmi.android.normal.spot.SplashViewSettings;
import net.youmi.android.normal.spot.SpotListener;
import net.youmi.android.normal.spot.SpotManager;

public class Activity_Splash_Ad extends Activity {
    private static final String TAG = "youmi-demo";

    private Context mContext = this;

    private PermissionHelper mPermissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 移除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //非首次启动显示广告
        setContentView(R.layout.activity_splash_ad);

        // 当系统为6.0以上时，需要申请权限
        mPermissionHelper = new PermissionHelper(this);
        mPermissionHelper.setOnApplyPermissionListener(new PermissionHelper.OnApplyPermissionListener() {
            @Override
            public void onAfterApplyAllPermission() {
                Log.i(TAG, "All of requested permissions has been granted, so run app logic.");
                runApp();
            }
        });
        if (Build.VERSION.SDK_INT < 23) {
            // 如果系统版本低于23，直接跑应用的逻辑
            Log.d(TAG, "The api level of system is lower than 23, so run app logic directly.");
            runApp();
        } else {
            // 如果权限全部申请了，那就直接跑应用逻辑
            if (mPermissionHelper.isAllRequestedPermissionGranted()) {
                Log.d(TAG, "All of requested permissions has been granted, so run app logic directly.");
                runApp();
            } else {
                // 如果还有权限为申请，而且系统版本大于23，执行申请权限逻辑
                Log.i(TAG, "Some of requested permissions hasn't been granted, so apply permissions first.");
                mPermissionHelper.applyPermissions();
            }
        }
    }

    //开屏广告退出时一定要调用，否则会显示“资源还没准备好”
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpotManager.getInstance(mContext).onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPermissionHelper.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 跑应用的逻辑
     */
    private void runApp() {
        //设置开屏
        setupSplashAd();
    }

    /**
     * 设置开屏广告
     */
    private void setupSplashAd() {
        // 创建开屏容器
        final RelativeLayout splashLayout = (RelativeLayout) findViewById(R.id.rl_splash);

        // 对开屏进行设置
        SplashViewSettings splashViewSettings = new SplashViewSettings();

        // 设置跳转的窗口类
        splashViewSettings.setTargetClass(Activity_Main.class);

        // 设置开屏的容器
        splashViewSettings.setSplashViewContainer(splashLayout);

        // 展示开屏广告
        SpotManager.getInstance(mContext).showSplash(mContext, splashViewSettings, new SpotListener() {

                    @Override
                    public void onShowSuccess() {
                        Log.d(TAG, "开屏展示成功");
                        splashLayout.setVisibility(View.VISIBLE);
                        splashLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_splash_enter));
                    }

                    @Override
                    public void onShowFailed(int errorCode) {
                        Log.d(TAG, "开屏展示失败");
                        switch (errorCode) {
                            case ErrorCode.NON_NETWORK:
                                Log.e(TAG, "无网络");
                                break;
                            case ErrorCode.NON_AD:
                                Log.e(TAG, "无广告");
                                break;
                            case ErrorCode.RESOURCE_NOT_READY:
                                Log.e(TAG, "资源还没准备好");
                                break;
                            case ErrorCode.SHOW_INTERVAL_LIMITED:
                                Log.e(TAG, "展示间隔限制");
                                break;
                            case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
                                Log.e(TAG, "控件处在不可见状态");
                                break;
                        }
                    }

                    @Override
                    public void onSpotClosed() {
                        Log.d(TAG, "开屏被关闭");
                    }

                    @Override
                    public void onSpotClicked(boolean isWebPage) {
                        Log.d(TAG, "开屏被点击");
                        Log.i(TAG, String.format("是否是网页广告？%s", isWebPage ? "是" : "不是"));
                    }
                });
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
