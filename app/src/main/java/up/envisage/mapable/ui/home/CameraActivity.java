package up.envisage.mapable.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Calendar;
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
import up.envisage.mapable.db.table.ReportTable;
import up.envisage.mapable.fragment.GoogleMapFragment;
import up.envisage.mapable.model.ReportViewModel;
import up.envisage.mapable.model.UserViewModel;
import up.envisage.mapable.ui.home.report.ReportClassResult;
import up.envisage.mapable.util.Constant;

public class CameraActivity extends AppCompatActivity {

    private ImageView imageView_reportImage, imageView_camera_back;
    private MaterialButton button_reportCamera, button_reportGallery, button_reportSave;

    private ReportViewModel reportViewModel;
    private UserViewModel userViewModel;

    String userID, dateTime, incidentType, answer, latitude, longitude, imgPath, outUserId;
    public static String imageString;
    Bitmap galleryPhoto;
    byte[] byteArray;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://ec2-54-91-89-105.compute-1.amazonaws.com/";
//    private String BASE_URL = "http://10.0.2.2:5000/";

    private Dialog dialog;

    @SuppressLint("LongLogTag")
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        //Initiating report class
        reportViewModel = ViewModelProviders.of(CameraActivity.this).get(ReportViewModel.class);
        userViewModel = ViewModelProviders.of(CameraActivity.this).get(UserViewModel.class);

        userViewModel.getLastUser().observe(CameraActivity.this, UserTable -> {
            outUserId = UserTable.getUniqueId();
            Log.v("[ReportingActivity.java userID from local]:", "UserID: " + outUserId);
        });

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        //Set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        //Add your other interceptors …
        httpClient.callTimeout(2, TimeUnit.MINUTES)
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

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Intent camera = getIntent();
        userID = camera.getStringExtra("userID");
        dateTime = camera.getStringExtra("Date and Time");
        incidentType = camera.getStringExtra("Incident Type");
        answer = camera.getStringExtra("Report");
        latitude = camera.getStringExtra("Latitude");
        longitude = camera.getStringExtra("Longitude");

        imageView_reportImage = findViewById(R.id.imageView_reportCamera);

        //Button camera
        button_reportCamera = findViewById(R.id.button_report_camera);
        button_reportCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermissions();
            }
        });

        // Button go to gallery
        button_reportGallery = findViewById(R.id.button_report_photoGallery);
        button_reportGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askGalleryPermission();
            }
        });

        // Button send report
        button_reportSave = findViewById(R.id.button_report_savePhoto);
        button_reportSave.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {
                if (imgPath == null) {
                    selectPhoto();
                } else {
/*                    Intent save = new Intent(CameraActivity.this, ReportingActivity.class);

                    Log.v("[ CameraActivity.java ]", "Image Path: " + imgPath  + "\n");

                    save.putExtra("userID", userID);
                    save.putExtra("Date and Time", dateTime);
                    save.putExtra("Incident Type", incidentType);
                    save.putExtra("Report", answer);
                    save.putExtra("Latitude", latitude);
                    save.putExtra("Longitude", longitude);
                    save.putExtra("image", imgPath);

                    startActivity(save);
                    Toast.makeText(CameraActivity.this, "Photo successfully saved", Toast.LENGTH_LONG).show();*/
                    Log.v("[ReportingActivity.java ]", "Image Path: " + imgPath  + "\n");

                    imageString = imageConvertToString(imgPath);
                    Log.v("[ ReportingActivity.java ]",
                            "IMAGE STRING: " + imageString);

                    HashMap<String, String> map = new HashMap<>();
                    map.put("userID", userID);
                    map.put("date", dateTime);
                    map.put("type", incidentType);
                    map.put("report", answer);
                    map.put("lon", longitude);
                    map.put("lat", latitude);
                    map.put("image", imageString); //imageString

                    Log.v("[ ReportingActivity.java ]",
                            "DATE & TIME: " + dateTime + "\n" +
                                    "USER ID: " + userID + "\n" +
                                    "INCIDENT TYPE: " + incidentType + "\n" +
                                    "REPORT: " + answer + "\n" +
                                    "LATITUDE: " + latitude + "\n" +
                                    "LONGITUDE: " + longitude + "\n" +
                                    "IMAGE: " + imageString + "\n" ); //imageString

                    if(isNetworkAvailable()){

                        Call<ReportClassResult> call = retrofitInterface.executeReportSubmit(map);

                        call.enqueue(new Callback<ReportClassResult>() {
                            @Override

                            public void onResponse(Call<ReportClassResult> call, Response<ReportClassResult> response) {
                                if (response.code() == 200) {
                                    Toast.makeText(CameraActivity.this, "Report Sent Successfully",
                                            Toast.LENGTH_LONG).show();
                                } else if (response.code() == 400){
                                    Toast.makeText(CameraActivity.this, "Error Sending Report",
                                            Toast.LENGTH_LONG).show();
                                    ReportTable report = new ReportTable();
                                    report.setUniqueId(outUserId);
                                    report.setDateTime(dateTime);
                                    report.setIncidentType(incidentType);
                                    report.setReport(answer);
                                    report.setLatitude(Double.parseDouble(latitude));
                                    report.setLongitude(Double.parseDouble(longitude));
                                    report.setPhoto(imgPath);
                                    reportViewModel.insert(report);
                                } else if (response.code() == 504){
                                    Toast.makeText(CameraActivity.this, "Timeout",
                                            Toast.LENGTH_LONG).show();
                                    ReportTable report = new ReportTable();
                                    report.setUniqueId(outUserId);
                                    report.setDateTime(dateTime);
                                    report.setIncidentType(incidentType);
                                    report.setReport(answer);
                                    report.setLatitude(Double.parseDouble(latitude));
                                    report.setLongitude(Double.parseDouble(longitude));
                                    report.setPhoto(imgPath);
                                    reportViewModel.insert(report);
                                }
                            }

                            @Override
                            public void onFailure(Call<ReportClassResult> call, Throwable t) {
                                Toast.makeText(CameraActivity.this, t.getMessage(),
                                        Toast.LENGTH_LONG).show();
                                Log.v("OnFailure Error Message", t.getMessage());

                                //if onFailure, report is stored in the local database
                                ReportTable report = new ReportTable();
                                report.setUniqueId(outUserId);
                                report.setDateTime(dateTime);
                                report.setIncidentType(incidentType);
                                report.setReport(answer);
                                report.setLatitude(Double.parseDouble(latitude));
                                report.setLongitude(Double.parseDouble(longitude));
                                report.setPhoto(imgPath);
                                reportViewModel.insert(report);
                            }
                        });
                    } else {
                        //if no internet connection, report is stored in the local database
                        //Toast.makeText(ReportingActivity.this, "No Internet Connection. Report will be saved in the device!",
                        //Toast.LENGTH_LONG).show();
                        ReportTable report = new ReportTable();
                        report.setUniqueId(outUserId);
                        report.setDateTime(dateTime);
                        report.setIncidentType(incidentType);
                        report.setReport(answer);
                        report.setLatitude(Double.parseDouble(latitude));
                        report.setLongitude(Double.parseDouble(longitude));
                        report.setPhoto(imgPath);
                        reportViewModel.insert(report);

                        errorNoConnection();
                    }

                    Intent goToMain = new Intent(CameraActivity.this, MainActivity.class);
                    goToMain.putExtra("userID", userID);
                    startActivity(goToMain);

                }
            }
        });

        // Back button
        imageView_camera_back = findViewById(R.id.imageView_camera_back);
        imageView_camera_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(CameraActivity.this, GoogleMapFragment.class);
                startActivity(back);
            }
        });

    }

    //----------------------------------------------------------------------------------------------Ask for Camera Permission
    public void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, Constant.REQUEST_CODE);
        } else {
            openCamera();
        }
    }

    //----------------------------------------------------------------------------------------------Ask for Gallery Permission
    public void askGalleryPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.ACTIVITY_SELECT_IMAGE);
        } else {
            openGallery();
        }
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constant.REQUEST_CODE){
            if (grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Log.e("[ CameraActivity.java ]", "Camera permission is required to use camera");
            }
        }
    }

    //----------------------------------------------------------------------------------------------Open Camera
    public void openCamera() {
        Log.v("[ CameraActivity.java ]", "Camera Open Request");
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, Constant.CAMERA_REQUEST_CODE);
    }

    //----------------------------------------------------------------------------------------------Open Gallery
    public void openGallery() {
        Log.v("[ CameraActivity.java ]", "Gallery Open Request");
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, Constant.ACTIVITY_SELECT_IMAGE);
    }

    //----------------------------------------------------------------------------------------------Load Photo from Camera/Gallery
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.CAMERA_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap cameraPhoto = (Bitmap) data.getExtras().get("data");

                    //Get image file path
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    cameraPhoto.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), cameraPhoto, "IMG_" + Calendar.getInstance().getTime(), null);
                    Uri selectedImage = Uri.parse(path);
                    imgPath = selectedImage.toString();

                    Log.v("[ CameraActivity.java ]", "Base64: " + imgPath  + "\n");

                    //Get the base 64 string
                    imageString = Base64.encodeToString(getBytesFromBitmap(cameraPhoto),
                            Base64.NO_WRAP);

                    Log.v("[ CameraActivity.java ]", "Base64: " + imageString  + "\n");

                    imageView_reportImage.setImageBitmap(cameraPhoto);
                }
                break;

            case Constant.ACTIVITY_SELECT_IMAGE:
                if (resultCode == Activity.RESULT_OK){
                    Uri selectedImage = data.getData();
                    imgPath = selectedImage.toString();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    galleryPhoto = BitmapFactory.decodeFile(filePath);

                    // get the base 64 string
                    imageString = Base64.encodeToString(getBytesFromBitmap(galleryPhoto),
                            Base64.NO_WRAP);

                    Log.v("[ CameraActivity.java ]",
                            "Base64: " + imageString  + "\n");

                    imageView_reportImage.setImageBitmap(galleryPhoto);
                }
                break;
        }
    }

    //----------------------------------------------------------------------------------------------Convert from bitmap to byte array (with compression)
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    //----------------------------------------------------------------------------------------------convert from bitmap to byte array (without compression)
    public static byte[] convertBitmapToByteArrayUncompressed(Bitmap bitmap){
        ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(byteBuffer);
        byteBuffer.rewind();
        return byteBuffer.array();
    }

    //----------------------------------------------------------------------------------------------Popup for no selected photo
    private void selectPhoto(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_error_nophoto);

        MaterialButton button_reportSelectPhoto_ok = dialog.findViewById(R.id.button_reportSelectPhoto_ok);
        button_reportSelectPhoto_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //----------------------------------------------------------------------------------------------check network availability
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    //----------------------------------------------------------------------------------------------convert image to string
    public String imageConvertToString(String image) {
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

    //----------------------------------------------------------------------------------------------Popup for successful data sending
    private void successDataSending(){
        dialog = new Dialog(CameraActivity.this);
        dialog.setContentView(R.layout.popup_success_datasent);

        dialog.show();
    }

    //----------------------------------------------------------------------------------------------Popup for internet connection failure
    private void errorNoConnection(){
        dialog = new Dialog(CameraActivity.this);
        dialog.setContentView(R.layout.popup_error_nointernet);

        dialog.show();

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
        finish();
    }

}
