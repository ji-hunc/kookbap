package com.kookmin.kookbap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
    RecyclerView recyclerView;

    HomeFragment homeFragment = new HomeFragment();
    ReviewFragment reviewFragment = new ReviewFragment();
    RestaurantFragment restaurantFragment = new RestaurantFragment();
    SettingFragment settingFragment = new SettingFragment();

    ArrayList<ReviewData> reviewData;

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
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;
                    case R.id.review:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, reviewFragment).commit();
                        return true;
                    case R.id.add_review:
                        Intent intent = new Intent(getApplicationContext(), WriteReview.class);
                        startActivity(intent);
                        return true;
                    case R.id.restaurant:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, restaurantFragment).commit();
                        return true;
                    case R.id.setting:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, settingFragment).commit();
                        return true;
                }
                return false;
            }
        });

        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://kmucoop.kookmin.ac.kr/menu/menujson.php?sdate=2022-10-31&edate=2022-10-31&today=2022-10-31");
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
                    Log.e("qwe", result);

                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject sick = jsonObject.getJSONObject("한울식당(법학관 지하1층)");
                    sick = sick.getJSONObject("2022-10-31");
                    sick = sick.getJSONObject("2코너<BR>NOODLE");
                    String menu = sick.getString("메뉴");
                    Log.e("zxc", jsonObject.toString());
                    Log.e("zxc", sick.toString());
                    Log.e("rty", menu);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}