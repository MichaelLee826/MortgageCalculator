package michaellee.mortgagecalculator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    private int mortgage;
    private double rate;
    private double montRate;
    private int time;
    private int aheadTime;

    private double sum = 0;
    private double interest = 0;

    //用于显示等额本息和等额本金的ViewPager
    private ViewPager viewPager;
    private List<View> viewList;
    private View view1;
    private View view2;


    private ListView listViewOne;       //等额本息的ListView
    private ListView listViewTwo;       //等额本金的ListView

    private TextView typeOneText;       //等额本息的标题
    private TextView typeTwoText;       //等额本金的标题

    private TextView textView1;         //显示等额本息提前还款情况
    private TextView textView2;         //显示等额本金提前还款情况

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        getData();      //1.获得数据
        init();         //2.初始化
    }

    //1.获得数据
    public void getData(){
        //从前一个Activity传来的数据
        Bundle bundle = this.getIntent().getExtras();
        String m = bundle.getString("mortgage");
        String r = bundle.getString("rate");
        String t = bundle.getString("time");
        String a = bundle.getString("aheadTime");


        //万元转换为元
        mortgage = Integer.valueOf(m);
        mortgage = mortgage * 10000;

        //年利率转换为月利率
        rate = Double.valueOf(r);
        rate = rate / 100;
        montRate = rate / 12;

        //贷款时间转换为月
        time = Integer.valueOf(t);
        time = time * 12;

        //第几年还款转换为月
        aheadTime = Integer.valueOf(a);
        aheadTime = aheadTime * 12;
    }

    //2.初始化
    public void init(){
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        typeOneText = (TextView)findViewById(R.id.typeOneTextView);
        typeTwoText = (TextView)findViewById(R.id.typeTwoTextView);

        //2-1.点击标题切换ViewPager
        typeOneText.setOnClickListener(new TitleTextViwOnClickListener(0));
        typeTwoText.setOnClickListener(new TitleTextViwOnClickListener(1));


        viewList = new ArrayList<View>();
        LayoutInflater layoutInflater = getLayoutInflater().from(this);

        view1 = layoutInflater.inflate(R.layout.viewpager1, null);
        view2 = layoutInflater.inflate(R.layout.viewpager2, null);

        viewList.add(view1);
        viewList.add(view2);

        listViewOne = (ListView)view1.findViewById(R.id.listOne);
        textView1 = (TextView)view1.findViewById(R.id.viewpagerText1);

        listViewTwo = (ListView)view2.findViewById(R.id.listTwo);
        textView2 = (TextView)view2.findViewById(R.id.viewpagerText2);

        mPageAdapter adapter = new mPageAdapter(viewList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new PageChangeListener());        //2-4.ViewPager的监听器

        //2-2.等额本息的计算方法
        String[] strings1 = calculateTypeOne(time, aheadTime);
        Adapter_PopupSpinner adapterList1 = new Adapter_PopupSpinner(this, strings1);
        listViewOne.setAdapter(adapterList1);

        //2-3.等额本金的计算方法
        String[] strings2 = calculateTypeTwo(time, aheadTime);
        Adapter_PopupSpinner adapterList2 = new Adapter_PopupSpinner(this, strings2);
        listViewTwo.setAdapter(adapterList2);
    }

    //2-1.点击标题切换ViewPager
    public class TitleTextViwOnClickListener implements View.OnClickListener{
        private int index = 0;

        public TitleTextViwOnClickListener(int i){
            index = i;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == typeOneText.getId()){
                typeOneText.setTextColor(Color.RED);
                typeTwoText.setTextColor(Color.BLACK);
            }
            if (v.getId() == typeTwoText.getId()){
                typeOneText.setTextColor(Color.BLACK);
                typeTwoText.setTextColor(Color.RED);
            }
            viewPager.setCurrentItem(index);
        }
    }

    //2-2.等额本息的计算方法
    public String[] calculateTypeOne(int time, int aheadTime){
        //如果没有提前还款
        if (aheadTime == 0){
            aheadTime = time;
        }

        String[] strings = new String[aheadTime + 1];
        String monthCapital[] = new String[time + 1];
        String monthInterest[] = new String[time + 1];
        String monthSum[] = new String[time + 1];

        double paidCapital = 0;     //已还本金
        double paidInterest = 0;    //已还利息
        double paid = 0;            //总共已还
        DecimalFormat df = new DecimalFormat("0.00");       //保留两位小数
        for (int i = 1; i <= aheadTime; i++){

            //每月应还本金：每月应还本金=贷款本金×月利率×(1+月利率)^(还款月序号-1)÷〔(1+月利率)^还款月数-1〕
            monthCapital[i] = df.format(mortgage * montRate * Math.pow((1 + montRate), i - 1) / (Math.pow(1 + montRate, time) - 1));

            //每月应还利息：贷款本金×月利率×〔(1+月利率)^还款月数-(1+月利率)^(还款月序号-1)〕÷〔(1+月利率)^还款月数-1〕
            monthInterest[i] = df.format(mortgage * montRate * (Math.pow(1 + montRate, time) - Math.pow(1 + montRate, i - 1)) / (Math.pow(1 + montRate, time) - 1));

            //月供：每月月供额=〔贷款本金×月利率×(1＋月利率)＾还款月数〕÷〔(1＋月利率)＾还款月数-1〕
            monthSum[i] = df.format(mortgage * montRate * Math.pow((1 + montRate), time) / (Math.pow(1 + montRate, time) - 1));

            //得到输出字符串
            strings[i] = "第" + i + "期" + "     " + monthCapital[i] + "     "+ monthInterest[i] + "     "+ monthSum[i];

            //已还本金
            paidCapital = paidCapital + mortgage * montRate * Math.pow((1 + montRate), i - 1) / (Math.pow(1 + montRate, time) - 1);

            //已还利息
            paidInterest = paidInterest + mortgage * montRate * (Math.pow(1 + montRate, time) - Math.pow(1 + montRate, i - 1)) / (Math.pow(1 + montRate, time) - 1);

            //总共已还
            paid = paid + mortgage * montRate * Math.pow((1 + montRate), time) / (Math.pow(1 + montRate, time) - 1);

        }

        //月供
        double monthsum = mortgage * montRate * Math.pow((1 + montRate), time) / (Math.pow(1 + montRate, time) - 1);

        //还款总额
        sum = monthsum * time;

        //还款利息总额
        interest =  monthsum * time - mortgage;
        String s = df.format(sum);
        String i = df.format(interest);

        //提前还款的相关数据
        String pi = df.format(paidInterest);
        String rest = df.format(mortgage - paidCapital);
        String p = df.format(paid);

        if (aheadTime == time){
            textView1.setText("贷款金额：" + mortgage + "元\n" +"共还款：" + s + "元\n" + "共还利息：" + i + "元\n");
        }
        else {
            textView1.setText("贷款金额：" + mortgage + "元\n" +"共还款：" + s + "元\n" + "共还利息：" + i + "元\n"
                    + "若果提前" + aheadTime / 12 + "年还款，\n则已还" + p + "元，\n" + "其中利息" + pi + "元，\n还需还款" + rest +"元");
        }
        //System.out.println("共还款：" + s + "\t" + "共还利息：" + i);
        return strings;
    }

    //2-3.等额本金的计算方法
    public String[] calculateTypeTwo(int time, int aheadTime){
        if (aheadTime == 0){
            aheadTime = time;
        }

        String[] strings = new String[aheadTime + 1];
        String monthCapital[] = new String[time + 1];
        String monthInterest[] = new String[time + 1];
        String monthSum[] = new String[time + 1];

        DecimalFormat df = new DecimalFormat("0.00");
        double paid = 0;
        double paidCapital = 0;
        double paidInterest = 0;
        double paidSum = 0;
        for (int i = 1; i <= aheadTime; i++){

            //每月应还本金：贷款本金÷还款月数
            monthCapital[i] = df.format(mortgage / time);

            //每月应还利息：剩余本金×月利率=(贷款本金-已归还本金累计额)×月利率
            monthInterest[i] = df.format((mortgage - paid) * montRate);

            //月供：(贷款本金÷还款月数)+(贷款本金-已归还本金累计额)×月利率
            monthSum[i] = df.format((mortgage / time) + (mortgage - paid) * montRate);

            //已归还本金累计额
            paid = paid + mortgage / time;

            //已还本金
            paidCapital = paidCapital + mortgage / time;

            //已还利息
            paidInterest = paidInterest + (mortgage - paid) * montRate;

            //总共已还
            paidSum = paidSum + (mortgage / time) + (mortgage - paid) * montRate;

            strings[i] = "第" + i + "期" + "     " + monthCapital[i] + "     "+ monthInterest[i] + "     "+ monthSum[i];
        }
        sum = time * (mortgage * montRate - montRate * (mortgage / time) * (time - 1) / 2 + mortgage / time);
        interest = sum - mortgage;
        String s = df.format(sum);
        String i = df.format(interest);

        //提前还款的相关数据
        String p = df.format(paidSum);
        String pi = df.format(paidInterest);
        String rest = df.format(mortgage - paidCapital);


        if (aheadTime == time){
            textView2.setText("贷款金额：" + mortgage + "元\n" +"共还款：" + s + "元\n" + "共还利息：" + i + "元\n");
        }else {
            textView2.setText("贷款金额：" + mortgage + "元\n" +"共还款：" + s + "元\n" + "共还利息：" + i + "元\n"
                    + "若果提前" + aheadTime / 12 + "年还款，\n则已还" + p + "元，\n" + "其中利息" + pi + "元，\n还需还款" + rest +"元");
        }

        return strings;
    }

    //2-4.ViewPager的监听器
    public class PageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}