package com.kookmin.kookbap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.kookmin.kookbap.ReviewRank.ReviewFragment;
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
                        // 아무 정보 없이 일반 적으로 작성하는 Signal 1번. 2번 -> 메뉴 접속후 작성, 3번 -> 수정
                        intent.putExtra("signal", 1);
                        startActivity(intent);
                        return true;
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

}