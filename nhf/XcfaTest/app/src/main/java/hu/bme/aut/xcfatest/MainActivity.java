package hu.bme.aut.xcfatest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        FloatingActionButton fab = findViewById(R.id.fab1);
        //Setting up RecyclerView
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MyRecyclerViewAdapter mAdapter = new MyRecyclerViewAdapter();
        recyclerView.setAdapter(mAdapter);
        //Fill up the list with data
        AsyncFiller filler = new AsyncFiller(mAdapter);
        filler.execute();


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
    }

}
