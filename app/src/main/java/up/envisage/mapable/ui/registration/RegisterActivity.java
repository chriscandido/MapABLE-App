package up.envisage.mapable.ui.registration;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import java.util.HashMap;
import java.util.Map;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import up.envisage.mapable.R;
import up.envisage.mapable.databinding.ActivityRegisterBinding;
import up.envisage.mapable.databinding.Listener;
import up.envisage.mapable.db.table.UserTable;
import up.envisage.mapable.model.UserViewModel;


public class RegisterActivity extends AppCompatActivity implements Listener {

    private UserViewModel registerViewModel;
    private ActivityRegisterBinding binding;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://ec2-54-91-89-105.compute-1.amazonaws.com/";

    public Boolean connection;

    private Dialog dialog;

    String username, password, userID, name, number, email;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(RegisterActivity.this, R.layout.activity_register);
        binding.setClickListener((RegisterActivity) this);

        registerViewModel = ViewModelProviders.of(RegisterActivity.this).get(UserViewModel.class);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(logging);  // <-- add interceptor

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onClick(View view) {
        connection = isNetworkAvailable();
        switch (view.getId()) {
            case R.id.button_submit:
                name = binding.textInputLayoutRegisterName.getEditText().getText().toString().trim();
                number = binding.textInputLayoutRegisterMobileNum.getEditText().getText().toString().trim();
                email = binding.textInputLayoutRegisterEmail.getEditText().getText().toString().trim();
                username = binding.textInputLayoutRegisterUsername.getEditText().getText().toString().trim();
                password = binding.textInputLayoutRegisterPassword.getEditText().getText().toString().trim();

                binding.textInputLayoutRegisterName.getEditText().setOnEditorActionListener(editorActionListener);
                binding.textInputLayoutRegisterMobileNum.getEditText().setOnEditorActionListener(editorActionListener);
                binding.textInputLayoutRegisterEmail.getEditText().setOnEditorActionListener(editorActionListener);
                binding.textInputLayoutRegisterUsername.getEditText().setOnEditorActionListener(editorActionListener);
                binding.textInputLayoutRegisterPassword.getEditText().setOnEditorActionListener(editorActionListener);

                UserTable user = new UserTable();
                if (TextUtils.isEmpty(name)) {
                    binding.textInputLayoutRegisterName.setError("Please Enter Your Name");
                }

                else if (!name.matches("^[a-zA-Z\\s]+$")) {
                    binding.textInputLayoutRegisterName.setError("Please enter only alphabetical character");
                }

                else if (TextUtils.isEmpty(email)) {
                    binding.textInputLayoutRegisterEmail.setError("Please Enter Your Email");
                }

                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.textInputLayoutRegisterEmail.setError("Wrong Email");
                }

                else if (TextUtils.isEmpty(username)) {
                    binding.textInputLayoutRegisterUsername.setError("Please Enter Username");
                }

                else if (TextUtils.isEmpty(password)) {
                    binding.textInputLayoutRegisterPassword.setError("Please Enter Password");
                }

                else {
                    //function to do what it does when login button is clicked
                    HashMap<String, String> map = new HashMap<>();
                    map.put("name", name);
                    map.put("number", number);
                    map.put("email", email);
                    map.put("username", username);
                    map.put("password", password);

                    if(connection == true) {

                        Call<Void> call = retrofitInterface.executeSignup(map);

                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.code() == 200) {

                                    Map<String, String> data = new HashMap<>();
                                    data.put("username", username);
                                    data.put("password", password);

                                    Call<userID> call2 = retrofitInterface.getUser(data);

                                    call2.enqueue(new Callback<userID>() {
                                        @Override
                                        public void onResponse(Call<userID> call, Response<userID> response) {
                                            if (response.isSuccessful()) {
                                                userID = response.body().get_id();
                                                Log.i("Get ID Response", userID);

                                                //Insert data to local server
                                                UserTable user = new UserTable();
                                                user.setUniqueId(userID);
                                                user.setName(name);
                                                user.setNumber(number);
                                                user.setEmail(email);
                                                user.setUsername(username);
                                                user.setPassword(password);
                                                registerViewModel.insert(user);
                                                Log.v("[ RegisterActivity.java ]", "Data successfully inserted");

                                                successRegistration();

                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                intent.putExtra("userID", userID);
                                                startActivity(intent);

                                            } else if (response.code() == 400){
                                                Toast.makeText(RegisterActivity.this, "Wrong Credentials",
                                                        Toast.LENGTH_LONG).show();
                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<userID> call, Throwable t) {
                                            Log.d("Error", t.getMessage());
                                        }
                                    });

                                } else if (response.code() == 400){
                                    Toast.makeText(RegisterActivity.this, "Cannot make account: Email already in use!",
                                            Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(RegisterActivity.this, t.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Cannot make account: No Internet Connection!",
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case R.id.textView_registerLogin:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void successRegistration(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_registration);

        MaterialButton button_registration_ok = dialog.findViewById(R.id.button_registration_ok);
        button_registration_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

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
}
