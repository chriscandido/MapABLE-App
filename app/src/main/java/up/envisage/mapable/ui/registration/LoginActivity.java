package up.envisage.mapable.ui.registration;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;
import up.envisage.mapable.db.table.UserTable;
import up.envisage.mapable.model.UserViewModel;

public class LoginActivity extends AppCompatActivity  {

    private TextInputLayout textInputLayout_loginUsername, textInputLayout_loginPassword;
    private TextView textView_eulaTitle, textView_signUp;
    private Button button_eulaAgree, button_login;
    private CheckBox checkBox_eulaAgree;

    Dialog dialog;

    private UserViewModel userViewModel;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://project-mapable.herokuapp.com/";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …

// add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        textInputLayout_loginUsername = findViewById(R.id.textInputLayout_loginUsername);
        textInputLayout_loginPassword = findViewById(R.id.textInputLayout_loginPassword);

        userViewModel = ViewModelProviders.of(LoginActivity.this).get(UserViewModel.class);

        //Initialize Terms of Use using Dialog Box
        initializeTermsOfUse();

        //Check username and password in local db
        button_login = findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = textInputLayout_loginUsername.getEditText().getText().toString().trim();
                final String password = textInputLayout_loginPassword.getEditText().getText().toString().trim();

//                function to do what it does when login button is clicked
                HashMap<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("password", password);

                Call<LoginResult> call = retrofitInterface.executeLogin(map);

                call.enqueue(new Callback<LoginResult>() {
                    @Override
                  
                  public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if (response.code() == 200) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                        } else if (response.code() == 400){
                            Toast.makeText(LoginActivity.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

//                userViewModel.getUsers().observe(LoginActivity.this, new Observer<List<UserTable>>() {
//                    @Override
//                    public void onChanged(List<UserTable> userTables) {
//                        for (UserTable ut: userTables){
//
//                            boolean isUsernameValid = ut.getUsername().equals(username);
//                            boolean isPasswordValid = ut.getPassword().equals(password);
//                            if (isUsernameValid && isPasswordValid) {
//                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                startActivity(intent);
//                            }
//                        }
//                    }
//                });
            }
        });
    }

    //----------------------------------------------------------------------------------------------Initialization of Terms of Use
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

    //----------------------------------------------------------------------------------------------Set the visibility of agree button
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

    //----------------------------------------------------------------------------------------------Go to Sign-up Page
    public void goToSignup(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

}
