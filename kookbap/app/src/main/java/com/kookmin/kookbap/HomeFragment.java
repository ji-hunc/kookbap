package com.kookmin.kookbap;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.kookmin.kookbap.cafeteriaFragments.CafeteriaChungHyangKorean;
import com.kookmin.kookbap.cafeteriaFragments.CafeteriaChungHyangWestern;
import com.kookmin.kookbap.cafeteriaFragments.CafeteriaDormitory;
import com.kookmin.kookbap.cafeteriaFragments.CafeteriaHanul;
import com.kookmin.kookbap.cafeteriaFragments.CafeteriaKBob;
import com.kookmin.kookbap.cafeteriaFragments.CafeteriaProfessor;
import com.kookmin.kookbap.cafeteriaFragments.CafeteriaStudent;
import com.kookmin.kookbap.cafeteriaFragments.CafeteriaViewPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    TextView dateTextView;
    ImageView calendarButton;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    CafeteriaViewPagerAdapter cafeteriaViewPagerAdapter;

    String date, nowYear, nowMonth, nowDate;

    JSONObject jsonObject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 날짜 변수에 오늘 날짜 초기화 및 날짜텍스트뷰에 날짜 초기화
        dateTextView = view.findViewById(R.id.dateTextView);
        Calendar calendar = Calendar.getInstance();
        nowYear = Integer.toString(calendar.get(Calendar.YEAR));
        nowMonth = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        nowDate = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        nowMonth = nowMonth.length() < 2 ? "0" + Integer.parseInt(nowMonth) + 1 : nowMonth;
        nowDate = nowDate.length() < 2 ? "0" + Integer.parseInt(nowDate) : nowDate;
        dateTextView.setText(nowYear + "-" + nowMonth + "-" + nowDate);

        date = nowYear + "-" + nowMonth + "-" + nowDate; // 오늘 날짜가 url에 포함됨. 일단 하드 코딩


        calendarButton = view.findViewById(R.id.calendarButton);
        // 달력아이콘 클릭했을 때
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    // 날자를 선택했을 때
                    @Override
                    public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                        nowYear = Integer.toString(yy);
                        nowMonth = mm + 1 < 10 ? "0" + (mm + 1) : "" + (mm + 1);
                        nowDate = dd < 10 ? "0" + dd : "" + dd;
                        date = nowYear + "-" + nowMonth + "-" + nowDate;
                        dateTextView.setText(date);

                        // 날짜를 선택하고 확인을 누르면, 어댑터가 그 날짜에 해당하는 것들로 다시 뿌려줌
                        cafeteriaViewPagerAdapter = new CafeteriaViewPagerAdapter(requireActivity(), jsonObject, date);
                        viewPager2.setAdapter(cafeteriaViewPagerAdapter);


                        // 리뷰 작성페이지를 위해 한 번도 생성된 페이지가 업도록, 말도 안되는 초기화...
                        // TODO 로직 바꿔야만 함...
                        CafeteriaHanul cafeteriaHanul = new CafeteriaHanul(jsonObject, date);
                        CafeteriaStudent CafeteriaStudent = new CafeteriaStudent(jsonObject, date);
                        CafeteriaProfessor CafeteriaProfessor = new CafeteriaProfessor(jsonObject, date);
                        CafeteriaKBob CafeteriaKBob = new CafeteriaKBob(jsonObject, date);
                        CafeteriaChungHyangKorean CafeteriaChungHyangKorean = new CafeteriaChungHyangKorean(jsonObject, date);
                        CafeteriaChungHyangWestern CafeteriaChungHyangWestern = new CafeteriaChungHyangWestern(jsonObject, date);
                        CafeteriaDormitory CafeteriaDormitory = new CafeteriaDormitory(jsonObject, date);
                    }
                }, Integer.parseInt(nowYear), Integer.parseInt(nowMonth) - 1, Integer.parseInt(nowDate)); // 처음 DatePicker가 켜졌을 때 최초로 선택되어 있는 날짜
                datePickerDialog.show();
            }
        });


        // Retrofit 으로 서버와 통신히여 menu 데이터를 받아오는 부분
        Call<Object> call; // 원래 Retrofit 은 받아올 데이터 클래스를 정의해야 하지만, 완전 통으로 가져올 때는 따로 정의 없이 Object로 가져올 수 있음
        call = RetrofitClient.getApiService().getMenuData();
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) { // 서버로부터 OK 사인을 받았을 때
                    try {
                        jsonObject = new JSONObject(new Gson().toJson(response.body()));

                        // menu 띄워주는 adapter에 받아온 jsonObject을 넘김
                        cafeteriaViewPagerAdapter = new CafeteriaViewPagerAdapter(requireActivity(), jsonObject, date);
                        viewPager2.setAdapter(cafeteriaViewPagerAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });


        // 각각의 탭을 선택했을 때 이벤트. 사실상 선택했을 때만 일어나도 무방하다고 생각
        tabLayout = view.findViewById(R.id.tabLayout);
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

        viewPager2 = view.findViewById(R.id.viewPager);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(tabLayout.getTabAt(position)).select();
            }
        });

        return view;
    }
}