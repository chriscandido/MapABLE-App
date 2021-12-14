package up.envisage.mapable.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import up.envisage.mapable.R;
import up.envisage.mapable.db.table.UserTable;
import up.envisage.mapable.model.ReportViewModel;
import up.envisage.mapable.model.UserViewModel;
import up.envisage.mapable.ui.home.LeaderboardActivity;
import up.envisage.mapable.ui.home.MyQuestActivity;
import up.envisage.mapable.ui.home.MyReportActivity;
import up.envisage.mapable.ui.home.UserStatisticsActivity;
import retrofitInterface.RetrofitInterface;
import up.envisage.mapable.model.StatsResult;

public class UserFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FragmentActivity listener;

    private Retrofit retrofit;
    public RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://ec2-54-91-89-105.compute-1.amazonaws.com/";

    private Dialog dialog;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    // GPS
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    LocationRequest locationRequest;
    GoogleApiClient googleApiClient;
    Location currentLocation;

    TextView textView_user_name, textView_user_username, textView_user_email, textView_user_myReport,
            textView_user_myStats, textView_myStats_submittedReports, textView_user_myReportsList, textView_user_leaderboard;
    TextView textView_userprofile_unsentreports, textView_userprofile_reportstatus, textView_userprofile_leaderboard,
            textView_userprofile_myyquests;

    String outUserId;
    public Integer algalBloom, fishKill, waterPollution, ongoingReclamation,
                    waterHyacinth, solidWaste, otherIssues, verified, unverified, falsePositive, total;

    public Object data;

    MaterialButton button_userMyStats_ok;

    ReportViewModel reportViewModel;
    UserViewModel userViewModel;

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.listener = (FragmentActivity) context;
        }
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        userViewModel = ViewModelProviders.of(UserFragment.this).get(UserViewModel.class);

        userViewModel.getLastUser().observe(UserFragment.this, UserTable -> {
            outUserId = UserTable.getUniqueId();
            Log.v("[UserFragment.java]", "UserID: " + outUserId);
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

        total = 0;
        Log.v("[UserFragment.java]", total.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        ImageView imageView_mainHeader_userprofile = view.findViewById(R.id.imageView_mainHeader_userprofile);
        ViewCompat.setTransitionName(imageView_mainHeader_userprofile, "imageView_mainHeader_userprofile_transition");


        textView_user_name = view.findViewById(R.id.textView_user_name);
        textView_user_username = view.findViewById(R.id.textView_user_username);
        textView_user_email = view.findViewById(R.id.textView_user_email);

        userDetails();

        //Pending reports button
        textView_userprofile_unsentreports = view.findViewById(R.id.textView_userprofile_unsentreports);
        textView_userprofile_unsentreports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myReport = new Intent(listener, MyReportActivity.class);
                startActivity(myReport);
            }
        });

        //Stats button
        textView_userprofile_reportstatus = view.findViewById(R.id.textView_userprofile_reportstatus);
        textView_userprofile_reportstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> map = new HashMap<>();
                map.put("userID", outUserId);

                Call<StatsResult> call = retrofitInterface.getStats(map);

                call.enqueue(new Callback<StatsResult>() {

                    @Override
                    public void onResponse(Call<StatsResult> call, Response<StatsResult> response) {
                        if(response.code() == 200) {
                            algalBloom = response.body().getAlgalBloom();
                            fishKill = response.body().getFishKill();
                            waterPollution = response.body().getWaterPollution();
                            ongoingReclamation = response.body().getOngoingReclamation();
                            waterHyacinth = response.body().getWaterHyacinth();
                            solidWaste = response.body().getSolidWaste();
                            otherIssues = response.body().getOthers();
                            verified = response.body().getVerified();
                            unverified = response.body().getUnverified();
                            falsePositive = response.body().getFalsePositive();
                            total = response.body().getTotal();

                            Intent userStatsIntent = new Intent(listener, UserStatisticsActivity.class);

                            userStatsIntent.putExtra("algalBloom", algalBloom.toString());
                            userStatsIntent.putExtra("fishKill", fishKill.toString());
                            userStatsIntent.putExtra("waterPollution", waterPollution.toString());
                            userStatsIntent.putExtra("ongoingReclamation", ongoingReclamation.toString());
                            userStatsIntent.putExtra("waterHyacinth", waterHyacinth.toString());
                            userStatsIntent.putExtra("solidWaste", solidWaste.toString());
                            userStatsIntent.putExtra("otherIssues", otherIssues.toString());
                            userStatsIntent.putExtra("verified", verified.toString());
                            userStatsIntent.putExtra("unverified", unverified.toString());
                            userStatsIntent.putExtra("falsePositive", falsePositive.toString());
                            userStatsIntent.putExtra("total", total.toString());
                            userStatsIntent.putExtra("userID", outUserId);

                            startActivity(userStatsIntent);

//                            textView_myStats_submittedReports.setText(total.toString());
//                            Log.v("[UserFragment.java]", "Algal Bloom: " + algalBloom.toString() + "\n" +"Fishkill : " + fishKill.toString() + "/\n" +
//                                    "Water Pollution: " + waterPollution.toString() + "\n" + "Ongoing Reclamation: " + ongoingReclamation.toString() + "\n" +
//                                    "Water Hyacinth: " + waterHyacinth.toString() + "\n" + "Solid Waste: " + solidWaste.toString() + "\n" + "Others: " + otherIssues.toString() + "\n" +
//                                    "Verified: " + verified.toString() + "\n" + "Unverified: " + unverified.toString() + "\n" + "False Positive: " + falsePositive.toString() + "\n" +
//                                    "Total: " + total.toString()
//
//                            );
                        }
                    }

                    @Override
                    public void onFailure(Call<StatsResult> call, Throwable t) {

                    }
                });
//                myStatsDialog();
            }
        });

        textView_userprofile_leaderboard = view.findViewById(R.id.textView_userprofile_leaderboard);
        textView_userprofile_leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(listener, LeaderboardActivity.class);
                startActivity(intent);
            }
        });

        textView_userprofile_myyquests = view.findViewById(R.id.textView_userprofile_myquests);
        textView_userprofile_myyquests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*                HashMap<String, String> map = new HashMap<>();
                map.put("userID", outUserId);

                Call<List<UserReport>> call = retrofitInterface.getUserReportsList(map);

                Intent intent = new Intent(listener, MyReportsListActivity.class);

                intent.putExtra("userID", outUserId);
                startActivity(intent);*/

/*                HashMap<String, String> map = new HashMap<>();
                map.put("userID", outUserId);

                Call<StatsResult> call = retrofitInterface.getStats(map);

                call.enqueue(new Callback<StatsResult>() {

                    @Override
                    public void onResponse(Call<StatsResult> call, Response<StatsResult> response) {
                        if(response.code() == 200) {
                            algalBloom = response.body().getAlgalBloom();
                            fishKill = response.body().getFishKill();
                            waterPollution = response.body().getWaterPollution();
                            ongoingReclamation = response.body().getOngoingReclamation();
                            waterHyacinth = response.body().getWaterHyacinth();
                            solidWaste = response.body().getSolidWaste();
                            otherIssues = response.body().getOthers();
                            verified = response.body().getVerified();
                            unverified = response.body().getUnverified();
                            falsePositive = response.body().getFalsePositive();
                            total = response.body().getTotal();

                            Intent myQuestIntent = new Intent(listener, MyQuestActivity.class);
                            myQuestIntent.putExtra("userID", outUserId);

                            startActivity(myQuestIntent);
                        }
                    }

                    @Override
                    public void onFailure(Call<StatsResult> call, Throwable t) {

                    }
                });*/

                Intent myQuestIntent = new Intent(listener, MyQuestActivity.class);
                myQuestIntent.putExtra("userID", outUserId);
                startActivity(myQuestIntent);
            }
        });
    }

    //----------------------------------------------------------------------------------------------Get report stats of user
    public void myStatsDialog() {
        dialog = new Dialog(listener);
        dialog.setContentView(R.layout.popup_mystats);

        textView_myStats_submittedReports = dialog.findViewById(R.id.textView_myStats_submittedReports);
        textView_myStats_submittedReports.setText(total.toString());
        button_userMyStats_ok = dialog.findViewById(R.id.button_userMyStats_ok);
        button_userMyStats_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    //----------------------------------------------------------------------------------------------Get user details from local db
    public void userDetails() {
        userViewModel = ViewModelProviders.of(UserFragment.this).get(UserViewModel.class);
        userViewModel.getLastUser().observe(UserFragment.this, new Observer<UserTable>() {
            @Override
            public void onChanged(UserTable userTable) {
                String name = userTable.getName();
                String username = userTable.getUsername();
                String email = userTable.getEmail();
                textView_user_name.setText(name);
                textView_user_username.setText("@" + username);
                textView_user_email.setText(email);
            }
        });
    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("[ UserFragment.java ]", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("[ UserFragment.java ] ", "printHashKey()", e);
        } catch (Exception e) {
            Log.e(" [ UserFragment.java ] ", "printHashKey()", e);
        }
    }

    //----------------------------------------------------------------------------------------------Location request
    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(listener);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, listener, 0).show();
            return false;
        }
    }

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


    public void onConnectionSuspended(int i) {

    }


    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }


    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        currentLocation = location;
        updateUI();
    }

    //----------------------------------------------------------------------------------------------Update TextView
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
                        address_string[0] = String.valueOf(addresses.get(0).getAddressLine(0));
                    } catch (IndexOutOfBoundsException e) {
                        address_string[0] = "NO ADDRESS LINE MATCHED";
                    }
                } else {
                    address_string[0] = "NO ADDRESS MATCHES WERE FOUND";
                }
                textView_homeLocation.setText(addresses.get(0).getSubLocality() + ", " + addresses.get(0).getLocality());
                Log.v("[ HomeFragment.java ]", lat);
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
