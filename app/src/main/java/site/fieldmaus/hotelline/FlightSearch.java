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

public class FlightSearch extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final String URL_FOR_LOGIN = "https://test.fieldmaus.site/flight_search.php";
    ProgressDialog progressDialog;

    TextView text_dept, text_dest, text_dept_date, text_head_cnt;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_search);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        final String dept = getIntent().getExtras().getString("dept");
        final String dest = getIntent().getExtras().getString("dest");
        final String dept_date = getIntent().getExtras().getString("dept_date");
        final String head_cnt = getIntent().getExtras().getString("head_cnt");

        text_dept = (TextView) findViewById(R.id.text_dept);
        text_dest = (TextView) findViewById(R.id.text_dest);
        text_dept_date = (TextView) findViewById(R.id.text_dept_date);
        text_head_cnt = (TextView) findViewById(R.id.text_head_cnt);
        text_dept.setText(dept);
        text_dest.setText(dest);
        text_dept_date.setText(dept_date);
        text_head_cnt.setText(head_cnt);

        listView = (ListView) findViewById(R.id.flight_list);
        getFlightList(dept, dest, dept_date, head_cnt);
    }

    private void getFlightList(final String flight_dept, final String flight_dest, final String dept_date, final String head_cnt) {
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
                        final String[] air_id = new String[flight.length()];
                        final String[] air_name = new String[flight.length()];
                        final String[] flight_id = new String[flight.length()];
                        final String[] flight_code = new String[flight.length()];
                        final String[] flight_start_time = new String[flight.length()];
                        final String[] flight_end_time = new String[flight.length()];
                        final String[] flight_score = new String[flight.length()];
                        final String[] flight_value = new String[flight.length()];
                        FlightSearch.FlightAdapter adapter = new FlightSearch.FlightAdapter();
                        for (int i = 0; i < flight.length(); i++) {
                            String aid = flight.getJSONObject(i).getString("airline_id");
                            String airname = flight.getJSONObject(i).getString("airline_name");
                            String fid = flight.getJSONObject(i).getString("flight_id");
                            String flightcode = flight.getJSONObject(i).getString("airline_code") + flight.getJSONObject(i).getString("flight_id");
                            String starttime = flight.getJSONObject(i).getString("flight_dept_time");
                            String endtime = flight.getJSONObject(i).getString("flight_dest_time");
                            String score = flight.getJSONObject(i).getString("flight_score");
                            String value = flight.getJSONObject(i).getString("flight_value");
                            air_id[i] = aid;
                            air_name[i] = airname;
                            flight_id[i] = fid;
                            flight_code[i] = flightcode;
                            flight_start_time[i] = starttime;
                            flight_end_time[i] = endtime;
                            flight_score[i] = score;
                            flight_value[i] = value;
                            adapter.addItem(new FlightSearchItem(airname, flight_dept, flight_dest, flightcode, starttime, endtime, score, value));
                        }
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getApplicationContext(), AirReserve.class);
                                intent.putExtra("member_id", getIntent().getExtras().getString("member_id"));
                                intent.putExtra("air_id", air_id[position]);
                                intent.putExtra("air_name", air_name[position]);
                                intent.putExtra("flight_id", flight_id[position]);
                                intent.putExtra("flight_code", flight_code[position]);
                                intent.putExtra("flight_start_time", flight_start_time[position]);
                                intent.putExtra("flight_end_time", flight_end_time[position]);
                                intent.putExtra("flight_score", flight_score[position]);
                                intent.putExtra("flight_value", flight_value[position]);
                                intent.putExtra("head_cnt", head_cnt);
                                startActivity(intent);
                            }
                        });
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                        finish();
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
                params.put("flight_dept", flight_dept);
                params.put("flight_dest", flight_dest);
                params.put("dept_date", dept_date);
                params.put("head_cnt", head_cnt);
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
        ArrayList<FlightSearchItem> items = new ArrayList<>();

        public int getCount() {
            return items.size();
        }

        public void addItem(FlightSearchItem item) {
            items.add(item);
        }

        public Object getItem(int position) {
            return items.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup viewGroup) {
            FlightSearchItemView view = new FlightSearchItemView(getApplicationContext());
            FlightSearchItem item = items.get(position);

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