package site.fieldmaus.hotelline;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HotelReserve extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private static final String URL_FOR_REGISTRATION = "https://test.fieldmaus.site/hotel_reserve.php";
    ProgressDialog progressDialog;

    TextView hotel_name, room_name, start_date, end_date, day, head_cnt, value, final_value;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_reserve);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        hotel_name = findViewById(R.id.flight_code);
        room_name = findViewById(R.id.room_name);
        start_date = findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);
        day = findViewById(R.id.day);
        head_cnt = findViewById(R.id.head_cnt);
        value = findViewById(R.id.value);
        final_value = findViewById(R.id.final_value);

        String begin = getIntent().getExtras().getString("start_date");
        String end = getIntent().getExtras().getString("end_date");
        Date beginDate = stringToDate(begin, "yyyy-MM-dd");
        Date endDate = stringToDate(end, "yyyy-MM-dd");
        Date nowdate = new Date(System.currentTimeMillis() + 32400000);
        SimpleDateFormat dtos = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int day_value = Integer.parseInt(getIntent().getExtras().getString("room_value"));
        int between_date = (int) (long) (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);

        final String ID = getIntent().getExtras().getString("member_id");
        final String HID = getIntent().getExtras().getString("hotel_id");
        final String RID = getIntent().getExtras().getString("room_id");
        final String HEADCNT = getIntent().getExtras().getString("head_cnt");
        final String STARTDATE = getIntent().getExtras().getString("start_date") + " 14:00:00";
        final String ENDDATE = getIntent().getExtras().getString("end_date") + " 11:00:00";
        final String VALUE = String.valueOf(day_value * between_date);
        final String RDATE = dtos.format(nowdate);

        hotel_name.setText(getIntent().getExtras().getString("hotel_name"));
        room_name.setText(getIntent().getExtras().getString("room_name"));
        start_date.setText(STARTDATE + " ~ " + ENDDATE);
        end_date.setText(RDATE);
        day.setText(String.valueOf(between_date));
        head_cnt.setText(HEADCNT + "인");
        final_value.setText(VALUE + "원");

        btnRegister = (Button) findViewById(R.id.btn_reserve);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reserveHotel(ID, HID, RID, HEADCNT, STARTDATE, ENDDATE, VALUE, RDATE);
            }
        });
    }

    private Date stringToDate(String aDate, String aFormat) {
        if (aDate == null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;
    }

    private void reserveHotel(final String member_id, final String hotel_id, final String room_id, final String head_cnt,
                              final String start_date, final String end_date, final String value, final String res_date) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Adding hotel ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_REGISTRATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        Toast.makeText(getApplicationContext(), "정상적으로 예약되었습니다!", Toast.LENGTH_SHORT).show();

                        // Launch login activity
                        finish();
                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", member_id);
                params.put("hid", hotel_id);
                params.put("rid", room_id);
                params.put("headcnt", head_cnt);
                params.put("startdate", start_date);
                params.put("enddate", end_date);
                params.put("value", value);
                params.put("resdate", res_date);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
