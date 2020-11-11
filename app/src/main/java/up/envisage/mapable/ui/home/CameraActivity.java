package up.envisage.mapable.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.File;
import java.nio.ByteBuffer;

import up.envisage.mapable.R;
import up.envisage.mapable.db.table.ReportTable;
import up.envisage.mapable.model.ReportViewModel;
import up.envisage.mapable.model.UserViewModel;
import up.envisage.mapable.ui.registration.RegisterActivity;
import up.envisage.mapable.util.Constant;

public class CameraActivity extends AppCompatActivity {

    private ImageView imageView_reportImage;
    private MaterialButton button_reportCamera, button_reportGallery, button_reportSave;
    private TextView textView_cameraBack;
    private ReportViewModel reportViewModel;

    String userID, dateTime, incidentType, answer, latitude, longitude, imgPath;
    public static String imageString;
    Bitmap galleryPhoto;
    byte[] byteArray;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        reportViewModel = ViewModelProviders.of(CameraActivity.this).get(ReportViewModel.class);

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

        //Button go to gallery
        button_reportGallery = findViewById(R.id.button_report_photoGallery);
        button_reportGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askGalleryPermission();
            }
        });

        //Button save photo
        button_reportSave = findViewById(R.id.button_report_savePhoto);
        button_reportSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportTable report = new ReportTable();
                report.setUniqueId(userID);
                report.setDateTime(dateTime);
                report.setIncidentType(incidentType);
                report.setReport(answer);
                report.setLatitude(Double.parseDouble(latitude));
                report.setLongitude(Double.parseDouble(longitude));
                report.setPhoto(imgPath);
                reportViewModel.insert(report);
                Intent save = new Intent(CameraActivity.this, ReportingActivity.class);

                save.putExtra("userID", userID);
                save.putExtra("Date and Time", dateTime);
                save.putExtra("Incident Type", incidentType);
                save.putExtra("Report", answer);
                save.putExtra("Latitude", latitude);
                save.putExtra("Longitude", longitude);
                save.putExtra("image", imgPath);
                //save.putExtra("image", imageString);

                startActivity(save);
                Toast.makeText(CameraActivity.this, "Photo successfully saved", Toast.LENGTH_LONG).show();
            }
        });

        //Text Button back to Main Menu
        textView_cameraBack = findViewById(R.id.textView_camera_back);
        textView_cameraBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(CameraActivity.this, ReportingActivity.class);
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
                    String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), cameraPhoto, "Image", null);
                    Uri selectedImage = Uri.parse(path);
                    imgPath = selectedImage.toString();

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

    // convert from bitmap to byte array (with compression)
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    // convert from bitmap to byte array (without compression)
    public static byte[] convertBitmapToByteArrayUncompressed(Bitmap bitmap){
        ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(byteBuffer);
        byteBuffer.rewind();
        return byteBuffer.array();
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
    }
}
