package com.developer.enjad.activities_fragments.activity_edit_report;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.developer.enjad.R;
import com.developer.enjad.adapter.ReportsAdapter;
import com.developer.enjad.databinding.ActivityEditReportBinding;
import com.developer.enjad.interfaces.Listeners;
import com.developer.enjad.language.LanguageHelper;
import com.developer.enjad.models.ReportModel;
import com.developer.enjad.tags.Tags;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class EditReportActivity extends AppCompatActivity implements Listeners.BackListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private ActivityEditReportBinding binding;
    private String lang;
    private List<ReportModel> data;
    private ReportsAdapter adapter;
    private DatabaseReference dRef;
    private double lat = 0.0, lng = 0.0;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private final String fineLocPerm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int loc_req = 1225;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_report);
        initView();

    }

    private void initView() {
        dRef = FirebaseDatabase.getInstance().getReference(Tags.DATABASE_NAME);
        binding.toolbar.setTitle("");
        data = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        adapter = new ReportsAdapter(this, data);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);
        CheckPermission();

    }

    private void getData() {
        dRef.child(Tags.TABLE_REPORTS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        binding.progBar.setVisibility(View.GONE);
                        if (dataSnapshot.getValue()!=null)
                        {
                            for (DataSnapshot ds:dataSnapshot.getChildren())
                            {
                                ReportModel reportModel = ds.getValue(ReportModel.class);
                                LatLng latLng = getLatLngFromLink(reportModel.getLink());
                                if (latLng!=null)
                                {
                                    double distance = getDistance(latLng);
                                    reportModel.setDistance(String.format(Locale.ENGLISH,"%.2f",distance)+getString(R.string.km));


                                }else
                                    {
                                        reportModel.setDistance(getString(R.string.undefined));
                                    }

                                data.add(reportModel);
                            }

                            if (data.size()>0)
                            {
                                binding.tvNoReport.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();

                            }else
                                {
                                    binding.tvNoReport.setVisibility(View.VISIBLE);

                                }
                        }else
                            {
                              binding.tvNoReport.setVisibility(View.VISIBLE);
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private double getDistance(LatLng latLng)
    {
        return SphericalUtil.computeDistanceBetween(new LatLng(lat,lng),latLng)/1000;
    }

    private LatLng getLatLngFromLink(String link)
    {
        try {
            String lat=  link.substring(link.indexOf("!3d"),link.indexOf("!4d"));
            String lng=  link.substring(link.indexOf("!4d"));

            String[] lngs = lng.split("d");
            String[] lats = lat.split("d");

            String latitude = lats[1];
            String longitude = lngs[1];

            return new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));

        }catch (Exception e)
        {
            Toast.makeText(this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void CheckPermission()
    {
        if (ActivityCompat.checkSelfPermission(this,fineLocPerm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{fineLocPerm}, loc_req);
        } else {

            initGoogleApi();
        }
    }
    private void initGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initLocationRequest();
    }

    private void initLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(60000);
        LocationSettingsRequest.Builder request = new LocationSettingsRequest.Builder();
        request.addLocationRequest(locationRequest);
        request.setAlwaysShow(false);


        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, request.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdate();
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(EditReportActivity.this,100);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;

                }
            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient!=null)
        {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @SuppressLint("MissingPermission")
    private void startLocationUpdate()
    {
        locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        getData();
        if (googleApiClient!=null)
        {
            LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
            googleApiClient.disconnect();
            googleApiClient = null;
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == loc_req)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                initGoogleApi();
            }else
            {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100&&resultCode== Activity.RESULT_OK)
        {

            startLocationUpdate();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (googleApiClient!=null)
        {
            if (locationCallback!=null)
            {
                LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
                googleApiClient.disconnect();
                googleApiClient = null;
            }
        }
    }

    @Override
    public void back() {
        finish();
    }

}
