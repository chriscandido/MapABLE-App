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

    String report, lon, lat, image;

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

        Intent algalBloomOk = getIntent();
        Intent submitLocation = getIntent();
        Intent save = getIntent();

        if(algalBloomOk.getStringExtra("report") == null) {
            if(submitLocation.getStringExtra("Longitude") == null || submitLocation.getStringExtra("Latitude") == null) {
                if(save.getStringExtra("image") == null) {
                    report = null;
                    lon = null;
                    lat = null;
                    image = null;
                } else {
                    report = save.getStringExtra("report");
                    lon = save.getStringExtra("Longitude");
                    lat = save.getStringExtra("Latitude");
                    image = save.getStringExtra("image");
                }

            } else {
                report = submitLocation.getStringExtra("report");
                lon = submitLocation.getStringExtra("Longitude");
                lat = submitLocation.getStringExtra("Latitude");
                image = submitLocation.getStringExtra("image");
            }
        } else {
            report = algalBloomOk.getStringExtra("report");
            lon = algalBloomOk.getStringExtra("Longitude");
            lat = algalBloomOk.getStringExtra("Latitude");
            image = algalBloomOk.getStringExtra("image");
        }
        
        //Button report incident
        button_reportIncident = findViewById(R.id.button_report_typeOfIncident);
        button_reportIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent incident = new Intent(ReportingActivity.this, ReportIncidentActivity.class);
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
                location.putExtra("report", report);
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
                camera.putExtra("report", report);
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
                map.put("report", report);
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
