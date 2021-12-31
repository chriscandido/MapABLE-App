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
    private ImageView imageView_myquest_algalbloom, imageView_myquest_fishkill, imageView_myquest_hyacinth,
                        imageView_myquest_solidwaste, imageView_myquest_waterpollution, imageView_myquest_reclamation, imageView_myquest_certified;
    private ImageView imageView_myquest_junior, imageView_myquest_senior, imageView_myquest_professional, imageView_myquest_master, imageView_myquest_thegreat;
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

                    setTagapagmasidImageResource(algalBloom,
                            fishKill,
                            waterHyacinth,
                            solidWaste,
                            waterPollution,
                            ongoingReclamation);

                    setCitizenScientistsImageResource(total,
                            verified,
                            unverified,
                            algalBloom,
                            fishKill,
                            waterHyacinth,
                            solidWaste,
                            waterPollution,
                            ongoingReclamation);

                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(@NonNull Call<StatsResult> call, @NonNull Throwable throwable) {
                Log.v("[ MyQuestActivity.java ]", "Failure to connect");
            }
        });


    }

    //----------------------------------------------------------------------------------------------Set condition of Basic Level badges
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

    //----------------------------------------------------------------------------------------------Set condition of Citizen Scientists badges
    @SuppressLint("CutPasteId")
    public void setCitizenScientistsImageResource(Integer total,
                                                  Integer verified,
                                                  Integer unverified,
                                                  Integer algalbloom,
                                                  Integer fishkill,
                                                  Integer hyacinth,
                                                  Integer solidwaste,
                                                  Integer waterpollution,
                                                  Integer reclamation) {

        imageView_myquest_junior = findViewById(R.id.imageView_myquest_junior);
        imageView_myquest_senior = findViewById(R.id.imageView_myquest_senior);
        imageView_myquest_professional = findViewById(R.id.imageView_myquest_professional);
        imageView_myquest_master = findViewById(R.id.imageView_myquest_master);
        imageView_myquest_thegreat = findViewById(R.id.imageView_myquest_great);

        // Junior Citizen Scientist
        if (total != null || total > 0) {
            imageView_myquest_junior.setImageResource(R.drawable.ic_badge_junior);
        }

        // Senior Citizen Scientist
        if (total > 1000) {
            imageView_myquest_senior.setImageResource(R.drawable.ic_badge_senior);
        }

        // Professional Citizen Scientists
        if (total > 1000 && (verified != null || verified > 0)) {
            imageView_myquest_professional.setImageResource(R.drawable.ic_badge_professional);
        }

        // Master Citizen Scientists
        if (total > 1000 && verified > 10) {
            imageView_myquest_master.setImageResource(R.drawable.ic_badge_master);
        }

        // The Great Citizen Scientists
        if (total > 1000 && verified > 10 && algalbloom > 0 && fishkill > 0 && hyacinth > 0 && solidwaste > 0 && waterpollution > 0 && reclamation > 0) {
            imageView_myquest_thegreat.setImageResource(R.drawable.ic_badge_great);
        }

    }

    //----------------------------------------------------------------------------------------------Set condition of Tagapagmasid badges
    public void setTagapagmasidImageResource(Integer algalbloom,
                                             Integer fishkill,
                                             Integer hyacinth,
                                             Integer solidwaste,
                                             Integer waterpollution,
                                             Integer reclamation){

        imageView_myquest_algalbloom = findViewById(R.id.imageView_myquest_algalbloom);
        imageView_myquest_fishkill = findViewById(R.id.imageView_myquest_fishkill);
        imageView_myquest_hyacinth = findViewById(R.id.imageView_myquest_hyacinth);
        imageView_myquest_solidwaste = findViewById(R.id.imageView_myquest_solidwaste);
        imageView_myquest_waterpollution = findViewById(R.id.imageView_myquest_waterpollution);
        imageView_myquest_reclamation = findViewById(R.id.imageView_myquest_reclamation);
        imageView_myquest_certified = findViewById(R.id.imageView_myquest_certified);

        //Algal Bloom Condition
        if ( algalbloom > 0) {
            if (algalbloom < 3 ) {
                imageView_myquest_algalbloom.setImageResource(R.drawable.ic_badge_bronze_algalbloom);
            } else if (algalbloom < 10) {
                imageView_myquest_algalbloom.setImageResource(R.drawable.ic_badge_silver_algalbloom);
            } else if (algalbloom > 10) {
                imageView_myquest_algalbloom.setImageResource(R.drawable.ic_badge_gold_algalbloom);
            } else {
                imageView_myquest_algalbloom.setImageResource(R.drawable.ic_badge_lock);
            }
        }

        // Fish kill Condition
        if ( fishkill > 0) {
            if (fishkill < 3 ) {
                imageView_myquest_fishkill.setImageResource(R.drawable.ic_badge_bronze_fishkill);
            } else if (fishkill < 10) {
                imageView_myquest_fishkill.setImageResource(R.drawable.ic_badge_silver_fishkill);
            } else if (fishkill > 10) {
                imageView_myquest_fishkill.setImageResource(R.drawable.ic_badge_gold_fishkill);
            }else {
                imageView_myquest_fishkill.setImageResource(R.drawable.ic_badge_lock);
            }
        }

        // Hyacinth Condition
        if ( hyacinth > 0) {
            if (hyacinth < 3 ) {
                imageView_myquest_hyacinth.setImageResource(R.drawable.ic_badge_bronze_hyacinth);
            } else if (hyacinth < 10) {
                imageView_myquest_hyacinth.setImageResource(R.drawable.ic_badge_silver_hyacinth);
            } else if (hyacinth > 10) {
                imageView_myquest_hyacinth.setImageResource(R.drawable.ic_badge_gold_hyacinth);
            } else {
                imageView_myquest_hyacinth.setImageResource(R.drawable.ic_badge_lock);
            }
        }

        // Solid Waste Condition
        if ( solidwaste > 0) {
            if (solidwaste < 3 ) {
                imageView_myquest_solidwaste.setImageResource(R.drawable.ic_badge_bronze_soildwaste);
            } else if (solidwaste < 10) {
                imageView_myquest_solidwaste.setImageResource(R.drawable.ic_badge_silver_solidwaste);
            } else if (solidwaste > 10) {
                imageView_myquest_solidwaste.setImageResource(R.drawable.ic_badge_gold_solidwaste);
            }else {
                imageView_myquest_solidwaste.setImageResource(R.drawable.ic_badge_lock);
            }
        }

        // Water pollution Condition
        if ( waterpollution > 0) {
            if (waterpollution < 3 ) {
                imageView_myquest_waterpollution.setImageResource(R.drawable.ic_badge_bronze_waterpollution);
            } else if (waterpollution < 10) {
                imageView_myquest_waterpollution.setImageResource(R.drawable.ic_badge_silver_waterpollution);
            } else if (waterpollution > 10) {
                imageView_myquest_waterpollution.setImageResource(R.drawable.ic_badge_gold_waterpollution);
            } else {
                imageView_myquest_waterpollution.setImageResource(R.drawable.ic_badge_lock);
            }
        }

        // Reclamation Condition
        if ( reclamation > 0) {
            if (reclamation < 3 ) {
                imageView_myquest_reclamation.setImageResource(R.drawable.ic_badge_bronze_reclamation);
            } else if (reclamation < 10) {
                imageView_myquest_reclamation.setImageResource(R.drawable.ic_badge_silver_reclamation);
            } else if (reclamation > 10) {
                imageView_myquest_reclamation.setImageResource(R.drawable.ic_badge_gold_reclamation);
            }else {
                imageView_myquest_reclamation.setImageResource(R.drawable.ic_badge_lock);
            }
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
