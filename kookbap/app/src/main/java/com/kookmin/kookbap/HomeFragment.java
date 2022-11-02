package com.kookmin.kookbap;

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

public class HomeFragment extends Fragment {

    JSONObject jsonObjectMain;

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    CafeteriaViewPagerAdapter cafeteriaViewPagerAdapter;

    String date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.viewPager);

        date = "2022-10-13"; // 오늘 날짜가 url에 포함됨. 일단 하드 코딩

        loadData(); // 스레드 함수 호출

        return view;
    }

    public void loadData() {
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