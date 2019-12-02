package hu.bme.aut.xcfatest;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
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

            FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.info_main, coordinatorLayout, false);
            TextView textView = frameLayout.findViewById(R.id.html_text);
            textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT));
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            Dialog dialog = new Dialog(this, android.R.style.ThemeOverlay_Material_Dialog);
            dialog.addContentView(frameLayout, frameLayout.getLayoutParams());
            dialog.show();
        });
        fab.setOnClickListener(view -> {
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
