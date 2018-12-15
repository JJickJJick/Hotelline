package site.fieldmaus.hotelline;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class HotelSearchItemView extends LinearLayout {
    TextView hotelname;
    TextView hotelvalue;
    RatingBar hotelscore;

    public HotelSearchItemView(Context context) {
        super(context);
        init(context);
    }

    public HotelSearchItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.hotel_search_item, this, true);
        hotelname = (TextView) findViewById(R.id.hotelname);
        hotelvalue = (TextView) findViewById(R.id.value);
        hotelscore = (RatingBar) findViewById(R.id.score);
    }

    public void setName(String name) {
        hotelname.setText(name);
    }

    public void setValue(String value) {
        hotelvalue.setText(value);
    }

    public void setScore(String score) {
        hotelscore.setRating(Float.parseFloat(score));
    }
}
