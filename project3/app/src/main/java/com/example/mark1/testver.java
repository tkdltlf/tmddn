package com.example.mark1;


import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class testver extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap googleMap;





    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
    DatabaseReference mDtabaseRef = FirebaseDatabase.getInstance().getReference("mark1");





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionbar= getSupportActionBar();
        actionbar.hide();
        setContentView(R.layout.activity_testver);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.smap);
        mapFragment.getMapAsync(this);





        }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        mDtabaseRef.child("Latitude").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String Latitude = String.valueOf(task.getResult().getValue());

                mDtabaseRef.child("Longitude").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        String Longitude = String.valueOf(task.getResult().getValue());
                        double La = Double.parseDouble(Latitude);
                        double Lg = Double.parseDouble(Longitude);


                        LatLng latLng= new LatLng(La,Lg);
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.title("사고위치");
                        markerOptions.snippet("제발좀 되라");
                        markerOptions.position(latLng);
                        googleMap.addMarker(markerOptions);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        googleMap.moveCamera(CameraUpdateFactory.zoomTo(16));
                    }
                });

            }
        });

    }
}





