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
import android.widget.RatingBar;
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

public class RoomSearch extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final String URL_FOR_LOGIN = "https://test.fieldmaus.site/room_search.php";
    ProgressDialog progressDialog;

    TextView hotel_name, hotel_local;
    RatingBar hotel_rate;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_search);
        hotel_name = findViewById(R.id.flight_code);
        hotel_local = findViewById(R.id.hotel_local);
        hotel_rate = findViewById(R.id.hotel_rate);
        hotel_local.setText(getIntent().getExtras().getString("hotel_local"));
        hotel_name.setText(getIntent().getExtras().getString("hotel_name"));
        hotel_rate.setRating(Float.parseFloat(getIntent().getExtras().getString("hotel_score")));

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        listView = (ListView) findViewById(R.id.room_list);
        getRoomList(getIntent().getExtras().getString("hotel_id"), getIntent().getExtras().getString("head_cnt"),
                getIntent().getExtras().getString("start_date"), getIntent().getExtras().getString("end_date"));
    }

    private void getRoomList(final String hotel_id, final String head_cnt, final String start_date, final String end_date) {
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
                        JSONArray room = jObj.getJSONArray("room");
                        final String[] room_id = new String[room.length()];
                        final String[] room_name = new String[room.length()];
                        final String[] limit_cnt = new String[room.length()];
                        final String[] room_value = new String[room.length()];
                        RoomSearch.RoomAdapter adapter = new RoomSearch.RoomAdapter();
                        for (int i = 0; i < room.length(); i++) {
                            String rid = room.getJSONObject(i).getString("room_id");
                            String name = room.getJSONObject(i).getString("room_name");
                            String cnt = room.getJSONObject(i).getString("limit_cnt");
                            String value = room.getJSONObject(i).getString("room_value");
                            room_id[i] = rid;
                            room_name[i] = name;
                            limit_cnt[i] = cnt;
                            room_value[i] = value;
                            adapter.addItem(new RoomSearchItem(name, cnt, value));
                        }
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getApplicationContext(), HotelReserve.class);
                                intent.putExtra("member_id", getIntent().getExtras().getString("member_id"));
                                intent.putExtra("hotel_id", getIntent().getExtras().getString("hotel_id"));
                                intent.putExtra("room_id", room_id[position]);
                                intent.putExtra("head_cnt", getIntent().getExtras().getString("head_cnt"));
                                intent.putExtra("start_date", getIntent().getExtras().getString("start_date"));
                                intent.putExtra("end_date", getIntent().getExtras().getString("end_date"));
                                intent.putExtra("room_value", room_value[position]);
                                intent.putExtra("hotel_name", getIntent().getExtras().getString("hotel_name"));
                                intent.putExtra("room_name", room_name[position]);
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
                params.put("hotel_id", hotel_id);
                params.put("head_cnt", head_cnt);
                params.put("start_date", start_date);
                params.put("end_date", end_date);
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

    private class RoomAdapter extends BaseAdapter {
        ArrayList<RoomSearchItem> items = new ArrayList<>();

        public int getCount() {
            return items.size();
        }

        public void addItem(RoomSearchItem item) {
            items.add(item);
        }

        public Object getItem(int position) {
            return items.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup viewGroup) {
            RoomSearchItemView view = new RoomSearchItemView(getApplicationContext());
            RoomSearchItem item = items.get(position);
            view.setName(item.getName());
            view.setLimitcnt(item.getCnt());
            view.setValue(item.getValue());

            return view;
        }
    }
}
