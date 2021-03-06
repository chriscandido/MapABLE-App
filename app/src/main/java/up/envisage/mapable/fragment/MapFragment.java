package up.envisage.mapable.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import up.envisage.mapable.R;
import up.envisage.mapable.db.table.ReportTable;
import up.envisage.mapable.model.ReportViewModel;
import up.envisage.mapable.model.UserViewModel;
import up.envisage.mapable.ui.home.ReportingActivity;
import up.envisage.mapable.ui.registration.RetrofitInterface;

import static android.os.Looper.getMainLooper;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;


public class MapFragment extends Fragment implements OnMapReadyCallback, PermissionsListener,
        OnCameraTrackingChangedListener {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://ec2-54-91-89-105.compute-1.amazonaws.com/";

    private UserViewModel userViewModel;

    private static final long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private static final long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;

    //    private final HashMap<String, LatLng> myReports = new HashMap<>();
    private final HashMap<String, LatLng> offshoreStation = new HashMap<>();
    private final HashMap<String, LatLng> prumStation = new HashMap<>();
    private final HashMap<String, LatLng> bathingBeachesStation = new HashMap<>();
    private final HashMap<String, LatLng> riverOutfallStation = new HashMap<>();
    private final HashMap<String, LatLng> lagunaDeBayStation = new HashMap<>();

    private PermissionsManager permissionsManager;
    private FragmentActivity listener;

    private MapView mapView;
    private MapboxMap mapboxMap;
    private LocationEngine locationEngine;

    private MaterialButton button_mapReports, button_mapOffshoreStation, button_mapBathingBeachesStation,
            button_mapRiverOutfallStation, button_mapPRUMStation, button_mapLagunaDeBayStation;

    private String outUserId;

    public ArrayList<List> reportsArrayList = new ArrayList<List>();

    private View rootView;
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

        userViewModel = ViewModelProviders.of(MapFragment.this).get(UserViewModel.class);

        userViewModel.getLastUser().observe(MapFragment.this, UserTable -> {
            outUserId = UserTable.getUniqueId();
            Log.v("[MapFragment.java]", "UserID from local DB: " + outUserId);
        });

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        //Set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        //Add your other interceptors …
        httpClient.callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(30, TimeUnit.SECONDS) // connect timeout
                .writeTimeout(30, TimeUnit.SECONDS) // write timeout
                .readTimeout(30, TimeUnit.SECONDS); // read timeout

        //Add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Mapbox.getInstance(getActivity(), getString(R.string.mapbox_access_token));
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_map, container, false);

        return rootView;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button_mapReports = view.findViewById(R.id.button_mapMyReports);
        button_mapOffshoreStation = view.findViewById(R.id.button_mapOffshoreStation);
        button_mapBathingBeachesStation = view.findViewById(R.id.button_mapBathingBeachesStation);
        button_mapRiverOutfallStation = view.findViewById(R.id.button_mapRiverOutfallStation);
        button_mapPRUMStation = view.findViewById(R.id.button_mapPRUMStation);
        button_mapLagunaDeBayStation = view.findViewById(R.id.button_mapLagunaDeBayStation);

        mapView = view.findViewById(R.id.mapBox_mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        MapFragment.this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.OUTDOORS,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
//                        setupData(mapboxMap);

                        button_mapReports.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                HashMap<String, String> map = new HashMap<>();
                                map.put("userID", outUserId);

                                Call<List> call2 = retrofitInterface.getUserReports(map);

                                call2.enqueue(new Callback<List>() {

                                    @Override
                                    public void onResponse(Call<List> call2, Response<List> response2) {
                                        if(response2.isSuccessful()) {

                                            for(int i=0; i<response2.body().size(); i++) {
                                                reportsArrayList.add((List) response2.body().get(i));
                                            }

//                                            Toast.makeText(getActivity(),"My Reports Retrieved!",Toast.LENGTH_SHORT).show();

                                            setupData(mapboxMap);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List> call, Throwable t) {

                                    }
                                });
                                mapboxMap.removeAnnotations();
                            }
                        });

                        // Offshore Station
                        button_mapOffshoreStation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mapboxMap.removeAnnotations();
                                viewOffshoreStation(mapboxMap);
                            }
                        });

                        // Bathing Beaches Station
                        button_mapBathingBeachesStation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mapboxMap.removeAnnotations();
                                viewBathingBeachesStation(mapboxMap);

                            }
                        });

                        // River Outfall Station
                        button_mapRiverOutfallStation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mapboxMap.removeAnnotations();
                                viewRiverOutfallStation(mapboxMap);

                            }
                        });

                        // PRUM Station
                        button_mapPRUMStation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mapboxMap.removeAnnotations();
                                viewPRUMStation(mapboxMap);

                            }
                        });

                        // Laguna de Bay Station
                        button_mapLagunaDeBayStation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mapboxMap.removeAnnotations();
                                viewLagunaDeBayStation(mapboxMap);
                            }
                        });

                    }
                });
    }

    //----------------------------------------------------------------------------------------------Get data from local db
    public void setupData (MapboxMap mapboxMap){
        Icon iconAlgalBloom = drawableToIcon(getApplicationContext(), R.drawable.ic_map_algalbloom120x120);
        Icon iconFishKill = drawableToIcon(getApplicationContext(), R.drawable.ic_map_fishkill120x120);
        Icon iconPollution = drawableToIcon(getApplicationContext(), R.drawable.ic_map_waterpollution120x120);
        Icon iconIllegalRec = drawableToIcon(getApplicationContext(), R.drawable.ic_map_illegalreclamation120x120);
        Icon iconWaterHyacinth = drawableToIcon(getApplicationContext(), R.drawable.ic_map_hyacinth120x120);
        Icon iconSolidWaste = drawableToIcon(getApplicationContext(), R.drawable.ic_map_solidwaste120x120);
        Icon iconIbaPa = drawableToIcon(getApplicationContext(), R.drawable.ic_map_ibapa120x120);

        for (int i = 0; i <reportsArrayList.size(); i++){
            String incidentType = reportsArrayList.get(i).get(0).toString();
            double latitude = (double) reportsArrayList.get(i).get(2);
            double longitude = (double) reportsArrayList.get(i).get(1);

            switch (incidentType) {
                case "Algal Bloom":
                    mapboxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title(incidentType)
                            .icon(iconAlgalBloom));
                    break;
                case "Fish Kill":
                    mapboxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title(incidentType)
                            .icon(iconFishKill));
                    break;
                case "Water Pollution":
                    mapboxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title(incidentType)
                            .icon(iconPollution));
                    break;
                case "Ongoing Reclamation":
                    mapboxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title(incidentType)
                            .icon(iconIllegalRec));
                    break;
                case "Water Hyacinth":
                    mapboxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title(incidentType)
                            .icon(iconWaterHyacinth));
                    break;
                case "Solid Waste":
                    mapboxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title(incidentType)
                            .icon(iconSolidWaste));
                    break;
                case "Iba Pa":
                    mapboxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title(incidentType)
                            .icon(iconIbaPa));
                    break;
            }
        }
    }

    //----------------------------------------------------------------------------------------------View Laguna de Bay
    public void viewLagunaDeBayStation(MapboxMap mapboxMap) {

        lagunaDeBayStation.put("Station I", new LatLng(14.4169472222222, 121.175988888889));
        lagunaDeBayStation.put("Station V", new LatLng(14.4891444444444, 121.139452777778));
        lagunaDeBayStation.put("Station XV", new LatLng(14.3704166666667, 121.113086111111));
        lagunaDeBayStation.put("Station XVI", new LatLng(14.3056444444444, 121.155505555556));
        lagunaDeBayStation.put("Station IV", new LatLng(14.3837166666667, 121.286716666667));
        lagunaDeBayStation.put("Station XVII", new LatLng(14.2798166666667, 121.276480555556));
        lagunaDeBayStation.put("Station VIII", new LatLng(14.2095805555556, 121.239480555556));
        lagunaDeBayStation.put("Station II", new LatLng(14.2679166666667, 121.34185));
        lagunaDeBayStation.put("Station XVIII", new LatLng(14.2970333333333, 121.362316666667));

        Icon iconLagunaDeBayStation = drawableToIcon(getApplicationContext(), R.drawable.ic_map_station36x36);

        for (String station : lagunaDeBayStation.keySet()) {
            String stationName = station;
            double latitude = lagunaDeBayStation.get(station).getLatitude();
            double longitude = lagunaDeBayStation.get(station).getLongitude();
            mapboxMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(stationName)
                    .snippet("Laguna de Bay Station").icon(iconLagunaDeBayStation));
        }
    }

    //----------------------------------------------------------------------------------------------View PRUM Station
    public void viewPRUMStation(MapboxMap mapboxMap){

        prumStation.put("Jones Bridge", new LatLng(14.5955, 120.9774));
        prumStation.put("Nagtahan Bridge", new LatLng(14.5954, 121.0016));
        prumStation.put("Lambingan Bridge", new LatLng(14.5864, 121.0200));
        prumStation.put("Guadalupe Ferry", new LatLng(14.5681, 121.0459));
        prumStation.put("Bambang Bridge", new LatLng(14.5536, 121.0759));
        prumStation.put("Napindan (C6) Bridge", new LatLng(14.5351, 121.1022));
        prumStation.put("Batasan Bridge", new LatLng(14.6794, 121.1097));
        prumStation.put("Tumana Bridge", new LatLng(14.6564, 121.0963));
        prumStation.put("Marikina Bridge", new LatLng(14.6343, 121.0933));
        prumStation.put("Rosario Bridge", new LatLng(14.5902, 121.0831));
        prumStation.put("Santa Rosa Bridge", new LatLng(14.5600, 121.0721));
        prumStation.put("Sevilla Bridge", new LatLng(14.5941, 121.0260));
        prumStation.put("Sta Ana Bridge", new LatLng(14.5259, 121.0735));
        prumStation.put("Levi Mariano Bridge", new LatLng(14.5317, 121.0687));
        prumStation.put("Buting Bridge", new LatLng(14.5549, 121.0652));
        prumStation.put("Havana Bridge", new LatLng(14.5795, 121.0157));
        prumStation.put("Guadalupe Viejo", new LatLng(14.5675, 121.0402));
        prumStation.put("Gudalupe Nuevo", new LatLng(14.5677, 121.0471));
        prumStation.put("Manila Bay", new LatLng(14.5930, 120.9464));

        Icon iconPRUMStation = drawableToIcon(getApplicationContext(), R.drawable.ic_map_station36x36);

        for (String station : prumStation.keySet()) {
            String stationName = station;
            double latitude = prumStation.get(station).getLatitude();
            double longitude = prumStation.get(station).getLongitude();
            mapboxMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(stationName)
                    .snippet("Pasig River Unified Monitoring Station").icon(iconPRUMStation));
        }

    }

    //----------------------------------------------------------------------------------------------View Bathing Beaches Station
    public void viewRiverOutfallStation(MapboxMap mapboxMap){

        riverOutfallStation.put("Balut", new LatLng(14.6008, 120.9581833));
        riverOutfallStation.put("Vitas", new LatLng(14.6294167, 120.959733));
        riverOutfallStation.put("San Antonio de Abad", new LatLng(14.5636167, 120.987567));
        riverOutfallStation.put("Macapagal 1", new LatLng(14.5443, 120.987533));
        riverOutfallStation.put("Macapagal 2", new LatLng(14.5476833, 120.9894));
        riverOutfallStation.put("Macapagal 3", new LatLng(14.519833, 120.990633));
        riverOutfallStation.put("Coastal 1", new LatLng(14.47533, 120.970167));
        riverOutfallStation.put("Coastal 2", new LatLng(14.476633, 120.972233));

        Icon iconRiverOutfallStation = drawableToIcon(getApplicationContext(), R.drawable.ic_map_station36x36);

        for (String station : riverOutfallStation.keySet()) {
            String stationName = station;
            double latitude = riverOutfallStation.get(station).getLatitude();
            double longitude = riverOutfallStation.get(station).getLongitude();
            mapboxMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(stationName)
                    .snippet("River Outfall Station").icon(iconRiverOutfallStation));
        }

    }

    //----------------------------------------------------------------------------------------------View Bathing Beaches Station
    public void viewBathingBeachesStation(MapboxMap mapboxMap){
        bathingBeachesStation.put("Navotas", new LatLng(14.64463611, 120.94823056));
        bathingBeachesStation.put("Luneta", new LatLng(14.581675, 120.9726361));
        bathingBeachesStation.put("CCP", new LatLng(14.558483, 120.9829));
        bathingBeachesStation.put("MOA", new LatLng(14.5419, 120.9803));
        bathingBeachesStation.put("PEATC", new LatLng(14.4928167, 120.975667));
        bathingBeachesStation.put("Balut", new LatLng(14.6008, 120.9581833));

        Icon iconBathingBeachesStation = drawableToIcon(getApplicationContext(), R.drawable.ic_map_station36x36);

        for (String station : bathingBeachesStation.keySet()) {
            String stationName = station;
            double latitude = bathingBeachesStation.get(station).getLatitude();
            double longitude = bathingBeachesStation.get(station).getLongitude();
            mapboxMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(stationName)
                    .snippet("Bathing Beaches Station").icon(iconBathingBeachesStation));
        }

    }

    //----------------------------------------------------------------------------------------------View Offshore Station
    public void viewOffshoreStation(MapboxMap mapboxMap) {
        offshoreStation.put("MBOS_01", new LatLng(14.6593, 120.86031));
        offshoreStation.put("MBOS_02", new LatLng(14.683111, 120.7612));
        offshoreStation.put("MBOS_03", new LatLng(14.67143056, 120.6248));
        offshoreStation.put("MBOS_04", new LatLng(14.57835, 120.634));
        offshoreStation.put("MBOS_05", new LatLng(14.4525, 120.6261));
        offshoreStation.put("MBOS_06", new LatLng(14.45305, 120.7599));
        offshoreStation.put("MBOS_07", new LatLng(14.46786111, 120.8411));
        offshoreStation.put("MBOS_08", new LatLng(14.58443889, 120.7621));
        offshoreStation.put("MBOS_09", new LatLng(14.59081944, 120.8573));

        Icon iconOffshoreStation = drawableToIcon(getApplicationContext(), R.drawable.ic_map_station36x36);

        for (String station : offshoreStation.keySet()) {
            String stationName = station;
            double latitude = offshoreStation.get(station).getLatitude();
            double lonigtude = offshoreStation.get(station).getLongitude();
            mapboxMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, lonigtude))
                    .title(stationName)
                    .snippet("Manila Bay Offshore Station").icon(iconOffshoreStation));
        }
    }

    /**
     * Initialize the Maps SDK's LocationComponent
     */
    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        //Check if the permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(listener)) {

            // Customize the LocationComponent's options
            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(getApplicationContext())
                    .elevation(5)
                    .accuracyAlpha(.6f)
                    .accuracyColor(Color.YELLOW)
                    .pulseEnabled(true)
                    .pulseColor(Color.BLUE)
                    .foregroundDrawable(R.drawable.ic_report_userlocation_60x60)
                    .build();

            //Get instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            //Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(listener, loadedMapStyle)
                            .useDefaultLocationEngine(false)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build());
            //Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            //Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            //Set the component's render mode
            locationComponent.setRenderMode(RenderMode.NORMAL);

            locationComponent.addOnCameraTrackingChangedListener(this);

            // Add the location icon click listener
            locationComponent.addOnLocationClickListener(this::initLocationEngine);

            //Set the component's zoom
            locationComponent.zoomWhileTracking(12.0, 10000);

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

    //----------------------------------------------------------------------------------------------Convert from bitmap to icon
    public static Icon drawableToIcon(@NonNull Context context, @DrawableRes int id) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(context.getResources(), id, context.getTheme());
        assert vectorDrawable != null;
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return IconFactory.getInstance(context).fromBitmap(bitmap);
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

    public void onDestroyView() {
        super.onDestroyView();
        //Prevent leaks
        if (locationEngine != null){
            locationEngine.removeLocationUpdates(callback);
        }
        mapView.onDestroy();
    }
}