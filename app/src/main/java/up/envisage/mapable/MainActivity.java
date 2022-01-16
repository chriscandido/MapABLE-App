package up.envisage.mapable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import up.envisage.mapable.fragment.HomeFragment;
import up.envisage.mapable.fragment.IncidentListFragment;
import up.envisage.mapable.fragment.MapFragment;
import up.envisage.mapable.fragment.SupportFragment;
import up.envisage.mapable.fragment.UserFragment;

public class MainActivity extends AppCompatActivity {

    String userID;
    private RelativeLayout relativeLayout_mainHeader;
    private GoogleSignInClient mSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent login = getIntent(); // gets intent from reportingActivity

        userID = login.getStringExtra("userID");

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
        mSignInClient = GoogleSignIn.getClient(this, options);

        //Bottom bar navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnavigation_menu);
        bottomNavigationView.setOnItemSelectedListener(bottomNavigation);

        //Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout_container, new HomeFragment())
                .commit();

        //Location settings
        displayLocationSettingsRequest(this);

    }

    //----------------------------------------------------------------------------------------------Bottom Navigation
    private final NavigationBarView.OnItemSelectedListener bottomNavigation = new NavigationBarView.OnItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment = null;
            switch (menuItem.getItemId()) {
                case R.id.mainMenu_home:
                    fragment = new HomeFragment();
                    Intent homeFragment = new Intent(MainActivity.this, HomeFragment.class);
                    homeFragment.putExtra("userID", userID);
                    break;
                case R.id.mainMenu_user:
                    fragment = new UserFragment();
                    break;
                case R.id.mainMenu_report:
                    fragment = new IncidentListFragment();
                    Intent incidentFragment = new Intent(MainActivity.this, IncidentListFragment.class);
                    incidentFragment.putExtra("userID", userID);
                    break;
                case R.id.mainMenu_map:
                    fragment = new MapFragment();
                    break;
                case R.id.mainMenu_support:
                    fragment = new SupportFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentLayout_container, fragment)
                    .commit();

            return true;
        }
    };

    //----------------------------------------------------------------------------------------------Location settings
    public void displayLocationSettingsRequest(Context context){

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000/2);

        LocationSettingsRequest.Builder locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        locationSettingsRequest.setAlwaysShow(true);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(locationSettingsRequest.build());

        // check location settings
        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Log.i("[ MainActivity.java ]", "Settings - LOCATION SETTINGS: All locations settings are satisfied");
                    }
                } catch (ApiException exception) {
                    exception.printStackTrace();
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.i("[ MainActivity.java ]", "Settings - LOCATION SETTINGS: Location settings are not satisfied. Show the user a dialog to upgrade location settings");
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) exception;
                                resolvableApiException.startResolutionForResult(MainActivity.this, 2001);
                            } catch (IntentSender.SendIntentException e) {
                                Log.i("[ MainActivity.java ]", "Settings - LOCATION SETTINGS: PendingIntent unable to execute request");
                            } catch (ClassCastException e) {
                                //ignored error
                            } break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Log.i("[ MainActivity.java ]", "Settings - LOCATION SETTINGS: Location settings are inadequate, and cannot be fixed here. Dialog not created");
                            break;
                    }
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