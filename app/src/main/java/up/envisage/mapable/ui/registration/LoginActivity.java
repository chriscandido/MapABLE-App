package up.envisage.mapable.ui.registration;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;
import up.envisage.mapable.db.table.UserTable;
import up.envisage.mapable.model.UserViewModel;

public class LoginActivity extends AppCompatActivity  {

    TextInputLayout textInputLayout_loginUsername, textInputLayout_loginPassword;
    TextView textView_eulaTitle, textView_signUp;
    Button button_eulaAgree, button_login;
    Dialog dialog;
    CheckBox checkBox_eulaAgree;

    private UserViewModel userViewModel;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputLayout_loginUsername = findViewById(R.id.textInputLayout_loginUsername);
        textInputLayout_loginPassword = findViewById(R.id.textInputLayout_loginPassword);

        button_login = findViewById(R.id.button_login);

        userViewModel = ViewModelProviders.of(LoginActivity.this).get(UserViewModel.class);

        //Initialize Terms of Use using Dialog Box
        initializeTermsOfUse();

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = textInputLayout_loginUsername.getEditText().getText().toString().trim();
                final String password = textInputLayout_loginPassword.getEditText().getText().toString().trim();

                userViewModel.getUsers().observe(LoginActivity.this, new Observer<List<UserTable>>() {
                    @Override
                    public void onChanged(List<UserTable> userTables) {
                        for (UserTable ut: userTables){
                            boolean isUsernameValid = ut.getUsername().equals(username);
                            boolean isPasswordValid = ut.getPassword().equals(password);
                            if (isUsernameValid && isPasswordValid) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        });
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

}
