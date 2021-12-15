package up.envisage.mapable.ui.home.report;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import up.envisage.mapable.R;
import up.envisage.mapable.adapter.IncidentListAdapter;
import up.envisage.mapable.fragment.GoogleMapFragment;
import up.envisage.mapable.fragment.IncidentListFragment;

public class ReportAlgalBloom extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    MaterialButton button_reportAlgalBloom_ok;
    TextInputLayout textInputLayout_reportAlgalBloom_q00, textInputLayout_reportAlgalBloom_q04;
    ImageView imageView_algalbloom_back;

    String input00, input01, input02, input03, input04;
    String userID, dateTime, incidentType, Report, lon, lat, image, imageID2;

    private final List<String> out = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_algal_bloom);

        Intent prevIntent = getIntent(); // gets intent from reportingActivity

        userID = prevIntent.getStringExtra("userID");
        dateTime = prevIntent.getStringExtra("Date and Time");
        incidentType = prevIntent.getStringExtra("Incident Type");
        Report = prevIntent.getStringExtra("Report");
        lon = prevIntent.getStringExtra("Longitude");
        lat = prevIntent.getStringExtra("Latitude");
        image = prevIntent.getStringExtra("image");

        Spinner spinner_reportAlgalBloom_q01 = findViewById(R.id.spinner_reportAgalBloom_q01);
        Spinner spinner_reportAlgalBloom_q02 = findViewById(R.id.spinner_reportAgalBloom_q02);
        Spinner spinner_reportAlgalBloom_q03 = findViewById(R.id.spinner_reportAgalBloom_q03);

        textInputLayout_reportAlgalBloom_q04 = findViewById(R.id.textInputLayout_reportAlgalBloom_q04);

        button_reportAlgalBloom_ok = findViewById(R.id.button_reportAlgalBloom_ok);

        //Question 01 spinner element
        spinner_reportAlgalBloom_q01.setOnItemSelectedListener(this);
        List<String> options01 = new ArrayList<>();
        options01.add("Mababa sa isang linggo");
        options01.add("Isang linggo");
        options01.add("Higit sa isang linggo");

        ArrayAdapter<String> adapter01 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options01);
        adapter01.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportAlgalBloom_q01.setAdapter(adapter01);

        //Question 02 spinner element
        spinner_reportAlgalBloom_q02.setOnItemSelectedListener(this);
        List<String> options02 = new ArrayList<>();
        options02.add("Mayroon");
        options02.add("Wala");

        ArrayAdapter<String> adapter02 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options02);
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportAlgalBloom_q02.setAdapter(adapter02);

        //Question 03 spinner element
        spinner_reportAlgalBloom_q03.setOnItemSelectedListener(this);
        List<String> options03 = new ArrayList<>();
        options03.add("Oo");
        options03.add("Hindi");

        ArrayAdapter<String> adapter03 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options03);
        adapter03.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportAlgalBloom_q03.setAdapter(adapter03);

        //Ok Button
        button_reportAlgalBloom_ok.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {

                input04 = textInputLayout_reportAlgalBloom_q04.getEditText().getText().toString();

                String ans =  input01 + "|" +input02 + "|" +input03 + "|" +input04;

                Log.v("[ ReportAlgalBloom.java ]", "ANSWER: " + ans);

                Intent intent = new Intent(ReportAlgalBloom.this, GoogleMapFragment.class);
                intent.putExtra("userID", userID);
                intent.putExtra("Date and Time", dateTime());
                intent.putExtra("Incident Type", "Algal Bloom");
                intent.putExtra("Report", ans);
                intent.putExtra("Longitude", lon);
                intent.putExtra("Latitude", lat);
                intent.putExtra("image", image);
                startActivity(intent);
            }
        });

        imageView_algalbloom_back = findViewById(R.id.imageView_algalbloom_back);
        imageView_algalbloom_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //----------------------------------------------------------------------------------------------Get selected item from the spinners
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (adapterView.getId() == R.id.spinner_reportAgalBloom_q01) {
            input01 = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getId() == R.id.spinner_reportAgalBloom_q02) {
            input02 = adapterView.getItemAtPosition(i).toString();
        } else {
            input03 = adapterView.getItemAtPosition(i).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }

    //----------------------------------------------------------------------------------------------Get current date and time
    public String dateTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }
}