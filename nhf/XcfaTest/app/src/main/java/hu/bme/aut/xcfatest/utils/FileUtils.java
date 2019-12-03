package hu.bme.aut.xcfatest.utils;

import android.content.Context;
import android.text.Editable;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import hu.bme.aut.xcfatest.R;

public class FileUtils {
    public static void writeFile(View view, String fileToModify, Editable text) {
        try {
            OutputStream os = view.getContext().openFileOutput(fileToModify, Context.MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
            writer.write(text.toString());
            writer.close();
            Snackbar.make(view, view.getContext().getString(R.string.saved_ok, fileToModify), Snackbar.LENGTH_SHORT).show();
        } catch (IOException e) {
            ErrorHandler.showErrorMessage(view, "There was a problem writing the file.", e);
        }
    }
}
