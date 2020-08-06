package up.envisage.mapable;

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

import up.envisage.mapable.adapter.MainMenuAdapter;
import up.envisage.mapable.ui.home.AboutManilaBayActivity;
import up.envisage.mapable.ui.home.FeedbackActivity;
import up.envisage.mapable.ui.home.InformationActivity;
import up.envisage.mapable.ui.home.ReportActivity;
import up.envisage.mapable.ui.Tracker;
import up.envisage.mapable.util.Constant;


public class HomeFragment extends Fragment implements MainMenuAdapter.OnMenuClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FragmentActivity listener;

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.listener = (FragmentActivity) context;
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
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

    public void onClick(int position){
        Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - " + position);
        switch (position) {
            case Constant.mainMenu_aboutManilaBay:
                Intent aboutmanilabay = new Intent(listener, AboutManilaBayActivity.class);
                startActivity(aboutmanilabay);
                Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - About Manila Bay" );
                break;
            case Constant.mainMenu_report:
                Intent report = new Intent(listener, ReportActivity.class);
                startActivity(report);
                Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - Report");
                break;
            case Constant.mainMenu_tracker:
                Intent tracker = new Intent(listener, Tracker.class);
                startActivity(tracker);
                Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - Tracker");
                break;
            case Constant.mainMenu_feedback:
                Intent feedback = new Intent(listener, FeedbackActivity.class);
                startActivity(feedback);
                Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - Feedback");
                break;
            case Constant.mainMenu_information:
                Intent information = new Intent(listener, InformationActivity.class);
                startActivity(information);
                Log.d("[ HomeFragment.java ]", "onMenuClick: clicked - Information");
                break;
        }

    }
}
