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

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import up.envisage.mapable.R;
import up.envisage.mapable.fragment.GoogleMapFragment;

public class ReportPollution extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextInputLayout textInputLayout_reportPollution_q00, textInputLayout_reportPollution_q05, textInputLayout_reportPollution_q07;

    private String input00, input01, input02, input03, input04, input05, input06, input07;

    private List<String> out = new ArrayList<>();

    String userID, dateTime, incidentType, Report, lon, lat, image, imageID2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_pollution);

        Intent prevIntent = getIntent(); // gets intent from reportingActivity

        userID = prevIntent.getStringExtra("userID");
        dateTime = prevIntent.getStringExtra("Date and Time");
        incidentType = prevIntent.getStringExtra("Incident Type");
        Report = prevIntent.getStringExtra("Report");
        lon = prevIntent.getStringExtra("Longitude");
        lat = prevIntent.getStringExtra("Latitude");
        image = prevIntent.getStringExtra("image");

        Spinner spinner_reportPollution_q01 = findViewById(R.id.spinner_reportPollution_q01);
        Spinner spinner_reportPollution_q02 = findViewById(R.id.spinner_reportPollution_q02);
        Spinner spinner_reportPollution_q03 = findViewById(R.id.spinner_reportPollution_q03);
        Spinner spinner_reportPollution_q04 = findViewById(R.id.spinner_reportPollution_q04);
        Spinner spinner_reportPollution_q06 = findViewById(R.id.spinner_reportPollution_q06);

        textInputLayout_reportPollution_q00 = findViewById(R.id.textInputLayout_reportPollution);
        textInputLayout_reportPollution_q05 = findViewById(R.id.textInputLayout_reportPollution_q05);
        textInputLayout_reportPollution_q07 = findViewById(R.id.textInputLayout_reportPollution_q07);

        MaterialButton button_reportPollution = findViewById(R.id.button_reportPollution_next);

        //Question01 spinner element
        spinner_reportPollution_q01.setOnItemSelectedListener(this);
        List<String> options01 = new ArrayList<>();
        options01.add("Estero");
        options01.add("Ilog");
        options01.add("Manila Bay");
        options01.add("Laguna de Bay");

        ArrayAdapter<String> adapter01 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options01);
        adapter01.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportPollution_q01.setAdapter(adapter01);

        //Question02 spinner element
        spinner_reportPollution_q02.setOnItemSelectedListener(this);
        List<String> options02 = new ArrayList<>();
        options02.add("Ilog");
        options02.add("Basura mula sa kabahayan o ibang pasilidad");
        options02.add("Maraming tubig o kemikal mula sa pabrika");
        options02.add("Dumi mula sa hayop (mula sa manukan o babuyan)");
        options02.add("Iba pang polusyon");

        ArrayAdapter<String> adapter02 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options02);
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportPollution_q02.setAdapter(adapter02);

        //Question03 spinner element
        spinner_reportPollution_q03.setOnItemSelectedListener(this);
        List<String> options03 = new ArrayList<>();
        options03.add("Mababa sa isang linggo");
        options03.add("Isang linggo");
        options03.add("Higit sa isang linggo");

        ArrayAdapter<String> adapter03 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options03);
        adapter03.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportPollution_q03.setAdapter(adapter03);

        //Question04 spinner element
        spinner_reportPollution_q04.setOnItemSelectedListener(this);
        List<String> options04 = new ArrayList<>();
        options04.add("Mayroon");
        options04.add("Wala");

        ArrayAdapter<String> adapter04 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options04);
        adapter04.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportPollution_q04.setAdapter(adapter04);

        //Question06 spinner element
        spinner_reportPollution_q06.setOnItemSelectedListener(this);
        List<String> options06 = new ArrayList<>();
        options06.add("Oo");
        options06.add("Hindi");

        ArrayAdapter<String> adapter06 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options06);
        adapter06.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportPollution_q06.setAdapter(adapter06);

        button_reportPollution.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {

                input00 = textInputLayout_reportPollution_q00.getEditText().getText().toString();
                input05 = textInputLayout_reportPollution_q05.getEditText().getText().toString();
                input07 = textInputLayout_reportPollution_q07.getEditText().getText().toString();

                String ans = input00 + "|" + input01 + "|" + input02 + "|" + input03 + "|" + input04+ "|" + input05+ "|" + input06 + "|" + input07;

                Log.v("[ ReportPollution.java ]", "ANSWER: " + ans);

                Intent intent = new Intent(ReportPollution.this, GoogleMapFragment.class);
                intent.putExtra("userID", userID);
                intent.putExtra("Date and Time", dateTime());
                intent.putExtra("Incident Type", "Pollution");
                intent.putExtra("Report", ans);
                intent.putExtra("Longitude", lon);
                intent.putExtra("Latitude", lat);
                intent.putExtra("image", image);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {

        if (adapterView.getId() == R.id.spinner_reportPollution_q01) {
            input01 = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getId() == R.id.spinner_reportPollution_q02) {
            input02 = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getId() == R.id.spinner_reportPollution_q03) {
            input03 = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getId() == R.id.spinner_reportPollution_q04) {
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
