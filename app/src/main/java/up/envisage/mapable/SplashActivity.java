package up.envisage.mapable;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import up.envisage.mapable.db.table.UserTable;
import up.envisage.mapable.model.UserViewModel;
import up.envisage.mapable.ui.registration.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    String uniqueID = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userViewModel = ViewModelProviders.of(SplashActivity.this).get(UserViewModel.class);

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
