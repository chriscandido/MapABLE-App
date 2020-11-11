package up.envisage.mapable.fragment;

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

import java.util.List;

import up.envisage.mapable.R;
import up.envisage.mapable.db.table.ReportTable;
import up.envisage.mapable.db.table.UserTable;
import up.envisage.mapable.model.ReportViewModel;
import up.envisage.mapable.model.UserViewModel;

public class UserFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FragmentActivity listener;

    TextView textView_user_name, textView_user_username, textView_user_email, textView_user_reports;

    ReportViewModel reportViewModel;
    UserViewModel userViewModel;

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
        textView_user_reports = view.findViewById(R.id.textView_user_reports);

        userDetails();
        reportDetails();

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

    //----------------------------------------------------------------------------------------------Get report details from local db
    public void reportDetails() {
        reportViewModel = ViewModelProviders.of(UserFragment.this).get(ReportViewModel.class);
        reportViewModel.getAllReports().observe(UserFragment.this, new Observer<List<ReportTable>>() {
            @Override
            public void onChanged(List<ReportTable> reportTables) {
                int numOfReports = reportTables.size();
                textView_user_reports.setText(String.valueOf(numOfReports));
            }
        });
    }
}
