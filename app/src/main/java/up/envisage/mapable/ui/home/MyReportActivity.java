package up.envisage.mapable.ui.home;

import android.annotation.SuppressLint;
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
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
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
import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;
import up.envisage.mapable.adapter.MyReportAdapter;
import up.envisage.mapable.db.table.ReportTable;
import up.envisage.mapable.model.ReportViewModel;
import up.envisage.mapable.ui.home.report.ReportClassResult;
import up.envisage.mapable.ui.registration.RetrofitInterface;

public class MyReportActivity extends AppCompatActivity implements MyReportAdapter.OnReportClickListener {

    private RecyclerView recyclerView;
    private ReportViewModel reportViewModel;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

    private Dialog dialog;
    private Button button_myReport_save;
    private CheckBox checkBox_myReport;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://ec2-54-91-89-105.compute-1.amazonaws.com/";

    Integer i, reportId, count;
    String userID, dateTime, incidentType, Report, lon, lat, image, imageString, outUserId, flag;
    Double Longitude, Latitude;
    Boolean connection;
    int dragFlags = 0;
    int swipeFlags;

    ArrayList<ReportTable> sentReportList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);

        swipeInstruction();

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

        connection = isNetworkAvailable();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        reportViewModel = ViewModelProviders.of(MyReportActivity.this).get(ReportViewModel.class);
        reportViewModel.getAllReports().observe(MyReportActivity.this, new Observer<List<ReportTable>>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onChanged(List<ReportTable> reportTables) {
                adapter = new MyReportAdapter(getApplicationContext(), MyReportActivity.this::onClick, reportTables);
                recyclerView = findViewById(R.id.recyclerView_myReport);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                count = reportTables.size();

                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, swipeFlags) {

                    @Override
                    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                        if(connection) {
                            swipeFlags = ItemTouchHelper.RIGHT;
                        }
                        else {
                            swipeFlags = 0;
                            errorNoConnection();
                        }
                        return makeMovementFlags(dragFlags, swipeFlags);
                    }

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                        userID = reportTables.get(viewHolder.getAdapterPosition()).getUniqueId();
                        dateTime = reportTables.get(viewHolder.getAdapterPosition()).getDateTime();
                        incidentType = reportTables.get(viewHolder.getAdapterPosition()).getIncidentType();
                        Report = reportTables.get(viewHolder.getAdapterPosition()).getReport();
                        Longitude = reportTables.get(viewHolder.getAdapterPosition()).getLongitude();
                        Latitude = reportTables.get(viewHolder.getAdapterPosition()).getLatitude();
                        image = reportTables.get(viewHolder.getAdapterPosition()).getPhoto();
                        reportId = reportTables.get(viewHolder.getAdapterPosition()).getReportId();

                        imageString = imageConvertToString(image);

                        lon = Longitude.toString();
                        lat = Latitude.toString();

                        HashMap<String, String> map = new HashMap<>();
                        map.put("userID", userID);
                        map.put("date", dateTime);
                        map.put("type", incidentType);
                        map.put("report", Report);
                        map.put("lon", lon);
                        map.put("lat", lat);
                        map.put("image", imageString); //imageString

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
                                if (response.code() == 400){
                                    Toast.makeText(MyReportActivity.this, "Error Sending Report",
                                            Toast.LENGTH_LONG).show();
                                    successDataSending();
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

                        reportViewModel.delete(reportTables.get(viewHolder.getAdapterPosition()));
                    }

                }).attachToRecyclerView(recyclerView);
            }
        });

        Button button_myReport_save = findViewById(R.id.button_myReport_save);


        if(connection){
            button_myReport_save.setVisibility(View.VISIBLE);
        } else {
            button_myReport_save.setVisibility(View.GONE);
        };

        button_myReport_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(i = 0; i<count; i++) {

                    sentReportList.add(reportViewModel.getAllReports().getValue().get(i));

                    Log.v("[MyReportActivity.java]", "Pending Report " + i + String.valueOf(reportViewModel.getAllReports().getValue().get(i)));

                    userID = reportViewModel.getAllReports().getValue().get(i).getUniqueId();
                    dateTime = reportViewModel.getAllReports().getValue().get(i).getDateTime();
                    incidentType = reportViewModel.getAllReports().getValue().get(i).getIncidentType();
                    Report = reportViewModel.getAllReports().getValue().get(i).getReport();
                    Longitude = reportViewModel.getAllReports().getValue().get(i).getLongitude();
                    Latitude = reportViewModel.getAllReports().getValue().get(i).getLatitude();
                    image = reportViewModel.getAllReports().getValue().get(i).getPhoto();

                    imageString = imageConvertToString(image);

                    lon = Longitude.toString();
                    lat = Latitude.toString();

                    HashMap<String, String> map = new HashMap<>();
                    map.put("userID", userID);
                    map.put("date", dateTime);
                    map.put("type", incidentType);
                    map.put("report", Report);
                    map.put("lon", lon);
                    map.put("lat", lat);
                    map.put("image", imageString); //imageString

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
                            if (response.code() == 400){
                                Toast.makeText(MyReportActivity.this, "Error Sending Report",
                                        Toast.LENGTH_LONG).show();
                                successDataSending();
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

                    reportViewModel.delete(sentReportList.get(i));
                }

                for(i=0; i<sentReportList.size(); i++) {
                    Log.v("[MyReportActivity.java]", "Sent Report " + i + " " + sentReportList.get(i));
                }
            }
        });

        TextView textView_myReport_back = findViewById(R.id.textView_myReport_back);
        textView_myReport_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                reportViewModel = ViewModelProviders.of(MyReportActivity.this).get(ReportViewModel.class);
                reportViewModel.getAllReports().observe(MyReportActivity.this, new Observer<List<ReportTable>>() {
                    @Override
                    public void onChanged(List<ReportTable> reportTables) {
                        int count = reportTables.size();
                        for (i = 0; i<count; i++){
                            reportViewModel.delete(reportTables.get(i));
                        }
                    }
                });**/

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

    //----------------------------------------------------------------------------------------------Popup for successful data sending
    private void successDataSending(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_success_registration);

        dialog.show();
    }

    //----------------------------------------------------------------------------------------------Popup for internet connection failure
    private void errorNoConnection(){
        dialog = new Dialog(MyReportActivity.this);
        dialog.setContentView(R.layout.popup_error_nointernet);

        dialog.show();
    }

    //----------------------------------------------------------------------------------------------Popup for swipe instruction
    private void swipeInstruction(){
        dialog = new Dialog(MyReportActivity.this);
        dialog.setContentView(R.layout.popup_instruction_swipe);

        MaterialButton button_userProfile_swipe = dialog.findViewById(R.id.button_userprofileSwipe_ok);
        button_userProfile_swipe.setOnClickListener(new View.OnClickListener() {
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
