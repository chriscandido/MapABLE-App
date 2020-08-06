package up.envisage.mapable.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

import up.envisage.mapable.HomeFragment;
import up.envisage.mapable.R;
import up.envisage.mapable.fragment.GoogleMapFragment;
import up.envisage.mapable.ui.home.report.CameraActivity;
import up.envisage.mapable.ui.home.report.ReportIncidentActivity;

public class ReportActivity extends AppCompatActivity {

    MaterialButton button_reportIncident, button_reportCamera, button_reportLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        //Button report incident
        button_reportIncident = findViewById(R.id.button_report_typeOfIncident);
        button_reportIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent incident = new Intent(ReportActivity.this, ReportIncidentActivity.class);
                startActivity(incident);
            }
        });

        //Button report location
        button_reportLocation = findViewById(R.id.button_report_locationOfIncident);
        button_reportLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent location = new Intent(ReportActivity.this, GoogleMapFragment.class);
                startActivity(location);
            }
        });

        //Button take photo
        button_reportCamera = findViewById(R.id.button_report_takePhoto);
        button_reportCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera = new Intent(ReportActivity.this, CameraActivity.class);
                startActivity(camera);
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
        super.onBackPressed();
        Intent intent = new Intent(ReportActivity.this, HomeFragment.class);
        startActivity(intent);
    }
}
