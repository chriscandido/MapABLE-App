package up.envisage.mapable.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import up.envisage.mapable.fragment.HomeFragment;

public class ReverseGeoCoder extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 101;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mGetedLocation;

    private double currentLat, currentLng;

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            mGetedLocation = task.getResult();
                            //currentLat = mGetedLocation.getLatitude();
                            //currentLng = mGetedLocation.getLongitude();

                            //start reverse geocoding
                            //little different with the doc
                            (new GetAddressTask(this)).execute(mGetedLocation);

                        } else {
                            Log.e(TAG, "no location detected");
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                        }
                    }
                });
    }

    public class GetAddressTask extends AsyncTask<Location, Void, String> {
        Context mContext;

        GetAddressTask(OnCompleteListener<Location> context) {
            super();
            mContext = (Context) context;
        }

        @Override
        protected String doInBackground(Location... params) {
            Geocoder geocoder =
                    new Geocoder(mContext, Locale.getDefault());
            // Get the current location from the input parameter list
            Location loc = params[0];
            // Create a list to contain the result address
            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
            } catch (IOException e1) {
                Log.e("LocationSampleActivity",
                        "IO Exception in getFromLocation()");
                e1.printStackTrace();
                return ("IO Exception trying to get address");
            } catch (IllegalArgumentException e2) {
                // Error message to post in the log
                String errorString = "Illegal arguments " +
                        Double.toString(loc.getLatitude()) +
                        " , " +
                        Double.toString(loc.getLongitude()) +
                        " passed to address service";
                Log.e("LocationSampleActivity", errorString);
                e2.printStackTrace();
                return errorString;
            }
            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
        /*
                    address.getLocality(),
                    address.getCountryName());
        */
                Intent submitLocationIntent = new Intent(ReverseGeoCoder.this, HomeFragment.class);
                submitLocationIntent.putExtra("location_address", address.getLocality());
                startActivity(submitLocationIntent);
                return address.getLocality();
            } else {
                return "No address found";
            }
        }

    }

}
