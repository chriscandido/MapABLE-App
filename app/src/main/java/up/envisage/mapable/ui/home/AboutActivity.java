package up.envisage.mapable.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.FacebookSdk;

import bolts.AppLink;
import bolts.AppLinks;
import up.envisage.mapable.BuildConfig;
import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;

public class AboutActivity extends AppCompatActivity {

    TextView textView_aboutUs_im4manilbayLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        FacebookSdk.sdkInitialize(this);

        TextView textView_aboutUs_back = findViewById(R.id.textView_aboutUs_back);
        textView_aboutUs_im4manilbayLink = findViewById(R.id.textView_aboutUs_im4manilabayLink);
        TextView textView_aboutUs_mapableLink = findViewById(R.id.textView_aboutUs_mapableLink);

        textView_aboutUs_back.setOnClickListener(v -> {
            Intent back = new Intent(AboutActivity.this, MainActivity.class);
            startActivity(back);
        });


        textView_aboutUs_im4manilbayLink.setOnClickListener(v -> {
            String facebookLink = "https://www.facebook.com/IM4ManilaBay";
            //String facebookLink = "IM4ManilaBay";
            openFacebookIntent(facebookLink);
        });

        textView_aboutUs_mapableLink.setOnClickListener(v -> {
            String facebookLink = "https://www.facebook.com/ProjectMapABLE";
            //String facebookLink = "ProjectMapABLE";
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