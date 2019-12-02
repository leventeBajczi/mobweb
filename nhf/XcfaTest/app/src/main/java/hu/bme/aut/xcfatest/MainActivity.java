package hu.bme.aut.xcfatest;

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

import java.io.File;
import java.io.IOException;

import hu.bme.aut.xcfatest.data.adapters.HtmlInfoAdapter;
import hu.bme.aut.xcfatest.data.adapters.MyRecyclerViewAdapter;
import hu.bme.aut.xcfatest.data.view.MyDialogBuilder;
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
                        Spinner spinner = new Spinner(MainActivity.this.getBaseContext());
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
                            MyDialogBuilder.getDialog(MainActivity.this,
                                    R.string.which_template_question,
                                    R.string.which_template_msg,
                                    android.R.string.yes,
                                    (dialogInterface1, i1) -> {
                                        intent.putExtra("template", "templates" + File.separator + spinner.getSelectedItem());
                                        startActivity(intent);
                                    },
                                    android.R.string.cancel,
                                    null).setView(spinner).show();
                        } else {
                            MyDialogBuilder.getDialog(MainActivity.this,
                                    R.string.no_template,
                                    R.string.no_template_msg,
                                    android.R.string.yes,
                                    null).show();
                        }
                    },
                    R.string.empty,
                    (dialogInterface, i) -> startActivity(intent),
                    android.R.string.cancel,
                    null).show();

        });
    }

}
