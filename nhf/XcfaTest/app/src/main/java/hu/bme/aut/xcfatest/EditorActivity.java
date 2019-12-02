package hu.bme.aut.xcfatest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import hu.bme.aut.xcfatest.data.view.MyDialogBuilder;
import hu.bme.aut.xcfatest.fragments.EditorFragment;

public class EditorActivity extends AppCompatActivity {

    private EditorFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        mFragment = ((EditorFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.fragment)));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (mFragment.getModified()) {
            MyDialogBuilder.getDialog(this,
                    R.string.discard_title,
                    R.string.discard_message,
                    R.string.discard_discard,
                    (a, b) -> super.onBackPressed(),
                    R.string.save,
                    (a, b) -> mFragment.save(super::onBackPressed)).show();
        } else
            super.onBackPressed();
    }
}
