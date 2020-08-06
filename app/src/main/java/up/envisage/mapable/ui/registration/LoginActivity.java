package up.envisage.mapable.ui.registration;

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
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout textInputLayout_username, textInputLayout_password;
    TextView textView_signup;
    Button button_login;

    Dialog dialog;
    Button button_eulaAgree;
    CheckBox checkBox_eulaAgree;
    TextView textView_eulaTitle;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize Terms of Use using Dialog Box
        initializeTermsOfUse();

        initViews();
    }

    public void initViews() {
        textInputLayout_username = findViewById(R.id.textInputLayout_loginUsername);
        textInputLayout_password = findViewById(R.id.textInputLayout_loginPassword);

        button_login = findViewById(R.id.button_login);

        textView_signup = findViewById(R.id.textView_loginSignUp);

    }

    public void initializeTermsOfUse(){

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean agreed = sharedPreferences.getBoolean("agreed", false);

        if (!agreed) {

            dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_eula);

            button_eulaAgree = dialog.findViewById(R.id.button_eulaAgree);
            checkBox_eulaAgree = dialog.findViewById(R.id.checkBox_eulaAgree);
            textView_eulaTitle = dialog.findViewById(R.id.textView_eulaTitle);

            //Set agree button to visible or invisible
            checkBox_eulaAgree.setOnCheckedChangeListener(new onCheckListener());

            //Shared preference
            button_eulaAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("agreed", true);
                    editor.apply();
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    public class onCheckListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                button_eulaAgree.setVisibility(View.VISIBLE);
            } else {
                button_eulaAgree.setVisibility(View.GONE);
            }
        }
    }

    public void goToSignup(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void goToMain(View view) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
