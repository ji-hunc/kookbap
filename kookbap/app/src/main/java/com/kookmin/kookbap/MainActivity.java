package com.kookmin.kookbap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    ReviewFragment reviewFragment = new ReviewFragment();
    RestaurantFragment restaurantFragment = new RestaurantFragment();
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
                        startActivity(intent);
                        return true;
                    case R.id.restaurant:
                        changeFragment(restaurantFragment);
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