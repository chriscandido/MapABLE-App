package up.envisage.mapable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bottom bar navigation
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnavigation_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigation);

        //Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout_container, new HomeFragment()).commit();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigation = new BottomNavigationView.OnNavigationItemSelectedListener() {
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment fragment = null;

            switch (menuItem.getItemId()) {
                case R.id.mainMenu_user:
                    fragment = new UserFragment();
                    break;
                case R.id.mainMenu_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.mainMenu_map:
                    fragment = new MapFragment();
                    break;
                case R.id.mainMenu_support:
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout_container, fragment).commit();
            return true;
        }
    };

}
