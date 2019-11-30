package hu.bme.aut.xcfatest;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.view.View;

import java.io.IOException;
import java.util.NoSuchElementException;

import hu.bme.aut.xcfatest.thetacompat.XcfaAbstraction;
import hu.bme.mit.theta.xcfa.XCFA;
import hu.bme.mit.theta.xcfa.dsl.XcfaDslManager;

public class MainActivity extends AppCompatActivity {

    private static String sampleXcfa = "" +
            "var x : int\n" +
            "main process mainProcess {\n" +
            "    main procedure mainProcedure() {\n" +
            "        var a : int\n" +
            "        var b : int\n" +
            "        init loc L0\n" +
            "        loc L1\n" +
            "        loc L2\n" +
            "        loc L3\n" +
            "        final loc END\n" +
            "\n" +
            "        L0 -> L1  { a := 1 }\n" +
            "        L1 -> L2  { b := 2 }\n" +
            "        L2 -> L3  { a -> x atomic @relaxed }\n" +
            "        L3 -> END { b -> x atomic @relaxed }\n" +
            "    }\n" +
            "}\n" +
            "process secondProcess{\n" +
            "    main procedure mainProcedure() {\n" +
            "        var a : int\n" +
            "        var b : int\n" +
            "        init loc L0\n" +
            "        loc L1\n" +
            "        loc L2\n" +
            "        loc L3\n" +
            "        loc L4\n" +
            "        final loc END\n" +
            "        error loc ERR\n" +
            "\n" +
            "        L0 -> L1 { a := 0 }\n" +
            "        L1 -> L2 { b := 0 }\n" +
            "        L2 -> L3 { a <- x atomic @relaxed }\n" +
            "        L3 -> L4 { b <- x atomic @relaxed }\n" +
            "        L4 -> ERR { assume ( (a - b) = 1 ) }\n" +
            "        L4 -> END { assume not ( (a - b) = 1 ) }\n" +
            "    }\n" +
            "}\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator_layout);
        try {
            XcfaAbstraction xcfaAbstraction = XcfaAbstraction.fromString(sampleXcfa);
            xcfaAbstraction.serialize();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            Snackbar.make(coordinatorLayout, "Xcfa is not deterministic, it would use resident garbage.", Snackbar.LENGTH_LONG).show();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

}
