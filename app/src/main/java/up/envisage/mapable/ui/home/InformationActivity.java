 package up.envisage.mapable.ui.home;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import up.envisage.mapable.BuildConfig;
import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;
import up.envisage.mapable.adapter.InformationAdapter;
import up.envisage.mapable.adapter.ReportIncidentAdapter;

 public class InformationActivity extends AppCompatActivity implements InformationAdapter.OnInformationClickListener {
     @RequiresApi(api = Build.VERSION_CODES.N)
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_information);

         RecyclerView recyclerView = findViewById(R.id.recyclerView_information);
         recyclerView.setHasFixedSize(true);

         RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
         recyclerView.setLayoutManager(layoutManager);

         RecyclerView.Adapter adapter = new InformationAdapter(getApplicationContext(), this);
         recyclerView.setAdapter(adapter);

         TextView textView_information_back = findViewById(R.id.textView_information_back);
         textView_information_back.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent back = new Intent(InformationActivity.this, MainActivity.class);
                 startActivity(back);
             }
         });
     }

     public void onStart() {
         super.onStart();
     }

     public void onResume() {
         super.onResume();
     }

     public void onPause() {
         super.onPause();
     }

     public void onBackPressed() {

     }

     @SuppressLint("LongLogTag")
     @Override
     public void onClick(int position) {
         Log.d("[ InformationActivity.java ]", "onInformationClick: clicked - " + position);
         switch (position) {
             case 0:
                 Intent intent = new Intent(InformationActivity.this, AboutManilaBayActivity.class);
                 startActivity(intent);
                 break;
             case 1:
                 String facebookLink01 = "https://www.facebook.com/ProjectMapABLE/photos/a.143299490614286/224398339171067/";
                 openFacebookPageIntent(facebookLink01);
                 break;
             case 2:
                 String facebookLink02 = "https://www.facebook.com/ProjectMapABLE/photos/a.143299490614286/228755065402061/";
                 openFacebookPageIntent(facebookLink02);
                 break;
             case 3:
                 String facebookLink03 = "https://www.facebook.com/ProjectMapABLE/photos/pcb.232793878331513/232792664998301/";
                 openFacebookPageIntent(facebookLink03);
                 break;
             case 4:
                 String facebookLink04 = "https://www.facebook.com/ProjectMapABLE/photos/a.143299490614286/236249687985932/";
                 openFacebookPageIntent(facebookLink04);
                 break;
             case 5:
                 String facebookLink05 = "https://www.facebook.com/ProjectMapABLE/photos/a.143299490614286/240185767592324/";
                 openFacebookPageIntent(facebookLink05);
                 break;
         }
     }

     //----------------------------------------------------------------------------------------------facebook link of program
     private void openFacebookPageIntent(String url) {
         Context context = null;
         try {
             if (BuildConfig.DEBUG) {
                 throw new AssertionError("Assertion failed");
             }
             ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo("com.facebook.katana", 0);
             if (applicationInfo.enabled) {
                 int versionCode = context.getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
                 String facebookUrl = null;
                 if (versionCode >= 3002850) {
                     facebookUrl = "fb://facewebmodal/f?href=" + url;
                     //facebookUrl = "fb://page/" + url;
                 }
                 startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
             } else {
                 throw new Exception("Facebook is disabled");
             }
         }  catch (PackageManager.NameNotFoundException e) {
             e.printStackTrace();
         } catch (Exception e) {
             startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
         }
     }
 }

