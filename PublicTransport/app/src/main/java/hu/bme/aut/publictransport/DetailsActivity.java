package hu.bme.aut.publictransport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    public static final String KEY_TRANSPORT_TYPE = "KEY_TRANSPORT_TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        final ListActivity.TYPE transportType = (ListActivity.TYPE) intent.getSerializableExtra(KEY_TRANSPORT_TYPE);
        TextView tvTicketType = findViewById(R.id.tvTicketType);
        tvTicketType.setText(getTypeString(transportType));

        final DatePicker dpStartDate = findViewById(R.id.dpStartDate);
        final DatePicker dpEndDate = findViewById(R.id.dpEndDate);
        final Button btnPurchase = findViewById(R.id.btnPurchase);
        btnPurchase.setOnClickListener(new MyPurchaseButtonListener(getTypeString(transportType), dpStartDate, dpEndDate));

    }

    private String getDateFrom(final DatePicker picker) {
        return String.format(Locale.getDefault(), "%04d.%02d.%02d.",
                picker.getYear(), picker.getMonth() + 1, picker.getDayOfMonth());
    }

    private String getTypeString(ListActivity.TYPE type) {
        switch (type) {
            case BUS:
                return getString(R.string.bus_pass);
            case TRAIN:
                return getString(R.string.train_pass);
            case BIKE:
                return getString(R.string.bike_pass);
            case BOAT:
                return getString(R.string.boat_pass);
        }
        return null;
    }

    private class MyPurchaseButtonListener implements View.OnClickListener {
        private final String typeString;
        private final DatePicker dateFrom;
        private final DatePicker dateEnd;

        public MyPurchaseButtonListener(String typeString, DatePicker dateFrom, DatePicker dateEnd) {

            this.dateEnd = dateEnd;
            this.dateFrom = dateFrom;
            this.typeString = typeString;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(DetailsActivity.this, PassActivity.class);
            intent.putExtra(PassActivity.KEY_TYPE_STRING, typeString);
            intent.putExtra(PassActivity.KEY_DATE_STRING, getDateFrom(dateFrom) + " - " + getDateFrom(dateEnd));
            startActivity(intent);
        }
    }
}
