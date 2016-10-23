package michaellee.mortgagecalculator;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

public class ExitApplication extends Application {
    private List<Activity> activityList = new LinkedList<Activity>();
    private static ExitApplication instance;

    //构造函数
    public ExitApplication() {
        super();
    }

    //单例模式中获取唯一的MyApplication实例
    public static ExitApplication getInstance(){
        if (instance == null){
            instance = new ExitApplication();
        }
        return instance;
    }

    //将Activity添加到列表中
    public void addActivity(Activity activity){
        activityList.add(activity);
    }

    //遍历所有Activity，并关闭
    public void exit(){
        for (Activity activity : activityList){
            activity.finish();
        }
        System.exit(0);
    }
}