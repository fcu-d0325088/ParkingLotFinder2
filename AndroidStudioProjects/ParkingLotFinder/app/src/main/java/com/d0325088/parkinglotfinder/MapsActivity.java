package com.d0325088.parkinglotfinder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, LocationListener {

    private GoogleMap mMap;
    public ImageButton showYourPosition, menuButton;
    private LocationManager lms;
    private UiSettings mUiSettings;
    MarkerOptions yourPositionMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        initialization();
    }

    public void initialization() {
        showYourPosition = (ImageButton) findViewById(R.id.showYourPositionButton);
//        Drawable myDrawable = getResources().getDrawable(R.drawable.show_your_position);
//        showYourPosition.setImageDrawable(myDrawable);

        menuButton = (ImageButton) findViewById(R.id.menuButton);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            return;
//        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showYourPositionButton:
                GPSisEnabled();

                break;
//            case R.id.menuButton:
//                Intent intent = new Intent();
//                intent.setClass(this,xxx)
//                startActivity();
//                break;
        }
    }

    public void GPSisEnabled() {
        LocationManager status = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));


        if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {


            locationServiceInitial();
        } else {
            Toast.makeText(this, "Please Enable your location sevice", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    private void locationServiceInitial() {
        mMap.clear();
        lms = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }

        Location location = lms.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);    //使用GPS定位座標

        getLocation(location);
    }

    private void getLocation(Location location) {
        if(location != null) {

            Double longitude = location.getLongitude();
            Double latitude = location.getLatitude();

            // set location
            LatLng yourPositionLatLng = new LatLng(latitude, longitude);
            yourPositionMarker = new MarkerOptions().position(yourPositionLatLng).title("Your Position");

            BitmapDescriptor yourPositionIcon = BitmapDescriptorFactory.fromResource(R.drawable.your_position);

            yourPositionMarker.icon(yourPositionIcon);
            mMap.addMarker(yourPositionMarker);

            // zoom camera and set location about zoom
            CameraUpdate locat = CameraUpdateFactory.newLatLng(yourPositionLatLng);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
            mMap.moveCamera(locat);
            mMap.animateCamera(zoom);
        }
        else {
            Toast.makeText(this, "無法定位座標", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // Add a marker in Sydney and move the camera
//        LatLng marker = new LatLng(0, 0);
//        mMap.addMarker(new MarkerOptions().position(marker).title("Marker"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));


        mUiSettings = mMap.getUiSettings();

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {

            @Override
            public boolean onMyLocationButtonClick() {
//                https://maps.googleapis.com/maps/api/place/nearbysearch/output?parameters

                return true;
            }
        });



      //  mUiSettings.setMyLocationButtonEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

    }

    TextView mTapTextView;

    public void onMapClick(LatLng point){
        Toast.makeText(this, "Tapped, point =" + point, Toast.LENGTH_SHORT).show();
        //mTapTextView.setText("tapped, point =" + point);
    }

    @Override
    public void onLocationChanged(Location location) {
//        if(mMap != null){
//            setCamera(location);
//            setMarker(location);
//            setPolyLine(location);
//        }
    }

//    private void setMarker(Location location) {
//        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
//        if(currentMarker == null){
//            currentMarker = mMap.addMarker(new MarkerOptions().position(current).title("Lat: " + location.getLatitude() +
//            " Long:" + location.getLongitude()));
//        }
//        else{
//            currentMarker.setPosition(current);
//            currentMarker.setTitle("Lat: " + location.getLatitude() +
//            " Long:" + location.getLongitude());
//        }
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
//    }
//
//
//    private void setPolyLine(Location location){
//        if(prevLatLng == null){
//            prevLatLng = new LatLng(location.getLatitude(),location.getLongitude());
//        }
//        else{
//            LatLng currentLatLng = new LatLng(location.getLatitude(),location.getLongitude()) ;
//            mMap.addPolyline(new PolylineOptions().add(prevLatLng, currentLatLng).width(5).color(Color.BLUE));
//        }
//    }
//
//
    private void setCamera(Location location){
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(
//            new LatLng(location.getLatitude(),location.getLongitude())));
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {


    }
}
