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
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Run #").append(i + 1).append(": ").append(System.lineSeparator());
                int j = 0;
                for (Map<String, Integer> stringIntegerMap : values.values()) {
                    stringBuilder.append("\tProcess #").append(j++).append(":").append(System.lineSeparator());
                    stringIntegerMap.forEach((s, integer) -> stringBuilder.append("\t\t").append(s).append(": ").append(integer).append(System.lineSeparator()));
                }
                Intent intent = new Intent("XCFA_RESULTS_DONE");
                intent.putExtra("value", stringBuilder.append(System.lineSeparator()).toString());
                intent.putExtra("last", false);
                applicationContext.sendBroadcast(intent);
            }
            Intent intent = new Intent("XCFA_RESULTS_DONE");
            intent.putExtra("last", true);
            applicationContext.sendBroadcast(intent);

        } catch (Exception e) {
            Intent i = new Intent("XCFA_RESULTS_DONE");
            i.putExtra("last", true);
            i.putExtra("error", e.getMessage());
            applicationContext.sendBroadcast(i);
        }
    }
}
