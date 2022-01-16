package up.envisage.mapable.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;
import up.envisage.mapable.db.table.ReportTable;
import up.envisage.mapable.model.ReportViewModel;
import up.envisage.mapable.model.UserViewModel;
import up.envisage.mapable.ui.home.ReportIncidentActivity;
import up.envisage.mapable.ui.home.report.ReportClassResult;
import retrofitInterface.RetrofitInterface;

public class ReportFragment extends Fragment {

    // Fragment activity
    private FragmentActivity listener;

    // Server
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://ec2-54-91-89-105.compute-1.amazonaws.com/";

    // User and Report room database
    private ReportViewModel reportViewModel;
    private UserViewModel userViewModel;

    // Variables
    private String userID, dateTime, incidentType, Report, lon, lat, image, imageString, outUserId;
    public Boolean connection;
    private Dialog dialog;

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.listener = (FragmentActivity) context;
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState){
        return layoutInflater.inflate(R.layout.fragment_report, container, false);
    }

    @SuppressLint("LongLogTag")
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prominentDisclosure();

        //Initiating report class
        reportViewModel = ViewModelProviders.of(listener).get(ReportViewModel.class);
        userViewModel = ViewModelProviders.of(listener).get(UserViewModel.class);

        userViewModel.getLastUser().observe(listener, UserTable -> {
            outUserId = UserTable.getUniqueId();
            Log.v("[ReportingActivity.java userID from local]:", "UserID: " + outUserId);
        });

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        // Set desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // Add other interceptors …
        httpClient.callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(30, TimeUnit.SECONDS) // connect timeout
                .writeTimeout(30, TimeUnit.SECONDS) // write timeout
                .readTimeout(30, TimeUnit.SECONDS); // read timeout

        // Add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Intent reportAct = getActivity().getIntent();

        userID = outUserId;
        dateTime = reportAct.getStringExtra("Date and Time");
        incidentType = reportAct.getStringExtra("Incident Type");
        Report = reportAct.getStringExtra("Report");
        lon = reportAct.getStringExtra("Longitude");
        lat = reportAct.getStringExtra("Latitude");
        image = reportAct.getStringExtra("image");

        // Button take survey
        MaterialButton button_takeSurvey = view.findViewById(R.id.button_report_takeSurvey);
        button_takeSurvey.setOnClickListener(v -> {
            Intent survey = new Intent(listener, ReportIncidentActivity.class);
            survey.putExtra("userID", outUserId);
            startActivity(survey);
        });

        // Back to Main Menu text button
        TextView textView_reportBack = view.findViewById(R.id.textView_report_back);
        textView_reportBack.setOnClickListener(view1 -> {
            Intent back = new Intent(listener, MainActivity.class);
            startActivity(back);
        });

        // Send to server button
        MaterialButton button_reportSend = view.findViewById(R.id.button_report_send);
        if (dateTime == null) {
            button_reportSend.setVisibility(View.GONE);
        } else {
            button_reportSend.setVisibility(View.VISIBLE);
        }

        // Checks for internet connection
        connection = isNetworkAvailable();

        button_reportSend.setOnClickListener(view12 -> {

            // Insert report details to server
            Log.v("[ReportingActivity.java ]", "Image Path: " + image  + "\n");

            imageString = imageConvertToString(image);
            Log.v("[ ReportingActivity.java ]",
                    "IMAGE STRING: " + imageString);

            HashMap<String, String> map = new HashMap<>();
            map.put("userID", outUserId);
            map.put("date", dateTime);
            map.put("type", incidentType);
            map.put("report", Report);
            map.put("lon", lon);
            map.put("lat", lat);
            map.put("image", imageString); //imageString

            Log.v("[ ReportingActivity.java ]",
                    "DATE & TIME: " + dateTime + "\n" +
                            "USER ID: " + userID + "\n" +
                            "INCIDENT TYPE: " + incidentType + "\n" +
                            "REPORT: " + Report + "\n" +
                            "LATITUDE: " + lat + "\n" +
                            "LONGITUDE: " + lon + "\n" +
                            "IMAGE: " + imageString + "\n" ); //imageString

            if(isNetworkAvailable()){

                Call<ReportClassResult> call = retrofitInterface.executeReportSubmit(map);

                call.enqueue(new Callback<ReportClassResult>() {
                    @Override

                    public void onResponse(Call<ReportClassResult> call, Response<ReportClassResult> response) {
                        if (response.code() == 200) {
                            Toast.makeText(listener, "Report Sent Successfully",
                                    Toast.LENGTH_LONG).show();
                        } else if (response.code() == 400){
                            Toast.makeText(listener, "Error Sending Report",
                                    Toast.LENGTH_LONG).show();
                            ReportTable report = new ReportTable();
                            report.setUniqueId(outUserId);
                            report.setDateTime(dateTime);
                            report.setIncidentType(incidentType);
                            report.setReport(Report);
                            report.setLatitude(Double.parseDouble(lat));
                            report.setLongitude(Double.parseDouble(lon));
                            report.setPhoto(image);
                            reportViewModel.insert(report);
                        } else if (response.code() == 504){
                            Toast.makeText(listener, "Timeout",
                                    Toast.LENGTH_LONG).show();
                            ReportTable report = new ReportTable();
                            report.setUniqueId(outUserId);
                            report.setDateTime(dateTime);
                            report.setIncidentType(incidentType);
                            report.setReport(Report);
                            report.setLatitude(Double.parseDouble(lat));
                            report.setLongitude(Double.parseDouble(lon));
                            report.setPhoto(image);
                            reportViewModel.insert(report);
                        }
                    }

                    @Override
                    public void onFailure(Call<ReportClassResult> call, Throwable t) {
                        Toast.makeText(listener, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                        Log.v("OnFailure Error Message", t.getMessage());

                        // If onFailure, report is stored in the local database
                        ReportTable report = new ReportTable();
                        report.setUniqueId(outUserId);
                        report.setDateTime(dateTime);
                        report.setIncidentType(incidentType);
                        report.setReport(Report);
                        report.setLatitude(Double.parseDouble(lat));
                        report.setLongitude(Double.parseDouble(lon));
                        report.setPhoto(image);
                        reportViewModel.insert(report);
                    }
                });
            } else {
                // If no internet connection, report is stored in the local database
                ReportTable report = new ReportTable();
                report.setUniqueId(outUserId);
                report.setDateTime(dateTime);
                report.setIncidentType(incidentType);
                report.setReport(Report);
                report.setLatitude(Double.parseDouble(lat));
                report.setLongitude(Double.parseDouble(lon));
                report.setPhoto(image);
                reportViewModel.insert(report);

                errorNoConnection();
            }

            Intent goToMain = new Intent(listener, MainActivity.class);
            goToMain.putExtra("userID", userID);
            startActivity(goToMain);
        });
    }

    //----------------------------------------------------------------------------------------------Check network connectivity
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //----------------------------------------------------------------------------------------------Convert from bitmap to byte array
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    //----------------------------------------------------------------------------------------------Convert image to string
    public String imageConvertToString(String image) {
        Uri selectedImage = Uri.parse(image);
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();

        Bitmap galleryPhoto = BitmapFactory.decodeFile(filePath);

        // Get the base 64 string
        return Base64.encodeToString(getBytesFromBitmap(galleryPhoto),
                Base64.NO_WRAP);
    }

    //----------------------------------------------------------------------------------------------Popup for internet connection failure
    private void errorNoConnection(){
        dialog = new Dialog(listener);
        dialog.setContentView(R.layout.popup_error_nointernet);

        dialog.show();
    }

    //----------------------------------------------------------------------------------------------Popup for prominent disclosure
    private void prominentDisclosure(){
        dialog = new Dialog(listener);
        dialog.setContentView(R.layout.popup_disclosure_report);

        MaterialButton button_disclosure_close = dialog.findViewById(R.id.button_disclosure_close);
        button_disclosure_close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
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

}
