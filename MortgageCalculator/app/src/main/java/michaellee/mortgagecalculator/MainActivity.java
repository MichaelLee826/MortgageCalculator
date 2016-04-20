package michaellee.mortgagecalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    String mortgage;
    String rate;
    String time;
    String aheadTime;

    EditText mortgageEdit;
    EditText timeEdit;
    EditText interestEdit;
    EditText aheadTimeEdit;
    Button calButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mortgageEdit = (EditText)findViewById(R.id.mortgageEditText);
        timeEdit = (EditText)findViewById(R.id.timeEditText);
        interestEdit = (EditText)findViewById(R.id.interestEditText);
        aheadTimeEdit = (EditText)findViewById(R.id.aheadTimeEditText);
        calButton = (Button)findViewById(R.id.calButton);

        calButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mortgage = mortgageEdit.getText().toString();
                rate = interestEdit.getText().toString();
                time = timeEdit.getText().toString();
                aheadTime = aheadTimeEdit.getText().toString();

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ResultActivity.class);      //跳转到课程列表
                Bundle bundle = new Bundle();
                bundle.putString("mortgage", mortgage);
                bundle.putString("rate", rate);
                bundle.putString("time", time);
                bundle.putString("aheadTime", aheadTime);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
