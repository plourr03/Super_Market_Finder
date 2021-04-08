package com.example.supermarket;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SuperMarketMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    final int PERMISSION_REQUEST_LOCATION = 101;
    GoogleMap gmap;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    ArrayList<Resturant> superMarkets = new ArrayList<>();
    Resturant currentSuperMarkets = null;
    SensorManager sensorManager;
    Sensor accelerometer,mangetometer;
    TextView textDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restuaurant_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Bundle extras = getIntent().getExtras();
        try {
            ResturantDataSource ds = new ResturantDataSource(SuperMarketMapActivity.this);
            ds.open();
            if (extras != null) {
                currentSuperMarkets = ds.getSpecificRestaurant(extras.getInt("restaurantID"));

            }
            else{
                superMarkets = ds.getRestaurants();
            }
            ds.close();
        }
        catch(Exception e){
            Toast.makeText(this,"Super Markets could not be retrived.",Toast.LENGTH_LONG).show();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sensorManager =(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mangetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if(accelerometer!= null && mangetometer!=null){
            sensorManager.registerListener(mySensorEventLsitener,accelerometer,SensorManager.SENSOR_DELAY_FASTEST);
            sensorManager.registerListener(mySensorEventLsitener,mangetometer,SensorManager.SENSOR_DELAY_FASTEST);
        }
        else {
            Toast.makeText(this, "Sensor not Found", Toast.LENGTH_LONG).show();
        }
        textDirection = findViewById(R.id.textHeading);
        createLocationRequest();
        createLocationCallback();
        initMapTypeButtons();
        initAddButton();
        initHomeButton();
        initListButton();
        initMapButton();
        initRateButton();

    }
    private void initAddButton(){
        ImageButton ibAdd =findViewById(R.id.imageButtonAdd);
        ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuperMarketMapActivity.this, RestaurantAddClass.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initRateButton(){
        ImageButton ibAdd =findViewById(R.id.imageButtonRate);
        ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuperMarketMapActivity.this, RestaurantListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("clicked rating",-999);
                startActivity(intent);
            }
        });
    }
    private void initMapButton(){
        ImageButton ibList =findViewById(R.id.imageButtonMap);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuperMarketMapActivity.this, SuperMarketMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initListButton(){
        ImageButton ibList =findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuperMarketMapActivity.this, RestaurantListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initHomeButton(){
        ImageButton ibHome =findViewById(R.id.imageButtonHome);
        ibHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuperMarketMapActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Toast.makeText(getBaseContext(), "Lat: " + location.getLatitude() + " Long: " + location.getLongitude() + " Accuracy: " + location.getAccuracy(), Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    @Override
    public void onPause(){
        super.onPause();
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
            return ;
        }
        try {
            stopLocationUpdates();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void initMapTypeButtons(){
        RadioGroup rgMapType = findViewById(R.id.radioGroupMap);
        rgMapType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbnormal = findViewById(R.id.normalbtn);
                if (rbnormal.isChecked()) {
                    gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } else {
                    gmap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
            }
        });

    }

    private void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        gmap.setMyLocationEnabled(true);
    }
    private void stopLocationUpdates() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return ;
        }
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
    @Override
    public void onMapReady(GoogleMap googleMap){
        gmap=googleMap;
        gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Point size = new Point();
        WindowManager w = getWindowManager();
        w.getDefaultDisplay().getSize(size);
        int mesuredWidth = size.x;
        int mesuredHight = size.y;
        if(superMarkets.size()>0){
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (int i =0;i<superMarkets.size();i++){
                currentSuperMarkets=superMarkets.get(i);
                Geocoder geo = new Geocoder(this);
                List<Address> addresses = null;
                float liqu = currentSuperMarkets.getLiquerRating();
                float pro = currentSuperMarkets.getProduceRating();
                float cheese = currentSuperMarkets.getCheeseRating();
                float overall = (liqu+pro+cheese)/3;
                DecimalFormat twoDForm = new DecimalFormat("#.##");
                overall = Float.valueOf(twoDForm.format(overall));
                String rating = "☆"+Float.toString(overall);
                String address = currentSuperMarkets.getStreetAddress()+", " +currentSuperMarkets.getCity()+", "+currentSuperMarkets.getState()+" "+currentSuperMarkets.getZipCode();
                try{
                    addresses = geo.getFromLocationName(address,1);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                LatLng point = new LatLng(addresses.get(0).getLatitude(),addresses.get(0).getLongitude());
                builder.include(point);

                gmap.addMarker(new MarkerOptions().position(point).title(currentSuperMarkets.getResturantName()).snippet(rating));
            }
            gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),mesuredWidth,mesuredHight,450));
        }
        else{
            if(currentSuperMarkets!=null){
                Geocoder geo = new Geocoder(this);
                List<Address> addresses = null;
                float liqu = currentSuperMarkets.getLiquerRating();
                float pro = currentSuperMarkets.getProduceRating();
                float cheese = currentSuperMarkets.getCheeseRating();
                float overall = (liqu+pro+cheese)/3;
                DecimalFormat twoDForm = new DecimalFormat("#.##");
                overall = Float.valueOf(twoDForm.format(overall));
                String rating = "☆"+Float.toString(overall);
                String address = currentSuperMarkets.getCheeseRating()+", " +currentSuperMarkets.getCity()+", "+currentSuperMarkets.getState()+", "+currentSuperMarkets.getZipCode();
                try{
                    addresses = geo.getFromLocationName(address,1);
                }
                catch (IOException e){
                    e.printStackTrace();;
                }
                LatLng point = new LatLng(addresses.get(0).getLatitude(),addresses.get(0).getLongitude());
                gmap.addMarker(new MarkerOptions().position(point).title(currentSuperMarkets.getResturantName()).snippet(rating));

                gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(point,16));
            }
            else {
                AlertDialog alertDialog = new AlertDialog.Builder(SuperMarketMapActivity.this).create();
                alertDialog.setTitle("no data");
                alertDialog.setMessage("no data avalible for the mapping function");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"ok", new  DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which){
                        finish();
                    }
                });
                alertDialog.show();
            }
        }
        //try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(SuperMarketMapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(SuperMarketMapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Snackbar.make(findViewById(R.id.activity_supermarket_map), "MySuperMarketList requires this permission to locate your Supermarkets", Snackbar.LENGTH_INDEFINITE).setAction("Okay", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(SuperMarketMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                            }
                        }).show();
                    } else {
                        ActivityCompat.requestPermissions(SuperMarketMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                    }
                } else {
                    startLocationUpdates();
                }
            } else {
                startLocationUpdates();
            }
       // }
       // catch (Exception e){
        //    Toast.makeText(getBaseContext(),"Error requesting permission",Toast.LENGTH_LONG).show();
        //}
        RadioButton rbNormal = findViewById(R.id.normalbtn);
        rbNormal.setChecked(true);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[],int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(SuperMarketMapActivity.this, "MySuperMarketList will not locate your SuperMarkets.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private SensorEventListener mySensorEventLsitener = new SensorEventListener() {
        float[] accelerometerValues;
        float[] magneticValues;
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType()== Sensor.TYPE_ACCELEROMETER)
                accelerometerValues = event.values;
            if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD)
                magneticValues = event.values;
            if(accelerometerValues!=null&&magneticValues!=null){
                float R[] = new float[9];
                float I[] = new  float[9];
                boolean success = SensorManager.getRotationMatrix(R,I,accelerometerValues,magneticValues);

                if(success){
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R,orientation);

                    float azimut =(float) Math.toDegrees(orientation[0]);
                    if(azimut<0.0f){azimut+=360.0f;}
                    String direction;
                    if(azimut>=315 || azimut<45){direction="N";}
                    else if (azimut>=225 && azimut<315){direction="W";}
                    else if (azimut>=135 && azimut<225){direction="S";}
                    else{direction="E";}
                    textDirection.setText(direction);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) { }
    };

}
