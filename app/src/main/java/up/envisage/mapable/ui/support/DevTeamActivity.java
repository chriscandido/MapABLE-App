package up.envisage.mapable.ui.support;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import up.envisage.mapable.R;

public class DevTeamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devteam);

        //back to Main Menu Text Button
        ImageView imageView_devTeam_back = findViewById(R.id.imageView_devTeam_back);
        imageView_devTeam_back.setOnClickListener(view -> finish());

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
        finish();
    }
}
