package site.fieldmaus.hotelline;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FlightInfo extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private static final String URL_FOR_REGISTRATION = "https://test.fieldmaus.site/flight_update.php";
    ProgressDialog progressDialog;

    private EditText text_dept, text_dest, text_dept_time, text_fly_time, text_limit_cnt, text_value;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_info);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        text_dept = (EditText) findViewById(R.id.text_dept);
        text_dest = (EditText) findViewById(R.id.text_dest);
        text_dept_time = (EditText) findViewById(R.id.text_dept_time);
        text_fly_time = (EditText) findViewById(R.id.text_fly_time);
        text_limit_cnt = (EditText) findViewById(R.id.text_limit_cnt);
        text_value = (EditText) findViewById(R.id.text_value);

        final String flight_id = getIntent().getExtras().getString("flight_id");
        final String flight_dept = getIntent().getExtras().getString("flight_dept");
        final String flight_dest = getIntent().getExtras().getString("flight_dest");
        final String flight_dept_time = getIntent().getExtras().getString("flight_dept_time");
        final String flight_fly_time = getIntent().getExtras().getString("flight_fly_time");
        final String flight_limit_cnt = getIntent().getExtras().getString("flight_limit_cnt");
        final String flight_value = getIntent().getExtras().getString("flight_value");

        text_dept.setText(flight_dept);
        text_dest.setText(flight_dest);
        text_dept_time.setText(flight_dept_time);
        text_fly_time.setText(flight_fly_time);
        text_limit_cnt.setText(flight_limit_cnt);
        text_value.setText(flight_value);

        showTimePickerDialog(text_dept_time);
        showTimePickerDialog(text_fly_time);

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout layout_dept = (TextInputLayout) findViewById(R.id.layout_dept);
                TextInputLayout layout_dest = (TextInputLayout) findViewById(R.id.layout_dest);
                TextInputLayout layout_dept_time = (TextInputLayout) findViewById(R.id.layout_dept_time);
                TextInputLayout layout_fly_time = (TextInputLayout) findViewById(R.id.layout_fly_time);
                TextInputLayout layout_limit_cnt = (TextInputLayout) findViewById(R.id.layout_limit_cnt);
                TextInputLayout layout_value = (TextInputLayout) findViewById(R.id.layout_value);

                if (text_dept.getText().toString().equals(""))
                    layout_dept.setError("출발지를 입력해주세요");
                else if (text_dest.getText().toString().equals(""))
                    layout_dest.setError("목적지를 입력해주세요");
                else if (text_dept_time.getText().toString().equals(""))
                    layout_dept_time.setError("출발 시각을 입력해주세요");
                else if (text_fly_time.getText().toString().equals(""))
                    layout_fly_time.setError("비행 시간을 입력해주세요");
                else if (text_limit_cnt.getText().toString().equals(""))
                    layout_limit_cnt.setError("정원을 입력해주세요");
                else if (text_value.getText().toString().equals(""))
                    layout_value.setError("가격을 입력해주세요");
                else {
                    registerFlight(flight_id, text_dept.getText().toString(), text_dest.getText().toString(), text_dept_time.getText().toString(), text_fly_time.getText().toString(), text_limit_cnt.getText().toString(), text_value.getText().toString());
                    finish();
                }
            }
        });
    }

    private void registerFlight(final String flight_id, final String flight_dept, final String flight_dest, final String flight_dept_time,
                                final String flight_fly_time, final String flight_limit_cnt, final String flight_value) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Updating Flight ...");
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
                        Toast.makeText(FlightInfo.this, flight_id, Toast.LENGTH_SHORT).show();
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
                params.put("flight_id", flight_id);
                params.put("flight_dept", flight_dept);
                params.put("flight_dest", flight_dest);
                params.put("flight_dept_time", flight_dept_time);
                params.put("flight_fly_time", flight_fly_time);
                params.put("flight_limit_cnt", flight_limit_cnt);
                params.put("flight_value", flight_value);
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

    private void showTimePickerDialog(final EditText editText) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new OnTimeSetListener instance. This listener will be invoked when user click ok button in TimePickerDialog.
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        StringBuffer strBuf = new StringBuffer();
                        strBuf.append(hour);
                        strBuf.append(":");
                        strBuf.append(minute);
                        strBuf.append(":00");

                        editText.setText(strBuf.toString());
                    }
                };

                // Whether show time in 24 hour format or not.
                boolean is24Hour = true;

                TimePickerDialog timePickerDialog = new TimePickerDialog(FlightInfo.this, onTimeSetListener, 0, 0, is24Hour);

                timePickerDialog.show();
            }
        });
    }

}
