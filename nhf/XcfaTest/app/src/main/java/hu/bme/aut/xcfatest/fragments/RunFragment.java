package hu.bme.aut.xcfatest.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Map;
import java.util.Objects;

import hu.bme.aut.xcfatest.EditorActivity;
import hu.bme.aut.xcfatest.R;
import hu.bme.aut.xcfatest.data.model.XcfaRow;
import hu.bme.aut.xcfatest.thetacompat.XcfaAbstraction;
import hu.bme.mit.theta.xcfa.XCFA;

public class RunFragment extends Fragment {
    private XcfaRow row;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        if (extras != null) {
            String file = extras.getString("file");
            if (file != null) {
                row = XcfaRow.get(file);
                if (row == null) row = new XcfaRow(file, false, 0, 0, null);
            }

        }
        return inflater.inflate(R.layout.fragment_run, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView filename = Objects.requireNonNull(getActivity()).findViewById(R.id.name);
        TextView xcfaOk = Objects.requireNonNull(getActivity()).findViewById(R.id.xcfa_ok);
        TextView noOfVars = Objects.requireNonNull(getActivity()).findViewById(R.id.no_of_vars);
        TextView noOfThreads = Objects.requireNonNull(getActivity()).findViewById(R.id.no_of_threads);
        TextView result = Objects.requireNonNull(getActivity()).findViewById(R.id.result);
        Button button = Objects.requireNonNull(getActivity()).findViewById(R.id.run);
        Button edit = Objects.requireNonNull(getActivity()).findViewById(R.id.modify);


        filename.setText(row.getName());
        xcfaOk.setText(row.isOk() ? R.string.card_status_ok : R.string.card_status_error);
        xcfaOk.setTextColor(row.isOk() ? getResources().getColor(R.color.ok, getActivity().getTheme()) : getResources().getColor(R.color.error, getActivity().getTheme()));

        noOfVars.setText(getString(R.string.card_var_display, row.getVars()));
        noOfThreads.setText(getString(R.string.card_thread_display, row.getThreads()));
        if (!row.isOk()) button.setEnabled(false);
        button.setOnClickListener(view -> {
            XcfaAbstraction xcfaAbstraction = row.getAbstraction();
            for (int i = 0; i < 1000; ++i) {
                Map<XCFA.Process.Procedure, Map<String, Integer>> values = xcfaAbstraction.run();
                values.values().forEach(stringIntegerMap -> stringIntegerMap.forEach((s, integer) ->
                        result.setText(result.getText() + System.lineSeparator() + s + ": " + integer)));
            }
        });
        edit.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), EditorActivity.class);
            intent.putExtra("file", row.getName());
            startActivity(intent);
        });
    }
}
