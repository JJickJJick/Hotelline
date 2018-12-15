package site.fieldmaus.hotelline;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class HotelItemView extends LinearLayout {
    TextView hotelname;
    TextView hotellocal;
    RatingBar hotelscore;

    public HotelItemView(Context context) {
        super(context);
        init(context);
    }

    public HotelItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.hotel_item, this, true);
        hotelname = (TextView) findViewById(R.id.hotelname);
        hotellocal = (TextView) findViewById(R.id.local);
        hotelscore = (RatingBar) findViewById(R.id.score);
    }

    public void setName(String name) {
        hotelname.setText(name);
    }

    public void setCnt(String local) {
        hotellocal.setText(local);
    }

    public void setRes(String score) {
        hotelscore.setRating(Float.parseFloat(score));
    }
}
