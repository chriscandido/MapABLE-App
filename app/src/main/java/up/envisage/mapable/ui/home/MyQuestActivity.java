package up.envisage.mapable.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import up.envisage.mapable.R;
import up.envisage.mapable.adapter.MyQuestAdapter;
import up.envisage.mapable.adapter.ReportIncidentAdapter;

public class MyQuestActivity extends AppCompatActivity implements MyQuestAdapter.OnQuestClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_quests);

        recyclerView = findViewById(R.id.recyclerView_myquest_basiclevel);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyQuestAdapter(getApplicationContext(), this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(int position) {

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
        super.onBackPressed();
        finish();
    }

}
