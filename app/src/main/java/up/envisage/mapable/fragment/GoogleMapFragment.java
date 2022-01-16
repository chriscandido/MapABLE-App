package up.envisage.mapable.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;

import up.envisage.mapable.R;
import up.envisage.mapable.ui.home.CameraActivity;
import up.envisage.mapable.util.Constant;

public class GoogleMapFragment extends FragmentActivity
        implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    // Variables
    private String userID, incidentType, dateTime, report, lat, lon, image;
    private GoogleMap map;
    private Dialog dialog;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        // Get intent
        Intent location = getIntent();

        userID = location.getStringExtra("userID");
        dateTime = location.getStringExtra("Date and Time");
        incidentType = location.getStringExtra("Incident Type");
        report = location.getStringExtra("Report");
        lon = location.getStringExtra("Longitude");
        lat = location.getStringExtra("Latitude");
        image = location.getStringExtra("image");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    //----------------------------------------------------------------------------------------------Google map
    @SuppressLint("LongLogTag")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        final LatLng[] pinnedLocation = new LatLng[1];

        // Marker icon
        @SuppressLint("UseCompatLoadingForDrawables") Drawable userLoc = getResources().getDrawable(R.drawable.ic_report_userlocation_90x90);
        final BitmapDescriptor userIcon = getMarkerIconFromDrawable(userLoc);

        // Get current location using FusedLocation
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            googleMap.addMarker(new MarkerOptions()
                                    .icon(userIcon)
                                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .title("My Current Location"));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16.0f));
                            Toast.makeText(getApplicationContext(), location.getLatitude() + "" + location.getLongitude(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Marker icon
        @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = getResources().getDrawable(R.drawable.ic_report_userlocation_target_90x90);
        final BitmapDescriptor icon = getMarkerIconFromDrawable(drawable);

        googleMap.setOnMapLongClickListener(
                latLng -> {
                    map.clear();
                    Marker marker;
                    marker = map.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .icon(icon)
                                    .draggable(true));
                    pinnedLocation[0] = latLng;
                }
        );

        // Check Permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, Constant.REQUEST_PERMISSIONS_REQUEST_CODE);
            return;
        }

        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(GoogleMapFragment.this);
        map.setOnMyLocationClickListener(GoogleMapFragment.this);

        // Get coordinates of the marker
        Button button_googleMaps_submitLocation = findViewById(R.id.button_googleMap_submit);
        button_googleMaps_submitLocation.setOnClickListener(
                view -> {
                    if (pinnedLocation[0] != null) {
                        Log.v("[ GoogleMapFragment.java ]", "Pinned Location - latitude: " +
                                pinnedLocation[0].latitude + " and " + "longitude: "+
                                pinnedLocation[0].longitude);
                        // Pass information to next activity
                        Intent submitLocation = new Intent(GoogleMapFragment.this, CameraActivity.class);
                        submitLocation.putExtra("userID", userID);
                        submitLocation.putExtra("Date and Time", dateTime);
                        submitLocation.putExtra("Incident Type", incidentType);
                        submitLocation.putExtra("Report", report);
                        submitLocation.putExtra("Latitude",  String.valueOf(pinnedLocation[0].latitude)); // Latitude
                        submitLocation.putExtra("Longitude", String.valueOf(pinnedLocation[0].longitude)); // Longitude
                        submitLocation.putExtra("image", image);
                        startActivity(submitLocation);
                        Toast.makeText(GoogleMapFragment.this, "Coordinates successfully saved", Toast.LENGTH_LONG).show();
                     } else {
                        errorMapCoordinates();
                    }
                }
        );
    }

    //----------------------------------------------------------------------------------------------Convert to bitmap
    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    //----------------------------------------------------------------------------------------------Popup for no location
    private void errorMapCoordinates(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_error_nolocation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        MaterialButton button_reportMap_ok = dialog.findViewById(R.id.button_reportMap_ok);
        button_reportMap_ok.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) { }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) { }

    public void onBackPressed() {
        finish();
    }

}
