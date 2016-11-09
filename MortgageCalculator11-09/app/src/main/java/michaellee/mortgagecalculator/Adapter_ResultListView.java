package michaellee.mortgagecalculator;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Adapter_ResultListView extends BaseAdapter{
    private Context context;
    private String[] timeStrings;
    private String[] capitalStrings;
    private String[] interestStrings;
    private String[] monthPayStrings;

    public Adapter_ResultListView(Context context, String[] timeStrings, String[] capitalStrings, String[] interestStrings, String[] monthPayStrings) {
        this.context = context;
        this.timeStrings = timeStrings;
        this.capitalStrings = capitalStrings;
        this.interestStrings = interestStrings;
        this.monthPayStrings = monthPayStrings;
    }

    @Override
    public int getCount() {
        return timeStrings.length;
    }

    @Override
    public Object getItem(int position) {
        return timeStrings[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.list_item, null);

        TextView timeTextView = (TextView)view.findViewById(R.id.List_Time_TextView);
        TextView capitalTextView = (TextView)view.findViewById(R.id.List_Capital_TextView);
        TextView interestTextView = (TextView)view.findViewById(R.id.List_Interest_TextView);
        TextView monthPayTextView = (TextView)view.findViewById(R.id.List_MonthPay_TextView);

        timeTextView.setText(timeStrings[position]);
        capitalTextView.setText(capitalStrings[position]);
        interestTextView.setText(interestStrings[position]);
        monthPayTextView.setText(monthPayStrings[position]);

        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }
}
