package site.fieldmaus.hotelline;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {


    public Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layout = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView nameview = (TextView) layout.findViewById(R.id.nameview);
        TextView emailview = (TextView) layout.findViewById(R.id.emailview);
        TextView typeview = (TextView) layout.findViewById(R.id.typeview);
        String name = getArguments().getString("name");
        String email = getArguments().getString("email");
        String type = getArguments().getString("type");
        nameview.setText(name);
        emailview.setText(email);
        typeview.setText(type);


        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        String[] list_menu = {"예약 목록 확인", "항공사 관리", "호텔 관리"};
        String[] list_menu_des = {"예약 확인, 수정, 취소", "예약 관리, 수입 관리", "예약 관리, 수입 관리"};

        for (int i = 0; i < list_menu.length; i++) {
            HashMap<String, String> item = new HashMap<>();
            item.put("item 1", list_menu[i]);
            item.put("item 2", list_menu_des[i]);
            list.add(item);
        }

        ListView listview = (ListView) layout.findViewById(R.id.profilelist);
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), list, android.R.layout.simple_list_item_2, new String[]{"item 1", "item 2"}, new int[]{android.R.id.text1, android.R.id.text2});
        listview.setAdapter(simpleAdapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent = new Intent();

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    intent = new Intent(getActivity().getBaseContext(), MyReserve.class);
                else if (position == 1)
                    intent = new Intent(getActivity().getBaseContext(), AirManagement.class);
                else if (position == 2)
                    intent = new Intent(getActivity().getBaseContext(), HotelManagement.class);

                intent.putExtra("member_id", getArguments().getString("id"));
                startActivity(intent);
            }
        });

        return layout;
    }

}
