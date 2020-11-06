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

public class ReportIllegalReclamation extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private MaterialButton button_reportIllegalReclamation_next;
    private TextInputLayout textInputLayout_reportIllegalReclamation_q00, textInputLayout_reportIllegalReclamation_q06;

    private String input00, input01, input02, input03, input04, input05, input06;

    private List<String> out = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_illegal_reclamation);

        Spinner spinner_reportIllegalReclamation_q01 = findViewById(R.id.spinner_reportIllegalReclamation_q01);
        Spinner spinner_reportIllegalReclamation_q02 = findViewById(R.id.spinner_reportIllegalReclamation_q02);
        Spinner spinner_reportIllegalReclamation_q03 = findViewById(R.id.spinner_reportIllegalReclamation_q03);
        Spinner spinner_reportIllegalReclamation_q04 = findViewById(R.id.spinner_reportIllegalReclamation_q04);
        Spinner spinner_reportIllegalReclamation_q05 = findViewById(R.id.spinner_reportIllegalReclamation_q05);

        textInputLayout_reportIllegalReclamation_q00 = findViewById(R.id.textInputLayout_reportIllegalReclamation);
        textInputLayout_reportIllegalReclamation_q06 = findViewById(R.id.textInputLayout_reportIllegalReclamation_q06);

        button_reportIllegalReclamation_next = findViewById(R.id.button_reportIllegalReclamation_next);

        //Question01 spinner element
        spinner_reportIllegalReclamation_q01.setOnItemSelectedListener(this);
        List<String> options01 = new ArrayList<>();
        options01.add("Mga ilang araw ang nakaraan");
        options01.add("Isang linggo na ang nakaraan");
        options01.add("Mahigit isang linggo na ang nakaraan");

        ArrayAdapter<String> adapter01 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options01);
        adapter01.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportIllegalReclamation_q01.setAdapter(adapter01);

        //Question02 spinner element
        spinner_reportIllegalReclamation_q02.setOnItemSelectedListener(this);
        List<String> options02 = new ArrayList<>();
        options02.add("Lupa");
        options02.add("Basura");
        options02.add("Iba pang materyales");

        ArrayAdapter<String> adapter02 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options02);
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportIllegalReclamation_q02.setAdapter(adapter01);

        //Question03 spinner element
        spinner_reportIllegalReclamation_q03.setOnItemSelectedListener(this);
        List<String> options03 = new ArrayList<>();
        options03.add("Mas maliit sa basketball court");
        options03.add("Kasukat ng basketball court");
        options03.add("Mas malaki sa isang basketball court");

        ArrayAdapter<String> adapter03 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options03);
        adapter03.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportIllegalReclamation_q03.setAdapter(adapter03);

        //Question04 spinner element
        spinner_reportIllegalReclamation_q04.setOnItemSelectedListener(this);
        List<String> options04 = new ArrayList<>();
        options04.add("Mayroon");
        options04.add("Wala");

        ArrayAdapter<String> adapter04 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options04);
        adapter04.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportIllegalReclamation_q04.setAdapter(adapter04);

        //Question04 spinner element
        spinner_reportIllegalReclamation_q05.setOnItemSelectedListener(this);
        List<String> options05 = new ArrayList<>();
        options05.add("Mayroon");
        options05.add("Wala");

        ArrayAdapter<String> adapter05 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options05);
        adapter05.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_reportIllegalReclamation_q05.setAdapter(adapter05);

        button_reportIllegalReclamation_next.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {

                input00 = textInputLayout_reportIllegalReclamation_q00.getEditText().getText().toString();
                input06 = textInputLayout_reportIllegalReclamation_q06.getEditText().getText().toString();

                //Insert report details
                out.add(input00);
                out.add(input01);
                out.add(input02);
                out.add(input03);
                out.add(input04);
                out.add(input05);
                out.add(input06);
                Gson gson = new Gson();
                String ans = gson.toJson(out);

                Log.v("[ ReportIllegalReclamation.java ]", "ANSWER: " + ans);

                Intent intent = new Intent(ReportIllegalReclamation.this, GoogleMapFragment.class);
                intent.putExtra("Date and Time", dateTime());
                intent.putExtra("Incident Type", "Illegal Reclamation");
                intent.putExtra("Report", ans);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {

        if (adapterView.getId() == R.id.spinner_reportIllegalReclamation_q01) {
            input01 = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getId() == R.id.spinner_reportIllegalReclamation_q02) {
            input02 = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getId() == R.id.spinner_reportIllegalReclamation_q03) {
            input03 = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getId() == R.id.spinner_reportIllegalReclamation_q04) {
            input04 = adapterView.getItemAtPosition(i).toString();
        } else {
            input05 = adapterView.getItemAtPosition(i).toString();
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
