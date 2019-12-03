package hu.bme.aut.xcfatest;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import hu.bme.aut.xcfatest.data.adapters.HtmlInfoAdapter;
import hu.bme.aut.xcfatest.data.adapters.MyRecyclerViewAdapter;
import hu.bme.aut.xcfatest.data.view.MyDialogBuilder;
import hu.bme.aut.xcfatest.tasks.AsyncFiller;
import hu.bme.aut.xcfatest.utils.ErrorHandler;

public class MainActivity extends AppCompatActivity {
    private MyRecyclerViewAdapter mAdapter;
    private ProgressBar progressBar;

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

        //Setting up RecyclerView
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyRecyclerViewAdapter();
        recyclerView.setAdapter(mAdapter);

        //Setting interaction listeners
        infoButton.setOnClickListener(view -> {
            String html = HtmlInfoAdapter.getHtmlText(getApplicationContext());
            FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_info, coordinatorLayout, false);
            TextView textView = frameLayout.findViewById(R.id.html_text);
            textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT));
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            Dialog dialog = new Dialog(MainActivity.this, android.R.style.ThemeOverlay_Material_Dialog);
            dialog.addContentView(frameLayout, frameLayout.getLayoutParams());
            dialog.show();
        });
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
