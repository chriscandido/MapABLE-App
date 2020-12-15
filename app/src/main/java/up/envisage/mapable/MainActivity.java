package up.envisage.mapable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.net.ssl.SSLEngineResult;

import up.envisage.mapable.fragment.HomeFragment;
import up.envisage.mapable.fragment.MapFragment;
import up.envisage.mapable.fragment.SupportFragment;
import up.envisage.mapable.fragment.UserFragment;
import up.envisage.mapable.ui.home.ReportIncidentActivity;
import up.envisage.mapable.ui.home.ReportingActivity;
import up.envisage.mapable.util.Constant;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

//    private SharedPreferences userIDPreferences;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent login = getIntent(); // gets intent from reportingActivity

        userID = login.getStringExtra("userID");

//        userIDPreferences = getSharedPreferences("userID", MODE_PRIVATE);
//        userIDPreferences.edit().putString("userID", userID).apply();

        //Bottom bar navigation
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnavigation_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigation);

        //Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout_container, new HomeFragment()).commit();

        //Location settings
        displayLocationSettingsRequest(this);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigation = new BottomNavigationView.OnNavigationItemSelectedListener() {
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment = null;
            switch (menuItem.getItemId()) {
                case R.id.mainMenu_user:
                    fragment = new UserFragment();
                    break;
                case R.id.mainMenu_home:
                    fragment = new HomeFragment();
                    Intent homeFragment = new Intent(MainActivity.this, HomeFragment.class);
                    homeFragment.putExtra("userID", userID);
                    break;
                case R.id.mainMenu_map:
                    fragment = new MapFragment();
                    break;
                case R.id.mainMenu_support:
                    fragment = new SupportFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout_container, fragment).commit();
            return true;
        }
    };

    //----------------------------------------------------------------------------------------------Location settings
    public void displayLocationSettingsRequest(Context context){
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000/2);

        LocationSettingsRequest.Builder locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        locationSettingsRequest.setAlwaysShow(true);

        final PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequest.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()){
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("[ MainActivity.java ]", "Settings - LOCATION SETTINGS: All locations settings are satisfied");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("[ MainActivity.java ]", "Settings - LOCATION SETTINGS: Location settings are not satisfied. Show the user a dialog to upgrade location settings");
                        try {
                            //Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult()
                            status.startResolutionForResult(MainActivity.this, Constant.REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("[ MainActivity.java ]", "Settings - LOCATION SETTINGS: PendingIntent unable to execute request");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("[ MainActivity.java ]", "Settings - LOCATION SETTINGS: Location settings are inadequate, and cannot be fixed here. Dialog not created");
                }
            }
        });
    }

    public void onStart(){
        super.onStart();
    }

    public void onResume(){
        super.onResume();
    }

    public void onPause(){
        super.onPause();
    }

    public void onBackPressed(){
    }

}