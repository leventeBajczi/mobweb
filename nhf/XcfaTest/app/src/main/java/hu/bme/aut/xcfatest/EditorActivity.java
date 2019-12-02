package hu.bme.aut.xcfatest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class EditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InputStream templateInputStream = null;
        File fileToModify = null;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String template = extras.getString("template");
            String file = extras.getString("file");
            if (template != null) {
                try {
                    templateInputStream = getAssets().open(template);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (file != null)
                fileToModify = new File(getFilesDir().getAbsolutePath() + File.separator + file);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
