package up.envisage.mapable.ui.home.report;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import up.envisage.mapable.R;
import up.envisage.mapable.fragment.GoogleMapFragment;

public class ReportFishKill extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private MaterialButton button_reportFishKill_next;
    private TextInputLayout textInputLayout_reportFishKill_q00, textInputLayout_reportFishKill_q03, textInputLayout_reportFishKill_q05;

    private String input00, input01, input02, input03, input04, input05, input06;

    private List<String> out = new ArrayList<>();

    String userID, dateTime, incidentType, Report, lon, lat, image, imageID2;

    @SuppressLint("LongLogTag")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_fish_kill);

        Intent prevIntent = getIntent(); // gets intent from reportingActivity

        userID = prevIntent.getStringExtra("userID");
        dateTime = prevIntent.getStringExtra("Date and Time");
        incidentType = prevIntent.getStringExtra("Incident Type");
        Report = prevIntent.getStringExtra("Report");
        lon = prevIntent.getStringExtra("Longitude");
        lat = prevIntent.getStringExtra("Latitude");
        image = prevIntent.getStringExtra("image");

        Spinner spinner_reportFishKill_q01 = findViewById(R.id.spinner_reportFishKill_q01);
        Spinner spinner_reportFishKill_q02 = findViewById(R.id.spinner_reportFishKill_q02);
        Spinner spinner_reportFishKill_q04 = findViewById(R.id.spinner_reportFishKill_q04);
        Spinner spinner_reportFishKill_q06 = findViewById(R.id.spinner_reportFishKill_q06);

        textInputLayout_reportFishKill_q03 = findViewById(R.id.textInputLayout_reportFishKill_q03);
        textInputLayout_reportFishKill_q05 = findViewById(R.id.textInputLayout_reportFishKill_q05);

        button_reportFishKill_next = findViewById(R.id.button_reportFishKill_next);

        //Question 01 spinner element
        spinner_reportFishKill_q01.setOnItemSelectedListener(this);
        List<String> options01 = new ArrayList<>();
        options01.add("Mababa sa isang linggo");
        options01.add("Isang linggo");
        options01.add("Higit sa isang linggo");

        ArrayAdapter<String> adapter01 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options01);
        adapter01.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportFishKill_q01.setAdapter(adapter01);

        //Question 02 spinner element
        spinner_reportFishKill_q02.setOnItemSelectedListener(this);
        List<String> options02 = new ArrayList<>();
        options02.add("Mayroon");
        options02.add("Wala");

        ArrayAdapter<String> adapter02 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options02);
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportFishKill_q02.setAdapter(adapter02);

        //Question 04 spinner element
        spinner_reportFishKill_q04.setOnItemSelectedListener(this);
        List<String> options04 = new ArrayList<>();
        options04.add("Oo");
        options04.add("Hindi");

        ArrayAdapter<String> adapter04 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options04);
        adapter04.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportFishKill_q04.setAdapter(adapter04);

        //Question 06 spinner element
        spinner_reportFishKill_q06.setOnItemSelectedListener(this);
        List<String> options06 = new ArrayList<>();
        options06.add("Isa hanggang tatlong banyera");
        options06.add("Mahigit sa tatlong banyera");
        options06.add("Hindi masabi");

        ArrayAdapter<String> adapter06 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options06);
        adapter06.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportFishKill_q06.setAdapter(adapter06);

        //Next Button
        button_reportFishKill_next.setOnClickListener(view -> {

            input03 = textInputLayout_reportFishKill_q03.getEditText().getText().toString();
            input05 = textInputLayout_reportFishKill_q05.getEditText().getText().toString();

            //String answers
            String ans = input01 + "|" +input02 + "|" +input03 + "|" +input04 + "|" + input05 + "|" + input06;

            Log.v("[ ReportFishKill.java ]", "ANSWER: " + ans);

            Intent intent = new Intent(ReportFishKill.this, GoogleMapFragment.class);
            intent.putExtra("userID", userID);
            intent.putExtra("Date and Time", dateTime());
            intent.putExtra("Incident Type", "Fish Kill");
            intent.putExtra("Report", ans);
            intent.putExtra("Longitude", lon);
            intent.putExtra("Latitude", lat);
            intent.putExtra("image", image);
            startActivity(intent);
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {

        if (adapterView.getId() == R.id.spinner_reportFishKill_q01) {
            input01 = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getId() == R.id.spinner_reportFishKill_q02) {
            input02 = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getId() == R.id.spinner_reportFishKill_q04){
            input04 = adapterView.getItemAtPosition(i).toString();
        } else {
            input06 = adapterView.getItemAtPosition(i).toString();
        }

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
