package com.kookmin.kookbap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Button mLogin_btn;
    EditText mEmail,mPassword;
    TextView mSingup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mLogin_btn = findViewById(R.id.lonin_btn);
        mSingup = findViewById(R.id.lognin_singup);
        mEmail = findViewById(R.id.login_Email);
        mPassword = findViewById(R.id.login_Password);


        mAuth = FirebaseAuth.getInstance(); // 파이어베이스 연결


        mLogin_btn.setOnClickListener(new View.OnClickListener() { //로그인 버튼 누름
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() { //아이디 존재여부 확인
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Intent intent = new Intent(LoginActivity.this,MainViewActivity.class); // 확인완료 -> 메인뷰로 이동
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(LoginActivity.this,"로그인이 실패했습니다",Toast.LENGTH_LONG).show(); // 실패시 출력
                                 return;
                            }
                        }
                    });

            }
        });

        mSingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SingUpActivity.class); // 회원가입 텍스트 -> 회원가입뷰로 이동
                startActivity(intent);
            }
        });



    }
}