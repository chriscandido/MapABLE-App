package up.envisage.mapable.ui.home.report;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import up.envisage.mapable.R;
import up.envisage.mapable.fragment.GoogleMapFragment;

public class ReportSolidWaste extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    MaterialButton button_reportSolidWaste_next;
    private TextInputLayout textInputLayout_reportSolidWaste_q00;
    private ImageView imageView_solidwaste_back;
    private String input00, input01, input02, input03, input04, input05, input06, input07, input08;
    private String userID, dateTime, incidentType, Report, lon, lat, image, imageID2;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_report_solidwaste);

        Intent prevIntent = getIntent();

        userID = prevIntent.getStringExtra("userID");
        dateTime = prevIntent.getStringExtra("Date and Time");
        incidentType = prevIntent.getStringExtra("Incident Type");
        Report = prevIntent.getStringExtra("Report");
        lon = prevIntent.getStringExtra("Longitude");
        lat = prevIntent.getStringExtra("Latitude");
        image = prevIntent.getStringExtra("image");

        Spinner spinner_reportSolidWaste_q01 = findViewById(R.id.spinner_reportSolidWaste_q01);
        Spinner spinner_reportSolidWaste_q02 = findViewById(R.id.spinner_reportSolidWaste_q02);
        Spinner spinner_reportSolidWaste_q03 = findViewById(R.id.spinner_reportSolidWaste_q03);
        Spinner spinner_reportSolidWaste_q04 = findViewById(R.id.spinner_reportSolidWaste_q04);
        Spinner spinner_reportSolidWaste_q05 = findViewById(R.id.spinner_reportSolidWaste_q05);
        Spinner spinner_reportSolidWaste_q06 = findViewById(R.id.spinner_reportSolidWaste_q06);
        Spinner spinner_reportSolidWaste_q07 = findViewById(R.id.spinner_reportSolidWaste_q07);
        Spinner spinner_reportSolidWaste_q08 = findViewById(R.id.spinner_reportSolidWaste_q08);

        button_reportSolidWaste_next = findViewById(R.id.button_reportSolidWaste_next);

        //Question 01 spinner element
        spinner_reportSolidWaste_q01.setOnItemSelectedListener(this);
        List<String> options01 = new ArrayList<>();
        options01.add("Pagtatapon sa maling lugar");
        options01.add("Nakatiwangwang o hindi nakolektang basura");

        ArrayAdapter<String> adapter01 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options01);
        adapter01.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportSolidWaste_q01.setAdapter(adapter01);

        //Question 02 spinner element
        spinner_reportSolidWaste_q02.setOnItemSelectedListener(this);
        List<String> options02 = new ArrayList<>();
        options02.add("Bakanteng lote");
        options02.add("Tabing kalsada");
        options02.add("Tabing dagat");
        options02.add("Iba pa");

        ArrayAdapter<String> adapter02 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options02);
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportSolidWaste_q02.setAdapter(adapter02);

        //Question 03 spinner element
        spinner_reportSolidWaste_q03.setOnItemSelectedListener(this);
        List<String> options03 = new ArrayList<>();
        options03.add("Isa hanggang limang bag");
        options03.add("Mahigit sa limang bag");

        ArrayAdapter<String> adapter03 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options03);
        adapter03.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportSolidWaste_q03.setAdapter(adapter03);

        //Question 04 spinner element
        spinner_reportSolidWaste_q04.setOnItemSelectedListener(this);
        List<String> options04 = new ArrayList<>();
        options04.add("Mga papel (e.g., karton, libro)");
        options04.add("Food wastes (e.g., tirang pagkain)");
        options04.add("Iba pang nabubulok na bagay (e.g., mga kahoy)");
        options04.add("Single-use plastics (e.g., sachets, plastic bags)");
        options04.add("Hard plastics (e.g., PET bottles, PVC pipes)");
        options04.add("Medical wastes (e.g., surgical masks/gloves)");
        options04.add("Electronic wastes (e.g., lumang appliances)");
        options04.add("Iba pang hindi nabubulok (e.g., mga bakal)");
        options04.add("Hindi masabi");

        ArrayAdapter<String> adapter04 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options04);
        adapter04.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportSolidWaste_q04.setAdapter(adapter04);

        //Question 05 spinner element
        spinner_reportSolidWaste_q05.setOnItemSelectedListener(this);
        List<String> options05 = new ArrayList<>();
        options05.add("Isa hanggang tatlong araw");
        options05.add("Mahigit sa tatlong araw");

        ArrayAdapter<String> adapter05 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options05);
        adapter05.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportSolidWaste_q05.setAdapter(adapter05);

        //Question 06 spinner element
        spinner_reportSolidWaste_q06.setOnItemSelectedListener(this);
        List<String> options06 = new ArrayList<>();
        options06.add("Mayroon");
        options06.add("Wala");

        ArrayAdapter<String> adapter06 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options06);
        adapter06.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportSolidWaste_q06.setAdapter(adapter06);

        //Question 07 spinner element
        spinner_reportSolidWaste_q07.setOnItemSelectedListener(this);
        List<String> options07 = new ArrayList<>();
        options07.add("Mayroon");
        options07.add("Wala");

        ArrayAdapter<String> adapter07 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options07);
        adapter07.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportSolidWaste_q07.setAdapter(adapter07);

        //Question 08 spinner element
        spinner_reportSolidWaste_q08.setOnItemSelectedListener(this);
        List<String> options08 = new ArrayList<>();
        options08.add("Oo ngunit wala pang aksyon");
        options08.add("Hindi");

        ArrayAdapter<String> adapter08 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options08);
        adapter08.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportSolidWaste_q08.setAdapter(adapter08);

        button_reportSolidWaste_next.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {

                String ans = input01 + "|" + input02 + "|" + input03 + "|" + input04+ "|" + input05+ "|" + input06 + "|" + input07 + "|" + input08;

                Log.v("[ ReportSolidWaste.java ]", "ANSWER: " + ans);

                Intent intent = new Intent(ReportSolidWaste.this, GoogleMapFragment.class);
                intent.putExtra("userID", userID);
                intent.putExtra("Date and Time", dateTime());
                intent.putExtra("Incident Type", "Solid Waste");
                intent.putExtra("Report", ans);
                intent.putExtra("Longitude", lon);
                intent.putExtra("Latitude", lat);
                intent.putExtra("image", image);
                startActivity(intent);
            }
        });

        imageView_solidwaste_back = findViewById(R.id.imageView_solidwaste_back);
        imageView_solidwaste_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {

        if (adapterView.getId() == R.id.spinner_reportSolidWaste_q01) {
            input01 = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getId() == R.id.spinner_reportSolidWaste_q02) {
            input02 = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getId() == R.id.spinner_reportSolidWaste_q03) {
            input03 = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getId() == R.id.spinner_reportSolidWaste_q04) {
            input04 = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getId() == R.id.spinner_reportSolidWaste_q05) {
            input05 = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getId() == R.id.spinner_reportSolidWaste_q06) {
            input06 = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getId() == R.id.spinner_reportSolidWaste_q07) {
            input07 = adapterView.getItemAtPosition(i).toString();
        } else {
            input08 = adapterView.getItemAtPosition(i).toString();
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
