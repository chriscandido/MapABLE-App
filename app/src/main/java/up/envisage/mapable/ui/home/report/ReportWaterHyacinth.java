package up.envisage.mapable.ui.home.report;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import up.envisage.mapable.R;
import up.envisage.mapable.fragment.GoogleMapFragment;

public class ReportWaterHyacinth extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    MaterialButton button_reportWaterHyacinth_ok;
    TextInputLayout textInputLayout_reportWaterHyacinth_q00;

    String input00, input01, input02, input03, input04, input05, input06;
    String userID, dateTime, incidentType, Report, lon, lat, image, imageID2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_hyacinth);

        Intent prevIntent = getIntent();

        userID = prevIntent.getStringExtra("userID");
        dateTime = prevIntent.getStringExtra("Date and Time");
        incidentType = prevIntent.getStringExtra("Incident Type");
        Report = prevIntent.getStringExtra("Report");
        lon = prevIntent.getStringExtra("Longitude");
        lat = prevIntent.getStringExtra("Latitude");
        image = prevIntent.getStringExtra("image");

        Spinner spinner_reportWaterHyacinth_q01 = findViewById(R.id.spinner_reportWaterHyacinth_q01);
        Spinner spinner_reportWaterHyacinth_q02 = findViewById(R.id.spinner_reportWaterHyacinth_q02);
        Spinner spinner_reportWaterHyacinth_q03 = findViewById(R.id.spinner_reportWaterHyacinth_q03);
        Spinner spinner_reportWaterHyacinth_q04 = findViewById(R.id.spinner_reportWaterHyacinth_q04);
        Spinner spinner_reportWaterHyacinth_q05 = findViewById(R.id.spinner_reportWaterHyacinth_q05);
        Spinner spinner_reportWaterHyacinth_q06 = findViewById(R.id.spinner_reportWaterHyacinth_q06);

        textInputLayout_reportWaterHyacinth_q00 = findViewById(R.id.textInputLayout_reportWaterHyacinth);

        button_reportWaterHyacinth_ok = findViewById(R.id.button_reportWaterHyacinth_next);

        //Question 01 spinner element
        spinner_reportWaterHyacinth_q01.setOnItemSelectedListener(this);
        List<String> options01 = new ArrayList<>();
        options01.add("Mababa sa isang linggo");
        options01.add("Isang linggo");
        options01.add("Higit sa isang linggo");

        ArrayAdapter<String> adapter01 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options01);
        adapter01.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportWaterHyacinth_q01.setAdapter(adapter01);

        //Question 02 spinner element
        spinner_reportWaterHyacinth_q02.setOnItemSelectedListener(this);
        List<String> options02 = new ArrayList<>();
        options02.add("Kalat-kalat");
        options02.add("Dikit-dikit");

        ArrayAdapter<String> adapter02 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options02);
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportWaterHyacinth_q02.setAdapter(adapter02);

        //Question 03 spinner element
        spinner_reportWaterHyacinth_q03.setOnItemSelectedListener(this);
        List<String> options03 = new ArrayList<>();
        options03.add("Mas maliit sa isang basketball court");
        options03.add("Kasukat ng isang basketball court");
        options03.add("Mas malaki sa isang basketball court");

        ArrayAdapter<String> adapter03 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options03);
        adapter03.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportWaterHyacinth_q03.setAdapter(adapter03);

        //Question 04 spinner element
        spinner_reportWaterHyacinth_q04.setOnItemSelectedListener(this);
        List<String> options04 = new ArrayList<>();
        options04.add("Oo");
        options04.add("Hindi");

        ArrayAdapter<String> adapter04 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options04);
        adapter04.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportWaterHyacinth_q04.setAdapter(adapter04);

        //Question 04 spinner element
        spinner_reportWaterHyacinth_q05.setOnItemSelectedListener(this);
        List<String> options05 = new ArrayList<>();
        options05.add("Oo");
        options05.add("Hindi");

        ArrayAdapter<String> adapter05 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options05);
        adapter05.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportWaterHyacinth_q05.setAdapter(adapter05);

        //Question 04 spinner element
        spinner_reportWaterHyacinth_q06.setOnItemSelectedListener(this);
        List<String> options06 = new ArrayList<>();
        options06.add("Oo");
        options06.add("Hindi");

        ArrayAdapter<String> adapter06 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options06);
        adapter06.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportWaterHyacinth_q05.setAdapter(adapter06);

        button_reportWaterHyacinth_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input00 = textInputLayout_reportWaterHyacinth_q00.getEditText().getText().toString();
                Intent intent = new Intent(ReportWaterHyacinth.this, GoogleMapFragment.class);
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
}
