package com.example.mark1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Calling extends AppCompatActivity {
    TextView textView;

    private PermissionSupport permission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionbar= getSupportActionBar();
        actionbar.hide();
        setContentView(R.layout.activity_calling);


        textView = findViewById(R.id.text_view);
        long duration = TimeUnit.MINUTES.toMillis(1);
        CountDownTimer CDT=new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long l) {
                String sDuration = String.format(Locale.ENGLISH,"%02d : %02d"
                        ,TimeUnit.MILLISECONDS.toMinutes(l)
                        ,TimeUnit.MILLISECONDS.toSeconds(l) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));
                textView.setText(sDuration);

            }

            @Override
            public void onFinish() {

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:010-2208-2117"));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }



        }.start();


        Button btn_calling = findViewById(R.id.btn_calling);
        btn_calling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CDT.cancel();
                finish();



            }
        });
    }
}