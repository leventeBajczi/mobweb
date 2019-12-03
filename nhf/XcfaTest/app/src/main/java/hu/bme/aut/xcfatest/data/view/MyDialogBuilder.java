package hu.bme.aut.xcfatest.data.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import hu.bme.aut.xcfatest.R;
import hu.bme.aut.xcfatest.data.adapters.HtmlInfoAdapter;

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

    public static Dialog getInfoDialog(Context context, String infoFile, ViewGroup root, int theme) {
        String html = HtmlInfoAdapter.getHtmlText(context, infoFile);
        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.dialog_info, root, false);
        TextView textView = frameLayout.findViewById(R.id.html_text);
        textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        Dialog dialog = new Dialog(context, theme);
        dialog.addContentView(frameLayout, frameLayout.getLayoutParams());
        return dialog;
    }

    public static Dialog getOnboardingDialog(Context context, String onboardingFile, ViewGroup root, int theme) {
        String html = HtmlInfoAdapter.getHtmlText(context, onboardingFile);
        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.dialog_onboarding, root, false);
        TextView textView = frameLayout.findViewById(R.id.html_text);
        textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        Dialog dialog = new Dialog(context, theme);
        dialog.addContentView(frameLayout, frameLayout.getLayoutParams());
        return dialog;
    }
}
