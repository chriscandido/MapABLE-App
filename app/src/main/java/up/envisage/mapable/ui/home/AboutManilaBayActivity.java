package up.envisage.mapable.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;

public class AboutManilaBayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutmanilabay);

        TextView textView_informationManilaBay_back = findViewById(R.id.textView_informationManilaBay_back);

        //Button go back
        textView_informationManilaBay_back.setOnClickListener(v -> {
            Intent back = new Intent(AboutManilaBayActivity.this, InformationActivity.class);
            startActivity(back);
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
        super.onBackPressed();
        Intent intent = new Intent(AboutManilaBayActivity.this, InformationActivity.class);
        startActivity(intent);
    }
}
