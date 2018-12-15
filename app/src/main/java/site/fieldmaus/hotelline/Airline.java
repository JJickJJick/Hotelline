package site.fieldmaus.hotelline;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class Airline extends DialogFragment {

    static final String[] locals = new String[]{
            "서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시", "대전광역시", "울산광역시", "세종특별자치시",
            "경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도", "제주특별자치도"
    };
    private AutoCompleteTextView text_dept, text_dest;
    private EditText text_dept_date, text_head_cnt;
    private Button btn_search;
    private TextInputLayout layout_dept, layout_dest, layout_dept_date, layout_head_cnt;

    public Airline() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_airline, container, false);

        ArrayAdapter<String> localadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, locals);
        text_dept = (AutoCompleteTextView) layout.findViewById(R.id.text_dept);
        text_dest = (AutoCompleteTextView) layout.findViewById(R.id.text_dest);
        text_dept.setAdapter(localadapter);
        text_dest.setAdapter(localadapter);

        text_dept_date = (EditText) layout.findViewById(R.id.text_dept_date);
        text_head_cnt = (EditText) layout.findViewById(R.id.text_head_cnt);
        this.deptdatePicker();

        btn_search = layout.findViewById(R.id.btn_search);
        layout_dept = (TextInputLayout) layout.findViewById(R.id.layout_dept);
        layout_dest = (TextInputLayout) layout.findViewById(R.id.layout_dest);
        layout_dept_date = (TextInputLayout) layout.findViewById(R.id.layout_dept_date);
        layout_head_cnt = (TextInputLayout) layout.findViewById(R.id.layout_head_cnt);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text_dept.getText().toString().equals(""))
                    layout_dept.setError("출발지를 입력해주세요");
                else if (text_dest.getText().toString().equals(""))
                    layout_dest.setError("목적지를 입력해주세요");
                else if (text_dept_date.getText().toString().equals(""))
                    layout_dept_date.setError("출발 일자를 입력해주세요");
                else if (text_head_cnt.getText().toString().equals(""))
                    layout_head_cnt.setError("탑승 인원을 입력해주세요");
                else if (Integer.parseInt(text_head_cnt.getText().toString()) < 1)
                    layout_head_cnt.setError("탑승 인원은 1명 이상이여야 합니다");
                else {
                    Intent intent = new Intent();
                    intent = new Intent(getActivity().getBaseContext(), FlightSearch.class);
                    intent.putExtra("member_id", getArguments().getString("id"));
                    intent.putExtra("dept", text_dept.getText().toString());
                    intent.putExtra("dest", text_dest.getText().toString());
                    intent.putExtra("dept_date", text_dept_date.getText().toString());
                    intent.putExtra("head_cnt", text_head_cnt.getText().toString());
                    startActivity(intent);
                }
            }
        });

        return layout;
    }

    private void deptdatePicker() {
        text_dept_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new OnDateSetListener instance. This listener will be invoked when user click ok button in DatePickerDialog.
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        StringBuffer strBuf = new StringBuffer();
                        strBuf.append(year);
                        strBuf.append("-");
                        strBuf.append(month + 1);
                        strBuf.append("-");
                        strBuf.append(dayOfMonth);

                        text_dept_date.setText(strBuf.toString());
                    }
                };

                // Get current year, month and day.
                Calendar now = Calendar.getInstance();
                int year = now.get(java.util.Calendar.YEAR);
                int month = now.get(java.util.Calendar.MONTH);
                int day = now.get(java.util.Calendar.DAY_OF_MONTH);

                // Create the new DatePickerDialog instance.
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), onDateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                // Popup the dialog.
                datePickerDialog.show();
            }
        });
    }

}
