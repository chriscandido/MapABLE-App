package up.envisage.mapable.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;
import up.envisage.mapable.fragment.GoogleMapFragment;
import up.envisage.mapable.fragment.SupportFragment;
import up.envisage.mapable.ui.home.report.ReportPollution;

public class FeedbackActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView textView_feedback_back;
    private TextInputLayout textInputLayout_feedback_q01, textInputLayout_feedback_q02;
    private MaterialButton button_feedback_send;
    private String input00, input01, input02, input03, input04, input05, input06, input07, input08,
                    input09, input10, input11, input12, input13, input14, input15, input16, input17;
    private List<String> out = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Spinner spinner_feedback_q01  = findViewById(R.id.spinner_feedback_q01);
        Spinner spinner_feedback_q02  = findViewById(R.id.spinner_feedback_q02);
        Spinner spinner_feedback_q03  = findViewById(R.id.spinner_feedback_q03);
        Spinner spinner_feedback_q04  = findViewById(R.id.spinner_feedback_q04);
        Spinner spinner_feedback_q05  = findViewById(R.id.spinner_feedback_q05);
        Spinner spinner_feedback_q06  = findViewById(R.id.spinner_feedback_q06);
        Spinner spinner_feedback_q07  = findViewById(R.id.spinner_feedback_q07);
        Spinner spinner_feedback_q08  = findViewById(R.id.spinner_feedback_q08);
        Spinner spinner_feedback_q09  = findViewById(R.id.spinner_feedback_q09);
        Spinner spinner_feedback_q10  = findViewById(R.id.spinner_feedback_q10);
        Spinner spinner_feedback_q11  = findViewById(R.id.spinner_feedback_q11);
        Spinner spinner_feedback_q12  = findViewById(R.id.spinner_feedback_q12);
        Spinner spinner_feedback_q13  = findViewById(R.id.spinner_feedback_q13);
        Spinner spinner_feedback_q14  = findViewById(R.id.spinner_feedback_q14);
        Spinner spinner_feedback_q15  = findViewById(R.id.spinner_feedback_q15);
        Spinner spinner_feedback_q16  = findViewById(R.id.spinner_feedback_q16);

        textInputLayout_feedback_q01 = findViewById(R.id.textInputLayout_feedback_q01);
        textInputLayout_feedback_q02 = findViewById(R.id.textInputLayout_feedback_q02);

        //Question01 spinner element
        spinner_feedback_q01.setOnItemSelectedListener(this);
        List<String> options01 = new ArrayList<>();
        options01.add("The installation was much slower than most apps I use.");
        options01.add("The installation was slower than most apps I use.");
        options01.add("The installation speed was just right.");
        options01.add("The installation was faster than most apps I use.");
        options01.add("The installation was much faster than most apps I use.");

        ArrayAdapter<String> adapter01 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options01);
        adapter01.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_feedback_q01.setAdapter(adapter01);

        //Question02 spinner element
        spinner_feedback_q02.setOnItemSelectedListener(this);
        List<String> options02 = new ArrayList<>();
        options02.add("The app is much slower than most apps I use.");
        options02.add("The app is slower than most apps I use.");
        options02.add("The app speed when running is just right.");
        options02.add("The app is faster than most apps I use.");
        options02.add("The app is much faster than most apps I use.");

        ArrayAdapter<String> adapter02 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options02);
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_feedback_q02.setAdapter(adapter02);

        //Question03 spinner element
        spinner_feedback_q03.setOnItemSelectedListener(this);
        List<String> options03 = new ArrayList<>();
        options03.add("No notification pops up after any of my submissions.");
        options03.add("Some of the submissions do not have pop-up notifications.");
        options03.add("Pop-up notifications appear in all my submissions.");

        ArrayAdapter<String> adapter03 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options03);
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_feedback_q03.setAdapter(adapter03);

        //Question04 spinner element
        spinner_feedback_q04.setOnItemSelectedListener(this);
        List<String> options04 = new ArrayList<>();
        options04.add("At least some of the buttons are not working properly.");
        options04.add("All buttons worked properly.");

        ArrayAdapter<String> adapter04 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options04);
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_feedback_q04.setAdapter(adapter04);

        //Question05 spinner element
        spinner_feedback_q05.setOnItemSelectedListener(this);
        List<String> options05 = new ArrayList<>();
        options05.add("The map does not automatically pin to my current location when opened.");
        options05.add("I need to manually pan to my location on the map.");

        ArrayAdapter<String> adapter05 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options05);
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_feedback_q05.setAdapter(adapter05);

        //Question06 spinner element
        spinner_feedback_q06.setOnItemSelectedListener(this);
        List<String> options06 = new ArrayList<>();
        options06.add("Yes");
        options06.add("No");

        ArrayAdapter<String> adapter06 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options06);
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_feedback_q06.setAdapter(adapter06);

        //Question07 spinner element
        spinner_feedback_q07.setOnItemSelectedListener(this);
        List<String> options07 = new ArrayList<>();
        options07.add("I can capture images from the app.");
        options07.add("The app crashes when I am trying to capture images.");

        ArrayAdapter<String> adapter07 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options07);
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_feedback_q07.setAdapter(adapter07);

        //Question08 spinner element
        spinner_feedback_q08.setOnItemSelectedListener(this);
        List<String> options08 = new ArrayList<>();
        options08.add("Yes");
        options08.add("No");

        ArrayAdapter<String> adapter08 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options08);
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_feedback_q08.setAdapter(adapter08);

        //Question09 spinner element
        spinner_feedback_q09.setOnItemSelectedListener(this);
        List<String> options09 = new ArrayList<>();
        options09.add("The questions ask for enough details about the reported concern and are easy to understand. ");
        options09.add("The required answers are appropriate and clear.");
        options09.add("The questions ask for enough details about the reported concern and are easy to understand, but the required answers are unclear.");
        options09.add("The questions are not quite clear about what they ask, but the required answers are.");
        options09.add("The questions and required answers are both unclear.");

        ArrayAdapter<String> adapter09 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options09);
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_feedback_q09.setAdapter(adapter09);

        //Question10 spinner element
        spinner_feedback_q10.setOnItemSelectedListener(this);
        List<String> options10 = new ArrayList<>();
        options10.add("The overall design and layout are good and easy to understand.");
        options10.add("The graphic design is good, but the layout is confusing.");
        options10.add("The graphic design could be better, but the layout is good.");
        options10.add("Both the graphics and layout need improvement.");

        ArrayAdapter<String> adapter10 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options10);
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_feedback_q10.setAdapter(adapter10);

        //Question11 spinner element
        spinner_feedback_q11.setOnItemSelectedListener(this);
        List<String> options11 = new ArrayList<>();
        options11.add("The contents are relevant and easy to understand.");
        options11.add("The contents are relevant, but the writing could be improved for easier understanding.");
        options11.add("Some of the contents are not necessary, but the writing is easy to understand.");
        options11.add("Some of the contents are not necessary, and the writing could be improved for easier understanding.");

        ArrayAdapter<String> adapter11 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options11);
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_feedback_q11.setAdapter(adapter11);

        //Question12 spinner element
        spinner_feedback_q12.setOnItemSelectedListener(this);
        List<String> options12 = new ArrayList<>();
        options12.add("Strongly Agree");
        options12.add("Agree");
        options12.add("Neutral");
        options12.add("Disagree");
        options12.add("Strongly Disagree");

        ArrayAdapter<String> adapter12 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options12);
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_feedback_q12.setAdapter(adapter12);

        //Question13 spinner element
        spinner_feedback_q13.setOnItemSelectedListener(this);
        List<String> options13 = new ArrayList<>();
        options13.add("I need someone to show me how to use the apps.");
        options13.add("I need someone to show me how to use certain features.");
        options13.add("It took me a few tries to fully understand how the app works.");
        options13.add("It felt like I have been using it for a long time.");

        ArrayAdapter<String> adapter13 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options13);
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_feedback_q13.setAdapter(adapter13);

        //Question14 spinner element
        spinner_feedback_q14.setOnItemSelectedListener(this);
        List<String> options14 = new ArrayList<>();
        options14.add("Very Satisfied");
        options14.add("Satisfied");
        options14.add("Neutral");
        options14.add("Unsatisfied");
        options14.add("Very Unsatisfied");

        ArrayAdapter<String> adapter14 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options14);
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_feedback_q14.setAdapter(adapter14);

        //Question15 spinner element
        spinner_feedback_q15.setOnItemSelectedListener(this);
        List<String> options15 = new ArrayList<>();
        options15.add("Absolutely Recommended");
        options15.add("Probably Recommended");
        options15.add("Probably not Recommended");
        options15.add("Absolutely not Recommended");

        ArrayAdapter<String> adapter15 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options15);
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_feedback_q15.setAdapter(adapter15);

        //Question16 spinner element
        spinner_feedback_q16.setOnItemSelectedListener(this);
        List<String> options16 = new ArrayList<>();
        options16.add("Registration/log in");
        options16.add("Main home page");
        options16.add("Profile page");
        options16.add("Reporting module");
        options16.add("Map interface");
        options16.add("Camera/image upload");
        options16.add("Information pages");

        ArrayAdapter<String> adapter16 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options16);
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_feedback_q16.setAdapter(adapter16);

        button_feedback_send = findViewById(R.id.button_feedback_send);
        button_feedback_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input00 = textInputLayout_feedback_q01.getEditText().getText().toString();
                input17 = textInputLayout_feedback_q02.getEditText().getText().toString();

                out.add(input00);
                out.add(input01);
                out.add(input02);
                out.add(input03);
                out.add(input04);
                out.add(input05);
                out.add(input06);
                out.add(input07);
                out.add(input08);
                out.add(input09);
                out.add(input10);
                out.add(input11);
                out.add(input12);
                out.add(input13);
                out.add(input14);
                out.add(input15);
                out.add(input16);
                out.add(input17);

                Gson gson = new Gson();
                String answer = gson.toJson(out);
                Intent intent = new Intent(FeedbackActivity.this, SupportFragment.class);
                startActivity(intent);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner_feedback_q01) {
            input01 = parent.getItemAtPosition(position).toString();
        } else if (parent.getId() == R.id.spinner_feedback_q02) {
            input02 = parent.getItemAtPosition(position).toString();
        } else if (parent.getId() == R.id.spinner_feedback_q03) {
            input03 = parent.getItemAtPosition(position).toString();
        } else if (parent.getId() == R.id.spinner_feedback_q04) {
            input04 = parent.getItemAtPosition(position).toString();
        } else if (parent.getId() == R.id.spinner_feedback_q05) {
            input05 = parent.getItemAtPosition(position).toString();
        } else if (parent.getId() == R.id.spinner_feedback_q06) {
            input06 = parent.getItemAtPosition(position).toString();
        } else if (parent.getId() == R.id.spinner_feedback_q07) {
            input07 = parent.getItemAtPosition(position).toString();
        } else if (parent.getId() == R.id.spinner_feedback_q08) {
            input08 = parent.getItemAtPosition(position).toString();
        } else if (parent.getId() == R.id.spinner_feedback_q09) {
            input09 = parent.getItemAtPosition(position).toString();
        } else if (parent.getId() == R.id.spinner_feedback_q10) {
            input10 = parent.getItemAtPosition(position).toString();
        } else if (parent.getId() == R.id.spinner_feedback_q11) {
            input11 = parent.getItemAtPosition(position).toString();
        } else if (parent.getId() == R.id.spinner_feedback_q12) {
            input12 = parent.getItemAtPosition(position).toString();
        } else if (parent.getId() == R.id.spinner_feedback_q13) {
            input13 = parent.getItemAtPosition(position).toString();
        } else if (parent.getId() == R.id.spinner_feedback_q14) {
            input14 = parent.getItemAtPosition(position).toString();
        } else if (parent.getId() == R.id.spinner_feedback_q15) {
            input15 = parent.getItemAtPosition(position).toString();
        } else {
            input16 = parent.getItemAtPosition(position).toString();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
