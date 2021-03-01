package up.envisage.mapable.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import up.envisage.mapable.R;
import up.envisage.mapable.db.table.ReportTable;
import up.envisage.mapable.db.table.UserTable;
import up.envisage.mapable.model.ReportViewModel;
import up.envisage.mapable.model.UserViewModel;
import up.envisage.mapable.ui.home.MyReportActivity;
import up.envisage.mapable.ui.home.ReportIncidentActivity;
import up.envisage.mapable.ui.home.ReportingActivity;

public class UserFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FragmentActivity listener;

    private Dialog dialog;

    TextView textView_user_name, textView_user_username, textView_user_email, textView_user_myReport,
            textView_user_myStats, textView_myStats_submittedReports;

    MaterialButton button_userMyStats_ok;

    ReportViewModel reportViewModel;
    UserViewModel userViewModel;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        textView_user_name = view.findViewById(R.id.textView_user_name);
        textView_user_username = view.findViewById(R.id.textView_user_username);
        textView_user_email = view.findViewById(R.id.textView_user_email);

        userDetails();

        //Pending reports button
        textView_user_myReport = view.findViewById(R.id.textView_user_myReport);
        textView_user_myReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myReport = new Intent(listener, MyReportActivity.class);
                startActivity(myReport);
            }
        });

        //declare view for Verified Reports
        //Set OnClick Listener
        //Define OnClick method
        //Intent to Verified Reports

        //Stats button
        textView_user_myStats = view.findViewById(R.id.textView_user_myStats);
        textView_user_myStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStatsDialog();
            }
        });
    }

    //----------------------------------------------------------------------------------------------Get report stats of user
    public void myStatsDialog() {
        dialog = new Dialog(listener);
        dialog.setContentView(R.layout.popup_mystats);

        textView_myStats_submittedReports = dialog.findViewById(R.id.textView_myStats_submittedReports);
        button_userMyStats_ok = dialog.findViewById(R.id.button_userMyStats_ok);
        button_userMyStats_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    //----------------------------------------------------------------------------------------------Get user details from local db
    public void userDetails() {
        userViewModel = ViewModelProviders.of(UserFragment.this).get(UserViewModel.class);
        userViewModel.getLastUser().observe(UserFragment.this, new Observer<UserTable>() {
            @Override
            public void onChanged(UserTable userTable) {
                String name = userTable.getName();
                String username = userTable.getUsername();
                String email = userTable.getEmail();
                textView_user_name.setText(name);
                textView_user_username.setText("@" + username);
                textView_user_email.setText(email);
            }
        });
    }

}
