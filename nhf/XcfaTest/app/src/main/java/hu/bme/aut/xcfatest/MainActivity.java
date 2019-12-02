package hu.bme.aut.xcfatest;

import android.app.AlertDialog;
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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import hu.bme.aut.xcfatest.adapter.MyRecyclerViewAdapter;
import hu.bme.aut.xcfatest.tasks.AsyncFiller;

public class MainActivity extends AppCompatActivity {

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
        ProgressBar progressBar = findViewById(R.id.progressBar);
        ImageButton infoButton = findViewById(R.id.info_button);
        //Setting up RecyclerView
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MyRecyclerViewAdapter mAdapter = new MyRecyclerViewAdapter();
        recyclerView.setAdapter(mAdapter);
        //Fill up the list with data
        AsyncFiller filler = new AsyncFiller(mAdapter, progressBar);
        filler.execute();
        //Setting interaction listeners
        infoButton.setOnClickListener(view -> {
            String html;
            try {
                InputStream is = getAssets().open("info.html");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder htmlBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) htmlBuilder.append(line);
                reader.close();
                html = htmlBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                html = "Asset could not be loaded.";
            }

            FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.info_main, coordinatorLayout, false);
            TextView textView = frameLayout.findViewById(R.id.html_text);
            textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT));
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            Dialog dialog = new Dialog(MainActivity.this, android.R.style.ThemeOverlay_Material_Dialog);
            dialog.addContentView(frameLayout, frameLayout.getLayoutParams());
            dialog.show();
        });
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EditorActivity.class);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.
                    setTitle(R.string.new_xcfa_alert_title).
                    setMessage(R.string.new_xcfa_alert_message).
                    setPositiveButton(R.string.use, (dialogInterface, i) -> {
                        Spinner spinner = new Spinner(builder.getContext());
                        String[] templates;
                        try {
                            templates = getAssets().list("templates");
                        } catch (IOException e) {
                            e.printStackTrace();
                            templates = new String[0];
                        }
                        if (templates != null && templates.length > 0) {
                            spinner.setAdapter(
                                    new ArrayAdapter<>(
                                            getApplicationContext(),
                                            android.R.layout.simple_spinner_dropdown_item,
                                            templates)
                            );
                            new AlertDialog.Builder(builder.getContext()).
                                    setTitle(R.string.which_template_question).
                                    setView(spinner).
                                    setPositiveButton(android.R.string.yes, (dialogInterface1, i1) -> {
                                        intent.putExtra("template", "templates" + File.separator + spinner.getSelectedItem());
                                        startActivity(intent);
                                    }).
                                    setNeutralButton(android.R.string.cancel, null).
                                    setCancelable(true).
                                    show();
                        } else {
                            new AlertDialog.Builder(builder.getContext()).
                                    setMessage("No template found!").
                                    show();
                        }

                    }).
                    setNegativeButton(R.string.empty, (dialogInterface, i) -> startActivity(intent)).
                    setNeutralButton(android.R.string.cancel, null).
                    setCancelable(true).
                    show();
        });

//        try {
//            XcfaAbstraction xcfaAbstraction = XcfaAbstraction.fromString(sampleXcfa);
//            for(int i = 0; i < 1000; ++i) {
//                Map<XCFA.Process.Procedure, Map<String, Integer>> values = xcfaAbstraction.run();
//                System.out.println("======= " + i + " =======");
//                values.values().forEach(stringIntegerMap -> {
//                    System.out.println("==");
//                    stringIntegerMap.forEach((s, integer) -> {
//                        System.out.println(s + " = " + integer);
//                    });
//                });
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (NoSuchElementException e) {
//            Snackbar.make(coordinatorLayout, "Xcfa is not deterministic, it would use resident garbage.", Snackbar.LENGTH_LONG).show();
//        }
    }

}
