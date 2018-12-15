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

public class RoomManagement extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final String URL_FOR_LOGIN = "https://test.fieldmaus.site/room_management.php";
    ProgressDialog progressDialog;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_management);
        final String hotel_id = getIntent().getExtras().getString("hotel_id");
        final String hotel_name = getIntent().getExtras().getString("hotel_name");
        final String hotel_local = getIntent().getExtras().getString("hotel_local");
        final float hotel_score = Float.parseFloat(getIntent().getExtras().getString("hotel_score"));
        TextView text_hotel_name = findViewById(R.id.hotelname);
        TextView text_hotel_local = findViewById(R.id.hotellocal);
        RatingBar rating_hotel_score = findViewById(R.id.scorebar);
        text_hotel_name.setText(hotel_name);
        text_hotel_local.setText(hotel_local);
        rating_hotel_score.setRating(hotel_score);
        Button ins_room = findViewById(R.id.insroom);
        ins_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RoomRegister.class);
                intent.putExtra("hotel_id", hotel_id);
                startActivity(intent);
            }
        });

        Button btn_hotel_info = findViewById(R.id.btn_hotel_info);
        btn_hotel_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HotelInfo.class);
                intent.putExtra("hotel_id", hotel_id);
                intent.putExtra("hotel_name", getIntent().getExtras().getString("hotel_name"));
                intent.putExtra("hotel_local", getIntent().getExtras().getString("hotel_local"));
                startActivity(intent);
            }
        });


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        listView = (ListView) findViewById(R.id.roommnglist);

        getRoomList(hotel_id);
    }

    private void getRoomList(final String hotel_id) {
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
                        final String[] room_limit_cnt = new String[room.length()];
                        final String[] room_value = new String[room.length()];
                        RoomManagement.RoomAdapter adapter = new RoomManagement.RoomAdapter();
                        for (int i = 0; i < room.length(); i++) {
                            String id = room.getJSONObject(i).getString("room_id");
                            String name = room.getJSONObject(i).getString("room_name");
                            String limit_cnt = room.getJSONObject(i).getString("limit_cnt");
                            String value = room.getJSONObject(i).getString("room_value");
                            room_id[i] = id;
                            room_name[i] = name;
                            room_limit_cnt[i] = limit_cnt;
                            room_value[i] = value;
                            adapter.addItem(new RoomItem(name, limit_cnt));
                        }
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getApplicationContext(), RoomInfo.class);
                                intent.putExtra("hotel_name", getIntent().getExtras().getString("hotel_name"));
                                intent.putExtra("room_id", room_id[position]);
                                intent.putExtra("room_name", room_name[position]);
                                intent.putExtra("limit_cnt", room_limit_cnt[position]);
                                intent.putExtra("room_value", room_value[position]);
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
        ArrayList<RoomItem> items = new ArrayList<>();

        public int getCount() {
            return items.size();
        }

        public void addItem(RoomItem item) {
            items.add(item);
        }

        public Object getItem(int position) {
            return items.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup viewGroup) {
            RoomItemView view = new RoomItemView(getApplicationContext());
            RoomItem item = items.get(position);
            view.setName(item.getName());
            view.setLimitcnt(item.getLimitcnt());

            return view;
        }
    }
}
