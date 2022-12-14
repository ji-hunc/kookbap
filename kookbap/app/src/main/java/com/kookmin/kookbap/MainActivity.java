package com.kookmin.kookbap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.kookmin.kookbap.ReviewRank.ReviewFragment;
import com.kookmin.kookbap.Setting.SettingFragment;
import com.kookmin.kookbap.cafeteriaFragments.HomeFragment;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    ReviewFragment reviewFragment = new ReviewFragment();
    RecommendMenuFragment recommendMenuFragment = new RecommendMenuFragment();
    SettingFragment settingFragment = new SettingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        changeFragment(homeFragment);
                        return true;
                    case R.id.review:
                        changeFragment(reviewFragment);
                        return true;
                    case R.id.add_review:
                        Intent intent = new Intent(getApplicationContext(), WriteReview.class);
                        // signal: 1 아무런 정보 없이 바텀네비게이션에서 클릭하여 생으로 리뷰를 작성하는 경우
                        // signal: 2 메뉴 상세페이지에서 리뷰를 작성하는 경우
                        // signal: 3 리뷰 수정하기로 리뷰 작성페이지를 들어가는 경우
                        intent.putExtra("signal", 1);
                        startActivity(intent);
                        return false;
                    case R.id.restaurant:
                        changeFragment(recommendMenuFragment);
                        return true;
                    case R.id.setting:
                        changeFragment(settingFragment);
                        return true;
                }
                return false;
            }
        });

    }
    // 프래그먼트 변경 메서드. mainActivity.Container 에서 사용가능. 다른 container에서 사용하고 싶으면 오버로딩하면됨.
    public void changeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAffinity(this);
        System.exit(0);
    }
}