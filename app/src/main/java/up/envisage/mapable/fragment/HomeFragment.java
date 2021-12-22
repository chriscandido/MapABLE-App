package up.envisage.mapable.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import up.envisage.mapable.R;
import up.envisage.mapable.adapter.MainMenuAdapter;
import up.envisage.mapable.ui.home.AboutActivity;
import up.envisage.mapable.ui.home.AboutManilaBayActivity;
import up.envisage.mapable.ui.home.InformationActivity;


public class HomeFragment extends Fragment implements MainMenuAdapter.OnMenuClickListener,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // Layout
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FragmentActivity listener;
    private RelativeLayout relativeLayout_mainHeader;

    // View
    ImageView imageView_mainMenu_alaminnatin, imageView_mainMenu_im4manilabay;
    TextView textView_homeLocation, textView_tingnanLahat;
    ImageView imageView_home_pin;

    // Variable
    String userID;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    // GPS
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    LocationRequest locationRequest;
    GoogleApiClient googleApiClient;
    Location currentLocation;

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.listener = (FragmentActivity) context;
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent homeFragment = requireActivity().getIntent();
        userID = homeFragment.getStringExtra("userID");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView_mainMenu);
        recyclerView.setHasFixedSize(true);

        //Linear layout manager
        layoutManager = new LinearLayoutManager(listener, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //Recycler view adapter
        adapter = new MainMenuAdapter(listener, this);
        recyclerView.setAdapter(adapter);

        isGooglePlayServicesAvailable();
        createLocationRequest();

        googleApiClient = new GoogleApiClient.Builder(listener)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        imageView_mainMenu_alaminnatin = view.findViewById(R.id.imageView_mainMenu_alaminnatin);
        imageView_mainMenu_im4manilabay = view.findViewById(R.id.imageView_mainMenu_im4ManilaBay);
        imageView_home_pin = view.findViewById(R.id.imageview_home_pin);

        textView_homeLocation = view.findViewById(R.id.textView_homeLocation);
        textView_tingnanLahat = view.findViewById(R.id.textView_mainMenu_tignanLahat);

        // Alamin natin button
        imageView_mainMenu_alaminnatin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent alaminIntent = new Intent(listener, AboutManilaBayActivity.class);

                startActivity(alaminIntent);
            }
        });

        // About Im4ManilaBay button
        imageView_mainMenu_im4manilabay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent ima4manilaBayIntent = new Intent(listener, AboutActivity.class);
                startActivity(ima4manilaBayIntent);
            }
        });

        // Update location pin
        imageView_home_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUI();
            }
        });

        // View all links
        textView_tingnanLahat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent information = new Intent(listener, InformationActivity.class);
                startActivity(information);
            }
        });
    }


    //----------------------------------------------------------------------------------------------Features for
    public void onClick(int position) {
        Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - " + position);
        switch (position) {
            case 0:
                String facebookLink01 = "https://www.facebook.com/ProjectMapABLE/photos/a.143299490614286/224398339171067/";
                openFacebookPageIntent(facebookLink01);
                Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - link01");
                break;
            case 1:
                String facebookLink02 = "https://www.facebook.com/ProjectMapABLE/photos/a.143299490614286/228755065402061/";
                openFacebookPageIntent(facebookLink02);
                Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - link02");
                break;
            case 2:
                String facebookLink03 = "https://www.facebook.com/ProjectMapABLE/photos/pcb.232793878331513/232792664998301/";
                openFacebookPageIntent(facebookLink03);
                Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - link03");
                break;
            case 3:
                String facebookLink04 = "https://www.facebook.com/ProjectMapABLE/photos/a.143299490614286/236249687985932/";
                openFacebookPageIntent(facebookLink04);
                Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - link04");
                break;
            case 4:
                String facebookLink05 = "https://www.facebook.com/ProjectMapABLE/photos/a.143299490614286/240185767592324/";
                openFacebookPageIntent(facebookLink05);
                Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - link05");
                break;
            case 5:
                String facebookLink06 = "https://www.facebook.com/ProjectMapABLE/photos/a.143299490614286/248369220107312/";
                openFacebookPageIntent(facebookLink06);
                Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - link06");
                break;
        }
    }

    //----------------------------------------------------------------------------------------------facebook links
    private void openFacebookPageIntent(String url) {
        Context context = null;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                int versionCode = context.getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
                String facebookUrl = null;
                if (versionCode >= 3002850) {
                    facebookUrl = "fb://facewebmodal/f?href=" + url;
                }
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
            } else {
                throw new Exception("Facebook is disabled");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }

    //----------------------------------------------------------------------------------------------Location request
    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    //----------------------------------------------------------------------------------------------Check Google Play Services
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(listener);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, listener, 0).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ..........................: " + googleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(listener, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(listener, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        Log.d(TAG, "Location update started ...........................: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        currentLocation = location;
        updateUI();
    }

    //----------------------------------------------------------------------------------------------Geocoder
    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        final String[] address_string = new String[1];
        if (null != currentLocation) {
            String lat = String.valueOf(currentLocation.getLatitude());
            String lng = String.valueOf(currentLocation.getLongitude());
            Geocoder geocoder = new Geocoder(listener, Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                if (addresses != null) {
                    try {
                        if (addresses.get(0).getSubLocality() != null && addresses.get(0).getLocality() != null) {
                            textView_homeLocation.setText(addresses.get(0).getSubLocality() + ", " + addresses.get(0).getLocality());
                        } else if (addresses.get(0).getSubLocality() == null && addresses.get(0).getLocality() != null){
                            textView_homeLocation.setText(addresses.get(0).getLocality());
                        } else if (addresses.get(0).getSubLocality() != null && addresses.get(0).getLocality() == null) {
                            textView_homeLocation.setText(addresses.get(0).getSubLocality());
                        } else {
                            textView_homeLocation.setText(addresses.get(0).getCountryName());
                        }
                        address_string[0] = String.valueOf(addresses.get(0).getAddressLine(0));
                    } catch (IndexOutOfBoundsException e) {
                        address_string[0] = "NO ADDRESS LINE MATCHED";
                    }
                } else {
                    address_string[0] = "NO ADDRESS MATCHES WERE FOUND";
                }
                Log.v("[ HomeFragment.java ]", "Address: " + address_string[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "location is null ...............");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..........................");
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..........................");
        googleApiClient.disconnect();
        Log.d(TAG, "isConnected ..........................: " + googleApiClient.isConnected());
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                googleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (googleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }

}
