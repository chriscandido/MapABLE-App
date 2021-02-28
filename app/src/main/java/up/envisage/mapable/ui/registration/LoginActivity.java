package up.envisage.mapable.ui.registration;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

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
import up.envisage.mapable.ui.home.ReportingActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class LoginActivity extends AppCompatActivity  {

    private UserViewModel registerViewModel;
    private TextInputLayout textInputLayout_loginUsername, textInputLayout_loginPassword;
    private TextView textView_signUp;
    private Button button_eulaAgree, button_login;
    private CheckBox checkBox_eulaAgree;

    Dialog dialog;

    private SharedPreferences sharedPreferences;
    private SharedPreferences userIdPreferences;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://ec2-54-91-89-105.compute-1.amazonaws.com/";

    String username, password;

    public Boolean connection;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerViewModel = ViewModelProviders.of(LoginActivity.this).get(UserViewModel.class);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        //set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        //add your other interceptors …

        //add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        textInputLayout_loginUsername = findViewById(R.id.textInputLayout_loginUsername);
        textInputLayout_loginPassword = findViewById(R.id.textInputLayout_loginPassword);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        userIdPreferences = getSharedPreferences("userID", MODE_PRIVATE);

        //Shared preference for one time login
        if (sharedPreferences.getBoolean("logged", false) == true ) {
            Log.v("[UserID]",  userIdPreferences.getString("userID", "Invalid User ID"));
            goToMainActivity();
        }

        //Initialize Terms of Use using Dialog Box
        initializeTermsOfUse();

        //Check username and password in local db
        button_login = findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                connection = isNetworkAvailable();
                username = textInputLayout_loginUsername.getEditText().getText().toString().trim();
                password = textInputLayout_loginPassword.getEditText().getText().toString().trim();

                textInputLayout_loginUsername.getEditText().setOnEditorActionListener(editorActionListener);
                textInputLayout_loginPassword.getEditText().setOnEditorActionListener(editorActionListener);

                //function to do what it does when login button is clicked
                HashMap<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("password", password);

                if(connection == true){
                    Call<LoginResult> call = retrofitInterface.executeLogin(map);

                    call.enqueue(new Callback<LoginResult>() {
                        @Override

                        public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                            if (response.code() == 200) {
//                            final String username2 = textInputLayout_loginUsername.getEditText().getText().toString().trim();

                                String userID2 = response.body().get_id();
                                String name2 = response.body().getName();
                                String number2 = response.body().getNumber();
                                String email2 = response.body().getEmail();

                                UserTable user = new UserTable();
                                user.setUniqueId(userID2);
                                user.setName(name2);
                                user.setNumber(number2);
                                user.setEmail(email2);
                                user.setUsername(username);
                                user.setPassword(password);
                                registerViewModel.insert(user);

                                Log.v("[ LoginActivity.java ]",
                                        "Name:" + name2 + "\n" +
                                                "Number: " + number2 + "\n" +
                                                "Email: " + email2 + "\n");

                                Log.i("Get ID Response [LOGIN]", userID2);

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("userID", userID2);
                                startActivity(intent);
                                sharedPreferences.edit().putBoolean("logged", true).apply();
                                userIdPreferences.edit().putString("userID", response.body().get_id()).apply();


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
                } else {
                    errorNoConnection();
                    //Toast.makeText(LoginActivity.this, "Cannot login: No Internet Connection!",
                            //Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //----------------------------------------------------------------------------------------------Checks for internet connection
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
            TextView textView_eulaTitle = dialog.findViewById(R.id.textView_eulaTitle);

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

    //----------------------------------------------------------------------------------------------Set Action for Keys
    private TextInputEditText.OnEditorActionListener editorActionListener = new TextInputEditText.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_NEXT:
                case EditorInfo.IME_ACTION_DONE:
                    break;
            }
            return false;
        }
    };

    //----------------------------------------------------------------------------------------------Popup for No Internet Connection
    private void errorNoConnection(){
        dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.popup_error_nointernet);

        MaterialButton button_reportNoInternet_ok = dialog.findViewById(R.id.button_reportNoInternet_ok);
        button_reportNoInternet_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    //----------------------------------------------------------------------------------------------Go to Sign-up Page
    public void goToSignup(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    //----------------------------------------------------------------------------------------------Go to Main Page
    public void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("userID", userIdPreferences.getString("userID", "Invalid User ID"));
        startActivity(intent);
    }
}
