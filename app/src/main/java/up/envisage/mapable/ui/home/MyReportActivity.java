package up.envisage.mapable.ui.home;

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
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
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
import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;
import up.envisage.mapable.adapter.MyReportAdapter;
import up.envisage.mapable.db.table.ReportTable;
import up.envisage.mapable.fragment.UserFragment;
import up.envisage.mapable.model.ReportViewModel;
import up.envisage.mapable.ui.home.report.ReportClassResult;
import up.envisage.mapable.ui.registration.RetrofitInterface;

public class MyReportActivity extends AppCompatActivity implements MyReportAdapter.OnReportClickListener {

    private RecyclerView recyclerView;
    private ReportViewModel reportViewModel;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

    private TextView textView_myReport_back;
    private Button button_myReport_save;

    private Dialog dialog;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://ec2-54-91-89-105.compute-1.amazonaws.com/";

    Integer count, i;
    String userID, dateTime, incidentType, Report, lon, lat, image, imageString, outUserId;
    Double Longitude, Latitude;
    Boolean connection;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        //Set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        //Add your other interceptors …
        httpClient.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES); // read timeout

        //Add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        connection = isNetworkAvailable();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        reportViewModel = ViewModelProviders.of(MyReportActivity.this).get(ReportViewModel.class);
        reportViewModel.getAllReports().observe(MyReportActivity.this, new Observer<List<ReportTable>>() {
            @Override
            public void onChanged(List<ReportTable> reportTables) {
                adapter = new MyReportAdapter(getApplicationContext(), MyReportActivity.this::onClick, reportTables);
                recyclerView = findViewById(R.id.recyclerView_myReport);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                Log.v("[ MyReportActivity ]", "Number of Reports: " + reportTables.get(1).getIncidentType());
                Log.v("[ MyReportActivity ]", "Number of Pending Reports: " + reportTables.size());
                count = reportTables.size();
            }
        });

        button_myReport_save = findViewById(R.id.button_myReport_save);

        if(connection){
            button_myReport_save.setVisibility(View.VISIBLE);
        } else {
            button_myReport_save.setVisibility(View.GONE);
        };

        button_myReport_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(i = 0; i<count; i++) {

                    Log.v("Count: ", i.toString());
                    reportViewModel.getAllReports().observe(MyReportActivity.this, new Observer<List<ReportTable>>() {
                        @Override
                        public void onChanged(List<ReportTable> reportTables) {

                            userID = reportTables.get(i).getUniqueId();
                            dateTime = reportTables.get(i).getDateTime();
                            incidentType = reportTables.get(i).getIncidentType();
                            Report = reportTables.get(i).getReport();
                            Longitude = reportTables.get(i).getLongitude();
                            Latitude = reportTables.get(i).getLatitude();
                            image = reportTables.get(i).getPhoto();

                            imageString = imageConvertToString(image);

                            HashMap<String, String> map = new HashMap<>();
                            map.put("userID", userID);
                            map.put("date", dateTime);
                            map.put("type", incidentType);
                            map.put("report", Report);
                            map.put("lon", lon);
                            map.put("lat", lat);
                            map.put("image", image); //imageString

                            Log.v("[MyReportActivity.java]",
                                    "DATE & TIME: " + dateTime + "\n" +
                                            "USER ID: " + userID + "\n" +
                                            "INCIDENT TYPE: " + incidentType + "\n" +
                                            "REPORT: " + Report + "\n" +
                                            "LATITUDE: " + Longitude.toString() + "\n" +
                                            "LONGITUDE: " + Latitude.toString() + "\n" +
                                            "IMAGE: " + image + "\n" ); //imageString

                            Call<ReportClassResult> call = retrofitInterface.executeReportSubmit(map);

                            call.enqueue(new Callback<ReportClassResult>() {

                                @Override
                                public void onResponse(Call<ReportClassResult> call, Response<ReportClassResult> response) {
                                    if (response.code() == 200) {
                                        Toast.makeText(MyReportActivity.this, "Pending Report for " + dateTime + " Sent Successfully",
                                                Toast.LENGTH_LONG).show();
                                    } else if (response.code() == 400){
                                        Toast.makeText(MyReportActivity.this, "Error Sending Report",
                                                Toast.LENGTH_LONG).show();
                                    } else if (response.code() == 504){
                                        Toast.makeText(MyReportActivity.this, "Timeout",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ReportClassResult> call, Throwable t) {
                                    Toast.makeText(MyReportActivity.this, t.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                    Log.v("OnFailure Error Message", t.getMessage());
                                }
                            });
                        }
                    });
                }

                Log.v("MyReportActivity.java","My Report Click Successful");
            }

        });
        //setOnClick Report for "Ipasa" button
            //for loop
            //query report using iterator number
            //Send report to DB
            //Delete that report
            //iterator++

        textView_myReport_back = findViewById(R.id.textView_myReport_back);
        textView_myReport_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myReportBack = new Intent(MyReportActivity.this, MainActivity.class);
                startActivity(myReportBack);
            }
        });

    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    private String imageConvertToString(String image) {
        Uri selectedImage = Uri.parse(image);
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();

        Bitmap galleryPhoto = BitmapFactory.decodeFile(filePath);

        //Get the base 64 string
        String img = Base64.encodeToString(getBytesFromBitmap(galleryPhoto),
                Base64.NO_WRAP);
        return img;
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void successDataSending(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_registration);

        MaterialButton button_reportDataSent_ok = dialog.findViewById(R.id.button_reportDataSent_ok);
        button_reportDataSent_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onClick(int position) {
    }
}
