package up.envisage.mapable.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import up.envisage.mapable.R;
import up.envisage.mapable.adapter.MyReportAdapter;
import up.envisage.mapable.db.table.ReportTable;
import up.envisage.mapable.fragment.UserFragment;
import up.envisage.mapable.model.ReportViewModel;

public class MyReportActivity extends AppCompatActivity implements MyReportAdapter.OnReportClickListener {

    private RecyclerView recyclerView;
    private ReportViewModel reportViewModel;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

    private TextView textView_myReport_back;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);

        reportViewModel = ViewModelProviders.of(MyReportActivity.this).get(ReportViewModel.class);
        reportViewModel.getAllReports().observe(MyReportActivity.this, new Observer<List<ReportTable>>() {
            @Override
            public void onChanged(List<ReportTable> reportTables) {
                adapter = new MyReportAdapter(getApplicationContext(), MyReportActivity.this::onClick, reportTables);
                recyclerView = findViewById(R.id.recyclerView_myReport);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                Log.v("[ MyReportActivity ]", "Number of Reports: " + reportTables.get(1).getIncidentType());
            }
        });

        textView_myReport_back = findViewById(R.id.textView_myReport_back);
        textView_myReport_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myReportBack = new Intent(MyReportActivity.this, UserFragment.class);
                startActivity(myReportBack);
            }
        });

    }

    @Override
    public void onClick(int position) {
    }
}
