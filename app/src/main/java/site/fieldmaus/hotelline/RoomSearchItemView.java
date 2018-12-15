package site.fieldmaus.hotelline;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RoomSearchItemView extends LinearLayout {
    TextView roomname;
    TextView limitcnt;
    TextView roomvalue;

    public RoomSearchItemView(Context context) {
        super(context);
        init(context);
    }

    public RoomSearchItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.room_search_item, this, true);
        roomname = (TextView) findViewById(R.id.roomname);
        limitcnt = (TextView) findViewById(R.id.limitcnt);
        roomvalue = (TextView) findViewById(R.id.value);
    }

    public void setName(String name) {
        roomname.setText(name);
    }

    public void setLimitcnt(String cnt) {
        limitcnt.setText(cnt);
    }

    public void setValue(String value) {
        roomvalue.setText(value);
    }
}
