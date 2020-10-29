package up.envisage.mapable.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

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
import up.envisage.mapable.fragment.GoogleMapFragment;
import up.envisage.mapable.model.ReportViewModel;
import up.envisage.mapable.ui.home.report.ReportClassResult;
import up.envisage.mapable.ui.registration.RetrofitInterface;

public class ReportingActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:5000";
    //    private String BASE_URL = "https://project-mapable.herokuapp.com/";

    private MaterialButton button_reportIncident, button_reportCamera, button_reportLocation, button_reportSend;
    private TextView textView_reportBack;

    String userID, dateTime, incidentType, Report, frequency, lon, lat, image;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …

// add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Intent reportAct = getIntent();

        userID = reportAct.getStringExtra("userID");

        if(reportAct.getStringExtra("Incident Type") == null) {
            if(reportAct.getStringExtra("Longitude") == null || reportAct.getStringExtra("Latitude") == null) {
                if(reportAct.getStringExtra("image") == null) {
                    dateTime = null;
                    incidentType = null;
                    Report = null;
                    lon = "122.54032239317894"; //add some default latitude in Manila Bay
                    lat = "12.65017682702677";
                    image = null;
                } else {
                    dateTime = reportAct.getStringExtra("Date and Time");
                    incidentType = reportAct.getStringExtra("Incident Type");
                    Report = reportAct.getStringExtra("Report");
                    lon = reportAct.getStringExtra("Longitude");
                    lat = reportAct.getStringExtra("Latitude");
                    image = reportAct.getStringExtra("image");
                }

            } else {
                dateTime = reportAct.getStringExtra("Date and Time");
                incidentType = reportAct.getStringExtra("Incident Type");
                Report = reportAct.getStringExtra("Report");
                lon = reportAct.getStringExtra("Longitude");
                lat = reportAct.getStringExtra("Latitude");
                image = reportAct.getStringExtra("image");
            }
        } else {
            dateTime = reportAct.getStringExtra("Date and Time");
            incidentType = reportAct.getStringExtra("Incident Type");
            Report = reportAct.getStringExtra("Report");
            lon = reportAct.getStringExtra("Longitude");
            lat = reportAct.getStringExtra("Latitude");
            image = reportAct.getStringExtra("image");

        }
        
        //Button report incident
        button_reportIncident = findViewById(R.id.button_report_typeOfIncident);
        button_reportIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent incident = new Intent(ReportingActivity.this, ReportIncidentActivity.class);
                incident.putExtra("userID", userID);
                incident.putExtra("Date and Time", dateTime);
                incident.putExtra("Incident Type", incidentType);
                incident.putExtra("Report", Report);
                incident.putExtra("Longitude", lon);
                incident.putExtra("Latitude", lat);
                incident.putExtra("image", image);
                startActivity(incident);
            }
        });

        //Button report location
        button_reportLocation = findViewById(R.id.button_report_locationOfIncident);
        button_reportLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent location = new Intent(ReportingActivity.this, GoogleMapFragment.class);
                location.putExtra("userID", userID);
                location.putExtra("Date and Time", dateTime);
                location.putExtra("Incident Type", incidentType);
                location.putExtra("Report", Report);
                location.putExtra("Longitude", lon);
                location.putExtra("Latitude", lat);
                location.putExtra("image", image);
                startActivity(location);
            }
        });

        //Take photo Button
        button_reportCamera = findViewById(R.id.button_report_takePhoto);
        button_reportCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera = new Intent(ReportingActivity.this, CameraActivity.class);
                camera.putExtra("userID", userID);
                camera.putExtra("Date and Time", dateTime);
                camera.putExtra("Incident Type", incidentType);
                camera.putExtra("Report", Report);
                camera.putExtra("Longitude", lon);
                camera.putExtra("Latitude", lat);
                camera.putExtra("image", image);
                startActivity(camera);
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
        button_reportSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Insert report details to server
                ReportViewModel reportViewModel = ViewModelProviders.of(ReportingActivity.this).get(ReportViewModel.class);
                reportViewModel.getLastReport().observe(ReportingActivity.this, reportTable -> {;
                    int outId = reportTable.getReportId();
                    String outDateTime = reportTable.getDateTime();
                    String outIncidentType = reportTable.getIncidentType();
                    String outReport = reportTable.getReport();
                    Double outLatitude = reportTable.getLatitude();
                    Double outLongitude = reportTable.getLongitude();
                    String outPhoto = reportTable.getPhoto();
                    Log.v("[ ReportingActivity.java ]",
                            "DATE & TIME: " + outDateTime + "\n" +
                            "INCIDENT TYPE: " + outIncidentType + "\n" +
                            "REPORT: " + outReport + "\n" +
                            "LATITUDE: " + outLatitude + "\n" +
                            "LONGITUDE: " + outLongitude + "\n");
                    Intent goToMain = new Intent(ReportingActivity.this, MainActivity.class);
                    startActivity(goToMain);
                });


                HashMap<String, String> map = new HashMap<>();
                map.put("userID", userID);
                map.put("date", dateTime);
                map.put("type", incidentType);
                map.put("report", Report);
                map.put("lon", lon);
                map.put("lat", lat);
                map.put("image", image);

                Call<ReportClassResult> call = retrofitInterface.executeReportSubmit(map);

                call.enqueue(new Callback<ReportClassResult>() {
                    @Override

                    public void onResponse(Call<ReportClassResult> call, Response<ReportClassResult> response) {
                        if (response.code() != 400) {
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
                    }
                });
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

    public void onBackPressed(){ }
}
