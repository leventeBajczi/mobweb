package hu.bme.aut.xcfatest.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.Objects;

import hu.bme.aut.xcfatest.R;

public class RunFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        File fileToRun = null;
        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        if (extras != null) {
            String file = extras.getString("file");
            if (file != null)
                fileToRun = new File(getActivity().getFilesDir().getAbsolutePath() + File.separator + file);
        }

        return inflater.inflate(R.layout.fragment_run, container, false);
    }
}
