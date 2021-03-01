package up.envisage.mapable.ui.support;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;
import up.envisage.mapable.model.UserViewModel;
import up.envisage.mapable.ui.home.MyReportActivity;
import up.envisage.mapable.ui.home.report.FeedbackResult;
import up.envisage.mapable.ui.registration.RetrofitInterface;

public class FeedbackActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://ec2-54-91-89-105.compute-1.amazonaws.com/";

    private TextInputLayout textInputLayout_feedback_q01;

    private String input00;
    private String outUserId;

    private List<String> out = new ArrayList<>();

    private UserViewModel userViewModel;

    public Boolean connection;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        connection = isNetworkAvailable();

        userViewModel = ViewModelProviders.of(FeedbackActivity.this).get(UserViewModel.class);

        userViewModel.getLastUser().observe(FeedbackActivity.this, UserTable -> {
            outUserId = UserTable.getUniqueId();
        });

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        //Set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        //Add your other interceptors …
        httpClient.callTimeout(2,TimeUnit.MINUTES)
                .connectTimeout(30, TimeUnit.SECONDS) // connect timeout
                .writeTimeout(30, TimeUnit.SECONDS) // write timeout
                .readTimeout(30, TimeUnit.SECONDS); // read timeout

        //Add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        TextView textView_feedback_back = findViewById(R.id.textView_feedback_back);

        textInputLayout_feedback_q01 = findViewById(R.id.textInputLayout_feedback_q01);

        MaterialButton button_feedback_send = findViewById(R.id.button_feedback_send);
        button_feedback_send.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                input00 = textInputLayout_feedback_q01.getEditText().getText().toString();

                String answer = input00;

                HashMap<String, String> map = new HashMap<>();
                map.put("userID", outUserId);
                map.put("model", input00);
                map.put("feedback", answer);
                Log.v("[ FeedbackActivity.java ]", "Feedback:" + answer);

                if(connection == true){
                    Call<FeedbackResult> call = retrofitInterface.submitFeedback(map);

                    call.enqueue(new Callback<FeedbackResult>() {
                        @Override
                        public void onResponse(Call<FeedbackResult> call, Response<FeedbackResult> response) {
                            if (response.code() == 200) {
                                Toast.makeText(FeedbackActivity.this, "Feedback Sent Successfully",
                                        Toast.LENGTH_LONG).show();
                                successDataSending();
                                Intent intent = new Intent(FeedbackActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else if (response.code() == 400){
                                Toast.makeText(FeedbackActivity.this, "Error Sending Feedback",
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FeedbackResult> call, Throwable t) {
                            Toast.makeText(FeedbackActivity.this, t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            Log.v("OnFailure Error Message", t.getMessage());
                        }


                    });
                } else {
                    Toast.makeText(FeedbackActivity.this, "No Internet Connection",
                            Toast.LENGTH_LONG).show();
                    errorNoConnection();
                }

            }
        });

        textView_feedback_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(FeedbackActivity.this, MainActivity.class);
                startActivity(back);
            }
        });


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void onStart(){
        super.onStart();
    }

    public void onResume(){
        super.onResume();
    }

    public void onPause(){
        super.onPause();
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent back = new Intent(FeedbackActivity.this, MainActivity.class);
        startActivity(back);

    }

    //----------------------------------------------------------------------------------------------Popup for successful data sending
    private void successDataSending(){
        dialog = new Dialog(FeedbackActivity.this);
        dialog.setContentView(R.layout.popup_success_registration);

        dialog.show();
    }

    //----------------------------------------------------------------------------------------------Popup for internet connection failure
    private void errorNoConnection(){
        dialog = new Dialog(FeedbackActivity.this);
        dialog.setContentView(R.layout.popup_error_nointernet);

        dialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
