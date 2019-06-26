package site.fieldmaus.hotelline;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AirReserve extends Activity {
    private static final String TAG = "RegisterActivity";
    private static final String URL_FOR_REGISTRATION = "https://test.fieldmaus.site/flight_reserve.php";
    ProgressDialog progressDialog;

    TextView flight_code, airline_name, start_date, end_date, head_value, head_cnt, final_value;
    Button btn_reserve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_reserve);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        flight_code = findViewById(R.id.flight_code);
        airline_name = findViewById(R.id.airline_name);
        start_date = findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);
        head_value = findViewById(R.id.head_value);
        head_cnt = findViewById(R.id.head_cnt);
        final_value = findViewById(R.id.final_value);

        Date nowdate = new Date(System.currentTimeMillis() + 32400000);
        SimpleDateFormat dtos = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int head = Integer.parseInt(getIntent().getExtras().getString("head_cnt"));
        int headvalue = Integer.parseInt(getIntent().getExtras().getString("flight_value"));

        final String ID = getIntent().getExtras().getString("member_id");
        final String AID = getIntent().getExtras().getString("air_id");
        final String FID = getIntent().getExtras().getString("flight_id");
        final String HEADCNT = getIntent().getExtras().getString("head_cnt");
        final String STARTDATE = getIntent().getExtras().getString("flight_start_time");
        final String ENDDATE = getIntent().getExtras().getString("flight_end_time");
        final String VALUE = String.valueOf(head * headvalue);
        final String RESDATE = dtos.format(nowdate);

        flight_code.setText(getIntent().getExtras().getString("flight_code") + "-" + getIntent().getExtras().getString("flight_id"));
        airline_name.setText(getIntent().getExtras().getString("air_name"));
        start_date.setText(STARTDATE);
        end_date.setText(ENDDATE);
        head_value.setText(String.valueOf(headvalue));
        head_cnt.setText(HEADCNT);
        final_value.setText(VALUE);

        btn_reserve = (Button) findViewById(R.id.btn_reserve);
        btn_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reserveFlight(ID, AID, FID, HEADCNT, STARTDATE, ENDDATE, VALUE, RESDATE);
            }
        });
    }

    private void reserveFlight(final String ID, final String AID, final String FID, final String HEADCNT,
                               final String STARTDATE, final String ENDDATE, final String VALUE, final String RESDATE) {
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", ID);
                params.put("aid", AID);
                params.put("fid", FID);
                params.put("headcnt", HEADCNT);
                params.put("startdate", STARTDATE);
                params.put("enddate", ENDDATE);
                params.put("value", VALUE);
                params.put("resdate", RESDATE);
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
