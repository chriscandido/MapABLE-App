package up.envisage.mapable;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import up.envisage.mapable.ui.registration.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LaunchApp();

    }

    public void LaunchApp() {
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 10 seconds
                    sleep(2*1000);
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                    finish();
                } catch (Exception ignored) {
                }
            }
        };
        background.start();
    }
}
