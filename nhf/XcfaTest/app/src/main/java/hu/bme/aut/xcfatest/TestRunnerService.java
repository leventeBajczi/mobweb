package hu.bme.aut.xcfatest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.FileNotFoundException;
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
