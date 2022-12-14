package com.kookmin.kookbap.cafeteriaFragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.tabs.TabLayout;
import com.kookmin.kookbap.LoginAndSignup.UserData;
import com.kookmin.kookbap.MenuData;
import com.kookmin.kookbap.R;
import com.kookmin.kookbap.Retrofits.RetrofitClient;

import java.util.ArrayList;
import java.util.Calendar;
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

    ArrayList<MenuData> todayMenus;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = LayoutInflater.from(getActivity()).inflate(
                R.layout.loading_dialog,
                null);
        builder.setView(dialogView);
        LottieAnimationView lottieAnimationView = (LottieAnimationView)dialogView.findViewById(R.id.loadingAnimationView);
        lottieAnimationView.setAnimation("loading.json");
        AlertDialog alertDialog =  builder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        lottieAnimationView.playAnimation();
        ((TextView)dialogView.findViewById(R.id.loadingDialogText)).setText("불러오는 중...");
        alertDialog.show();

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        userID = UserData.getUserData(getContext()).getUserId(); // 유저 id 가져오기
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
                        // Retrofit 으로 서버와 통신히여 날짜별 menu 데이터를 받아오는 부분
                        Call<ArrayList<MenuData>> call;
                        call = RetrofitClient.getApiService().getMenuDataEachDate(date,userID); // 날짜 상수 말고 date가 들어가야함. 일단 오늘 메뉴가 없어서 상수로..
                        call.enqueue(new Callback<ArrayList<MenuData>>() {
                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) {
                                if (response.code() == 200) { // 서버로부터 OK 사인을 받았을 때
                                    todayMenus = (ArrayList<MenuData>) response.body();

                                    // menu 띄워주는 adapter에 받아온 날짜별 ArrayList를 넘김
                                    cafeteriaViewPagerAdapter = new CafeteriaViewPagerAdapter(requireActivity(), todayMenus, date);
                                    viewPager2.setAdapter(cafeteriaViewPagerAdapter);
                                } else {
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                                Log.e("Error", t.getMessage());
                            }
                        });

                        cafeteriaViewPagerAdapter = new CafeteriaViewPagerAdapter(requireActivity(), todayMenus, date);
                        viewPager2.setAdapter(cafeteriaViewPagerAdapter);
                    }
                }, Integer.parseInt(nowYear), Integer.parseInt(nowMonth) - 1, Integer.parseInt(nowDate)); // 처음 DatePicker가 켜졌을 때 최초로 선택되어 있는 날짜
                datePickerDialog.show();
            }
        });


//        // Retrofit 으로 서버와 통신히여 menu 데이터를 받아오는 부분
//        Call<Object> call; // 원래 Retrofit 은 받아올 데이터 클래스를 정의해야 하지만, 완전 통으로 가져올 때는 따로 정의 없이 Object로 가져올 수 있음
//        call = RetrofitClient.getApiService().getMenuData();
//        call.enqueue(new Callback<Object>() {
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) {
//                if (response.code() == 200) { // 서버로부터 OK 사인을 받았을 때
//                    try {
//                        jsonObject = new JSONObject(new Gson().toJson(response.body()));
//
//                        // menu 띄워주는 adapter에 받아온 jsonObject을 넘김
//                        cafeteriaViewPagerAdapter = new CafeteriaViewPagerAdapter(requireActivity(), jsonObject, date);
//                        viewPager2.setAdapter(cafeteriaViewPagerAdapter);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
//                Log.e("Error", t.getMessage());
//            }
//        });

        // Retrofit 으로 서버와 통신히여 날짜별 menu 데이터를 받아오는 부분
        Call<ArrayList<MenuData>> call;
        call = RetrofitClient.getApiService().getMenuDataEachDate(date,userID); // 날짜 상수 말고 date가 들어가야함. 일단 오늘 메뉴가 없어서 상수로..
        call.enqueue(new Callback<ArrayList<MenuData>>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) { // 서버로부터 OK 사인을 받았을 때
                    todayMenus = (ArrayList<MenuData>) response.body();

                    // menu 띄워주는 adapter에 받아온 날짜별 ArrayList를 넘김
                    cafeteriaViewPagerAdapter = new CafeteriaViewPagerAdapter(requireActivity(), todayMenus, date);
                    viewPager2.setAdapter(cafeteriaViewPagerAdapter);
                } else {
                }
                alertDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.e("Error", t.getMessage());
                alertDialog.dismiss();
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