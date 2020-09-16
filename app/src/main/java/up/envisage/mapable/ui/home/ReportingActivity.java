package up.envisage.mapable.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;
import up.envisage.mapable.fragment.GoogleMapFragment;

public class ReportingActivity extends AppCompatActivity {

    private MaterialButton button_reportIncident, button_reportCamera, button_reportLocation;
    private TextView textView_reportBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        //Button report incident
        button_reportIncident = findViewById(R.id.button_report_typeOfIncident);
        button_reportIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent incident = new Intent(ReportingActivity.this, ReportIncidentActivity.class);
                startActivity(incident);
            }
        });

        //Button report location
        button_reportLocation = findViewById(R.id.button_report_locationOfIncident);
        button_reportLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent location = new Intent(ReportingActivity.this, GoogleMapFragment.class);
                startActivity(location);
            }
        });

        //Button take photo
        button_reportCamera = findViewById(R.id.button_report_takePhoto);
        button_reportCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera = new Intent(ReportingActivity.this, CameraActivity.class);
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
