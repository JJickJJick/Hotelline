package site.fieldmaus.hotelline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AirManagement extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final String URL_FOR_LOGIN = "https://test.fieldmaus.site/airline_management.php";
    ProgressDialog progressDialog;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_management);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        listView = (ListView) findViewById(R.id.airmnglist);
        getAirList(getIntent().getExtras().getString("member_id"));

        Button btn_insair = findViewById(R.id.btn_insair);
        btn_insair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AirRegister.class);
                intent.putExtra("member_id", getIntent().getExtras().getString("member_id"));
                startActivity(intent);
            }
        });
    }

    private void getAirList(final String airline_owner) {
        // Tag used to cancel the request
        String cancel_req_tag = "login";
        progressDialog.setMessage("ttt");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        JSONArray airline = jObj.getJSONArray("airline");
                        final String[] airline_id = new String[airline.length()];
                        final String[] airline_name = new String[airline.length()];
                        final String[] airline_code = new String[airline.length()];
                        AirlineAdapter adapter = new AirlineAdapter();
                        for (int i = 0; i < airline.length(); i++) {
                            String aid = airline.getJSONObject(i).getString("airline_id");
                            String name = airline.getJSONObject(i).getString("airline_name");
                            String code = airline.getJSONObject(i).getString("airline_code");
                            airline_id[i] = aid;
                            airline_name[i] = name;
                            airline_code[i] = code;
                            adapter.addItem(new AirlineItem(name, code));
                        }
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getApplicationContext(), FlightManagement.class);
                                intent.putExtra("airline_id", airline_id[position]);
                                intent.putExtra("airline_name", airline_name[position]);
                                intent.putExtra("airline_code", airline_code[position]);
                                startActivity(intent);
                            }
                        });
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("airline_owner", airline_owner);
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

    private class AirlineAdapter extends BaseAdapter {
        ArrayList<AirlineItem> items = new ArrayList<>();

        public int getCount() {
            return items.size();
        }

        public void addItem(AirlineItem item) {
            items.add(item);
        }

        public Object getItem(int position) {
            return items.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup viewGroup) {
            AirlineItemView view = new AirlineItemView(getApplicationContext());
            AirlineItem item = items.get(position);
            view.setName(item.getName());
            view.setCode(item.getCode());

            return view;
        }
    }
}