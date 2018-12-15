package site.fieldmaus.hotelline;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class AirlineItemView extends LinearLayout {
    TextView text_airline_name;
    TextView text_airline_code;

    public AirlineItemView(Context context) {
        super(context);
        init(context);
    }

    public AirlineItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.airline_item, this, true);
        text_airline_name = (TextView) findViewById(R.id.text_airline_name);
        text_airline_code = (TextView) findViewById(R.id.text_airline_code);
    }

    public void setName(String name) {
        text_airline_name.setText(name);
    }

    public void setCode(String code) {
        text_airline_code.setText(code);
    }
}
