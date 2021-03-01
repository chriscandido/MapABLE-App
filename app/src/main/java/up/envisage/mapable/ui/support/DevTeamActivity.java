package up.envisage.mapable.ui.support;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;
import up.envisage.mapable.ui.home.ReportingActivity;

public class DevTeamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devteam);

        //back to Main Menu Text Button
        TextView textView_supportDev_back = findViewById(R.id.textView_devTeam_back);
        textView_supportDev_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(DevTeamActivity.this, MainActivity.class);
                startActivity(back);
            }
        });

    }
}
