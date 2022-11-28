package com.kookmin.kookbap.LoginAndSignup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kookmin.kookbap.R;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    EditText mEmail,mPassword,mName;
    TextView mDatcom;
    Button mInput_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        mAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.Singup_email);
        mPassword = findViewById(R.id.Singup_password);
        mName = findViewById(R.id.Singup_name);
        mDatcom = findViewById(R.id.Singup_datcom);
        mInput_btn = findViewById(R.id.fragment_signupActivity_input_btn);

        SharedPreferences outo_login_prf = SignUpActivity.this.getSharedPreferences("outo_login_id",0);
        SharedPreferences.Editor editor = outo_login_prf.edit();


        mDatcom.setText("@kookmin.ac.kr"); // 레이아웃에서는 @가 안써져서 코드로 기입

        // TODO: 2022-11-17  나중에 데이터 베이스 연동 하고 중복검사 실행

        // 회원 가입
        mInput_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String singup_email = mEmail.getText().toString() + mDatcom.getText().toString();
                String singup_password = mPassword.getText().toString();
                // TODO: 2022-11-21 비밀번호 특수문자 유무 판단 추가 예정
                mAuth.createUserWithEmailAndPassword(singup_email,singup_password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {// 일단 회원가입
                        mUser = mAuth.getCurrentUser();
                        if(task.isSuccessful()) {// 회원으로 추가
                            mUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) { //회원추가후  이메일 전송
                                    if(task.isSuccessful()){
                                        Log.d("send","sendEmail");
                                        Intent intent = new Intent(SignUpActivity.this, CheckEmailActivity.class);
                                        startActivity(intent);
                                    }
                                    else{ // 잘못된 이메일
                                        mUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("can't send","reTry");
                                                }
                                            }
                                        });

                                    }
                                }
                            });
                        }
                        else{ // 이메일
                            // TODO: 2022-11-21 비밀번호가 문제인지 이메일이 문제인지 추가 예정

                        }
                    }
                });
            }
        });
    }
}
