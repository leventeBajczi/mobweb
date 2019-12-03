package hu.bme.aut.xcfatest.utils;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import hu.bme.aut.xcfatest.R;
import hu.bme.aut.xcfatest.data.view.MyDialogBuilder;

public class ErrorHandler {

    public static void showErrorMessage(View root, String msg, String e) {
        Snackbar.
                make(root, msg, Snackbar.LENGTH_LONG).
                setAction(
                        root.getContext().getString(R.string.details),
                        view -> {
                            @SuppressLint("InflateParams") RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(root.getRootView().getContext()).inflate(R.layout.dialog_content, null);
                            TextView textView = new TextView(root.getRootView().getContext());
                            textView.setText(e);
                            relativeLayout.addView(textView);
                            MyDialogBuilder.getDialog(
                                    root.getRootView().getContext(),
                                    R.string.error_dialog_title,
                                    R.string.error_dialog_msg,
                                    android.R.string.yes,
                                    null).setView(relativeLayout).show();
                        }).
                show();

    }

    public static void showErrorMessage(View root, String msg, Exception e) {
        showErrorMessage(root, msg, e.toString());
    }

    public static void showErrorMessage(View root, String msg) {
        Snackbar.make(root, msg, Snackbar.LENGTH_SHORT).show();

    }
}
