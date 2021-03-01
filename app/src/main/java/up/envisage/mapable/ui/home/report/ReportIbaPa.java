package up.envisage.mapable.ui.home.report;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import up.envisage.mapable.R;
import up.envisage.mapable.fragment.GoogleMapFragment;

public class ReportIbaPa extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String userID, dateTime, incidentType, Report, lon, lat, image, imageID2;
    private String input00, input01, input02;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_iba_pa);

        Intent prevIntent = getIntent(); // gets intent from reportingActivity

        userID = prevIntent.getStringExtra("userID");
        dateTime = prevIntent.getStringExtra("Date and Time");
        incidentType = prevIntent.getStringExtra("Incident Type");
        Report = prevIntent.getStringExtra("Report");
        lon = prevIntent.getStringExtra("Longitude");
        lat = prevIntent.getStringExtra("Latitude");
        image = prevIntent.getStringExtra("image");

        TextInputLayout textInputLayout_reportIbaPa_q00 = findViewById(R.id.textInputLayout_reportIbaPa_q00);
        TextInputLayout textInputLayout_reportIbaPa_q01 = findViewById(R.id.textInputLayout_reportIbaPa_q01);
        TextInputLayout textInputLayout_reportIbaPa_q02 = findViewById(R.id.textInputLayout_reportIbaPa_q02);

        MaterialButton button_reportIbaPa_next = findViewById(R.id.button_reportIbaPa_next);
        button_reportIbaPa_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                input00 = textInputLayout_reportIbaPa_q00.getEditText().getText().toString();
                input01 = textInputLayout_reportIbaPa_q01.getEditText().getText().toString();
                input02 = textInputLayout_reportIbaPa_q02.getEditText().getText().toString();

                String ans = input00 + "|" + input01 + "|" +input02;

                Log.v("[ ReportIbaPa.java ]", "ANSWER: " + ans);

                Intent intent = new Intent(ReportIbaPa.this, GoogleMapFragment.class);
                intent.putExtra("userID", userID);
                intent.putExtra("Date and Time", dateTime());
                intent.putExtra("Incident Type", "Iba Pa");
                intent.putExtra("Report", ans);
                intent.putExtra("Longitude", lon);
                intent.putExtra("Latitude", lat);
                intent.putExtra("image", image);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //----------------------------------------------------------------------------------------------Get current date and time
    public String dateTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }
}
