package hu.bme.aut.xcfatest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TestRunner extends Service {
    public TestRunner() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
