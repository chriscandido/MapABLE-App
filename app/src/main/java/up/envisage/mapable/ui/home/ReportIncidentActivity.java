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
import up.envisage.mapable.ui.home.report.ReportResult;
import up.envisage.mapable.ui.registration.LoginActivity;
import up.envisage.mapable.ui.registration.LoginResult;
import up.envisage.mapable.ui.registration.RetrofitInterface;

public class ReportIncidentActivity extends AppCompatActivity implements ReportIncidentAdapter.OnIncidentClickListener, AdapterView.OnItemSelectedListener {

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

    String report, lon, lat, image;

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

        Intent incident = getIntent();

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
                        final String report = textInputLayout_reportAlgalBloom.getEditText().getText().toString().trim();

                        Intent algalBloomOk = new Intent(ReportIncidentActivity.this, ReportingActivity.class);
                        algalBloomOk.putExtra("report", report);
                        algalBloomOk.putExtra("Longitude", lon);
                        algalBloomOk.putExtra("Latitude", lat);
                        algalBloomOk.putExtra("image", image);
                        startActivity(algalBloomOk);

                        HashMap<String, String> map = new HashMap<>();
                        map.put("report", report);

                        Call<ReportResult> call = retrofitInterface.executeSubmit(map);

                        call.enqueue(new Callback<ReportResult>() {
                            @Override

                            public void onResponse(Call<ReportResult> call, Response<ReportResult> response) {
                                if (response.code() != 400) {
                                    Toast.makeText(ReportIncidentActivity.this, "Report Submitted Successfully",
                                            Toast.LENGTH_LONG).show();

                                } else if (response.code() == 400){
                                    Toast.makeText(ReportIncidentActivity.this, "Error Submitting",
                                            Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ReportResult> call, Throwable t) {
                                Toast.makeText(ReportIncidentActivity.this, t.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });


                        Toast.makeText(ReportIncidentActivity.this, "Data successfully saved", Toast.LENGTH_LONG).show();
                    }
                });

                dialog.show();

                break;
            case 1:
                dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_report_fishkill);

                textInputLayout_reportFishKill = dialog.findViewById(R.id.textInputLayout_reportFishKill);
                button_reportFishKill_ok = dialog.findViewById(R.id.button_reportFishKill_ok);

                button_reportFishKill_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent fishKillOk = new Intent(ReportIncidentActivity.this, ReportingActivity.class);
                        startActivity(fishKillOk);
                        Toast.makeText(ReportIncidentActivity.this, "Data successfully saved", Toast.LENGTH_LONG).show();
                    }
                });

                dialog.show();
                break;
            case 2:
                dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_report_pollution);

                textInputLayout_reportPollution = dialog.findViewById(R.id.textInputLayout_reportPollution);
                button_reportPollution_ok = dialog.findViewById(R.id.button_reportPollution_ok);

                button_reportPollution_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent pollutionOk = new Intent(ReportIncidentActivity.this, ReportingActivity.class);
                        startActivity(pollutionOk);
                        Toast.makeText(ReportIncidentActivity.this, "Data successfully saved", Toast.LENGTH_LONG).show();
                    }
                });

                dialog.show();
                break;
            case 3:
                break;
            case 4:
                dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_report_illegalreclamation);

                textInputLayout_reportIllegalReclamation = dialog.findViewById(R.id.textInputLayout_reportIllegalReclamation);
                button_reportIllegalReclamation_ok = dialog.findViewById(R.id.button_reportIllegalReclamation_ok);

                button_reportIllegalReclamation_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent illegalReclamationOk = new Intent(ReportIncidentActivity.this, ReportingActivity.class);
                        startActivity(illegalReclamationOk);
                        Toast.makeText(ReportIncidentActivity.this, "Data successfully sent", Toast.LENGTH_LONG).show();
                    }
                });

                dialog.show();
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String item = adapterView.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
