package up.envisage.mapable.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

import up.envisage.mapable.R;
import up.envisage.mapable.ui.home.CameraActivity;
import up.envisage.mapable.ui.home.ReportIncidentActivity;
import up.envisage.mapable.ui.home.ReportingActivity;
import up.envisage.mapable.util.Constant;

public class GoogleMapFragment extends FragmentActivity
    implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    private GoogleMap map;
    private Button button_googleMaps_submitLocation;

    //String userID, type, incident, frequency, a1, a2, a3, a4, a5, a6, a7, lon, lat, image;
    String incidentType, dateTime, report, lat, lon;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        Intent location = getIntent();
        /*
        userID = location.getStringExtra("userID");
        type = location.getStringExtra("type");
        incident = location.getStringExtra("incident");
        frequency = location.getStringExtra("frequency");
        a1 = location.getStringExtra("a1");
        a2 = location.getStringExtra("a2");
        a3 = location.getStringExtra("a3");
        a4 = location.getStringExtra("a4");
        a5 = location.getStringExtra("a5");
        a6 = location.getStringExtra("a6");
        a7 = location.getStringExtra("a7");
        image = location.getStringExtra("image");*/

        dateTime = location.getStringExtra("Date and Time");
        incidentType = location.getStringExtra("Incident Type");
        report = location.getStringExtra("Report");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
    }

    //----------------------------------------------------------------------------------------------Google map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        final LatLng[] pinnedLocation = new LatLng[1];

        //Add marker, move camera and zoom level
        final LatLng[] location = {new LatLng(12.65017682702677, 122.54032239317894)};
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location[0], 12.0f));

        //Marker icon
        Drawable drawable = getResources().getDrawable(R.drawable.ic_report_location_target);
        final BitmapDescriptor icon = getMarkerIconFromDrawable(drawable);

        googleMap.setOnMapLongClickListener(
                new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng){
                        map.clear();
                        Marker marker;
                        marker = map.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .icon(icon)
                                        .draggable(true));
                        pinnedLocation[0] = latLng;
                    }
                }
        );

        //Check Permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, Constant.REQUEST_PERMISSIONS_REQUEST_CODE);
            return;
        }

        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(GoogleMapFragment.this);
        map.setOnMyLocationClickListener(GoogleMapFragment.this);

        //Get coordinates of the marker
        button_googleMaps_submitLocation = findViewById(R.id.button_googleMap_submit);
        button_googleMaps_submitLocation.setOnClickListener(
                new View.OnClickListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onClick(View view) {
                        if (pinnedLocation[0] != null) {
                            Log.v("[ GoogleMapFragment.java ]", "Pinned Location - latitude: " +
                                    pinnedLocation[0].latitude + " and " + "longitude: "+
                                    pinnedLocation[0].longitude);
                            Intent submitLocation = new Intent(GoogleMapFragment.this, CameraActivity.class);
                            submitLocation.putExtra("Date and Time", dateTime);
                            submitLocation.putExtra("Incident Type", incidentType);
                            submitLocation.putExtra("Report", report);
                            submitLocation.putExtra("Latitude", String.valueOf(pinnedLocation[0].latitude));
                            submitLocation.putExtra("Longitude", String.valueOf(pinnedLocation[0].longitude));
                            //submitLocation.putExtra("userID", userID);
                            //submitLocation.putExtra("type", type);
                            //submitLocation.putExtra("incident", incident);
                            //submitLocation.putExtra("frequency", frequency);
                            //submitLocation.putExtra("a1", a1);
                            //submitLocation.putExtra("a2", a2);
                            //submitLocation.putExtra("a3", a3);
                            //submitLocation.putExtra("a4", a4);
                            //submitLocation.putExtra("a5", a5);
                            //submitLocation.putExtra("a6", a6);
                            //submitLocation.putExtra("a7", a7);
                            //submitLocation.putExtra("Latitude", String.valueOf(pinnedLocation[0].latitude));
                            //submitLocation.putExtra("Longitude", String.valueOf(pinnedLocation[0].longitude));
                            //submitLocation.putExtra("image", image);
                            startActivity(submitLocation);
                            Toast.makeText(GoogleMapFragment.this, "Coordinates successfully saved", Toast.LENGTH_LONG).show();
                         }
                    }
                }
        );
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) { }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) { }

}
