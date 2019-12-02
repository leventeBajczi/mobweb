package hu.bme.aut.xcfatest.tasks;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import hu.bme.aut.xcfatest.data.adapters.MyRecyclerViewAdapter;
import hu.bme.aut.xcfatest.data.model.XcfaRow;
import hu.bme.aut.xcfatest.thetacompat.XcfaAbstraction;

/**
 * This AsyncTask descendant is responsible for filling up the adapter from another thread.
 * This is useful as the XCFA parsing and compilation is long-running and therefore it would lock up
 * the UI thread were it to run on that.
 */
public class AsyncFiller extends AsyncTask<Void, XcfaRow, Void> {
    private final MyRecyclerViewAdapter adapter;
    @SuppressLint("StaticFieldLeak")
    //We will definitely have the progress bar the entire time filling up elements
    private final ProgressBar progressBar;

    /**
     * @param adapter The adapter that will hold the values.
     * @param progressBar The progressbar which has to be disabled upon completion
     */
    public AsyncFiller(MyRecyclerViewAdapter adapter, ProgressBar progressBar) {
        this.adapter = adapter;
        this.progressBar = progressBar;
    }

    /**
     * This function is run on a separate thread and therefore it contains the long-running task
     * of creating XcfaRow objects from existing XCFA files.
     *
     * @param voids dummy parameter
     * @return null, as that value is never used - the adapter holds the entire list
     */
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
                        e.printStackTrace();
                        xcfaRow = new XcfaRow(fileName, false, 0, 0, null);
                    }
                    publishProgress(xcfaRow);
                }
            }
        }
        return null;
    }

    /**
     * This function is called on the main (UI) thread, and therefore we can do UI updates from here.
     * It receives a single XcfaRow element.
     *
     * @param values a single XcfaRow element (never called with more/less params, therefore the
     *               0th element is used)
     */
    @Override
    protected void onProgressUpdate(XcfaRow... values) {
        adapter.addData(values[0]);
    }

    /**
     * When completed, the progressbar is no longer necessary. We display a tick, which fades away.
     *
     * @param aVoid dummy parameter
     */
    @Override
    protected void onPostExecute(Void aVoid) {
        progressBar.animate().
                setDuration(250).
                alpha(0f).
                start();
    }
}
