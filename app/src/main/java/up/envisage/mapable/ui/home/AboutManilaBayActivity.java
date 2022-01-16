package up.envisage.mapable.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import up.envisage.mapable.R;

public class AboutManilaBayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutmanilabay);

        ImageView imageView_aboutManilaBay_back = findViewById(R.id.imageView_aboutManilaBay_back);

        // Go Back Button
        imageView_aboutManilaBay_back.setOnClickListener(this::onClick);
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
        finish();
    }

    private void onClick(View v) {
        finish();
    }
}
