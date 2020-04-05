package com.developer.enjad.activities_fragments.activity_home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.developer.enjad.R;
import com.developer.enjad.activities_fragments.activity_edit_report.EditReportActivity;
import com.developer.enjad.activities_fragments.activity_login.LoginActivity;
import com.developer.enjad.activities_fragments.activity_new_communication.NewCommunicationActivity;
import com.developer.enjad.databinding.ActivityHomeBinding;
import com.developer.enjad.language.LanguageHelper;
import com.developer.enjad.models.NewReport2;
import com.developer.enjad.models.Reports_Model;
import com.developer.enjad.models.UserModel;
import com.developer.enjad.preferences.Preferences;
import com.developer.enjad.tags.Tags;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private String lang;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    Double lat1, lng1, lat2, lng2;
    private DatabaseReference dRef;
    private List<Reports_Model> reports_modelList;
    private Preferences preferences;
    private UserModel userModel;
    private FirebaseAuth mAuth;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        initView();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getDataFromIntent();
        getLastLocation();
        getLatLong();

    }

    private void initView() {
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        mAuth = FirebaseAuth.getInstance();
        dRef = FirebaseDatabase.getInstance().getReference(Tags.DATABASE_NAME).child(Tags.TABLE_REPORTS);

        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        binding.setUserModel(userModel);
        binding.flNewComm.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewCommunicationActivity.class);
            startActivity(intent);


        });

        binding.flEditReport.setOnClickListener(view -> {
            Intent intent = new Intent(this, EditReportActivity.class);
            startActivity(intent);


        });

        binding.cardLogout.setOnClickListener(view -> {
            mAuth.signOut();
            preferences.clear(this);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private void getDataFromIntent() {

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("latitude")) {
            String latitude = intent.getStringExtra("latitude");
            String longitude = intent.getStringExtra("longitude");
            lat2 = Double.parseDouble(latitude);
            lng2 = Double.parseDouble(longitude);
//            Log.e("lat",lat2+"");
//            Log.e("lat",lng2+"");
//            Log.e("lat1",lat1+"");
//            Log.e("lat1",lng1+"");

        }else {
            lat2 =26.254;
            lng2 = 46.515;
        }
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Granted. Start getting the location information
            }
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    lat1 = location.getLatitude();
                                    lng1 = location.getLongitude();

                                    distance(lat1, lng1, lat2,lng2 );
//                                    Log.e("nnnn",lat1+"  lat2 "+lat2+"   lng2"+lng2);
//                                    Log.e("eeeeee", location.getLatitude() + "" + location.getLongitude());
//                                    Toast.makeText(HomeActivity.this, location.getLatitude() + "" + location.getLongitude(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            Log.e("mmmmm", mLastLocation.getLatitude() + "" + mLastLocation.getLongitude());
            lat1=mLastLocation.getLatitude();
            lng1=mLastLocation.getLongitude();

            Toast.makeText(HomeActivity.this, mLastLocation.getLatitude() + "" + mLastLocation.getLongitude() + "", Toast.LENGTH_SHORT).show();

        }
    };

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        Log.e("distance",dist+"");

        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void getLatLong()
    {
        dRef.child(Tags.TABLE_REPORTS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      //  binding.progBar.setVisibility(View.GONE);
                        if (dataSnapshot.getValue()!=null)
                        {
                            for (DataSnapshot ds:dataSnapshot.getChildren())
                            {
                                Log.e("mmmmmmmmm", ds.getKey()+"");

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }



}
