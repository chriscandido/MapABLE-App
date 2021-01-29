package up.envisage.mapable.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import up.envisage.mapable.ui.home.FeedbackActivity;
import up.envisage.mapable.ui.home.MyReportActivity;


public class SupportFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FragmentActivity listener;

    private TextView textView_support_about, textView_support_policy, textView_support_feedback;

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

        textView_support_about = view.findViewById(R.id.textView_support_about);
        textView_support_policy = view.findViewById(R.id.textView_support_policy);
        textView_support_feedback = view.findViewById(R.id.textView_support_feedback);

        textView_support_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myReport = new Intent(listener, FeedbackActivity.class);
                startActivity(myReport);
            }
        });

    }

}