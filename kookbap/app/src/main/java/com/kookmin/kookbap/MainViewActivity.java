package com.kookmin.kookbap;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainViewActivity extends AppCompatActivity {
    Button mLogout_btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainview_activity);
        // TODO: 2022-11-17 국밥에 연결 시킬 곳
        mLogout_btn = findViewById(R.id.mainview_logout_Btn);

        mLogout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prf = getSharedPreferences("outo_login_id", MODE_PRIVATE);
                SharedPreferences.Editor editor = prf.edit();
                editor.clear();
                editor.apply();
                finish();
            }
        });


    }
}
