package com.example.mark1;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;


public class Bluetooth extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter; // 블루투스 어댑터
    private Set<BluetoothDevice> devices; // 블루투스 디바이스 데이터 셋
    private BluetoothDevice bluetoothDevice; // 블루투스 디바이스
    private BluetoothSocket bluetoothSocket = null; // 블루투스 소켓
    private OutputStream outputStream = null; // 블루투스에 데이터를 출력하기 위한 출력 스트림
    private InputStream inputStream = null; // 블루투스에 데이터를 입력하기 위한 입력 스트림
    private Thread workerThread = null; // 문자열 수신에 사용되는 쓰레드
    private byte[] readBuffer; // 수신 된 문자열을 저장하기 위한 버퍼
    private int readBufferPosition; // 버퍼 내 문자 저장 위치

    private TextView textViewReceive, state, state2; // 수신 된 데이터를 표시하기 위한 텍스트 뷰
    private EditText editTextSend; // 송신 할 데이터를 작성하기 위한 에딧 텍스트
    private Button buttonSend; // 송신하기 위한 버튼
    private ActivityResultLauncher<Intent> resultLauncher;
    private Activity activity;
    private final int MULTIPLE_PERMISSIONS = 1023;
    private PermissionSupport permission;
    private TextView repode;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDtabaseRef;
    private LottieAnimationView lottieloading, lottiepictureride, lottiesearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionbar= getSupportActionBar();
        actionbar.hide();
        setContentView(R.layout.activity_bluetooth);




        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        mDtabaseRef = FirebaseDatabase.getInstance().getReference("mark1");


        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == RESULT_OK) {

                            Intent intent = result.getData();
                            selectBluetoothDevice();

                        } else {
                            Toast.makeText(Bluetooth.this, "error 2222222", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        state = findViewById(R.id.state);
        state2 = findViewById(R.id.state2);
        textViewReceive = (TextView) findViewById(R.id.textView_receive);
        buttonSend = (Button) findViewById(R.id.button_send);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        lottieloading = findViewById(R.id.lottieloading);
        lottiepictureride = findViewById(R.id.lottiepictureride);
        lottiesearch = findViewById(R.id.lottiesearch);

        if (bluetoothAdapter == null) { // 디바이스가 블루투스를 지원하지 않을 때 여기에 처리 할 코드를 작성하세요.
            Toast.makeText(Bluetooth.this, "error 1111111", Toast.LENGTH_SHORT).show();

        } else {
            if (bluetoothAdapter.isEnabled()) {// 블루투스가 활성화 상태 (기기에 블루투스가 켜져있음)
                selectBluetoothDevice();// 블루투스 디바이스 선택 함수 호출
            } else {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                resultLauncher.launch(intent);
            }
        }

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    try {
                        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, new LocationListener() {
                            @Override
                            public void onLocationChanged(@NonNull Location location) {
                                mDtabaseRef.child("latiti").setValue(location.getLatitude());
                                mDtabaseRef.child("longiti").setValue(location.getLongitude());

                            }
                        });
                    }catch (SecurityException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(Bluetooth.this,"운행종료",Toast.LENGTH_SHORT).show();
                    bluetoothSocket.close();


                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });




    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 리턴이 false일 경우 다시 권한 요청
        if (!permission.permissionResult(requestCode, permissions, grantResults)) {
            permission.requestPermission();
        }
    }


    public boolean selectBluetoothDevice() {

        if (Build.VERSION.SDK_INT >= 23) {

            // 클래스 객체 생성
            permission = new PermissionSupport(this, this);

            // 권한 체크한 후에 리턴이 false일 경우 권한 요청을 해준다.
            if (!permission.checkPermission()) {
                permission.requestPermission();
            }
        }
        devices = bluetoothAdapter.getBondedDevices();

        if (devices.size() <= 0) {
            Toast.makeText(Bluetooth.this, "error 3333333333", Toast.LENGTH_SHORT).show();

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("페어링 되어있는 블루투스 디바이스 목록");
            List<String> list = new ArrayList<>();
            for (BluetoothDevice bluetoothDevice : devices) {
                list.add(bluetoothDevice.getName());
            }
            list.add("취소");
            final CharSequence[] charSequences = list.toArray(new CharSequence[list.size()]);
            builder.setItems(charSequences, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    connectDevice(charSequences[i].toString());
                }
            });
            builder.setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return false;
    }


    private void connectDevice(String deviceName) {

        for (BluetoothDevice tempDevice : devices) {


            if (Build.VERSION.SDK_INT >= 23) {

                // 클래스 객체 생성
                permission = new PermissionSupport(this, this);

                // 권한 체크한 후에 리턴이 false일 경우 권한 요청을 해준다.
                if (!permission.checkPermission()) {
                    permission.requestPermission();
                }
            }
            if (deviceName.equals(tempDevice.getName())) {
                bluetoothDevice = tempDevice;
                break;
            }
        }
        UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        try {
            bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();
            //SendData();추가
            receiveData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void receiveData() {
        buttonSend.setVisibility(View.VISIBLE);
        state2.setVisibility(View.INVISIBLE);
        lottiesearch.setVisibility(View.INVISIBLE);

        state.setVisibility(View.VISIBLE);
        lottieloading.setVisibility(View.VISIBLE);
        lottieloading.playAnimation();
        lottiepictureride.setVisibility(View.VISIBLE);
        lottiepictureride.playAnimation();
        final Handler handler = new Handler();
        readBufferPosition = 0;
        readBuffer = new byte[1024];

        workerThread = new Thread(new Runnable() {

            @Override
            public void run() {

                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        int byteAvailable = inputStream.available();
                        if (byteAvailable > 0) {
                            byte[] bytes = new byte[byteAvailable];
                            inputStream.read(bytes);
                            for (int i = 0; i < byteAvailable; i++) {
                                byte tempByte = bytes[i];
                                if (tempByte == '\n') {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String text = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;
                                    handler.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            textViewReceive.append(text + "\n");
                                            double LKJ = Double.parseDouble(text);

                                            //if문 추가

                                            if (LKJ == 123) {
                                                Toast.makeText(Bluetooth.this, "사고가 발생했습니다", Toast.LENGTH_SHORT).show();
                                                LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                              try {
                                                  manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, new LocationListener() {
                                                      @Override
                                                      public void onLocationChanged(@NonNull Location location) {
                                                          mDtabaseRef.child("Latitude").setValue(location.getLatitude());
                                                          mDtabaseRef.child("Longitude").setValue(location.getLongitude());

                                                      }
                                                  });
                                              }catch (SecurityException e) {
                                                  e.printStackTrace();
                                              }
                                                Intent intent = new Intent(Bluetooth.this,Calling.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                } else {
                                    readBuffer[readBufferPosition++] = tempByte;
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        workerThread.start();
    }
}