package up.envisage.mapable;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import up.envisage.mapable.ui.home.ReportingActivity;
import up.envisage.mapable.ui.registration.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LaunchApp();
        //startActivity(new Intent(this, LoginActivity.class));
        //finish();
    }

    public void LaunchApp() {
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 10 seconds
                    sleep(2*1000);
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                    finish();
                } catch (Exception e) {
                }
            }
        };
        background.start();
    }
}
