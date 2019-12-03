package hu.bme.aut.xcfatest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Objects;

import hu.bme.aut.xcfatest.tasks.AsyncRunner;

public class TestRunnerService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String name = Objects.requireNonNull(intent.getExtras()).getString("filename");
        int loops = intent.getExtras().getInt("loops");
        if (name != null) {
            try {
                AsyncRunner asyncRunner = new AsyncRunner(openFileInput(name), loops, getApplicationContext());
                asyncRunner.start();
            } catch (Exception e) {
                Intent i = new Intent("XCFA_RESULTS_DONE");
                i.putExtra("last", true);
                i.putExtra("error", e.getMessage());
                getApplicationContext().sendBroadcast(i);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
