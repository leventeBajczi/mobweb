package hu.bme.aut.xcfatest.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private final TextView result;

    public MyBroadcastReceiver(TextView result) {
        this.result = result;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        result.setText(result.getText() + System.lineSeparator() + intent.getExtras().getString("value"));

    }
}
