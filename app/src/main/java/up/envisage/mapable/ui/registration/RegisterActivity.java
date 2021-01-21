package up.envisage.mapable.ui.registration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;
import up.envisage.mapable.databinding.ActivityRegisterBinding;
import up.envisage.mapable.databinding.Listener;
import up.envisage.mapable.db.table.UserTable;
import up.envisage.mapable.model.UserViewModel;


public class RegisterActivity extends AppCompatActivity implements Listener {

    private UserViewModel registerViewModel;
    private ActivityRegisterBinding binding;

    private SharedPreferences userIdPreferences;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://ec2-54-91-89-105.compute-1.amazonaws.com/";

    String username, password, userID;

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
        switch (view.getId()) {
            case R.id.button_submit:
                String name = binding.textInputLayoutRegisterName.getEditText().getText().toString().trim();
                String number = binding.textInputLayoutRegisterMobileNum.getEditText().getText().toString().trim();
                String email = binding.textInputLayoutRegisterEmail.getEditText().getText().toString().trim();
                username = binding.textInputLayoutRegisterUsername.getEditText().getText().toString().trim();
                password = binding.textInputLayoutRegisterPassword.getEditText().getText().toString().trim();

                UserTable user = new UserTable();
                if (TextUtils.isEmpty(name)) {
                    binding.textInputLayoutRegisterName.setError("Please Enter Your Name");
                }

                else if (TextUtils.isEmpty(email)) {
                    binding.textInputLayoutRegisterEmail.setError("Please Enter Your Email");
                }

                else if (TextUtils.isEmpty(username)) {
                    binding.textInputLayoutRegisterUsername.setError("Please Enter Username");
                }

                else if (TextUtils.isEmpty(password)) {
                    binding.textInputLayoutRegisterPassword.setError("Please Enter Password");
                }

                else {
//                    function to do what it does when login button is clicked
                    HashMap<String, String> map = new HashMap<>();
                    map.put("name", name);
                    map.put("number", number);
                    map.put("email", email);
                    map.put("username", username);
                    map.put("password", password);

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

                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
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
                    //Insert data to local server
                    user.setUniqueId(userID);
                    user.setName(name);
                    user.setNumber(number);
                    user.setEmail(email);
                    user.setUsername(username);
                    user.setPassword(password);
                    registerViewModel.insert(user);
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    Log.v("[ RegisterActivity.java ]", "Data successfully inserted");
                }
                break;

            case R.id.textView_registerLogin:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
