package bw.ac.biust.lovingbirds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import bw.ac.biust.lovingbirds.Fragments.HomeFragment;
import bw.ac.biust.lovingbirds.Fragments.MatchesFragment;
import bw.ac.biust.lovingbirds.Fragments.ProfileFragment;
import bw.ac.biust.lovingbirds.Fragments.SettingsFragment;

public class LoadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListner);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListner = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectFrag = null;

            switch (item.getItemId()){
                case R.id.home_menu:
                    selectFrag = new HomeFragment();
                    break;

                case R.id.matches_menu:
                    selectFrag = new MatchesFragment();
                    break;

                case R.id.setting_menu:
                   selectFrag = new SettingsFragment();
                   break;

                case R.id.profile_menu:
                    selectFrag = new  ProfileFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectFrag).commit();

            return true;
        }
    };
}