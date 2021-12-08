package up.envisage.mapable.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofitInterface.RetrofitInterface;
import up.envisage.mapable.R;
import up.envisage.mapable.model.StatsResult;

public class MyQuestActivity extends AppCompatActivity {

    RetrofitInterface retrofitInterface;
    private ImageView imageView_myquest_tutorial, imageView_myquest_firstreport, imageView_myquest_verifiedreport;
    private HashMap<String, Integer> userStat;

    @SuppressLint("LongLogTag")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_quests);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        //Set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        //Add your other interceptors …
        httpClient.callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(30, TimeUnit.SECONDS) // connect timeout
                .writeTimeout(30, TimeUnit.SECONDS) // write timeout
                .readTimeout(30, TimeUnit.SECONDS); // read timeout

        //Add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        String BASE_URL = "http://ec2-54-91-89-105.compute-1.amazonaws.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");

        setBadges(userID);

    }

    public void setBadges(String userID) {

        HashMap<String, String> map = new HashMap<>();
        map.put("userID", userID);

        Call<StatsResult> call = retrofitInterface.getStats(map);

        call.enqueue(new Callback<StatsResult>() {

            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(@NonNull Call<StatsResult> call, @NonNull Response<StatsResult> response) {
                if(response.code() == 200) {
                    Integer algalBloom = response.body().getAlgalBloom();
                    Integer fishKill = response.body().getFishKill();
                    Integer waterPollution = response.body().getWaterPollution();
                    Integer ongoingReclamation = response.body().getOngoingReclamation();
                    Integer waterHyacinth = response.body().getWaterHyacinth();
                    Integer solidWaste = response.body().getSolidWaste();
                    Integer otherIssues = response.body().getOthers();
                    Integer verified = response.body().getVerified();
                    Integer unverified = response.body().getUnverified();
                    Integer falsePositive = response.body().getFalsePositive();
                    Integer total = response.body().getTotal();

                    Log.v("[ Call Enqueue ]", "Total: " + String.valueOf(verified));

                    setBasicLevelImageResource(total, verified);

                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(@NonNull Call<StatsResult> call, @NonNull Throwable throwable) {
                Log.v("[ MyQuestActivity.java ]", "Failure to connect");
            }
        });


    }

    @SuppressLint("LongLogTag")
    public void setBasicLevelImageResource(Integer total, Integer verified) {

        imageView_myquest_firstreport = findViewById(R.id.imageView_myquest_firstreport);
        imageView_myquest_verifiedreport = findViewById(R.id.imageView_myquest_verifiedreport);

        if (total != null){
            imageView_myquest_firstreport.setImageResource(R.drawable.ic_badge_firstreport);
        } else {
            imageView_myquest_firstreport.setImageResource(R.drawable.ic_badge_lock);
        }

        if (verified > 0) {
            imageView_myquest_verifiedreport.setImageResource(R.drawable.ic_badge_firstverified);
        } else {
            imageView_myquest_verifiedreport.setImageResource(R.drawable.ic_badge_lock);
        }

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
        finish();
    }

}
