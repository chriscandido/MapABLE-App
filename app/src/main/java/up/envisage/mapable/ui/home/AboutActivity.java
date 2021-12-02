package up.envisage.mapable.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        /*TextView textView_aboutUs_back = findViewById(R.id.textView_aboutUs_back);
        TextView textView_aboutUs_im4manilbayLink = findViewById(R.id.textView_aboutUs_im4manilabayLink);
        TextView textView_aboutUs_iwasto = findViewById(R.id.textView_aboutUs_iwasto);
        TextView textView_aboutUs_esmart = findViewById(R.id.textView_aboutUs_esmart);
        TextView textView_aboutUs_mapableLink = findViewById(R.id.textView_aboutUs_mapableLink);

        //Button go back
        textView_aboutUs_back.setOnClickListener(v -> {
            Intent back = new Intent(AboutActivity.this, MainActivity.class);
            startActivity(back);
        });

        //Button go to IM4ManilaBay link
        textView_aboutUs_im4manilbayLink.setOnClickListener(v -> {
            String facebookLink = "https://www.facebook.com/IM4ManilaBay";
            openFacebookIntent(facebookLink);
        });

        //Button go to iWASTO
        textView_aboutUs_iwasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookLink = "https://www.facebook.com/projectiwasto";
                openFacebookIntent(facebookLink);
            }
        });

        //Button go to eSMART
        textView_aboutUs_esmart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookLink = "https://www.facebook.com/esmart.im4manilabay";
                openFacebookIntent(facebookLink);
            }
        });

        //Button go to Project MapABLE link
        textView_aboutUs_mapableLink.setOnClickListener(v -> {
            String facebookLink = "https://www.facebook.com/ProjectMapABLE";
            openFacebookIntent(facebookLink);
        });
    }


    //----------------------------------------------------------------------------------------------facebook link of program
    private void openFacebookIntent(String url) {
        Context context = null;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                int versionCode = context.getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
                String facebookUrl = null;
                if (versionCode >= 3002850) {
                    facebookUrl = "fb://facewebmodal/f?href=" + url;
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
    }*/
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
        Intent intent = new Intent(AboutActivity.this, MainActivity.class);
        startActivity(intent);
    }
}