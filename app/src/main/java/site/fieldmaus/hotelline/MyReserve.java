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

public class MyReserve extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final String URL_FOR_LOGIN = "https://test.fieldmaus.site/reserve_search.php";
    ProgressDialog progressDialog;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reserve);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        final String member_id = getIntent().getExtras().getString("member_id");


        listView = (ListView) findViewById(R.id.res_list);
        getFlightList(member_id);
    }

    private void getFlightList(final String member_id) {
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
                        JSONArray reserve = jObj.getJSONArray("reserve");
                        final String[] reserve_id = new String[reserve.length()];
                        final String[] product_type = new String[reserve.length()];
                        final String[] hotel_id = new String[reserve.length()];
                        final String[] hotel_name = new String[reserve.length()];
                        final String[] room_id = new String[reserve.length()];
                        final String[] room_name = new String[reserve.length()];
                        final String[] airline_id = new String[reserve.length()];
                        final String[] airline_name = new String[reserve.length()];
                        final String[] flight_id = new String[reserve.length()];
                        final String[] flight_name = new String[reserve.length()];
                        final String[] head_cnt = new String[reserve.length()];
                        final String[] start_date = new String[reserve.length()];
                        final String[] end_date = new String[reserve.length()];
                        final String[] reserve_date = new String[reserve.length()];
                        final String[] product_score = new String[reserve.length()];
                        final String[] product_value = new String[reserve.length()];
                        MyReserve.ReserveAdapter adapter = new MyReserve.ReserveAdapter();
                        for (int i = 0; i < reserve.length(); i++) {
                            String resid = reserve.getJSONObject(i).getString("reserve_id");
                            String type = reserve.getJSONObject(i).getString("product_type");
                            String hid = reserve.getJSONObject(i).getString("hotel_id");
                            String hname = reserve.getJSONObject(i).getString("hotel_name");
                            String rid = reserve.getJSONObject(i).getString("room_id");
                            String rname = reserve.getJSONObject(i).getString("room_name");
                            String aid = reserve.getJSONObject(i).getString("airline_id");
                            String aname = reserve.getJSONObject(i).getString("airline_name");
                            String fid = reserve.getJSONObject(i).getString("flight_id");
                            String fname = reserve.getJSONObject(i).getString("airline_code") + reserve.getJSONObject(i).getString("flight_id");
                            String headcnt = reserve.getJSONObject(i).getString("head_cnt");
                            String startdate = reserve.getJSONObject(i).getString("start_date");
                            String enddate = reserve.getJSONObject(i).getString("end_date");
                            String resdate = reserve.getJSONObject(i).getString("reserve_date");
                            String score = reserve.getJSONObject(i).getString("product_score");
                            String value = reserve.getJSONObject(i).getString("product_value");
                            reserve_id[i] = resid;
                            product_type[i] = type;
                            hotel_id[i] = hid;
                            hotel_name[i] = hname;
                            room_id[i] = rid;
                            room_name[i] = rname;
                            airline_id[i] = aid;
                            airline_name[i] = aname;
                            flight_id[i] = fid;
                            flight_name[i] = fname;
                            head_cnt[i] = headcnt;
                            start_date[i] = startdate;
                            end_date[i] = enddate;
                            reserve_date[i] = resdate;
                            product_score[i] = score;
                            product_value[i] = value;
                            if (type.equals("hotel"))
                                adapter.addItem(new ReserveItem("호텔", resdate, rname, hname, value, score));
                            else
                                adapter.addItem(new ReserveItem("항공", resdate, fname, aname, value, score));

                        }
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getApplicationContext(), ReserveInfo.class);
                                intent.putExtra("product_type", product_type[position]);
                                intent.putExtra("reserve_id", reserve_id[position]);
                                intent.putExtra("head_cnt", head_cnt[position]);
                                intent.putExtra("start_date", start_date[position]);
                                intent.putExtra("end_date", end_date[position]);
                                intent.putExtra("product_score", product_score[position]);
                                intent.putExtra("product_value", product_value[position]);
                                if (product_type[position].equals("hotel")) {
                                    intent.putExtra("hotel_name", hotel_name[position]);
                                    intent.putExtra("room_name", room_name[position]);
                                } else {
                                    intent.putExtra("airline_name", airline_name[position]);
                                    intent.putExtra("flight_name", flight_name[position]);
                                }
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
                params.put("member_id", member_id);
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

    private class ReserveAdapter extends BaseAdapter {
        ArrayList<ReserveItem> items = new ArrayList<>();

        public int getCount() {
            return items.size();
        }

        public void addItem(ReserveItem item) {
            items.add(item);
        }

        public Object getItem(int position) {
            return items.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ReserveItemView view = new ReserveItemView(getApplicationContext());
            ReserveItem item = items.get(position);

            view.setProduct_type(item.getProduct_type());
            view.setReserve_date(item.getReserve_date());
            view.setProduct_name(item.getProduct_name());
            view.setProduct_company(item.getProduct_company());
            view.setProduct_value(item.getProduct_value());
            view.setProduct_score(item.getProduct_score());

            return view;
        }
    }
}