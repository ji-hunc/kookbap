package com.kookmin.kookbap.LoginAndSignup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kookmin.kookbap.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                String signup_email = mEmail.getText().toString() + mDatcom.getText().toString();
                String signup_password = mPassword.getText().toString();
                if(check_validation(signup_password)) {
                    onSignup(signup_email,signup_password);
                }
                else{
                    Toast.makeText(SignUpActivity.this,"이메일 양식이 잘못되었습니다",Toast.LENGTH_LONG);
                }
            }
        });
    }

    boolean check_validation(String password) {
        // 비밀번호 유효성 검사식1 : 숫자, 특수문자가 포함되어야 한다.
        String val_symbol = "([0-9].*[!,@,#,^,&,*,(,)])|([!,@,#,^,&,*,(,)].*[0-9])"; // 정규표현식으로 구현 (배껴옴)
        // 비밀번호 유효성 검사식2 : 영문자 대소문자가 적어도 하나씩은 포함되어야 한다.
        // 정규표현식 컴파일
        Pattern pattern_symbol = Pattern.compile(val_symbol);
        Matcher matcher_symbol = pattern_symbol.matcher(password);
        if (matcher_symbol.find()) {
            return true;
        }else {
            return false;
        }
    }

    void onSignup(String signup_email,String signup_password){
        mAuth.createUserWithEmailAndPassword(signup_email,signup_password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
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
                                intent.putExtra("name", mName.getText().toString());

                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }


}
