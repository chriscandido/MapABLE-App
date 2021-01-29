 package up.envisage.mapable.ui.home;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;
import up.envisage.mapable.adapter.InformationAdapter;

public class InformationActivity extends AppCompatActivity implements InformationAdapter.OnInformationClickListener {

    private RecyclerView.Adapter adapter;
    private ViewPager2 viewPager2;
    private TextView textView_ainformation_back;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        viewPager2 = findViewById(R.id.viewPager_lawsAndPolicy);

        adapter = new InformationAdapter(getApplicationContext(), this);

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        viewPager2.setAdapter(adapter);

        CompositePageTransformer transformer = new CompositePageTransformer();

        transformer.addTransformer(new MarginPageTransformer(8));
        transformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float v = 1 - Math.abs(position);
                page.setScaleY(0.8f + v * 0.2F);
            }
        });

        viewPager2.setPageTransformer(transformer);

        textView_ainformation_back = findViewById(R.id.textView_information_back);
        textView_ainformation_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(InformationActivity.this, MainActivity.class);
                startActivity(back);
            }
        });

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
