package up.envisage.mapable.fragment;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import up.envisage.mapable.R;
import up.envisage.mapable.adapter.MainMenuAdapter;
import up.envisage.mapable.databinding.Listener;
import up.envisage.mapable.ui.home.AboutActivity;
import up.envisage.mapable.ui.home.AboutManilaBayActivity;
import up.envisage.mapable.ui.home.InformationActivity;
import up.envisage.mapable.ui.home.LeaderboardActivity;
import up.envisage.mapable.ui.home.MyReportsListActivity;
import up.envisage.mapable.ui.home.ReportingActivity;
import up.envisage.mapable.util.Constant;

public class HomeFragment extends Fragment implements MainMenuAdapter.OnMenuClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FragmentActivity listener;
    private RelativeLayout relativeLayout_mainHeader;

    String userID;

    private ImageView imageView_mainMenu_alaminnatin, imageView_mainMenu_im4manilabay;

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.listener = (FragmentActivity) context;
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent homeFragment = Objects.requireNonNull(getActivity()).getIntent();
        userID = homeFragment.getStringExtra("userID");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView_mainMenu);
        recyclerView.setHasFixedSize(true);

        //Linear layout manager
        layoutManager = new LinearLayoutManager(listener, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //Recycler view adapter
        adapter = new MainMenuAdapter(listener,  this);
        recyclerView.setAdapter(adapter);

        imageView_mainMenu_alaminnatin = view.findViewById(R.id.imageView_mainMenu_alaminnatin);
        imageView_mainMenu_im4manilabay = view.findViewById(R.id.imageView_mainMenu_im4ManilaBay);

        imageView_mainMenu_alaminnatin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent alaminIntent = new Intent(listener, AboutManilaBayActivity.class);

                startActivity(alaminIntent);
            }
        });

        imageView_mainMenu_im4manilabay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent ima4manilaBayIntent = new Intent(listener, AboutActivity.class);
                startActivity(ima4manilaBayIntent);
            }
        });
    }


    //----------------------------------------------------------------------------------------------Main Menu Features
    public void onClick(int position){
        Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - " + position);
        switch (position) {
            case 0:
                String facebookLink01 = "https://www.facebook.com/ProjectMapABLE/photos/a.143299490614286/224398339171067/";
                openFacebookPageIntent(facebookLink01);
                Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - link01");
                break;
            case 1:
                String facebookLink02 = "https://www.facebook.com/ProjectMapABLE/photos/a.143299490614286/228755065402061/";
                openFacebookPageIntent(facebookLink02);
                Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - link02");
                break;
            case 2:
                String facebookLink03 = "https://www.facebook.com/ProjectMapABLE/photos/pcb.232793878331513/232792664998301/";
                openFacebookPageIntent(facebookLink03);
                Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - link03");
                break;
            case 3:
                String facebookLink04 = "https://www.facebook.com/ProjectMapABLE/photos/a.143299490614286/236249687985932/";
                openFacebookPageIntent(facebookLink04);
                Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - link04");
                break;
            case 4:
                String facebookLink05 = "https://www.facebook.com/ProjectMapABLE/photos/a.143299490614286/240185767592324/";
                openFacebookPageIntent(facebookLink05);
                Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - link05");
                break;
            case 5:
                String facebookLink06 = "https://www.facebook.com/ProjectMapABLE/photos/a.143299490614286/248369220107312/";
                openFacebookPageIntent(facebookLink06);
                Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - link06");
                break;
        }
    }

    //----------------------------------------------------------------------------------------------facebook link of program
    private void openFacebookPageIntent(String url) {
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
    }
}
