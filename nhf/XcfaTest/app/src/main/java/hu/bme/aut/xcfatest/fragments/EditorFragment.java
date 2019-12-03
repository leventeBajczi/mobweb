package hu.bme.aut.xcfatest.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import hu.bme.aut.xcfatest.R;
import hu.bme.aut.xcfatest.data.model.XcfaRow;
import hu.bme.aut.xcfatest.data.view.MyDialogBuilder;
import hu.bme.aut.xcfatest.utils.ErrorHandler;
import hu.bme.aut.xcfatest.utils.FileUtils;


public class EditorFragment extends Fragment {
    private InputStream templateInputStream;
    private String fileToModify;
    private EditText editor;
    private boolean modified;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        modified = false;
        templateInputStream = null;
        fileToModify = null;
        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        if (extras != null) {
            String template = extras.getString("template");
            if (template != null) {
                try {
                    templateInputStream = getActivity().getAssets().open(template);
                } catch (IOException e) {
                    if (container != null)
                        ErrorHandler.showErrorMessage(container, "File could not be read!", e);
                    else e.printStackTrace();
                }
            }
            fileToModify = extras.getString("file");
        }
        return inflater.inflate(R.layout.fragment_editor, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        editor = Objects.requireNonNull(getView()).findViewById(R.id.editor);
        String text = "";
        if (fileToModify != null) {
            try {
                templateInputStream = Objects.requireNonNull(getActivity()).openFileInput(fileToModify);
            } catch (FileNotFoundException e) {
                if (getView() != null)
                    ErrorHandler.showErrorMessage(getView(), "File not found!", e);
                else e.printStackTrace();
            }
        }
        if (templateInputStream != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(templateInputStream));
            try {
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null)
                    builder.append(line).append(System.lineSeparator());
                bufferedReader.close();
                text = builder.toString();
            } catch (IOException e) {
                if (getView() != null)
                    ErrorHandler.showErrorMessage(getView(), "File could not be read!", e);
                else e.printStackTrace();
            }
        }
        editor.setText(text);
        editor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                modified = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        TextView saveTv = getView().findViewById(R.id.save);
        TextView discard = getView().findViewById(R.id.discard);
        saveTv.setOnClickListener(view -> save(null));
        discard.setOnClickListener(view -> Objects.requireNonNull(getActivity()).onBackPressed());
    }

    public void save(Runnable onSuccess) {
        if (fileToModify != null) {
            @SuppressLint("InflateParams") RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.dialog_content, null);
            TextView textView = new TextView(getContext());
            relativeLayout.addView(textView);
            textView.setText(fileToModify);
            MyDialogBuilder.getDialog(getContext(),
                    R.string.save,
                    R.string.save_msg,
                    android.R.string.yes,
                    (dialogInterface, i) -> {
                        FileUtils.writeFile(Objects.requireNonNull(getView()), fileToModify, editor.getText());
                        modified = false;
                        XcfaRow.remove(fileToModify);
                        if (onSuccess != null) onSuccess.run();
                    },
                    R.string.save_as,
                    (dialogInterface, i) -> saveAs(onSuccess)).setView(relativeLayout).show();
        } else {
            saveAs(onSuccess);
        }
    }

    private void saveAs(Runnable onSuccess) {
        @SuppressLint("InflateParams") RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.dialog_content, null);
        EditText editText = new EditText(getContext());
        relativeLayout.addView(editText);
        editText.setHint("example.xcfa");
        editText.setSingleLine();
        MyDialogBuilder.getDialog(getContext(),
                R.string.save_as,
                R.string.save_as_msg,
                android.R.string.yes,
                (dialogInterface, i) -> {
                    String choice = editText.getText().toString();
                    fileToModify = choice.endsWith(".xcfa") ? choice : choice + ".xcfa";
                    FileUtils.writeFile(Objects.requireNonNull(getView()), fileToModify, editor.getText());
                    modified = false;
                    XcfaRow.remove(fileToModify);
                    if (onSuccess != null) onSuccess.run();
                }).setView(relativeLayout).show();
    }

    public boolean getModified() {
        return modified;
    }
}
