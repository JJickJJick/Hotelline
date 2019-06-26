package site.fieldmaus.hotelline;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AccountRegister extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private static final String URL_FOR_REGISTRATION = "https://test.fieldmaus.site/user_register.php";
    ProgressDialog progressDialog;

    private EditText signupInputName, signupInputEmail, signupInputPassword, signupInputPhone;
    private Button btnSignUp;
    private Button btnLinkLogin;
    private RadioGroup typeRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_register);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        signupInputEmail = (EditText) findViewById(R.id.signup_input_email);
        signupInputPassword = (EditText) findViewById(R.id.signup_input_password);
        signupInputName = (EditText) findViewById(R.id.signup_input_name);
        signupInputPhone = (EditText) findViewById(R.id.signup_input_phone);

        btnSignUp = (Button) findViewById(R.id.btn_signup);
        btnLinkLogin = (Button) findViewById(R.id.btn_link_login);

        typeRadioGroup = (RadioGroup) findViewById(R.id.type_radio_group);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout emaillayout = (TextInputLayout) findViewById(R.id.signup_input_layout_email);
                TextInputLayout pwlayout = (TextInputLayout) findViewById(R.id.signup_input_layout_password);
                TextInputLayout namelayout = (TextInputLayout) findViewById(R.id.signup_input_layout_name);
                TextInputLayout phonelayout = (TextInputLayout) findViewById(R.id.signup_input_layout_phone);

                if (signupInputEmail.getText().toString().equals(""))
                    emaillayout.setError("이메일을 정확히 입력해주세요");
                else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(signupInputEmail.getText().toString()).matches())
                    emaillayout.setError("이메일 형식이 올바르지 않습니다");
                else if (signupInputPassword.getText().toString().equals(""))
                    pwlayout.setError("비밀번호를 입력해주세요");
                else if (signupInputName.getText().toString().equals(""))
                    namelayout.setError("이름 입력해주세요");
                else if (signupInputPhone.getText().toString().equals(""))
                    phonelayout.setError("전화번호를 입력해주세요");
                else
                    submitForm();
            }
        });
        btnLinkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void submitForm() {

        int selectedId = typeRadioGroup.getCheckedRadioButtonId();
        String type;
        if (selectedId == R.id.usr_radio_btn)
            type = "User";
        else if (selectedId == R.id.air_radio_btn)
            type = "Air";
        else
            type = "Hotel";

        registerUser(signupInputName.getText().toString(),
                signupInputEmail.getText().toString(),
                signupInputPassword.getText().toString(),
                type,
                signupInputPhone.getText().toString());
    }

    private void registerUser(final String name, final String email, final String password,
                              final String type, final String tel) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Adding you ...");
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
                        String user = jObj.getJSONObject("user").getString("name");
                        Toast.makeText(getApplicationContext(), user + "님의 가입을 환영합니다!", Toast.LENGTH_SHORT).show();

                        // Launch login activity
                        //Intent intent = new Intent(AccountRegister.this, Login.class);
                        //startActivity(intent);
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
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("type", type);
                params.put("tel", tel);
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