package up.envisage.mapable;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import up.envisage.mapable.ui.registration.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= 23) {
                    permissionCheck();
                } else {
                    LaunchApp();
                }
            }
        }, 3000);

        //startActivity(new Intent(this, LoginActivity.class));
        //finish();
    }

    private void permissionCheck() {
        List<String> permissionNeeded = new ArrayList<String>();

        final List<String> permissionList = new ArrayList<String>();
        if (!addPermission(permissionList, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            permissionNeeded.add("READ");
        }
        if (!addPermission(permissionList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissionNeeded.add("WRITE");
        }
        if (!addPermission(permissionList, Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionNeeded.add("FINE");
        }
        if (!addPermission(permissionList, Manifest.permission.CAMERA)) {
            permissionNeeded.add("CAMERA");
        }

        if (permissionList.size() > 0) {
            if (permissionNeeded.size() > 0) {
                //Need Rationale
                String message = "You need to grant access to " + permissionNeeded.get(0);
                for (int i = 1; i < permissionNeeded.size(); i++) {
                    message = message + ", " + permissionNeeded.get(i);
                    showMessageOKCancel(message, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= 23) {
                                requestPermissions(permissionList.toArray(new String[permissionList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            } else {

                            }
                        }
                    });
                } return;
            } if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(permissionList.toArray(new String[permissionList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            } else {

            }
            return;
        } else {
            LaunchApp();
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {

        Boolean condition;
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    condition = false;
            }
            condition = true;
        } else {
            condition = true;
        }
        return condition;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SplashActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == 23) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Permission Needed To Run The App", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            Map<String, Integer> perms = new HashMap<String, Integer>();
            // Initial
            perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
            // Fill with results
            for (int i = 0; i < permissions.length; i++)
                perms.put(permissions[i], grantResults[i]);
            // Check for ACCESS_FINE_LOCATION
            if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                LaunchApp();

            } else {
                // Permission Denied
                Toast.makeText(SplashActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                        .show();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                        finish();
                    }
                }, 3000);
            }

        }
    }

    public void LaunchApp() {
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 10 seconds
                    sleep(4*1000);
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                    finish();
                } catch (Exception e) {
                }
            }
        };
        background.start();
    }
}
