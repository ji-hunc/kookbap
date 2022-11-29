package com.kookmin.kookbap.LoginAndSignup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kookmin.kookbap.MainActivity;
import com.kookmin.kookbap.R;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Boolean mCheck;
    Button mLogin_btn,mLogin_toGoogle_btn;
    EditText mEmail,mPassword;
    TextView mSingup;
    CheckedTextView mOutologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mLogin_btn = findViewById(R.id.login_btn);
        mSingup = findViewById(R.id.lognin_singup);
        mEmail = findViewById(R.id.login_Email);
        mPassword = findViewById(R.id.login_Password);
        mOutologin = findViewById(R.id.login_outo_Login);
        mAuth = FirebaseAuth.getInstance(); // 파이어베이스 연결



        SharedPreferences prf = getSharedPreferences("outo_login_id",0);

        if(prf.getBoolean("outoLogin",false)) { // 자동 로그인이 체크 되어있다면 바로 이동
            if (prf.getString("ID","") != null) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }

        mOutologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = prf.edit();
                if(mOutologin.isChecked()){
                    mCheck = false;
                    ((CheckedTextView) view).setChecked(false);
                }
                else {
                    mCheck = true;
                    ((CheckedTextView) view).setChecked(true);
                }
            }
        });


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
                                SharedPreferences prf = LoginActivity.this.getSharedPreferences("outo_login_id",0);
                                SharedPreferences.Editor editor = prf.edit();
                                editor.putBoolean("outoLogin",mCheck);
                                editor.apply();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class); // 확인완료 -> 메인뷰로 이동
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(LoginActivity.this,password,Toast.LENGTH_LONG).show(); // 실패시 출력
                            }
                        }
                    });

            }
        });

        mSingup.setOnClickListener(new View.OnClickListener() {// 회원가입 버튼
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class); // 회원가입 텍스트 -> 회원가입뷰로 이동
                startActivity(intent);
            }
        });
    }

    /*public void onBackPressed() {
        mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("can't send","reTry");
                }
            }
        });
        super.onBackPressed();
    }*/
}