package com.example.mark1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.net.Uri;

import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.messaging.FirebaseMessaging;



public class MainActivity<mDtabaseRef> extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDtabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionbar= getSupportActionBar();
        actionbar.hide();

        setContentView(R.layout.activity_main);



        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        mDtabaseRef = FirebaseDatabase.getInstance().getReference("mark1");


        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("FCM Log", "getInstanceId failed", task.getException());
                    return;
                }
                String token = task.getResult();

                Log.d("FCM Log", "FCM 토큰:" + token);
            }
        });

      //  VideoView v = findViewById(R.id.servicevideo);


        LottieAnimationView btn_cammera = findViewById(R.id.btn_camera);
        btn_cammera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this, Loding.class);
                startActivity(intent);
            }
        });


        LottieAnimationView btn_map = findViewById(R.id.btn_map);
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FindMap.class);
                startActivity(intent);
            }
        });

        LottieAnimationView btn_test = findViewById(R.id.btn_test);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, testver.class);
                startActivity(intent);
            }
        });


        /* mDtabaseRef.child("UserAccount").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
          @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {
            UserAccount account = snapshot.getValue(UserAccount.class);

           TextView txt_name = findViewById(R.id.txt_name);
         txt_name.setText(account.getUsername());

          TextView txt_email = findViewById(R.id.txt_email);
          txt_email.setText(account.getEmailId());


           }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
            });


        하단부 3가지 버튼
        Button homepage = findViewById(R.id.homepage);
        Button customer = findViewById(R.id.customer);
        Button service = findViewById(R.id.service);
        TextView method = findViewById(R.id.method); //ㅣㅣ이용안내 문구
        ImageView activity1 = findViewById(R.id.activity1);

        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customer.animate().translationY(1400).setDuration(1000).setStartDelay(0);
                service.animate().translationY(1400).setDuration(1000).setStartDelay(0);
                homepage.animate().translationY(1400).setDuration(1000).setStartDelay(0);
                activity1.animate().translationY(2100).setDuration(1000).setStartDelay(0);

                btn_test.animate().translationY(-750).setDuration(1000).setStartDelay(0);
                btn_cammera.animate().translationY(-750).setDuration(1000).setStartDelay(0);
                btn_map.animate().translationY(-750).setDuration(1000).setStartDelay(0);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_test.animate().translationY(-500).setDuration(1000).setStartDelay(0);
                        method.setText("\n\n사고자 위치\n\n" +
                                "앱에서 알람이 오는 순간이있을겁니다. \n" +
                                "그때에 위의 이미지를 터치하시면\n" +
                                "사고자의 위치가 표시된 지도로 이동합니다. \n");
                        method.setVisibility(View.VISIBLE);
                        v.setVisibility(View.VISIBLE);
                        try {
                            Uri videofile1 = Uri.parse("android.resource://com.example.mark1/" + R.raw.a1);
                            v.setVideoURI(videofile1);
                            v.start();

                        } catch (Exception e) {
                            e.printStackTrace();

                        }


                    }
                }, 1000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_test.animate().translationY(-750).setDuration(1000).setStartDelay(0);
                        btn_map.animate().translationY(-500).setDuration(1000).setStartDelay(0);
                        method.setText("\n\n헬멧위치\n\n" +
                                "현재의 헬멧위치가 지도상 나타납니다\n" +
                                "이를통해 헬멧을 찾아주세요");
                        try {
                            Uri videofile2 = Uri.parse("android.resource://com.example.mark1/" + R.raw.a2);
                            v.setVideoURI(videofile2);
                            v.start();

                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    }
                }, 8000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_map.animate().translationY(-750).setDuration(1000).setStartDelay(0);
                        btn_cammera.animate().translationY(-500).setDuration(1000).setStartDelay(0);
                        method.setText("\n\n기능을 이용할시간입니다.\n\n" +
                                "먼저 헬멧을 착용하신후 착용했다는것을 사진으로 인증해주세요\n" +
                                "그후 헬멧과 연동되는 올바른 블루투스를 찾아주시면됩니다.\n" +
                                "주행을 마치시면 운행종료를 누르시게 되면 헬멧과의 연동이 종료됩니다.");
                        try {
                            Uri videofile3 = Uri.parse("android.resource://com.example.mark1/" + R.raw.a3);
                            v.setVideoURI(videofile3);
                            v.start();

                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    }
                }, 16000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        customer.animate().translationY(0).setDuration(1000).setStartDelay(0);
                        service.animate().translationY(0).setDuration(1000).setStartDelay(0);
                        homepage.animate().translationY(0).setDuration(1000).setStartDelay(0);
                        activity1.animate().translationY(0).setDuration(1000).setStartDelay(0);

                        btn_test.animate().translationY(0).setDuration(1000).setStartDelay(0);
                        btn_cammera.animate().translationY(0).setDuration(1000).setStartDelay(0);
                        btn_map.animate().translationY(0).setDuration(1000).setStartDelay(0);
                        method.setVisibility(View.INVISIBLE);
                        v.setVisibility(View.INVISIBLE);

                    }
                }, 28000);


            }
        }); */


    }


}
