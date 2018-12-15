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

public class HotelManagement extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final String URL_FOR_LOGIN = "https://test.fieldmaus.site/hotel_management.php";
    ProgressDialog progressDialog;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_management);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        listView = (ListView) findViewById(R.id.hotelmnglist);
        getHotelList(getIntent().getExtras().getString("member_id"));

        Button btninshotel = findViewById(R.id.inshotel);
        btninshotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HotelRegister.class);
                intent.putExtra("member_id", getIntent().getExtras().getString("member_id"));
                startActivity(intent);
            }
        });
    }

    private void getHotelList(final String hotel_owner) {
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
                        JSONArray hotel = jObj.getJSONArray("hotel");
                        final String[] hotel_id = new String[hotel.length()];
                        final String[] hotel_name = new String[hotel.length()];
                        final String[] hotel_local = new String[hotel.length()];
                        final String[] hotel_score = new String[hotel.length()];
                        HotelAdapter adapter = new HotelAdapter();
                        for (int i = 0; i < hotel.length(); i++) {
                            String hid = hotel.getJSONObject(i).getString("hotel_id");
                            String name = hotel.getJSONObject(i).getString("hotel_name");
                            String local = hotel.getJSONObject(i).getString("hotel_local");
                            String score = hotel.getJSONObject(i).getString("hotel_score");
                            hotel_id[i] = hid;
                            hotel_name[i] = name;
                            hotel_local[i] = local;
                            hotel_score[i] = score;
                            adapter.addItem(new HotelItem(name, local, score));
                        }
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getApplicationContext(), RoomManagement.class);
                                intent.putExtra("hotel_id", hotel_id[position]);
                                intent.putExtra("hotel_name", hotel_name[position]);
                                intent.putExtra("hotel_local", hotel_local[position]);
                                intent.putExtra("hotel_score", hotel_score[position]);
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
                params.put("hotel_owner", hotel_owner);
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

    private class HotelAdapter extends BaseAdapter {
        ArrayList<HotelItem> items = new ArrayList<>();

        public int getCount() {
            return items.size();
        }

        public void addItem(HotelItem item) {
            items.add(item);
        }

        public Object getItem(int position) {
            return items.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup viewGroup) {
            HotelItemView view = new HotelItemView(getApplicationContext());
            HotelItem item = items.get(position);
            view.setName(item.getName());
            view.setCnt(item.getLocal());
            view.setRes(item.getScore());

            return view;
        }
    }
}