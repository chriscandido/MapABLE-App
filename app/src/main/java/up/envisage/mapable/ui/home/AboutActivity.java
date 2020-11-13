package up.envisage.mapable.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import up.envisage.mapable.R;

public class AboutActivity extends AppCompatActivity {

    private TextView textView_aboutUs_im4manilbayLink, textView_aboutUs_mapableLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        textView_aboutUs_im4manilbayLink = findViewById(R.id.textView_aboutUs_im4manilabayLink);
        textView_aboutUs_mapableLink = findViewById(R.id.textView_aboutUs_mapableLink);

        textView_aboutUs_im4manilbayLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookLink = textView_aboutUs_im4manilbayLink.getText().toString();
                openFacebookIntent(facebookLink);
            }
        });
    }

    private void openFacebookIntent(String url) {
        Context context = null;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                int versionCode = context.getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
                String facebookUrl = null;
                if (versionCode >= 3002850) {
                    facebookUrl = "fb://facewebmodal/f?href=" + url;
                } else {
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