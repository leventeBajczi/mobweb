package hu.bme.aut.xcfatest.data.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.gc.materialdesign.views.ButtonFlat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.synnapps.carouselview.CarouselView;

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

    private static final int NUMBER_OF_PAGES = 7;
    private static final Integer[][] DATA = {
            {R.mipmap._1empty, R.string.onboarding_1},
            {R.mipmap._2info, R.string.onboarding_2},
            {R.mipmap._3new, R.string.onboarding_3},
            {R.mipmap._4template, R.string.onboarding_4},
            {R.mipmap._5save, R.string.onboarding_5},
            {R.mipmap._6list, R.string.onboarding_6},
            {R.mipmap._7run, R.string.onboarding_7}
    };

    public static Dialog getOnboardingDialog(Context context, ViewGroup root, int theme) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) LayoutInflater.from(context).inflate(R.layout.dialog_onboarding, root, false);
        CarouselView customCarouselView = coordinatorLayout.findViewById(R.id.carouselView);
        FloatingActionButton skip = coordinatorLayout.findViewById(R.id.skip);
        customCarouselView.setPageCount(NUMBER_OF_PAGES);
        Dialog dialog = new Dialog(context, theme);

        customCarouselView.setViewListener(position -> {
            @SuppressLint("InflateParams") View customView = LayoutInflater.from(context).inflate(R.layout.onboarding_element, null);
            ImageView imageView = customView.findViewById(R.id.image);
            TextView textView = customView.findViewById(R.id.text);
            if (position == NUMBER_OF_PAGES - 1) {
                ButtonFlat exit = customView.findViewById(R.id.exit);
                exit.setAlpha(1.0f);
                exit.setOnClickListener(view -> dialog.dismiss());
            }
            imageView.setImageResource(DATA[position][0]);
            textView.setText(DATA[position][1]);
            return customView;
        });

        skip.setOnClickListener(view -> dialog.dismiss());
        dialog.addContentView(coordinatorLayout, coordinatorLayout.getLayoutParams());
        return dialog;
    }
}
