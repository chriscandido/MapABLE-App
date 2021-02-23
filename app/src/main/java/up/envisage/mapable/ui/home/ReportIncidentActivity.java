package up.envisage.mapable.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import up.envisage.mapable.R;
import up.envisage.mapable.adapter.ReportIncidentAdapter;
import up.envisage.mapable.ui.home.report.ReportAlgalBloom;
import up.envisage.mapable.ui.home.report.ReportFishKill;
import up.envisage.mapable.ui.home.report.ReportIllegalReclamation;
import up.envisage.mapable.ui.home.report.ReportPollution;
import up.envisage.mapable.ui.registration.RetrofitInterface;

public class ReportIncidentActivity extends AppCompatActivity implements ReportIncidentAdapter.OnIncidentClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private TextView textView_reportIncident_back;

    private Retrofit retrofit;
    private String BASE_URL = "http://ec2-54-91-89-105.compute-1.amazonaws.com/";
    private String userID, dateTime, incidentType, Report, lon, lat, image, imageID2;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);

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

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        Intent incident = getIntent(); // gets intent from reportingActivity

        userID = incident.getStringExtra("userID");
        dateTime = incident.getStringExtra("Date and Time");
        incidentType = incident.getStringExtra("Incident Type");
        Report = incident.getStringExtra("Report");
        lon = incident.getStringExtra("Longitude");
        lat = incident.getStringExtra("Latitude");
        image = incident.getStringExtra("image");

        recyclerView = findViewById(R.id.recyclerView_reportIncident);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ReportIncidentAdapter(getApplicationContext(), this);
        recyclerView.setAdapter(adapter);

        textView_reportIncident_back = findViewById(R.id.textView_reportIncident_back);
        textView_reportIncident_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(ReportIncidentActivity.this, ReportingActivity.class);
                startActivity(back);
            }
        });
    }

    //----------------------------------------------------------------------------------------------Incident Type Menu
    public void onClick(int position){
        Log.d("[ ReportIncident.java ]", "onIncidentClick: clicked - " + position);
        switch (position) {
            case 0:
                Intent algalBloomOk = new Intent(ReportIncidentActivity.this, ReportAlgalBloom.class);
                algalBloomOk.putExtra("userID", userID);
                algalBloomOk.putExtra("Date and time", dateTime);
                algalBloomOk.putExtra("Incident Type", incidentType);
                algalBloomOk.putExtra("Report", Report);
                algalBloomOk.putExtra("Longitude", lon);
                algalBloomOk.putExtra("Latitude", lat);
                algalBloomOk.putExtra("image", image);
                startActivity(algalBloomOk);
                break;
            case 1:
                Intent fishKillOk = new Intent(ReportIncidentActivity.this, ReportFishKill.class);
                fishKillOk.putExtra("userID", userID);
                fishKillOk.putExtra("Date and time", dateTime);
                fishKillOk.putExtra("Incident Type", incidentType);
                fishKillOk.putExtra("Report", Report);
                fishKillOk.putExtra("Longitude", lon);
                fishKillOk.putExtra("Latitude", lat);
                fishKillOk.putExtra("image", image);
                startActivity(fishKillOk);
                break;
            case 2:
                Intent pollutionOk = new Intent(ReportIncidentActivity.this, ReportPollution.class);
                pollutionOk.putExtra("userID", userID);
                pollutionOk.putExtra("Date and time", dateTime);
                pollutionOk.putExtra("Incident Type", incidentType);
                pollutionOk.putExtra("Report", Report);
                pollutionOk.putExtra("Longitude", lon);
                pollutionOk.putExtra("Latitude", lat);
                pollutionOk.putExtra("image", image);
                startActivity(pollutionOk);
                break;
            case 3:
                Intent illegalReclamationOk = new Intent(ReportIncidentActivity.this, ReportIllegalReclamation.class);
                illegalReclamationOk.putExtra("userID", userID);
                illegalReclamationOk.putExtra("Date and time", dateTime);
                illegalReclamationOk.putExtra("Incident Type", incidentType);
                illegalReclamationOk.putExtra("Report", Report);
                illegalReclamationOk.putExtra("Longitude", lon);
                illegalReclamationOk.putExtra("Latitude", lat);
                illegalReclamationOk.putExtra("image", image);
                startActivity(illegalReclamationOk);
                break;
        }
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
