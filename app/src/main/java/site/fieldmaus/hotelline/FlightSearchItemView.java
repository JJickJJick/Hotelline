package site.fieldmaus.hotelline;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class FlightSearchItemView extends LinearLayout {
    TextView text_flight_code, text_airline_name, text_flight_value, text_dept, text_dest, text_dept_time, text_dest_time;
    RatingBar rating_flight_score;

    public FlightSearchItemView(Context context) {
        super(context);
        init(context);
    }

    public FlightSearchItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.flight_search_item, this, true);
        text_flight_code = (TextView) findViewById(R.id.text_flight_code);
        text_airline_name = (TextView) findViewById(R.id.text_airline_name);
        text_flight_value = (TextView) findViewById(R.id.text_flight_value);
        text_dept = (TextView) findViewById(R.id.text_dept);
        text_dest = (TextView) findViewById(R.id.text_dest);
        text_dept_time = (TextView) findViewById(R.id.label_airline_code);
        text_dest_time = (TextView) findViewById(R.id.text_dest_time);
        rating_flight_score = (RatingBar) findViewById(R.id.rating_flight_score);
    }

    public void setFlightcode(String flightcode) {
        text_flight_code.setText(flightcode);
    }

    public void setAirname(String airlinename) {
        text_airline_name.setText(airlinename);
    }

    public void setValue(String flightvalue) {
        text_flight_value.setText(flightvalue);
    }

    public void setDept(String dept) {
        text_dept.setText(dept);
    }

    public void setDest(String dest) {
        text_dest.setText(dest);
    }

    public void setStarttime(String depttime) {
        text_dept_time.setText(depttime);
    }

    public void setEndtime(String desttime) {
        text_dest_time.setText(desttime);
    }

    public void setScore(String score) {
        rating_flight_score.setRating(Float.parseFloat(score));
    }
}
