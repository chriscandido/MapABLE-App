package up.envisage.mapable.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import up.envisage.mapable.R;
import up.envisage.mapable.adapter.MainMenuAdapter;
import up.envisage.mapable.ui.home.AboutActivity;
import up.envisage.mapable.ui.home.InformationActivity;
import up.envisage.mapable.ui.home.LeaderboardActivity;
import up.envisage.mapable.ui.home.ReportingActivity;
import up.envisage.mapable.util.Constant;

public class HomeFragment extends Fragment implements MainMenuAdapter.OnMenuClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FragmentActivity listener;

    String userID;

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
        layoutManager = new LinearLayoutManager(listener);
        recyclerView.setLayoutManager(layoutManager);

        //Recycler view adapter
        adapter = new MainMenuAdapter(listener,  this);
        recyclerView.setAdapter(adapter);
    }

    //----------------------------------------------------------------------------------------------Main Menu Features
    public void onClick(int position){
        Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - " + position);
        switch (position) {
            case Constant.mainMenu_report:
                Intent report = new Intent(listener, ReportingActivity.class);
                report.putExtra("userID", userID);
                startActivity(report);
                Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - Report");
                break;
            case Constant.mainMenu_information:
                Intent information = new Intent(listener, InformationActivity.class);
                startActivity(information);
                Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - Information");
                break;
            case Constant.mainMenu_about:
                Intent about = new Intent(listener, AboutActivity.class);
                startActivity(about);
                Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - About");
                break;
            case Constant.mainMenu_Leaderboard:
                Intent report2 = new Intent(listener, LeaderboardActivity.class);
//                report2.putExtra("userID", userID);
                startActivity(report2);
                Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - Report");
                break;
        }
    }
}
