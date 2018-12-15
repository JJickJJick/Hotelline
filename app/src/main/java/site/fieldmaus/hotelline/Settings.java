package site.fieldmaus.hotelline;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {


    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_settings, container, false);

        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        String[] list_menu = {"버전", "제작"};
        String[] list_menu_des = {"0.1.0", "이태구, 김영민, 이재환, 이해솔, 배재혁"};

        for (int i = 0; i < list_menu.length; i++) {
            HashMap<String, String> item = new HashMap<>();
            item.put("item 1", list_menu[i]);
            item.put("item 2", list_menu_des[i]);
            list.add(item);
        }

        ListView listview = (ListView) layout.findViewById(R.id.setlist);
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), list, android.R.layout.simple_list_item_2, new String[]{"item 1", "item 2"}, new int[]{android.R.id.text1, android.R.id.text2});
        listview.setAdapter(simpleAdapter);

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    Toast.makeText(getActivity(), "과제에서 해방", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(), "S/W 공학 A+ 받고 싶습니다.", Toast.LENGTH_LONG).show();
                return true;
            }
        });


        return layout;
    }


}
