package hu.bme.aut.xcfatest.tasks;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import hu.bme.aut.xcfatest.data.adapters.MyRecyclerViewAdapter;
import hu.bme.aut.xcfatest.data.model.XcfaRow;
import hu.bme.aut.xcfatest.thetacompat.XcfaAbstraction;
import hu.bme.aut.xcfatest.utils.ErrorHandler;

public class AsyncFiller extends AsyncTask<Void, XcfaRow, Void> {
    private final MyRecyclerViewAdapter adapter;
    @SuppressLint("StaticFieldLeak")
    //We will definitely have the progress bar the entire time filling up elements
    private final ProgressBar progressBar;

    public AsyncFiller(MyRecyclerViewAdapter adapter, ProgressBar progressBar) {
        this.adapter = adapter;
        this.progressBar = progressBar;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for (String fileName : progressBar.getContext().fileList()) {
            if (fileName.endsWith(".xcfa")) {
                if (XcfaRow.exists(fileName)) {
                    publishProgress(XcfaRow.get(fileName));
                } else {
                    XcfaRow xcfaRow;
                    try {
                        XcfaAbstraction xcfa = XcfaAbstraction.fromStream(progressBar.getContext().openFileInput(fileName));
                        xcfaRow = new XcfaRow(fileName, true, xcfa.getVars(), xcfa.getThreads(), xcfa);
                    } catch (Exception e) {
                        ErrorHandler.showErrorMessage(progressBar, "File " + fileName + " could not be compiled!", e);
                        xcfaRow = new XcfaRow(fileName, false, 0, 0, null);
                    }
                    publishProgress(xcfaRow);
                }
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(XcfaRow... values) {
        adapter.addData(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressBar.animate().
                setDuration(250).
                alpha(0f).
                start();
    }
}
