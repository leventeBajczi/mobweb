package hu.bme.aut.workplaceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import adapter.ProfilePagerAdapter;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ViewPager vpProfile = findViewById(R.id.vpProfile);
        vpProfile.setAdapter(new ProfilePagerAdapter(getSupportFragmentManager()));
    }
}
