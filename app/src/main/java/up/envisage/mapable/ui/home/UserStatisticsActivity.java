package up.envisage.mapable.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;
import up.envisage.mapable.model.Badges;

public class UserStatisticsActivity extends AppCompatActivity {

    TextView textView_algalBloom, textView_fishKill, textView_ongoingReclamation, textView_waterPollution, textView_waterHyacinth, textView_others,
            textView_verified, textView_unverified, textView_falsePositive, textView_solidWaste, textView_total, textView_badgeAB, textView_badgeFK,
            textView_badgeWP, textView_badgeOR, textView_badgeWH, textView_badgeSW, textView_badgeTOT, textView_badgeVer, textView_badgeOAK;

    String userID, algalBloom, fishKill, waterPollution, ongoingReclamation, waterHyacinth, solidWaste, others, verified, unverified, falsePositive, total;

    String badge_ab, badge_fk, badge_wp, badge_or, badge_wh, badge_sw, badge_total, badge_verified, badge_oak;
    Retrofit retrofit;
    RetrofitInterface retrofitInterface;
    String BASE_URL = "http://ec2-54-91-89-105.compute-1.amazonaws.com/";
//    private String BASE_URL = "http://10.0.2.2:5000/";

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stats);

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

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Intent intent = getIntent();

        algalBloom = intent.getStringExtra("algalBloom");
        fishKill = intent.getStringExtra("fishKill");
        waterPollution = intent.getStringExtra("waterPollution");
        ongoingReclamation = intent.getStringExtra("ongoingReclamation");
        waterHyacinth = intent.getStringExtra("waterHyacinth");
        solidWaste = intent.getStringExtra("solidWaste");
        others = intent.getStringExtra("otherIssues");
        verified = intent.getStringExtra("verified");
        unverified = intent.getStringExtra("unverified");
        falsePositive = intent.getStringExtra("falsePositive");
        total = intent.getStringExtra("total");
        userID = intent.getStringExtra("userID");

        HashMap<String, String> map = new HashMap<>();
        map.put("userID", userID);

        Call<Badges> call = retrofitInterface.getBadges(map);

        call.enqueue(new Callback<Badges>() {
            @Override
            public void onResponse(Call<Badges> call, Response<Badges> response) {
                if(response.code() == 200){

                    Log.v("UserStats", response.body().toString());
                    badge_ab = response.body().getAlgalBloom();
                    badge_fk = response.body().getFishKill();
                    badge_wp = response.body().getWaterPollution();
                    badge_or = response.body().getOngoingReclamation();
                    badge_wh = response.body().getWaterHyacinth();
                    badge_sw = response.body().getSolidWaste();
                    badge_total = response.body().getTotal();
                    badge_verified = response.body().getVerified();
                    badge_oak = response.body().getOneOfAKind();

                    textView_badgeAB = findViewById(R.id.textView_badgeAB);
                    textView_badgeFK = findViewById(R.id.textView_badgeFK);
                    textView_badgeWP = findViewById(R.id.textView_badgeWP);
                    textView_badgeOR = findViewById(R.id.textView_badgeOR);
                    textView_badgeWH = findViewById(R.id.textView_badgeWH);
                    textView_badgeSW = findViewById(R.id.textView_badgeSW);
                    textView_badgeTOT = findViewById(R.id.textView_badgeTOT);
                    textView_badgeVer = findViewById(R.id.textView_badgeVer);
                    textView_badgeOAK = findViewById(R.id.textView_badgeOAK);

                    if(badge_ab.equals("N")) {
                        textView_badgeAB.setVisibility(View.GONE);
                    } else {
                        textView_badgeAB.setVisibility(View.VISIBLE);
                        textView_badgeAB.setText(badge_ab);
                        //Set Icon ror badge graphics instead of text
                    }

                    if(badge_fk.equals("N")) {
                        textView_badgeFK.setVisibility(View.GONE);
                    } else {
                        textView_badgeFK.setVisibility(View.VISIBLE);
                        textView_badgeFK.setText(badge_fk);
                        //Set Icon ror badge graphics instead of text
                    }

                    if(badge_wp.equals("N")) {
                        textView_badgeWP.setVisibility(View.GONE);
                    } else {
                        textView_badgeWP.setVisibility(View.VISIBLE);
                        textView_badgeWP.setText(badge_wp);
                        //Set Icon ror badge graphics instead of text
                    }

                    if(badge_or.equals("N")) {
                        textView_badgeOR.setVisibility(View.GONE);
                    } else {
                        textView_badgeOR.setVisibility(View.VISIBLE);
                        textView_badgeOR.setText(badge_or);
                        //Set Icon ror badge graphics instead of text
                    }

                    if(badge_wh.equals("N")) {
                        textView_badgeWH.setVisibility(View.GONE);
                    } else {
                        textView_badgeWH.setVisibility(View.VISIBLE);
                        textView_badgeWH.setText(badge_wh);
                        //Set Icon ror badge graphics instead of text
                    }

                    if(badge_sw.equals("N")) {
                        textView_badgeSW.setVisibility(View.GONE);
                    } else {
                        textView_badgeSW.setVisibility(View.VISIBLE);
                        textView_badgeSW.setText(badge_sw);
                        //Set Icon ror badge graphics instead of text
                    }

                    if(badge_total.equals("N")) {
                        textView_badgeTOT.setVisibility(View.GONE);
                    } else {
                        textView_badgeTOT.setVisibility(View.VISIBLE);
                        textView_badgeTOT.setText(badge_total);
                        //Set Icon ror badge graphics instead of text
                    }

                    if(badge_verified.equals("N")) {
                        textView_badgeVer.setVisibility(View.GONE);
                    } else {
                        textView_badgeVer.setVisibility(View.VISIBLE);
                        textView_badgeVer.setText(badge_verified);
                        //Set Icon ror badge graphics instead of text
                    }

                    if(badge_oak.equals("N")) {
                        textView_badgeOAK.setVisibility(View.GONE);
                    } else {
                        textView_badgeOAK.setVisibility(View.VISIBLE);
                        textView_badgeOAK.setText(badge_oak);
                        //Set Icon ror badge graphics instead of text
                    }
                }

            }

            @Override
            public void onFailure(Call<Badges> call, Throwable t) {

            }
        });


        textView_algalBloom = findViewById(R.id.textView_algalBloom);
        textView_fishKill = findViewById(R.id.textView_fishKill);
        textView_ongoingReclamation = findViewById(R.id.textView_ongoingReclamation);
        textView_waterPollution = findViewById(R.id.textView_waterPollution);
        textView_waterHyacinth = findViewById(R.id.textView_waterHyacinth);
        textView_verified = findViewById(R.id.textView_verified);
        textView_unverified = findViewById(R.id.textView_unverified);
        textView_falsePositive= findViewById(R.id.textView_falsePositive);
        textView_solidWaste= findViewById(R.id.textView_solidWaste);
        textView_total = findViewById(R.id.textView_total);
        textView_others = findViewById(R.id.textView_others);

        textView_algalBloom.setText(algalBloom);
        textView_fishKill.setText(fishKill);
        textView_ongoingReclamation.setText(ongoingReclamation);
        textView_waterPollution.setText(waterPollution);
        textView_waterHyacinth.setText(waterHyacinth);
        textView_verified.setText(verified);
        textView_unverified.setText(unverified);
        textView_falsePositive.setText(falsePositive);
        textView_solidWaste.setText(solidWaste);
        textView_total.setText(total);
        textView_others.setText(others);

        //back to Main Menu Text Button
        TextView textView_reportBack = findViewById(R.id.textView_report_back);
        textView_reportBack.setOnClickListener(view -> finish());

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
        Intent intent = new Intent(UserStatisticsActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
