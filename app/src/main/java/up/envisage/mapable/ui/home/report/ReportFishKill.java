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
    private TextInputLayout textInputLayout_reportFishKill_q00, textInputLayout_reportFishKill_q03;

    private String input00, input01, input02, input03, input04;

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

        textInputLayout_reportFishKill_q00 = findViewById(R.id.textInputLayout_reportFishKill);
        textInputLayout_reportFishKill_q03 = findViewById(R.id.textInputLayout_reportFishKill_q03);

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
        List<String> options03 = new ArrayList<>();
        options03.add("Oo");
        options03.add("Hindi");

        ArrayAdapter<String> adapter03 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options03);
        adapter03.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportFishKill_q04.setAdapter(adapter03);

        //Next Button
        button_reportFishKill_next.setOnClickListener(view -> {

            input00 = textInputLayout_reportFishKill_q00.getEditText().getText().toString();
            input03 = textInputLayout_reportFishKill_q03.getEditText().getText().toString();

            //Insert report to list
            out.add(input00);
            out.add(input01);
            out.add(input02);
            out.add(input03);
            out.add(input04);
            Gson gson = new Gson();
            String ans = gson.toJson(out);

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
        } else {
            input04 = adapterView.getItemAtPosition(i).toString();
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
