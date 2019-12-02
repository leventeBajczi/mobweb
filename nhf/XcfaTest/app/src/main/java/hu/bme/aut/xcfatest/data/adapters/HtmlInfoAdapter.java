package hu.bme.aut.xcfatest.data.adapters;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HtmlInfoAdapter {

    private final static String INFO_HTML_PATH = "info.html";

    public static String getHtmlText(Context context) {
        String html;
        try {
            InputStream is = context.getAssets().open(INFO_HTML_PATH);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder htmlBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) htmlBuilder.append(line);
            reader.close();
            html = htmlBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            html = "Asset could not be loaded.";
        }
        return html;
    }
}
