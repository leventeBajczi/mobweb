package hu.bme.aut.publictransport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class PassActivity extends AppCompatActivity {

    public final static String KEY_DATE_STRING = "KEY_DATE_STRING";
    public final static String KEY_TYPE_STRING = "KEY_TYPE_STRING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);
        Intent intent = getIntent();

        TextView tvTicketType = findViewById(R.id.tvTicketType);
        tvTicketType.setText(intent.getStringExtra(KEY_TYPE_STRING));

        TextView tvDates = findViewById(R.id.tvDates);
        tvDates.setText(intent.getStringExtra(KEY_DATE_STRING));
    }
}
