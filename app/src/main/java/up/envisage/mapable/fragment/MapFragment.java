package up.envisage.mapable.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.transition.MaterialArcMotion;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import up.envisage.mapable.R;
import up.envisage.mapable.db.table.ReportTable;
import up.envisage.mapable.model.ReportViewModel;
import up.envisage.mapable.ui.home.ReportingActivity;

import static android.os.Looper.getMainLooper;


public class MapFragment extends Fragment implements OnMapReadyCallback, PermissionsListener,
    OnCameraTrackingChangedListener {

    private static final long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private static final long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private PermissionsManager permissionsManager;
    private FragmentActivity listener;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private LocationEngine locationEngine;
    private LocationChangeListeningActivityLocationCallback callback =
            new LocationChangeListeningActivityLocationCallback(this);

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.listener = (FragmentActivity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Mapbox.getInstance(getActivity(), getString(R.string.mapbox_access_token));
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.mapBox_mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        MapFragment.this.mapboxMap = mapboxMap;

        List<Feature> symbolLayerFeatureList = new ArrayList<>();

        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjerxnqt3cgvp2rmyuxbeqme7"),
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);

                        //Load report data of the user
                        ReportViewModel reportViewModel = ViewModelProviders.of(MapFragment.this).get(ReportViewModel.class);
                        reportViewModel.getAllReports().observe(MapFragment.this, new Observer<List<ReportTable>>() {
                            @Override
                            public void onChanged(List<ReportTable> reportTables) {
                                int count = reportTables.size();
                                for (int i = 0; i < count; i++){
                                    String incidentType = reportTables.get(i).getIncidentType();
                                    Double latitude = reportTables.get(i).getLatitude();
                                    Double longitude = reportTables.get(i).getLongitude();
                                    mapboxMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(latitude, longitude))
                                    .title(incidentType));
                                }
                            }
                        });

                    }
                });
    }

    /**
     * Initialize the Maps SDK's LocationComponent
     */
    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        //Check if the permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(listener)) {

            //Get instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            //Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(listener, loadedMapStyle)
                            .useDefaultLocationEngine(false)
                            .build());
            //Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            //Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            //Set the component's render mode
            locationComponent.setRenderMode(RenderMode.GPS);

            locationComponent.addOnCameraTrackingChangedListener(this);

            //Set the component's zoom
            locationComponent.zoomWhileTracking(15.0, 15000);

            initLocationEngine();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(listener);
        }
    }

    /**
     * Set up the LocationEngine and the parameters for querying the device's location
     */
    @SuppressLint("MissingPermission")
    private void initLocationEngine(){
        locationEngine = LocationEngineProvider.getBestLocationEngine(listener);
        LocationEngineRequest request = new LocationEngineRequest
                .Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }

    private void setCameraPosition(double latitude, double longitude) {
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(17)
                .tilt(45)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 15000);
    }

    @Override
    public void onCameraTrackingDismissed() {

    }

    @Override
    public void onCameraTrackingChanged(int currentMode) {

    }

    private static class LocationChangeListeningActivityLocationCallback implements
            LocationEngineCallback<LocationEngineResult> {
        private final WeakReference<MapFragment> mapFragmentWeakReference;

        LocationChangeListeningActivityLocationCallback(MapFragment activity) {
            this.mapFragmentWeakReference = new WeakReference<>(activity);
        }
        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @Override
        public void onSuccess(LocationEngineResult result) {
            MapFragment activity = mapFragmentWeakReference.get();
            if (activity != null) {
                Location location = result.getLastLocation();
                if (location == null) {
                    return;
                }
                if (activity.mapboxMap != null && result.getLastLocation() != null) {
                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                }
            }
        }
        /**
         * The LocationEngineCallback interface's method which fires when the device's location can't be captured
         *
         * @param exception the exception message
         */
        @Override
        public void onFailure(@NonNull Exception exception){
            MapFragment activity = mapFragmentWeakReference.get();
            if (activity != null) {
                Log.e("[ MapFragment.java ]", exception.getLocalizedMessage());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(listener, "Permission not granted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
        mapView.onStop();
    }

    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        mapView.onSaveInstanceState(savedInstanceState);
    }

    public void onDestroyView(){
        super.onDestroyView();
        //Prevent leaks
        if (locationEngine != null){
            locationEngine.removeLocationUpdates(callback);
        }
        mapView.onDestroy();
    }
}
