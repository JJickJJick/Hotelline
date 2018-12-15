package site.fieldmaus.hotelline;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RoomItemView extends LinearLayout {
    TextView roomname;
    TextView limitcnt;

    public RoomItemView(Context context) {
        super(context);
        init(context);
    }

    public RoomItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.room_item, this, true);
        roomname = (TextView) findViewById(R.id.roomname);
        limitcnt = (TextView) findViewById(R.id.limitcnt);
    }

    public void setName(String name) {
        roomname.setText(name);
    }

    public void setLimitcnt(String cnt) {
        limitcnt.setText(cnt);
    }
}
