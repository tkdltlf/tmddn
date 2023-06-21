package com.example.mark1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Activity_register extends AppCompatActivity {


    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증처리
    private DatabaseReference mDtabaseRef; // 실시간 데이터베이스
    private EditText mEtEmail,mEtPwd,mEtusername;       //회원가입 입력필드
    private Button mEtRegister;     //회원가입 입력버튼
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 초기화
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDtabaseRef = FirebaseDatabase.getInstance().getReference("mark1");

        mEtEmail = findViewById(R.id.et_email);
        mEtPwd = findViewById(R.id.et_pwd);
        mEtusername = findViewById(R.id.et_username);
        mEtRegister = findViewById(R.id.btn_register);
        mEtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 처리
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();
                String strusername = mEtusername.getText().toString().trim();

                // firebaseauth 진행
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail,strPwd).addOnCompleteListener(Activity_register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail());
                            account.setPassword(strPwd);
                            account.setUsername(strusername);


                            mDtabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);


                            Toast.makeText(Activity_register.this,"회원가입에 성공하셨습니다",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Activity_register.this,"회원가입에 실패했습니다",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}