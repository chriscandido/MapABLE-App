package up.envisage.mapable.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterViewFlipper;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;
import up.envisage.mapable.adapter.ReportIncidentAdapter;
import up.envisage.mapable.ui.home.report.ReportAlgalBloom;
import up.envisage.mapable.ui.home.report.ReportFishKill;
import up.envisage.mapable.ui.home.report.ReportIllegalReclamation;
import up.envisage.mapable.ui.home.report.ReportPollution;
import up.envisage.mapable.ui.home.report.ReportResult;
import up.envisage.mapable.ui.registration.LoginActivity;
import up.envisage.mapable.ui.registration.LoginResult;
import up.envisage.mapable.ui.registration.RetrofitInterface;

public class ReportIncidentActivity extends AppCompatActivity implements ReportIncidentAdapter.OnIncidentClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private TextInputLayout textInputLayout_reportAlgalBloom, textInputLayout_reportFishKill, textInputLayout_reportPollution,
                            textInputLayout_reportIllegalFishing, textInputLayout_reportIllegalReclamation;
    private MaterialButton button_reportAlgalBloom_ok, button_reportFishKill_ok, button_reportPollution_ok,
                            button_reportIllegalFishing_ok, button_reportIllegalReclamation_ok;
    private TextView textView_reportIncident_back;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:5000";
    //            "http://10.0.2.2:5000";
    //"https://project-mapable.herokuapp.com/"

    String userID, frequency, a1, a2, a3, a4, a5, a6, a7, lon, lat, image;

    Dialog dialog;

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

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Intent incident = getIntent(); // gets intent from reportingActivity

        userID = incident.getStringExtra("userID");
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
                /*
                dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_report_algalbloom);

                textInputLayout_reportAlgalBloom = dialog.findViewById(R.id.textInputLayout_reportAlgalBloom);
                button_reportAlgalBloom_ok = dialog.findViewById(R.id.button_reportAlgalBloom_ok);

                //Spinner element
                Spinner spinner_reportAlgalBloom_q01 = dialog.findViewById(R.id.spinner_reportAgalBloom_q01);
                spinner_reportAlgalBloom_q01.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                List<String> categories = new ArrayList<String>();
                categories.add("Mababa sa isang linggo");
                categories.add("Isang linggo");
                categories.add("Higit sa isang linggo");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spinner_reportAlgalBloom_q01.setAdapter(adapter);

                button_reportAlgalBloom_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String incident = textInputLayout_reportAlgalBloom.getEditText().getText().toString().trim();

                        Intent algalBloomOk = new Intent(ReportIncidentActivity.this, ReportingActivity.class);
                        algalBloomOk.putExtra("userID", userID);
                        algalBloomOk.putExtra("type", "Algal Bloom Incident");
                        algalBloomOk.putExtra("incident", incident);
                        algalBloomOk.putExtra("frequency", "Mababa sa isang linggo");
                        algalBloomOk.putExtra("a1", "answer to a1");
                        algalBloomOk.putExtra("a2", "answer to a2");
                        algalBloomOk.putExtra("a3", "answer to a3");
                        algalBloomOk.putExtra("a4", "answer to a4");
                        algalBloomOk.putExtra("a5", "answer to a5");
                        algalBloomOk.putExtra("a6", "answer to a6");
                        algalBloomOk.putExtra("a7", "answer to a7");
                        algalBloomOk.putExtra("Longitude", lon);
                        algalBloomOk.putExtra("Latitude", lat);
                        algalBloomOk.putExtra("image", image);
                        startActivity(algalBloomOk);

                        Toast.makeText(ReportIncidentActivity.this, "Data successfully saved", Toast.LENGTH_LONG).show();
                    }
                });

                dialog.show();*/

                Intent algalBloomOk = new Intent(ReportIncidentActivity.this, ReportAlgalBloom.class);
                startActivity(algalBloomOk);

                break;
            case 1:

                Intent fishKillOk = new Intent(ReportIncidentActivity.this, ReportFishKill.class);
                startActivity(fishKillOk);
                /*
                dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_report_fishkill);

                textInputLayout_reportFishKill = dialog.findViewById(R.id.textInputLayout_reportFishKill);
                button_reportFishKill_ok = dialog.findViewById(R.id.button_reportFishKill_ok);

                button_reportFishKill_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String incident = textInputLayout_reportFishKill.getEditText().getText().toString().trim();

                        Intent fishKillOk = new Intent(ReportIncidentActivity.this, ReportingActivity.class);
                        fishKillOk.putExtra("type", "Fish Kill");
                        fishKillOk.putExtra("incident", incident);
                        fishKillOk.putExtra("frequency", "Mababa sa isang linggo");
                        fishKillOk.putExtra("a1", "answer to a1");
                        fishKillOk.putExtra("a2", "answer to a2");
                        fishKillOk.putExtra("a3", "answer to a3");
                        fishKillOk.putExtra("a4", "answer to a4");
                        fishKillOk.putExtra("a5", "answer to a5");
                        fishKillOk.putExtra("a6", "answer to a6");
                        fishKillOk.putExtra("a7", "answer to a7");
                        fishKillOk.putExtra("Longitude", lon);
                        fishKillOk.putExtra("Latitude", lat);
                        fishKillOk.putExtra("image", image);
                        startActivity(fishKillOk);

                        Toast.makeText(ReportIncidentActivity.this, "Data successfully saved", Toast.LENGTH_LONG).show();
                    }
                });

                dialog.show();*/
                break;
            case 2:

                Intent pollutionOk = new Intent(ReportIncidentActivity.this, ReportPollution.class);
                startActivity(pollutionOk);
                /*
                dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_report_pollution);

                textInputLayout_reportPollution = dialog.findViewById(R.id.textInputLayout_reportPollution);
                button_reportPollution_ok = dialog.findViewById(R.id.button_reportPollution_ok);

                button_reportPollution_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String incident = textInputLayout_reportPollution.getEditText().getText().toString().trim();

                        Intent pollutionOk = new Intent(ReportIncidentActivity.this, ReportingActivity.class);
                        pollutionOk.putExtra("type", "Pollution");
                        pollutionOk.putExtra("incident", incident);
                        pollutionOk.putExtra("frequency", "Mababa sa isang linggo");
                        pollutionOk.putExtra("a1", "answer to a1");
                        pollutionOk.putExtra("a2", "answer to a2");
                        pollutionOk.putExtra("a3", "answer to a3");
                        pollutionOk.putExtra("a4", "answer to a4");
                        pollutionOk.putExtra("a5", "answer to a5");
                        pollutionOk.putExtra("a6", "answer to a6");
                        pollutionOk.putExtra("a7", "answer to a7");
                        pollutionOk.putExtra("Longitude", lon);
                        pollutionOk.putExtra("Latitude", lat);
                        pollutionOk.putExtra("image", image);
                        startActivity(pollutionOk);

                        Toast.makeText(ReportIncidentActivity.this, "Data successfully saved", Toast.LENGTH_LONG).show();
                    }
                });

                dialog.show();*/

                break;
            case 3:
                break;
            case 4:
                Intent illegalReclamationOk = new Intent(ReportIncidentActivity.this, ReportIllegalReclamation.class);
                startActivity(illegalReclamationOk);
                /*
                dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_report_illegalreclamation);

                textInputLayout_reportIllegalReclamation = dialog.findViewById(R.id.textInputLayout_reportIllegalReclamation);
                button_reportIllegalReclamation_ok = dialog.findViewById(R.id.button_reportIllegalReclamation_ok);

                button_reportIllegalReclamation_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String incident = textInputLayout_reportIllegalReclamation.getEditText().getText().toString().trim();

                        Intent illegalReclamationOk = new Intent(ReportIncidentActivity.this, ReportingActivity.class);
                        illegalReclamationOk.putExtra("type", "Illegal Reclamation");
                        illegalReclamationOk.putExtra("incident", incident);
                        illegalReclamationOk.putExtra("frequency", "Mababa sa isang linggo");
                        illegalReclamationOk.putExtra("a1", "answer to a1");
                        illegalReclamationOk.putExtra("a2", "answer to a2");
                        illegalReclamationOk.putExtra("a3", "answer to a3");
                        illegalReclamationOk.putExtra("a4", "answer to a4");
                        illegalReclamationOk.putExtra("a5", "answer to a5");
                        illegalReclamationOk.putExtra("a6", "answer to a6");
                        illegalReclamationOk.putExtra("a7", "answer to a7");
                        illegalReclamationOk.putExtra("Longitude", lon);
                        illegalReclamationOk.putExtra("Latitude", lat);
                        illegalReclamationOk.putExtra("image", image);
                        startActivity(illegalReclamationOk);

                        Toast.makeText(ReportIncidentActivity.this, "Data successfully sent", Toast.LENGTH_LONG).show();
                    }
                });

                dialog.show();*/
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
