package hu.bme.aut.xcfatest.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import java.util.Objects;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private final TextView result;
    private boolean started = false;
    public MyBroadcastReceiver(TextView result) {
        this.result = result;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.requireNonNull(intent.getExtras()).getBoolean("last")) {
            String error;
            if ((error = Objects.requireNonNull(intent.getExtras()).getString("error")) != null)
                ErrorHandler.showErrorMessage(result.getRootView(), "There was a problem running the test.", error);
            context.unregisterReceiver(this);
        } else if (started) {
            result.setText(new StringBuilder().append(result.getText()).append(intent.getExtras().getString("value")).toString());
        } else {
            result.setText(intent.getExtras().getString("value"));
            started = true;
        }

    }
}
