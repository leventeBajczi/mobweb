package hu.bme.aut.xcfatest;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import java.io.IOException;

import hu.bme.mit.theta.xcfa.XCFA;
import hu.bme.mit.theta.xcfa.dsl.XcfaDslManager;

public class MainActivity extends AppCompatActivity {

    private static String sampleXcfa = "" +
            "var x : int\n" +
            "main process mainProcess {\n" +
            "    main procedure mainProcedure() {\n" +
            "        init loc L0\n" +
            "        loc L1\n" +
            "        loc L2\n" +
            "        loc L3\n" +
            "        final loc END\n" +
            "        error loc ERR\n" +
            "\n" +
            "        L0 -> L1 { x := 0 }\n" +
            "        L1 -> L2 { assume x < 5 }\n" +
            "        L1 -> L3 { assume not (x < 5) }\n" +
            "        L2 -> L1 { x := x + 1 }\n" +
            "        L3 -> END { assume x <= 5 }\n" +
            "        L3 -> ERR { assume not (x <= 5) }\n" +
            "    }\n" +
            "}\n";

    static {
        System.loadLibrary("xcfa");
    }

    public native String textFromNative();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        XCFA xcfa;
        try {
            xcfa = XcfaDslManager.createXcfa(sampleXcfa);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, textFromNative(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

}
