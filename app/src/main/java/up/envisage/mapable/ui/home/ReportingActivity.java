package up.envisage.mapable.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import up.envisage.mapable.ui.home.report.ReportClassResult;
import retrofitInterface.RetrofitInterface;

public class ReportingActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://ec2-54-91-89-105.compute-1.amazonaws.com/";
//    private String BASE_URL = "http://10.0.2.2:5000/";

    private ReportViewModel reportViewModel;
    private UserViewModel userViewModel;

    String userID, dateTime, incidentType, Report, lon, lat, image, imageID2, outPhoto, imageString, outUserId;

    public Boolean connection;

    private Dialog dialog;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        //Initiating report class
        reportViewModel = ViewModelProviders.of(ReportingActivity.this).get(ReportViewModel.class);
        userViewModel = ViewModelProviders.of(ReportingActivity.this).get(UserViewModel.class);

        userViewModel.getLastUser().observe(ReportingActivity.this, UserTable -> {
            outUserId = UserTable.getUniqueId();
            Log.v("[ReportingActivity.java userID from local]:", "UserID: " + outUserId);
        });

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        //Set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        //Add your other interceptors …
        httpClient.callTimeout(2,TimeUnit.MINUTES)
                .connectTimeout(60, TimeUnit.SECONDS) // connect timeout
                .writeTimeout(60, TimeUnit.SECONDS) // write timeout
                .readTimeout(60, TimeUnit.SECONDS); // read timeout

        //Add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Intent reportAct = getIntent();

        userID = outUserId;
        dateTime = reportAct.getStringExtra("Date and Time");
        incidentType = reportAct.getStringExtra("Incident Type");
        Report = reportAct.getStringExtra("Report");
        lon = reportAct.getStringExtra("Longitude");
        lat = reportAct.getStringExtra("Latitude");
        image = reportAct.getStringExtra("image");

       //Button take survey
        MaterialButton button_takeSurvey = findViewById(R.id.button_report_takeSurvey);
        button_takeSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(ReportingActivity.this);
                dialog.setContentView(R.layout.popup_disclosure_report);

                MaterialButton button_disclosure_close = dialog.findViewById(R.id.button_disclosure_close);
                button_disclosure_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent survey = new Intent(ReportingActivity.this, ReportIncidentActivity.class);
                        survey.putExtra("userID", outUserId);
                        startActivity(survey);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        //back to Main Menu Text Button
        TextView textView_reportBack = findViewById(R.id.textView_report_back);
        textView_reportBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(ReportingActivity.this, MainActivity.class);
                startActivity(back);
            }
        });

        //Send to server Button
        MaterialButton button_reportSend = findViewById(R.id.button_report_send);
        if (dateTime == null) {
            button_reportSend.setVisibility(View.GONE);
        } else {
            button_reportSend.setVisibility(View.VISIBLE);
        }

        //checks for internet connection
        connection = isNetworkAvailable();

        button_reportSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Insert report details to server
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
                                Toast.makeText(ReportingActivity.this, "Report Sent Successfully",
                                        Toast.LENGTH_LONG).show();
                            } else if (response.code() == 400){
                                Toast.makeText(ReportingActivity.this, "Error Sending Report",
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
                                Toast.makeText(ReportingActivity.this, "Timeout",
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
                            Toast.makeText(ReportingActivity.this, t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            Log.v("OnFailure Error Message", t.getMessage());

                            //if onFailure, report is stored in the local database
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
                    //if no internet connection, report is stored in the local database
                    //Toast.makeText(ReportingActivity.this, "No Internet Connection. Report will be saved in the device!",
                    //Toast.LENGTH_LONG).show();
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

                Intent goToMain = new Intent(ReportingActivity.this, MainActivity.class);
                goToMain.putExtra("userID", userID);
                startActivity(goToMain);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //----------------------------------------------------------------------------------------------convert from bitmap to byte array
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    //----------------------------------------------------------------------------------------------convert image to string
    public String imageConvertToString(String image) {
        Uri selectedImage = Uri.parse(image);
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();

        Bitmap galleryPhoto = BitmapFactory.decodeFile(filePath);

        //Get the base 64 string
        String img = Base64.encodeToString(getBytesFromBitmap(galleryPhoto),
                Base64.NO_WRAP);
        return img;
    }

    //----------------------------------------------------------------------------------------------Popup for successful data sending
    private void successDataSending(){
        dialog = new Dialog(ReportingActivity.this);
        dialog.setContentView(R.layout.popup_success_datasent);

        dialog.show();
    }

    //----------------------------------------------------------------------------------------------Popup for internet connection failure
    private void errorNoConnection(){
        dialog = new Dialog(ReportingActivity.this);
        dialog.setContentView(R.layout.popup_error_nointernet);

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

    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(ReportingActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
