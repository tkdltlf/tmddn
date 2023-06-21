package com.example.mark1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;



import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class CameraExample extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;
    private ActivityResultLauncher<Intent> resultLauncher;
    private PermissionSupport permission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionbar= getSupportActionBar();
        actionbar.hide();
        setContentView(R.layout.activity_camera_example);
        Random random = new Random();
        int randomValue = random.nextInt(10);

        if (Build.VERSION.SDK_INT >= 23) {

            // 클래스 객체 생성
            permission = new PermissionSupport(this, this);

            // 권한 체크한 후에 리턴이 false일 경우 권한 요청을 해준다.
            if (!permission.checkPermission()) {
                permission.requestPermission();
            }
        }


        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Bundle extras = result.getData().getExtras();
                            Bitmap bitmap = (Bitmap)extras.get("data");
                            ImageView imageView = findViewById(R.id.iv_result);
                            imageView.setImageBitmap(bitmap);


                            new Handler().postDelayed(new Runnable() {                          //만약  AI를 넣게되면 AI판단을 할 공간
                                @Override
                                public void run() {


                                    if (randomValue<=8){
                                        Toast.makeText(CameraExample.this, "인증 성공", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(CameraExample.this,Bluetooth.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else if (randomValue>=9){
                                        Toast.makeText(CameraExample.this,"인증 실패",Toast.LENGTH_SHORT).show();
                                        Intent intent2 = new Intent(CameraExample.this,MainActivity.class);
                                        startActivity(intent2);
                                        finish();
                                    }

                                }
                            },3000);

                        }

                    }


                });


                findViewById(R.id.btn_capture).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        resultLauncher.launch(intent);
                    }
                });




    }


}