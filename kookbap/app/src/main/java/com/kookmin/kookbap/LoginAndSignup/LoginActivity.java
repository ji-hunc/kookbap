package com.kookmin.kookbap.LoginAndSignup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kookmin.kookbap.MainActivity;
import com.kookmin.kookbap.R;
import com.kookmin.kookbap.Retrofits.Result;
import com.kookmin.kookbap.Retrofits.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Boolean mCheck = false;
    Button mLogin_btn;
    TextInputEditText mEmail,mPassword;
    TextView mSingup;
    CheckedTextView mOutologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mLogin_btn = findViewById(R.id.login_btn);
        mSingup = findViewById(R.id.lognin_singup);
        mEmail = findViewById(R.id.login_Email_edit);
        mPassword = findViewById(R.id.login_Password_edit);
        mOutologin = findViewById(R.id.login_outo_Login);
        mAuth = FirebaseAuth.getInstance(); // 파이어베이스 연결


        SharedPreferences prf = getSharedPreferences("userData", 0);

        if (prf.getBoolean("outoLogin", false)) { // 자동 로그인이 체크 되어있다면 바로 이동
            if (prf.getString("ID", "") != null) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
        mOutologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = prf.edit();
                if (mOutologin.isChecked()) {
                    mCheck = false;
                    ((CheckedTextView) view).setChecked(false);
                } else {
                    mCheck = true;
                    ((CheckedTextView) view).setChecked(true);
                }
            }
        });

        mLogin_btn.setOnClickListener(new View.OnClickListener() { //로그인 버튼 누름
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString() + "@kookmin.ac.kr";
                String password = mPassword.getText().toString();
                if (email.equals("") || password.equals("")) {
                    if (email.equals("") && password.equals("")) {
                        Toast.makeText(LoginActivity.this, "모든 항목을 채워주십시오", Toast.LENGTH_SHORT).show();
                    } else if (email.equals("")) {
                        Toast.makeText(LoginActivity.this, "이메일을 입력하십시오", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "비밀번호를 입력하십시오", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() { //아이디 존재여부 확인
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SharedPreferences prf = LoginActivity.this.getSharedPreferences("userData", 0);
                                SharedPreferences.Editor editor = prf.edit();
                                editor.putBoolean("outoLogin", mCheck);
                                editor.putString("Email", email); //로그인 시 email Preference에 저장
                                editor.putString("ID", email.split("@")[0]); //로그인 시 id preference에 저장
                                editor.commit();

                                // TODO: 2022-11-29 이게 맞나 모르겠지만 일단 해놨습니다 데이터베이스 연결예정
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class); // 확인완료 -> 메인뷰로 이동
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_LONG).show(); // 실패시 출력
                            }
                        }
                    });
                }
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
}