package up.envisage.mapable.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import up.envisage.mapable.ui.home.report.ReportClassResult;
import up.envisage.mapable.ui.home.report.ReportResult;
import up.envisage.mapable.ui.registration.RetrofitInterface;

public class ReportingActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:5000";

    private MaterialButton button_reportIncident, button_reportCamera, button_reportLocation, button_reportSend;
    private TextView textView_reportBack;

    String userID, type, incident, frequency, a1, a2, a3, a4, a5, a6, a7, lon, lat, image;

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
        Intent submitLocation = getIntent();
        Intent save = getIntent();
        Intent user = getIntent();

        userID = user.getStringExtra("userID");

        if(reportAct.getStringExtra("type") == null) {
            if(submitLocation.getStringExtra("Longitude") == null || submitLocation.getStringExtra("Latitude") == null) {
                if(save.getStringExtra("image") == null) {
                    type = null;
                    incident = null;
                    frequency = null;
                    a1 = null;
                    a2 = null;
                    a3 = null;
                    a4 = null;
                    a5 = null;
                    a6 = null;
                    a7 = null;
                    lon = "122.54032239317894";
                    lat = "12.65017682702677";
                    image = null;
                } else {
                    type = save.getStringExtra("type");
                    incident = save.getStringExtra("incident");
                    frequency = save.getStringExtra("frequency");
                    a1 = save.getStringExtra("a1");
                    a2 = save.getStringExtra("a2");
                    a3 = save.getStringExtra("a3");
                    a4 = save.getStringExtra("a4");
                    a5 = save.getStringExtra("a5");
                    a6 = save.getStringExtra("a6");
                    a7 = save.getStringExtra("a7");
                    lon = save.getStringExtra("Longitude");
                    lat = save.getStringExtra("Latitude");
                    image = save.getStringExtra("image");
                }

            } else {
                type = submitLocation.getStringExtra("type");
                incident = submitLocation.getStringExtra("incident");
                frequency = submitLocation.getStringExtra("frequency");
                a1 = submitLocation.getStringExtra("a1");
                a2 = submitLocation.getStringExtra("a2");
                a3 = submitLocation.getStringExtra("a3");
                a4 = submitLocation.getStringExtra("a4");
                a5 = submitLocation.getStringExtra("a5");
                a6 = submitLocation.getStringExtra("a6");
                a7 = submitLocation.getStringExtra("a7");
                lon = submitLocation.getStringExtra("Longitude");
                lat = submitLocation.getStringExtra("Latitude");
                image = submitLocation.getStringExtra("image");
            }
        } else {
            type = reportAct.getStringExtra("type");
            incident = reportAct.getStringExtra("incident");
            frequency = reportAct.getStringExtra("frequency");
            a1 = reportAct.getStringExtra("a1");
            a2 = reportAct.getStringExtra("a2");
            a3 = reportAct.getStringExtra("a3");
            a4 = reportAct.getStringExtra("a4");
            a5 = reportAct.getStringExtra("a5");
            a6 = reportAct.getStringExtra("a6");
            a7 = reportAct.getStringExtra("a7");
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
                location.putExtra("type", type);
                location.putExtra("incident", incident);
                location.putExtra("frequency", frequency);
                location.putExtra("a1", a1);
                location.putExtra("a2", a2);
                location.putExtra("a3", a3);
                location.putExtra("a4", a4);
                location.putExtra("a5", a5);
                location.putExtra("a6", a6);
                location.putExtra("a7", a7);
                location.putExtra("image", image);
                startActivity(location);
            }
        });

        //Button take photo
        button_reportCamera = findViewById(R.id.button_report_takePhoto);
        button_reportCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera = new Intent(ReportingActivity.this, CameraActivity.class);
                camera.putExtra("userID", userID);
                camera.putExtra("type", type);
                camera.putExtra("incident", incident);
                camera.putExtra("frequency", frequency);
                camera.putExtra("a1", a1);
                camera.putExtra("a2", a2);
                camera.putExtra("a3", a3);
                camera.putExtra("a4", a4);
                camera.putExtra("a5", a5);
                camera.putExtra("a6", a6);
                camera.putExtra("a7", a7);
                camera.putExtra("Longitude", lon);
                camera.putExtra("Latitude", lat);
                startActivity(camera);
            }
        });

        //Text Button back to Main Menu
        textView_reportBack = findViewById(R.id.textView_report_back);
        textView_reportBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(ReportingActivity.this, MainActivity.class);
                startActivity(back);
            }
        });

        //Send Button to DB
        button_reportSend = findViewById(R.id.button_report_send);
        button_reportSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> map = new HashMap<>();
                map.put("userID", userID);
                map.put("type", type);
                map.put("incident", incident);
                map.put("frequency", frequency);
                map.put("a1", a1);
                map.put("a2", a2);
                map.put("a3", a3);
                map.put("a4", a4);
                map.put("a5", a5);
                map.put("a6", a6);
                map.put("a7", a7);
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

       public void onBackPressed(){
    }
}
