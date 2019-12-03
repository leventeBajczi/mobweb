package hu.bme.aut.xcfatest.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import hu.bme.aut.xcfatest.EditorActivity;
import hu.bme.aut.xcfatest.R;
import hu.bme.aut.xcfatest.TestRunnerService;
import hu.bme.aut.xcfatest.data.model.XcfaRow;
import hu.bme.aut.xcfatest.thetacompat.XcfaAbstraction;
import hu.bme.aut.xcfatest.utils.MyBroadcastReceiver;

public class RunFragment extends Fragment {
    private XcfaRow row;
    private boolean modified = false;
    private String file;
    private TextView filename;
    private TextView xcfaOk;
    private TextView noOfVars;
    private TextView noOfThreads;
    private TextView result;
    private FloatingActionButton button;
    private FloatingActionButton edit;
    private EditText editText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        if (extras != null) {
            String file = extras.getString("file");
            if (file != null) {
                this.file = file;
                row = XcfaRow.get(file);
                if (row == null) row = new XcfaRow(file, false, 0, 0, null);
            }

        }
        return inflater.inflate(R.layout.fragment_run, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        filename = Objects.requireNonNull(getActivity()).findViewById(R.id.name);
        xcfaOk = Objects.requireNonNull(getActivity()).findViewById(R.id.xcfa_ok);
        noOfVars = Objects.requireNonNull(getActivity()).findViewById(R.id.no_of_vars);
        noOfThreads = Objects.requireNonNull(getActivity()).findViewById(R.id.no_of_threads);
        result = Objects.requireNonNull(getActivity()).findViewById(R.id.result);
        button = Objects.requireNonNull(getActivity()).findViewById(R.id.run);
        edit = Objects.requireNonNull(getActivity()).findViewById(R.id.modify);
        editText = Objects.requireNonNull(getActivity()).findViewById(R.id.loops);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (modified) {
            try {
                XcfaAbstraction abstraction = XcfaAbstraction.fromStream(Objects.requireNonNull(getActivity().openFileInput(file)));
                row = new XcfaRow(file, true, abstraction.getVars(), abstraction.getThreads(), abstraction);
                modified = false;
            } catch (Exception e) {
                e.printStackTrace();
                row = new XcfaRow(file, false, 0, 0, null);
            }
        }
        filename.setText(row.getName());
        xcfaOk.setText(row.isOk() ? R.string.card_status_ok : R.string.card_status_error);
        xcfaOk.setTextColor(row.isOk() ? getResources().getColor(R.color.ok, getActivity().getTheme()) : getResources().getColor(R.color.error, getActivity().getTheme()));

        noOfVars.setText(getString(R.string.card_var_display, row.getVars()));
        noOfThreads.setText(getString(R.string.card_thread_display, row.getThreads()));
        if (!row.isOk()) {
            button.setEnabled(false);
            button.setAlpha(0.0f);
        } else {
            button.setEnabled(true);
            button.setAlpha(1.0f);
        }
        button.setOnClickListener(view -> {
            Objects.requireNonNull(getContext()).registerReceiver(new MyBroadcastReceiver(result), new IntentFilter("XCFA_RESULTS_DONE"));
            Intent intent = new Intent(getContext(), TestRunnerService.class);
            intent.putExtra("filename", row.getName());
            intent.putExtra("loops", Integer.parseInt(editText.getText().toString()));
            Objects.requireNonNull(getContext()).startService(intent);
        });
        edit.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), EditorActivity.class);
            intent.putExtra("file", row.getName());
            modified = true;
            startActivity(intent);
        });
    }
}
