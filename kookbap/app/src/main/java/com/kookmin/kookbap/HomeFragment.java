package com.kookmin.kookbap;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.kookmin.kookbap.cafeteriaFragments.CafeteriaViewPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    JSONObject jsonObjectMain;

    TextView dateTextView;
    ImageView calendarButton;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    CafeteriaViewPagerAdapter cafeteriaViewPagerAdapter;

    String date, nowYear, nowMonth, nowDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 날짜 변수에 오늘 날짜 초기화 및 날짜텍스트뷰에 날짜 초기화
        dateTextView = view.findViewById(R.id.dateTextView);
        Calendar calendar = Calendar.getInstance();
        nowYear = Integer.toString(calendar.get(Calendar.YEAR));
        nowMonth = Integer.toString(calendar.get(Calendar.MONTH)+1);
        nowDate = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        nowMonth = nowMonth.length() < 2 ? "0" + Integer.parseInt(nowMonth)+1 : nowMonth;
        nowDate = nowDate.length() < 2 ? "0" + Integer.parseInt(nowDate) : nowDate;
        dateTextView.setText(nowYear +"-"+ nowMonth +"-"+ nowDate);


        calendarButton = view.findViewById(R.id.calendarButton);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                        nowYear = Integer.toString(yy);
                        nowMonth = mm+1 < 10 ? "0" + (mm+1) : "" + (mm+1);
                        nowDate = dd < 10 ? "0" + dd : "" + dd;
                        date = nowYear + "-" + nowMonth + "-" + nowDate;
                        dateTextView.setText(date);
                        loadData(nowYear + "-" + nowMonth + "-" + nowDate); // 스레드 함수 호출
                    }
                }, Integer.parseInt(nowYear), Integer.parseInt(nowMonth)-1, Integer.parseInt(nowDate));
                datePickerDialog.show();
            }
        });

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.viewPager);

        date = nowYear + "-" + nowMonth + "-" + nowDate; // 오늘 날짜가 url에 포함됨. 일단 하드 코딩
        loadData(date); // 스레드 함수 호출

        return view;
    }

    public void loadData(String date) {
        final Handler handler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {

                cafeteriaViewPagerAdapter = new CafeteriaViewPagerAdapter(requireActivity(), jsonObjectMain, date);
                viewPager2.setAdapter(cafeteriaViewPagerAdapter);

                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager2.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });

                viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        tabLayout.getTabAt(position).select();
                    }
                });
            }
        };
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://kmucoop.kookmin.ac.kr/menu/menujson.php?" +
                            "sdate=" + date + "&edate=" + date + "&today=" + date);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);

                    InputStream is = connection.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String result;
                    while ((result = br.readLine()) != null) {
                        sb.append(result += "\n");
                        String line = br.readLine();
                    }
                    result = sb.toString();
                    // 위의 마법의 코드로 url 에 있는 Json 객체 result에 얻어오고 초기화
                    jsonObjectMain = new JSONObject(result);

                    // 핸들러 부름
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}