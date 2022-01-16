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
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import up.envisage.mapable.adapter.DisclosureAdapter;

public class DisclosureAdapterActivity extends AppCompatActivity {

    private final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private MaterialButton button_disclosure_ok, button_disclosure_cancel;

    String userID;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclosure);

        Intent login = getIntent(); // gets intent from reportingActivity
        userID = login.getStringExtra("userID");

        viewPager = findViewById(R.id.slideViewPager);
        tabLayout = findViewById(R.id.linearLayout_dots);
        tabLayout.setupWithViewPager(viewPager, true);

        DisclosureAdapter disclosureAdapter = new DisclosureAdapter(this);
        viewPager.setAdapter(disclosureAdapter);
        viewPager.addOnPageChangeListener(viewListener);

        //addDotsIndicator(0);

        button_disclosure_ok = findViewById(R.id.button_disclosure_turnon);
        button_disclosure_ok.setOnClickListener(v -> {
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (Build.VERSION.SDK_INT >= 23) {
                    permissionCheck();
                } else {
                    LaunchApp();
                }
            }, 1000);
        });

        button_disclosure_cancel = findViewById(R.id.button_disclosure_cancel);
        button_disclosure_cancel.setOnClickListener(v -> {
            Intent intent = new Intent(DisclosureAdapterActivity.this, MainActivity.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        });

    }

    //----------------------------------------------------------------------------------------------View pager
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                button_disclosure_ok.setVisibility(View.GONE);
                button_disclosure_cancel.setVisibility(View.GONE);
            } else {
                button_disclosure_ok.setVisibility(View.VISIBLE);
                button_disclosure_cancel.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

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
                    showMessageOKCancel(message, (dialog, which) -> {
                        if (Build.VERSION.SDK_INT >= 23) {
                            requestPermissions(permissionList.toArray(new String[permissionList.size()]),
                                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                        }
                    });
                } return;
            } if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(permissionList.toArray(new String[permissionList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
        } else {
            LaunchApp();
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {

        boolean condition;
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                shouldShowRequestPermissionRationale(permission);
            }
        }
        condition = true;
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(DisclosureAdapterActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
            Map<String, Integer> perms = new HashMap<>();
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
                Toast.makeText(DisclosureAdapterActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                        .show();

                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    //Do something after 100
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    getResult.launch(intent);
                    finish();
                }, 1000);
            }

        }
    }

    ActivityResultLauncher<Intent> getResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSION = 124;
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == REQUEST_CODE_ASK_MULTIPLE_PERMISSION) {
                        Intent data = result.getData();
                    }
                }
            });


    //----------------------------------------------------------------------------------------------Launch application and load permissions
    public void LaunchApp() {
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 10 seconds
                    sleep(1000);
                    Intent intent = new Intent(DisclosureAdapterActivity.this, MainActivity.class);
                    intent.putExtra("userID", userID);
                    startActivity(intent);

                    finish();
                } catch (Exception ignored) {
                }
            }
        };
        background.start();
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
