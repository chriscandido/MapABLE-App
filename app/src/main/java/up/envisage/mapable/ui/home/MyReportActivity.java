package up.envisage.mapable.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import up.envisage.mapable.R;
import up.envisage.mapable.adapter.MyReportAdapter;
import up.envisage.mapable.db.Database;
import up.envisage.mapable.db.table.ReportTable;
import up.envisage.mapable.model.ReportViewModel;
import up.envisage.mapable.model.UserViewModel;

public class MyReportActivity extends AppCompatActivity implements MyReportAdapter.OnReportClickListener {

    private RecyclerView recyclerView;
    private ReportViewModel reportViewModel;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

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
    }

    @Override
    public void onClick(int position) {
    }
}
