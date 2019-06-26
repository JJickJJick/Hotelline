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
import android.widget.TextView;
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

public class FlightManagement extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final String URL_FOR_LOGIN = "https://test.fieldmaus.site/flight_management.php";
    ProgressDialog progressDialog;

    TextView text_airline_name;
    Button btn_flight_insert, btn_airline_info;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_management);

        text_airline_name = findViewById(R.id.text_airline_name);
        text_airline_name.setText(getIntent().getExtras().getString("airline_name"));
        btn_flight_insert = findViewById(R.id.btn_flight_insert);
        btn_flight_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FlightRegister.class);
                intent.putExtra("airline_id", getIntent().getExtras().getString("airline_id"));
                startActivity(intent);
            }
        });
        btn_airline_info = findViewById(R.id.btn_airline_info);
        btn_airline_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AirInfo.class);
                intent.putExtra("airline_id", getIntent().getExtras().getString("airline_id"));
                intent.putExtra("airline_name", getIntent().getExtras().getString("airline_name"));
                intent.putExtra("airline_code", getIntent().getExtras().getString("airline_code"));
                startActivity(intent);
            }
        });


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        listView = (ListView) findViewById(R.id.flightmnglist);

        getFlightList(getIntent().getExtras().getString("airline_id"));
    }

    private void getFlightList(final String airline_id) {
        // Tag used to cancel the request
        String cancel_req_tag = "login";
        progressDialog.setMessage("ttt");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        JSONArray flight = jObj.getJSONArray("flight");
                        final String[] flight_id = new String[flight.length()];
                        final String[] flight_dept = new String[flight.length()];
                        final String[] flight_dest = new String[flight.length()];
                        final String[] flight_start_time = new String[flight.length()];
                        final String[] flight_fly_time = new String[flight.length()];
                        final String[] flight_limit_cnt = new String[flight.length()];
                        final String[] flight_value = new String[flight.length()];
                        FlightAdapter adapter = new FlightAdapter();
                        for (int i = 0; i < flight.length(); i++) {
                            String aid = getIntent().getExtras().getString("airline_id");
                            String airname = getIntent().getExtras().getString("airline_name");
                            String fid = flight.getJSONObject(i).getString("flight_id");
                            String flightcode = getIntent().getExtras().getString("airline_code") + flight.getJSONObject(i).getString("flight_id");
                            String dept = flight.getJSONObject(i).getString("flight_dept");
                            String dest = flight.getJSONObject(i).getString("flight_dest");
                            String starttime = flight.getJSONObject(i).getString("flight_dept_time");
                            String endtime = flight.getJSONObject(i).getString("flight_fly_time");
                            String score = flight.getJSONObject(i).getString("flight_score");
                            String value = flight.getJSONObject(i).getString("flight_value");
                            String flytime = flight.getJSONObject(i).getString("flight_fly_time");
                            String limitcnt = flight.getJSONObject(i).getString("flight_limit_cnt");
                            flight_id[i] = fid;
                            flight_dept[i] = dept;
                            flight_dest[i] = dest;
                            flight_start_time[i] = starttime;
                            flight_fly_time[i] = flytime;
                            flight_limit_cnt[i] = limitcnt;
                            flight_value[i] = value;
                            adapter.addItem(new FlightItem(airname, dept, dest, flightcode, starttime, endtime, score, value));
                        }
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getApplicationContext(), FlightInfo.class);
                                intent.putExtra("flight_id", flight_id[position]);
                                intent.putExtra("flight_dept", flight_dept[position]);
                                intent.putExtra("flight_dest", flight_dest[position]);
                                intent.putExtra("flight_dept_time", flight_start_time[position]);
                                intent.putExtra("flight_fly_time", flight_fly_time[position]);
                                intent.putExtra("flight_limit_cnt", flight_limit_cnt[position]);
                                intent.putExtra("flight_value", flight_value[position]);
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
                params.put("airline_id", airline_id);
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

    private class FlightAdapter extends BaseAdapter {
        ArrayList<FlightItem> items = new ArrayList<>();

        public int getCount() {
            return items.size();
        }

        public void addItem(FlightItem item) {
            items.add(item);
        }

        public Object getItem(int position) {
            return items.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup viewGroup) {
            FlightItemView view = new FlightItemView(getApplicationContext());
            FlightItem item = items.get(position);

            view.setAirname(item.getAirname());
            view.setDept(item.getDept());
            view.setDest(item.getDest());
            view.setFlightcode(item.getFlightcode());
            view.setStarttime(item.getStarttime());
            view.setEndtime(item.getEndtime());
            view.setValue(item.getValue());
            view.setScore(item.getScore());

            return view;
        }
    }
}
