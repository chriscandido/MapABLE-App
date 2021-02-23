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

        textView_informationManilaBay_back.setOnClickListener(v -> {
            Intent back = new Intent(AboutManilaBayActivity.this, MainActivity.class);
            startActivity(back);
        });
    }
}
