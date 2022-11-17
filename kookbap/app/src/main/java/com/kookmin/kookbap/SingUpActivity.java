package com.kookmin.kookbap;

import android.os.Bundle;
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
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SingUpActivity extends AppCompatActivity {
    // actionCodeSetting 잘모르겠음
    ActionCodeSettings actionCodeSettings =
            ActionCodeSettings.newBuilder()
                    // URL you want to redirect back to. The domain (www.example.com) for this
                    // URL must be whitelisted in the Firebase Console.
                    .setUrl("https://www.example.com/finishSignUp?cartId=1234")
                    // This must be true
                    .setHandleCodeInApp(true)
                    .setIOSBundleId("com.example.ios")
                    .setAndroidPackageName(
                            "com.example.login_demo",
                            true, /* installIfNotAvailable */
                            "12"    /* minimumVersion */)
                    .build();
    private FirebaseAuth mAuth;
    EditText mEmail,mPassword,mName;
    TextView mDatcom;
    Button mCheckEmail,mSingup_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singup_activity);

        mAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.Singup_email);
        mPassword = findViewById(R.id.Singup_password);
        mName = findViewById(R.id.Singup_name);
        mCheckEmail = findViewById(R.id.Singup_checkEmail);
        mSingup_btn = findViewById(R.id.Singup_singup_btn);
        mDatcom = findViewById(R.id.Singup_datcom);


        // TODO: 2022-11-17  나중에 데이터 베이스 연동 하고 중복검사 실행


        mDatcom.setText("@kookmin.ac.kr");
        // 회원 가입
        mSingup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String singup_email = mEmail.getText().toString() + mDatcom.getText().toString();;
                String singup_password = mPassword.getText().toString();
                mAuth.createUserWithEmailAndPassword(singup_email,singup_password).addOnCompleteListener(SingUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            //이메일 인증
                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(SingUpActivity.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // TODO: 2022-11-17 데이터 베이스에 추가하는것을 추가해야함
                                        Toast.makeText(SingUpActivity.this, "Can Signup Email", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(SingUpActivity.this, "Can't Signup Email Retry again", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(SingUpActivity.this,"이미 있습니다",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }


    // TODO: 2022-11-17 데이터 베이스 연동후 사용할 예정
    public boolean Email_Pass_isnotnull(String email , String password){
        if(email != null && password != null)
        {
            return true;
        }
        return false;
    }


}
