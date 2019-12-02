package hu.bme.aut.xcfatest.data.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class MyDialogBuilder {

    public static AlertDialog.Builder getDialog(Context context,
                                                int title,
                                                int message,
                                                int positiveText,
                                                DialogInterface.OnClickListener positiveListener,
                                                int negativeText,
                                                DialogInterface.OnClickListener negativeListener,
                                                int neutralText,
                                                DialogInterface.OnClickListener neutralListener) {
        return new AlertDialog.Builder(context).
                setTitle(title).
                setMessage(message).
                setPositiveButton(positiveText, positiveListener).
                setNegativeButton(negativeText, negativeListener).
                setNeutralButton(neutralText, neutralListener).
                setCancelable(true);

    }

    public static AlertDialog.Builder getDialog(Context context,
                                                int title,
                                                int message,
                                                int positiveText,
                                                DialogInterface.OnClickListener positiveListener,
                                                int neutralText,
                                                DialogInterface.OnClickListener neutralListener) {
        return new AlertDialog.Builder(context).
                setTitle(title).
                setMessage(message).
                setPositiveButton(positiveText, positiveListener).
                setNeutralButton(neutralText, neutralListener).
                setCancelable(true);
    }

    public static AlertDialog.Builder getDialog(Context context,
                                                int title,
                                                int message,
                                                int positiveText,
                                                DialogInterface.OnClickListener positiveListener) {
        return new AlertDialog.Builder(context).
                setTitle(title).
                setMessage(message).
                setPositiveButton(positiveText, positiveListener).
                setCancelable(true);
    }
}
