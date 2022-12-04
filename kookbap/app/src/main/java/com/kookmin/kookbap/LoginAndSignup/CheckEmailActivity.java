package com.kookmin.kookbap.LoginAndSignup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kookmin.kookbap.R;
import com.kookmin.kookbap.Retrofits.Result;
import com.kookmin.kookbap.Retrofits.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckEmailActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    Button mCheck_Email_btn;
    boolean isVerified = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_email_checking);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mCheck_Email_btn = findViewById(R.id.fragment_checkEmail_check_btn);
        Intent intent = getIntent();

        mCheck_Email_btn.setOnClickListener(new View.OnClickListener() { // 인증메일을 확인여부
            @Override
            public void onClick(View view) {
                mUser.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        isVerified = mUser.isEmailVerified();
                        if(isVerified){
                            Log.d("success","드디어!");
                            Call<Result> call = RetrofitClient.getApiService().postUserInfo(mUser.getEmail(),intent.getStringExtra("name"));
                            call.enqueue(new Callback<Result>() {
                                @Override
                                public void onResponse(@NotNull Call<Result> call, @NotNull Response<Result> response) {

                                    // 서버에서 응답을 받아옴
                                    if (response.isSuccessful() && response.body()   != null) {

                                        // 응답을 받아오지 못했을경우
                                    } else {
                                        assert response.body() != null;
                                    }
                                }

                                // 통신실패시
                                @Override
                                public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {

                                }
                            });
                            finish();
                        }
                        else{
                            Log.d("false","응 아니야");
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        mUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("can't send","reTry");
                }
            }
        });
        super.onBackPressed();
    }
}