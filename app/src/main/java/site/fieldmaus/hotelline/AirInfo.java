package site.fieldmaus.hotelline;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AirInfo extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private static final String URL_FOR_REGISTRATION = "https://test.fieldmaus.site/airline_update.php";
    ProgressDialog progressDialog;

    private EditText text_airline_name, text_airline_code;
    private Button btn_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_info);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        text_airline_name = (EditText) findViewById(R.id.text_airline_name);
        text_airline_code = (EditText) findViewById(R.id.text_airline_code);

        text_airline_name.setText(getIntent().getExtras().getString("airline_name"));
        text_airline_code.setText(getIntent().getExtras().getString("airline_code"));

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout layout_airline_name = (TextInputLayout) findViewById(R.id.layout_airline_name);
                TextInputLayout layout_airline_code = (TextInputLayout) findViewById(R.id.layout_airline_code);

                if (text_airline_name.getText().toString().equals(""))
                    layout_airline_name.setError("항공사 이름을 입력해주세요");
                else if (text_airline_code.getText().toString().equals(""))
                    layout_airline_code.setError("항공사 코드를 입력해주세요");
                else
                    registerHotel(text_airline_name.getText().toString(), getIntent().getExtras().getString("airline_id"), text_airline_code.getText().toString());
            }
        });
    }

    private void registerHotel(final String airline_name, final String airline_id, final String airline_code) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Updating Airline ...");
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
                params.put("airline_name", airline_name);
                params.put("airline_id", airline_id);
                params.put("airline_code", airline_code);
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
