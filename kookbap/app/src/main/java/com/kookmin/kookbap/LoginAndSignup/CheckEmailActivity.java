package com.kookmin.kookbap.LoginAndSignup;

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

        SharedPreferences outo_login_prf = CheckEmailActivity.this.getSharedPreferences("outo_login_id", 0);
        SharedPreferences.Editor editor = outo_login_prf.edit();

        mCheck_Email_btn.setOnClickListener(new View.OnClickListener() { // 인증메일을 확인여부
            @Override
            public void onClick(View view) {
                mUser.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        isVerified = mUser.isEmailVerified();
                        if(isVerified){
                            Log.d("success","드디어!");
                            editor.putString("ID",mUser.getEmail());
                            editor.apply();
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