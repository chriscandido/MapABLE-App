package up.envisage.mapable.ui.home;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.TextView;

import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;
import up.envisage.mapable.adapter.InformationAdapter;
import up.envisage.mapable.adapter.ReportIncidentAdapter;

public class InformationActivity extends AppCompatActivity implements InformationAdapter.OnInformationClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        recyclerView = findViewById(R.id.recyclerView_information);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new InformationAdapter(getApplicationContext(), this);
        recyclerView.setAdapter(adapter);

    }

    public void onStart(){
        super.onStart();
    }

    public void onResume(){
        super.onResume();
    }

    public void onPause(){
        super.onPause();
    }

    public void onBackPressed(){
    }

    @Override
    public void onClick(int position) {

    }
}
