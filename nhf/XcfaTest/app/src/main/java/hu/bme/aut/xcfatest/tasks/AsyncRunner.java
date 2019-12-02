package hu.bme.aut.xcfatest.tasks;

import android.content.Context;
import android.content.Intent;

import java.io.FileInputStream;
import java.util.Map;

import hu.bme.aut.xcfatest.thetacompat.XcfaAbstraction;
import hu.bme.mit.theta.xcfa.XCFA;

public class AsyncRunner extends Thread {
    private final FileInputStream fis;
    private final int loops;
    private final Context applicationContext;

    public AsyncRunner(FileInputStream openFileInput, int loops, Context applicationContext) {
        fis = openFileInput;
        this.loops = loops;
        this.applicationContext = applicationContext;
    }

    @Override
    public void run() {
        try {
            XcfaAbstraction xcfaAbstraction = XcfaAbstraction.fromStream(fis);
            for (int i = 0; i < loops; ++i) {
                Map<XCFA.Process.Procedure, Map<String, Integer>> values = xcfaAbstraction.run();
                values.values().forEach(stringIntegerMap -> stringIntegerMap.forEach((s, integer) -> {
                    Intent intent = new Intent("XCFA_RESULTS_DONE");
                    intent.putExtra("value", s + ": " + integer);
                    applicationContext.sendBroadcast(intent);
                }));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
