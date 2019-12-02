package hu.bme.aut.xcfatest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * A placeholder fragment containing a simple view.
 */
public class EditorActivityFragment extends Fragment {

    public EditorActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        InputStream templateInputStream = null;
        File fileToModify = null;

        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        if (extras != null) {
            String template = extras.getString("template");
            String file = extras.getString("file");
            if (template != null) {
                try {
                    templateInputStream = getActivity().getAssets().open(template);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (file != null)
                fileToModify = new File(getActivity().getFilesDir().getAbsolutePath() + File.separator + file);
        }

        return inflater.inflate(R.layout.fragment_editor, container, false);
    }
}
