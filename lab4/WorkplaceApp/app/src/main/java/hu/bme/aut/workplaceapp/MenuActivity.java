package hu.bme.aut.workplaceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_activty);


        ImageButton btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(MenuActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
            }
        });

        ImageButton btnHoliday = findViewById(R.id.btnHoliday);
        btnHoliday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent holidayIntent = new Intent(MenuActivity.this, HolidayActivity.class);
                startActivity(holidayIntent);
            }
        });
    }
}
