package michaellee.mortgagecalculator;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Adapter_PopupSpinner extends BaseAdapter{
    Context context;
    String[] strings;

    public Adapter_PopupSpinner(Context context, String[] strings) {
        this.context = context;
        this.strings = strings;
    }

    @Override
    public int getCount() {
        return strings.length;
    }

    @Override
    public Object getItem(int position) {
        return strings[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.popup_spinner, null);
        TextView textView = (TextView)view.findViewById(R.id.popup_spinner_TextView);
        String string = strings[position];
        textView.setText(string);
        return view;
    }
}
