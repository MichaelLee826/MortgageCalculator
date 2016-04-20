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
    int mortgage;
    double rate;
    double montRate;
    int time;
    int aheadTime;

    double sum = 0;
    double interest = 0;
    double paidCapital = 0;
    double paidInterest = 0;

    ViewPager viewPager;
    List<View> viewList;
    View view1;
    View view2;
    ListView listViewOne;
    ListView listViewTwo;
    TextView typeOneText;
    TextView typeTwoText;
    TextView textView1;
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle bundle = this.getIntent().getExtras();
        String m = bundle.getString("mortgage");
        String r = bundle.getString("rate");
        String t = bundle.getString("time");
        String a = bundle.getString("aheadTime");

        mortgage = Integer.valueOf(m);
        mortgage = mortgage * 10000;

        rate = Double.valueOf(r);
        rate = rate / 100;
        montRate = rate / 12;

        time = Integer.valueOf(t);
        time = time * 12;

        aheadTime = Integer.valueOf(a);
        aheadTime = aheadTime * 12;


        viewPager = (ViewPager)findViewById(R.id.viewpager);
        typeOneText = (TextView)findViewById(R.id.typeOneTextView);
        typeTwoText = (TextView)findViewById(R.id.typeTwoTextView);

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
        viewPager.setOnPageChangeListener(new PageChangeListener());



        String[] strings1 = calculateTypeOne(time, aheadTime);
        Adapter_PopupSpinner adapterList1 = new Adapter_PopupSpinner(this, strings1);
        listViewOne.setAdapter(adapterList1);

        String[] strings2 = calculateTypeTwo(time, aheadTime);
        Adapter_PopupSpinner adapterList2 = new Adapter_PopupSpinner(this, strings2);
        listViewTwo.setAdapter(adapterList2);


    }

    public String[] calculateTypeOne(int time, int aheadTime){
        if (aheadTime == 0){
            aheadTime = time;
        }

        String[] strings = new String[aheadTime + 1];
        String monthCapital[] = new String[time + 1];
        String monthInterest[] = new String[time + 1];
        String monthSum[] = new String[time + 1];

        //strings[0] = "期数" + "\t\t" + "偿还本金" + "\t\t" + "偿还利息" + "\t\t" + "月供";
        double paidCapital = 0;
        double paidInterest = 0;
        double paid = 0;
        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 1; i <= aheadTime; i++){
            monthCapital[i] = df.format(mortgage * montRate * Math.pow((1 + montRate), i - 1) / (Math.pow(1 + montRate, time) - 1));
            monthInterest[i] = df.format(mortgage * montRate * (Math.pow(1 + montRate, time) - Math.pow(1 + montRate, i - 1)) / (Math.pow(1 + montRate, time) - 1));
            monthSum[i] = df.format(mortgage * montRate * Math.pow((1 + montRate), time) / (Math.pow(1 + montRate, time) - 1));
            strings[i] = "第" + i + "期" + "     " + monthCapital[i] + "     "+ monthInterest[i] + "     "+ monthSum[i];
            paidCapital = paidCapital + mortgage * montRate * Math.pow((1 + montRate), i - 1) / (Math.pow(1 + montRate, time) - 1);
            paidInterest = paidInterest + mortgage * montRate * (Math.pow(1 + montRate, time) - Math.pow(1 + montRate, i - 1)) / (Math.pow(1 + montRate, time) - 1);
            paid = paid + mortgage * montRate * Math.pow((1 + montRate), time) / (Math.pow(1 + montRate, time) - 1);
            //System.out.println("第" + i + "期" + "\t\t" + monthCapital[i] + "\t\t"+ monthInterest[i] + "\t\t"+ monthSum[i]);
        }

        double monthsum = mortgage * montRate * Math.pow((1 + montRate), time) / (Math.pow(1 + montRate, time) - 1);
        sum = monthsum * time;
        interest =  monthsum * time - mortgage;
        String s = df.format(sum);
        String i = df.format(interest);
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
            monthCapital[i] = df.format(mortgage / time);
            monthInterest[i] = df.format((mortgage - paid) * montRate);
            monthSum[i] = df.format((mortgage / time) + (mortgage - paid) * montRate);
            paid = paid + mortgage / time;
            paidCapital = paidCapital + mortgage / time;
            paidInterest = paidInterest + (mortgage - paid) * montRate;
            paidSum = paidSum + (mortgage / time) + (mortgage - paid) * montRate;
            strings[i] = "第" + i + "期" + "     " + monthCapital[i] + "     "+ monthInterest[i] + "     "+ monthSum[i];
        }
        sum = time * (mortgage * montRate - montRate * (mortgage / time) * (time - 1) / 2 + mortgage / time);
        interest = sum - mortgage;
        String s = df.format(sum);
        String i = df.format(interest);
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
}
