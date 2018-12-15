package site.fieldmaus.hotelline;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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
public class Hotel extends Fragment {

    static final String[] locals = new String[]{
            "서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시", "대전광역시", "울산광역시", "세종특별자치시",
            "경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도", "제주특별자치도"
    };
    private AutoCompleteTextView local;
    private EditText startdate, enddate, limitcnt;
    private Button searchbtn;
    private TextInputLayout local_layout, startdate_layout, enddate_layout, limitcnt_layout;

    public Hotel() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layout = inflater.inflate(R.layout.fragment_hotel, container, false);

        ArrayAdapter<String> localadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, locals);
        local = (AutoCompleteTextView) layout.findViewById(R.id.local);
        local.setAdapter(localadapter);

        startdate = (EditText) layout.findViewById(R.id.startdate);
        enddate = (EditText) layout.findViewById(R.id.enddate);
        limitcnt = (EditText) layout.findViewById(R.id.limitcnt);
        this.startdatePicker();
        this.enddatePicker();

        searchbtn = layout.findViewById(R.id.btn_search);
        local_layout = (TextInputLayout) layout.findViewById(R.id.local_layout);
        startdate_layout = (TextInputLayout) layout.findViewById(R.id.startdate_layout);
        enddate_layout = (TextInputLayout) layout.findViewById(R.id.enddate_layout);
        limitcnt_layout = (TextInputLayout) layout.findViewById(R.id.limitcnt_layout);

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (local.getText().toString().equals(""))
                    local_layout.setError("지역을 입력해주세요");
                else if (startdate.getText().toString().equals(""))
                    startdate_layout.setError("체크인 날짜를 입력해주세요");
                else if (enddate.getText().toString().equals(""))
                    enddate_layout.setError("체크아웃 날짜를 입력해주세요");
                else if (limitcnt.getText().toString().equals(""))
                    limitcnt_layout.setError("투숙 인원을 입력해주세요");
                else if (Integer.parseInt(limitcnt.getText().toString()) < 1)
                    limitcnt_layout.setError("투숙 인원은 1명 이상이여야 합니다");
                else {
                    Intent intent = new Intent();
                    intent = new Intent(getActivity().getBaseContext(), HotelSearch.class);
                    intent.putExtra("member_id", getArguments().getString("id"));
                    intent.putExtra("hotel_local", local.getText().toString());
                    intent.putExtra("start_date", startdate.getText().toString());
                    intent.putExtra("end_date", enddate.getText().toString());
                    intent.putExtra("head_cnt", limitcnt.getText().toString());
                    startActivity(intent);
                }
            }
        });

        return layout;
    }

    private void startdatePicker() {
        startdate.setOnClickListener(new View.OnClickListener() {
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

                        startdate.setText(strBuf.toString());
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

    private void enddatePicker() {
        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startdate.getText().toString().equals(""))
                    Toast.makeText(getActivity(), "체크 인 날짜를 선택해주세요", Toast.LENGTH_SHORT).show();
                else {
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

                            enddate.setText(strBuf.toString());
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
            }
        });
    }
}
