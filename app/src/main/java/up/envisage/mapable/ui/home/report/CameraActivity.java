package up.envisage.mapable.ui.home.report;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

import up.envisage.mapable.R;
import up.envisage.mapable.ui.home.ReportActivity;
import up.envisage.mapable.util.Constant;

public class CameraActivity extends Activity {

    ImageView imageView_reportImage;
    MaterialButton button_reportCamera, button_reportGallery, button_reportSave;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageView_reportImage = findViewById(R.id.imageView_reportCamera);
        button_reportCamera = findViewById(R.id.button_report_camera);
        button_reportGallery = findViewById(R.id.button_report_photoGallery);
        button_reportSave = findViewById(R.id.button_report_savePhoto);

        button_reportCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermissions();
            }
        });

        button_reportSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent save = new Intent(CameraActivity.this, ReportActivity.class);
                startActivity(save);
            }
        });

    }

    public void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, Constant.REQUEST_CODE);
        } else {
            openCamera();
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

    public void openCamera() {
        Log.v("[ CameraActivity.java ]", "Camera Open Request");
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, Constant.CAMERA_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == Constant.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView_reportImage.setImageBitmap(photo);
        }
    }
}
