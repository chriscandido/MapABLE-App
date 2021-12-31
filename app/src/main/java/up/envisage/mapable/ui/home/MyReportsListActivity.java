package up.envisage.mapable.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import up.envisage.mapable.adapter.UserReportAdapter;
import up.envisage.mapable.model.UserReport;

public class MyReportsListActivity extends AppCompatActivity implements UserReportAdapter.OnReportClickListener{

    private Retrofit retrofit;
    public RetrofitInterface retrofitInterface;
    String BASE_URL = "http://ec2-54-91-89-105.compute-1.amazonaws.com/";
    String userID;
//    private String BASE_URL = "http://10.0.2.2:5000/";

    RecyclerView recyclerView;
    List<UserReport> UserReportList;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myreportslist);

        Intent intent = getIntent();

        userID = intent.getStringExtra("userID");

        UserReportList = new ArrayList<>();

        recyclerView = findViewById(R.id.UserReports_Recyclerview);

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

        HashMap<String, String> map = new HashMap<>();
        map.put("userID", userID);

        RetrofitInterface UserReportListAPI = retrofit.create(RetrofitInterface.class);

        Call<List<UserReport>> call = UserReportListAPI.getUserReportsList(map);

        call.enqueue(new Callback<List<UserReport>>() {

            @Override
            public void onResponse(Call<List<UserReport>> call, Response<List<UserReport>> response) {

                if (response.code() != 200) {
                    return;
                }

                List<UserReport> reports = response.body();

                assert reports != null;
                UserReportList.addAll(reports);

                PutDataIntoRecyclerView(UserReportList);
            }

            private void PutDataIntoRecyclerView(List<UserReport> userReportList) {

                UserReportAdapter userReportAdapter = new UserReportAdapter(getApplicationContext(), userReportList, MyReportsListActivity.this::onReportClick);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(userReportAdapter);

            }

            @Override
            public void onFailure(Call<List<UserReport>> call, Throwable t) {

            }
        });

        // Back button
        ImageView imageView_reportlist_back = findViewById(R.id.imageView_reportlist_back);
        imageView_reportlist_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    //----------------------------------------------------------------------------------------------Pass report data to ViewReportActivity
    @Override
    public void onReportClick(int position) {

        UserReportList.get(position);
        Log.v("MyReportsListActivity", UserReportList.get(position).getType());

        Intent intent = new Intent(MyReportsListActivity.this, ViewReportActivity.class);

        intent.putExtra("report", UserReportList.get(position).getReport());
        intent.putExtra("type", UserReportList.get(position).getType());
        intent.putExtra("date", UserReportList.get(position).getDate());
        intent.putExtra("status", UserReportList.get(position).getStatus());
        intent.putExtra("priority", UserReportList.get(position).getPriority());
        intent.putExtra("closed", UserReportList.get(position).getClosed());
        intent.putExtra("remarks", UserReportList.get(position).getRemarks());
        intent.putExtra("region", UserReportList.get(position).getReg());
        intent.putExtra("province", UserReportList.get(position).getProv());
        intent.putExtra("city", UserReportList.get(position).getMuni());

        intent.putExtra("userID", userID);

        startActivity(intent);
        //navigate to activity
        //start intent
        //put extra
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

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
