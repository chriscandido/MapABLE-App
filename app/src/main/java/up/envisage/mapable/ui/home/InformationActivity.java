package up.envisage.mapable.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;

public class InformationActivity extends AppCompatActivity {

    private TextView textView_information_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        //Text Button go to Main Menu
        textView_information_back = findViewById(R.id.textView_information_back);
        textView_information_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(InformationActivity.this, MainActivity.class);
                startActivity(back);
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
}
