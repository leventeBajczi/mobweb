package hu.bme.aut.xcfatest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator_layout);
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

}
