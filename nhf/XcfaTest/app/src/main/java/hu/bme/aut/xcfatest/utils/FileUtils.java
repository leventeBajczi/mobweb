package hu.bme.aut.xcfatest.utils;

import android.content.Context;
import android.text.Editable;
import android.view.View;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class FileUtils {
    public static void writeFile(View view, String fileToModify, Editable text) {
        try {
            OutputStream os = view.getContext().openFileOutput(fileToModify, Context.MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
            writer.write(text.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
