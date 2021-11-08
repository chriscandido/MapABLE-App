package up.envisage.mapable.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofitInterface.LeaderboardAPI;
import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;
import up.envisage.mapable.adapter.LeaderboardAdapter;
import up.envisage.mapable.db.table.ReportTable;
import up.envisage.mapable.db.table.UserTable;
import up.envisage.mapable.model.Leaderboard;
import up.envisage.mapable.model.ReportViewModel;
import up.envisage.mapable.model.UserViewModel;
import up.envisage.mapable.ui.home.report.ReportClassResult;
import up.envisage.mapable.ui.registration.LoginActivity;
import up.envisage.mapable.ui.registration.RetrofitInterface;

public class LeaderboardActivity extends AppCompatActivity {

    Retrofit retrofit;
    String BASE_URL = "http://ec2-54-91-89-105.compute-1.amazonaws.com/";
//    private String BASE_URL = "http://10.0.2.2:5000/";

    RecyclerView recyclerView;
    List<Leaderboard> leaderboardList;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        recyclerView = findViewById(R.id.Leaderboard_Recyclerview);
        leaderboardList = new ArrayList<>();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        //Set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        //Add your other interceptors …
        httpClient.callTimeout(2,TimeUnit.MINUTES)
                .connectTimeout(60, TimeUnit.SECONDS) // connect timeout
                .writeTimeout(60, TimeUnit.SECONDS) // write timeout
                .readTimeout(60, TimeUnit.SECONDS); // read timeout

        //Add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        LeaderboardAPI leaderboardAPI = retrofit.create(LeaderboardAPI.class);

        Call<List<Leaderboard>> call = leaderboardAPI.getLeaderboard();

        call.enqueue(new Callback<List<Leaderboard>>() {

            @Override
            public void onResponse(Call<List<Leaderboard>> call, Response<List<Leaderboard>> response) {

                if (response.code() !=  200) {
                    return;
                }

                List<Leaderboard> leaders = response.body();

                for(Leaderboard leader : leaders ) {
                    leaderboardList.add(leader);
                }

                PutDataIntoRecyclerView(leaderboardList);
            }

            @Override
            public void onFailure(Call<List<Leaderboard>> call, Throwable t) {

            }

            private void PutDataIntoRecyclerView(List<Leaderboard> leaderboardList) {

                LeaderboardAdapter leaderboardAdapter = new LeaderboardAdapter(getApplicationContext(), leaderboardList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(leaderboardAdapter);
            }
        });

        //back to Main Menu Text Button
        TextView textView_reportBack = findViewById(R.id.textView_report_back);
        textView_reportBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(LeaderboardActivity.this, MainActivity.class);
                startActivity(back);
            }
        });

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
        Intent intent = new Intent(LeaderboardActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
