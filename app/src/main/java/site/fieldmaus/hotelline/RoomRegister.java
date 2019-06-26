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

public class RoomRegister extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private static final String URL_FOR_REGISTRATION = "https://test.fieldmaus.site/room_register.php";
    ProgressDialog progressDialog;

    private EditText roomname, limitcnt, text_value;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_register);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        roomname = (EditText) findViewById(R.id.roomname);
        limitcnt = (EditText) findViewById(R.id.limitcnt);
        text_value = (EditText) findViewById(R.id.text_value);

        btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout roomnamelayout = (TextInputLayout) findViewById(R.id.roomname_layout);
                TextInputLayout limitcntlayout = (TextInputLayout) findViewById(R.id.limitcnt_layout);
                TextInputLayout layout_value = (TextInputLayout) findViewById(R.id.layout_value);

                if (roomname.getText().toString().equals(""))
                    roomnamelayout.setError("방 이름을 입력해주세요");
                else if (Integer.parseInt(limitcnt.getText().toString()) < 1)
                    limitcntlayout.setError("정원은 최소 1명 이상이어야 합니다.");
                else if (text_value.getText().toString().equals(""))
                    layout_value.setError("가격을 입력해주세요");
                else
                    submitForm();
            }
        });
    }

    private void submitForm() {
        registerRoom(roomname.getText().toString(), getIntent().getExtras().getString("hotel_id"), limitcnt.getText().toString(), text_value.getText().toString());
    }

    private void registerRoom(final String room_name, final String hotel_id, final String limit_cnt, final String product_value) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Adding room ...");
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
                        Toast.makeText(getApplicationContext(), "정상적으로 등록했습니다!", Toast.LENGTH_SHORT).show();

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
                params.put("room_name", room_name);
                params.put("hotel_id", hotel_id);
                params.put("limit_cnt", limit_cnt);
                params.put("product_value", product_value);
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
