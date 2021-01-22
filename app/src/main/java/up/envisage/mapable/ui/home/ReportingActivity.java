package up.envisage.mapable.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;
import up.envisage.mapable.model.ReportViewModel;
import up.envisage.mapable.ui.home.report.ReportClassResult;
import up.envisage.mapable.ui.registration.RetrofitInterface;

public class ReportingActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://ec2-54-91-89-105.compute-1.amazonaws.com/";

    private MaterialButton button_reportIncident, button_reportCamera, button_reportLocation, button_takeSurvey, button_reportSend;
    private TextView textView_reportBack;

    String userID, dateTime, incidentType, Report, lon, lat, image, imageID2, outPhoto, imageString;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        //Set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        //Add your other interceptors …

        //Add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Intent reportAct = getIntent();

        userID = reportAct.getStringExtra("userID");
        dateTime = reportAct.getStringExtra("Date and Time");
        incidentType = reportAct.getStringExtra("Incident Type");
        Report = reportAct.getStringExtra("Report");
        lon = reportAct.getStringExtra("Longitude");
        lat = reportAct.getStringExtra("Latitude");
        image = reportAct.getStringExtra("image");

        Boolean connection = isNetworkAvailable();
        Log.v("Internet Connection:", connection.toString());

        //Button take survey
        button_takeSurvey = findViewById(R.id.button_report_takeSurvey);
        button_takeSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent survey = new Intent(ReportingActivity.this, ReportIncidentActivity.class);
                survey.putExtra("userID", userID);
                startActivity(survey);
            }
        });

        //back to Main Menu Text Button
        textView_reportBack = findViewById(R.id.textView_report_back);
        textView_reportBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(ReportingActivity.this, MainActivity.class);
                startActivity(back);
            }
        });

        //Send to server Button
        button_reportSend = findViewById(R.id.button_report_send);
        if (dateTime == null) {
            button_reportSend.setVisibility(View.GONE);
        } else {
            button_reportSend.setVisibility(View.VISIBLE);
        }
        button_reportSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Insert report details to server
                Log.v("[ReportingActivity.java ]", "Image Path: " + image  + "\n");

                imageString = imageConvertToString(image);
                Log.v("[ ReportingActivity.java ]",
                        "IMAGE STRING: " + imageString);

                HashMap<String, String> map = new HashMap<>();
                map.put("userID", userID);
                map.put("date", dateTime);
                map.put("type", incidentType);
                map.put("report", Report);
                map.put("lon", lon);
                map.put("lat", lat);
                map.put("image", imageString);

                Log.v("[ ReportingActivity.java ]",
                        "DATE & TIME: " + dateTime + "\n" +
                                "USER ID: " + userID + "\n" +
                                "INCIDENT TYPE: " + incidentType + "\n" +
                                "REPORT: " + Report + "\n" +
                                "LATITUDE: " + lat + "\n" +
                                "LONGITUDE: " + lon + "\n" +
                                "IMAGE: " + imageString + "\n" );

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
                        }
                    }

                    @Override
                    public void onFailure(Call<ReportClassResult> call, Throwable t) {
                        Toast.makeText(ReportingActivity.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                        Log.v("Onfailure Error Message", t.getMessage());
                    }
                });

                ReportViewModel reportViewModel = ViewModelProviders.of(ReportingActivity.this).get(ReportViewModel.class);
                reportViewModel.getLastReport().observe(ReportingActivity.this, reportTable -> {;
                    int outId = reportTable.getReportId();
                    String outDateTime = reportTable.getDateTime();
                    String outIncidentType = reportTable.getIncidentType();
                    String outReport = reportTable.getReport();
                    Double outLatitude = reportTable.getLatitude();
                    Double outLongitude = reportTable.getLongitude();
                    outPhoto = reportTable.getPhoto();
                    Log.v("[ ReportingActivity.java Result ]",
                            "DATE & TIME: " + outDateTime + "\n" +
                                    "USER ID: " + userID + "\n" +
                            "INCIDENT TYPE: " + outIncidentType + "\n" +
                            "REPORT: " + outReport + "\n" +
                            "LATITUDE: " + outLatitude + "\n" +
                            "LONGITUDE: " + outLongitude + "\n");
                });

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

    public void onStart(){
        super.onStart();
    }

    public void onResume(){
        super.onResume();
    }

    public void onPause(){
        super.onPause();
    }

    public void onBackPressed(){ }
}
