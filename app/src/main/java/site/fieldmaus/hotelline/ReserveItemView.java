package site.fieldmaus.hotelline;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class ReserveItemView extends LinearLayout {
    TextView text_product_type, text_reserve_date, text_product_name, text_product_company, text_product_value;
    RatingBar rating_product_score;

    public ReserveItemView(Context context) {
        super(context);
        init(context);
    }

    public ReserveItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.reserve_item, this, true);
        text_product_type = (TextView) findViewById(R.id.text_product_type);
        text_reserve_date = (TextView) findViewById(R.id.text_reserve_date);
        text_product_name = (TextView) findViewById(R.id.text_product_name);
        text_product_company = (TextView) findViewById(R.id.text_product_company);
        text_product_value = (TextView) findViewById(R.id.text_product_value);
        rating_product_score = (RatingBar) findViewById(R.id.rating_product_score);
    }

    public void setProduct_type(String product_type) {
        text_product_type.setText(product_type);
    }

    public void setReserve_date(String res_date) {
        text_reserve_date.setText(res_date);
    }

    public void setProduct_name(String product_name) {
        text_product_name.setText(product_name);
    }

    public void setProduct_company(String product_company) {
        text_product_company.setText(product_company);
    }

    public void setProduct_value(String product_value) {
        text_product_value.setText(product_value);
    }

    public void setProduct_score(String product_score) {
        rating_product_score.setRating(Float.parseFloat(product_score));
    }
}
