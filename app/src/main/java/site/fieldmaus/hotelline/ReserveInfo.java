package site.fieldmaus.hotelline;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReserveInfo extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private static final String URL_FOR_REGISTRATION = "https://test.fieldmaus.site/reserve_update.php";
    ProgressDialog progressDialog;

    private EditText text_company, text_product, text_start_name, text_end_time, text_head_cnt, text_value;
    private RatingBar rating_value;
    private Button btn_register;
    private float score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_info);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        text_company = (EditText) findViewById(R.id.text_company);
        text_product = (EditText) findViewById(R.id.text_product);
        text_start_name = (EditText) findViewById(R.id.text_start_name);
        text_end_time = (EditText) findViewById(R.id.text_end_time);
        text_head_cnt = (EditText) findViewById(R.id.text_head_cnt);
        text_value = (EditText) findViewById(R.id.text_value);
        rating_value = (RatingBar) findViewById(R.id.rating_value);

        text_start_name.setText(getIntent().getExtras().getString("start_date"));
        text_end_time.setText(getIntent().getExtras().getString("end_date"));
        text_head_cnt.setText(getIntent().getExtras().getString("head_cnt"));
        text_value.setText(getIntent().getExtras().getString("product_value"));
        rating_value.setRating(Float.parseFloat(getIntent().getExtras().getString("product_score")));

        final String type = getIntent().getExtras().getString("product_type");
        if (type.equals("hotel")) {
            text_company.setText(getIntent().getExtras().getString("hotel_name"));
            text_product.setText(getIntent().getExtras().getString("room_name"));
            text_company.setHint("호텔 이름");
            text_product.setHint("방 이름");
        } else {
            text_company.setText(getIntent().getExtras().getString("airline_name"));
            text_product.setText(getIntent().getExtras().getString("flight_name"));
            text_company.setHint("항공사 이름");
            text_product.setHint("항공 편명");
        }

        rating_value.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                score = rating;
            }
        });

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout layout_company = (TextInputLayout) findViewById(R.id.layout_company);
                TextInputLayout layout_product = (TextInputLayout) findViewById(R.id.layout_product);
                TextInputLayout layout_start_time = (TextInputLayout) findViewById(R.id.layout_start_time);
                TextInputLayout layout_end_time = (TextInputLayout) findViewById(R.id.layout_end_time);
                TextInputLayout layout_head_cnt = (TextInputLayout) findViewById(R.id.layout_head_cnt);
                TextInputLayout layout_value = (TextInputLayout) findViewById(R.id.layout_value);

                registerScore(getIntent().getExtras().getString("reserve_id"), String.valueOf(score));
            }
        });
    }

    private void registerScore(final String reserve_id, final String reserve_score) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Updating Score ...");
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
                        Toast.makeText(getApplicationContext(), "정상적으로 변경했습니다!", Toast.LENGTH_SHORT).show();

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
                params.put("reserve_id", reserve_id);
                params.put("reserve_score", reserve_score);
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
