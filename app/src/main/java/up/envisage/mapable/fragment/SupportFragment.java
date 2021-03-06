package up.envisage.mapable.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import up.envisage.mapable.R;
import up.envisage.mapable.ui.support.DevTeamActivity;
import up.envisage.mapable.ui.support.FeedbackActivity;

public class SupportFragment extends Fragment {

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

    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState){
        return layoutInflater.inflate(R.layout.fragment_support, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        TextView textView_support_about = view.findViewById(R.id.textView_support_about);
        TextView textView_support_policy = view.findViewById(R.id.textView_support_policy);
        TextView textView_support_feedback = view.findViewById(R.id.textView_support_feedback);
        TextView textView_support_devTeam = view.findViewById(R.id.textView_support_devTeam);

        //About MASDAN App button
        textView_support_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aboutLink = "http://ec2-54-91-89-105.compute-1.amazonaws.com/about";
                openWebsiteLink(aboutLink);
            }
        });

        //Policy link button
        textView_support_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String policyLink = "http://ec2-54-91-89-105.compute-1.amazonaws.com/privacypolicy";
                openWebsiteLink(policyLink);
            }
        });

        //Feedback question button
        textView_support_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myReport = new Intent(listener, FeedbackActivity.class);
                startActivity(myReport);
            }
        });

        //Dev Team button
        textView_support_devTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent devTeam = new Intent(listener, DevTeamActivity.class);
                startActivity(devTeam);
            }
        });
    }

    //----------------------------------------------------------------------------------------------Parsing of website link
    public void openWebsiteLink(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

}