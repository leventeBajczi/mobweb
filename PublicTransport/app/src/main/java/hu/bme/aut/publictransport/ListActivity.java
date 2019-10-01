package hu.bme.aut.publictransport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ListActivity extends AppCompatActivity {

    public enum TYPE{BUS, TRAIN, BIKE, BOAT}

    //Manuális konstans létrehozás helyett enum, mivel ez pontosan erre van

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ImageButton btnBike = findViewById(R.id.btnBike);
        ImageButton btnBus = findViewById(R.id.btnBus);
        ImageButton btnTrain = findViewById(R.id.btnTrain);
        ImageButton btnBoat = findViewById(R.id.btnBoat);

        btnBike.setOnClickListener(new ShowDetailsActivityListener(TYPE.BIKE));
        btnBus.setOnClickListener(new ShowDetailsActivityListener(TYPE.BUS));
        btnTrain.setOnClickListener(new ShowDetailsActivityListener(TYPE.TRAIN));
        btnBoat.setOnClickListener(new ShowDetailsActivityListener(TYPE.BOAT));
    }

    private class ShowDetailsActivityListener implements View.OnClickListener {

        private final TYPE type;
        ShowDetailsActivityListener(TYPE type) {
            this.type = type;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ListActivity.this, DetailsActivity.class);
            intent.putExtra(DetailsActivity.KEY_TRANSPORT_TYPE, type);
            startActivity(intent);

        }
    }
}
