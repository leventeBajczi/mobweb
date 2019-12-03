package hu.bme.aut.xcfatest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import hu.bme.aut.xcfatest.data.adapters.MyRecyclerViewAdapter;
import hu.bme.aut.xcfatest.data.view.MyDialogBuilder;
import hu.bme.aut.xcfatest.tasks.AsyncFiller;
import hu.bme.aut.xcfatest.utils.ErrorHandler;

public class MainActivity extends AppCompatActivity {
    private MyRecyclerViewAdapter mAdapter;
    private ProgressBar progressBar;
    private static final String PREFERENCES = "MainActivityDefaultPreferences";
    private static final String FIRST_TIME = "FirstTime";
    private final static String INFO_HTML_PATH = "info.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting the root layout and toolbar
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Collecting views
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator_layout);
        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        FloatingActionButton fab = findViewById(R.id.fab1);
        progressBar = findViewById(R.id.progressBar);
        ImageButton infoButton = findViewById(R.id.info_button);

        //start Onboarding when this is the first time
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        boolean firstTime = sharedPreferences.getBoolean(FIRST_TIME, false);
        if (firstTime) {
            //Dialog dialog = MyDialogBuilder.getOnboardingDialog(this, coordinatorLayout, android.R.style.Theme_Material_NoActionBar_Fullscreen);

            //dialog.show();
            //sharedPreferences.edit().putBoolean(FIRST_TIME, true).apply();
        }

        //Setting up RecyclerView
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyRecyclerViewAdapter();
        recyclerView.setAdapter(mAdapter);

        //Setting interaction listeners
        infoButton.setOnClickListener(view -> MyDialogBuilder.getInfoDialog(MainActivity.this, INFO_HTML_PATH, coordinatorLayout, android.R.style.ThemeOverlay_Material_Dialog).show());
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EditorActivity.class);
            MyDialogBuilder.getDialog(MainActivity.this,
                    R.string.new_xcfa_alert_title,
                    R.string.new_xcfa_alert_message,
                    R.string.use,
                    (dialogInterface, i) -> {
                        @SuppressLint("InflateParams") RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(getBaseContext()).inflate(R.layout.dialog_content, null);
                        Spinner spinner = new Spinner(getBaseContext());
                        relativeLayout.addView(spinner);
                        String[] templates;
                        try {
                            templates = getAssets().list("templates");
                            if (Objects.requireNonNull(templates).length > 0) {
                                spinner.setAdapter(
                                        new ArrayAdapter<>(
                                                getApplicationContext(),
                                                android.R.layout.simple_spinner_dropdown_item,
                                                templates)
                                );
                                MyDialogBuilder.getDialog(MainActivity.this,
                                        R.string.which_template_question,
                                        R.string.which_template_msg,
                                        android.R.string.yes,
                                        (dialogInterface1, i1) -> {
                                            intent.putExtra("template", "templates" + File.separator + spinner.getSelectedItem());
                                            startActivity(intent);
                                        },
                                        android.R.string.cancel,
                                        null).setView(relativeLayout).show();

                            } else {
                                ErrorHandler.showErrorMessage(coordinatorLayout, "No templates found!");
                            }
                        } catch (IOException e) {
                            ErrorHandler.showErrorMessage(coordinatorLayout, "Templates could not be read!", e);
                        }
                    },
                    R.string.empty,
                    (dialogInterface, i) -> startActivity(intent),
                    android.R.string.cancel,
                    null).show();

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.clear();
        //Fill up the list with data
        AsyncFiller filler = new AsyncFiller(mAdapter, progressBar);
        filler.execute();
    }
}
