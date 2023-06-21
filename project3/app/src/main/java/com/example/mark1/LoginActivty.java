package com.example.mark1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivty extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증처리
    private DatabaseReference mDtabaseRef; // 실시간 데이터베이스
    private EditText mEtEmail,mEtPwd;       //로그인 입력필드
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionbar= getSupportActionBar();
        actionbar.hide();
        setContentView(R.layout.activity_login_activty);



        mFirebaseAuth = FirebaseAuth.getInstance();
        mDtabaseRef = FirebaseDatabase.getInstance().getReference("mark1");

        mEtEmail = findViewById(R.id.et_email);
        mEtPwd = findViewById(R.id.et_pwd);
        LottieAnimationView Lottie= findViewById(R.id.lottieAnimationView);
        final Animation zoomin = AnimationUtils.loadAnimation(this,R.anim.zoom_in);


        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //로그인 요청
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail,strPwd).addOnCompleteListener(LoginActivty.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Lottie.startAnimation(zoomin);

                        new Handler().postDelayed(new Runnable() {                          //만약  AI를 넣게되면 AI판단을 할 공간
                            @Override
                            public void run() {

                                Intent intent = new Intent(LoginActivty.this, MainActivity.class);
                                startActivity(intent);
                            }
                        },1900);


                        //로그인 성공

                    }else{
                        Toast.makeText(LoginActivty.this,"로그인실패",Toast.LENGTH_SHORT).show();
                    }
                    }
                });
            }
        });

        Button btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 화면 이동
                Intent intent = new Intent(LoginActivty.this, Activity_register.class);
                startActivity(intent);
            }
        });





    }
}