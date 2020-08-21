package up.envisage.mapable.ui.home.report;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import up.envisage.mapable.R;
import up.envisage.mapable.adapter.ReportIncidentAdapter;

public class ReportIncidentActivity extends AppCompatActivity implements ReportIncidentAdapter.OnIncidentClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    TextInputLayout textInputLayout_reportAlgalBloom;

    Dialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);

        recyclerView = findViewById(R.id.recyclerView_reportIncident);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ReportIncidentAdapter(getApplicationContext(), this);
        recyclerView.setAdapter(adapter);
    }

    public void onClick(int position){
        Log.d("[ Incident.java ]", "onIncidentClick: clicked - " + position);
        switch (position) {
            case 0:
                dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_report_algalbloom);

                textInputLayout_reportAlgalBloom = dialog.findViewById(R.id.textInputLayout_reportAlgalBloom);

                dialog.show();

                break;
            case 2:
                dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_report_pollution);
                dialog.show();
        }
    }

}
