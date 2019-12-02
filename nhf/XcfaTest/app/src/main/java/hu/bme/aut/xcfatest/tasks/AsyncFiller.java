package hu.bme.aut.xcfatest.tasks;

import android.os.AsyncTask;

import hu.bme.aut.xcfatest.adapter.MyRecyclerViewAdapter;
import hu.bme.aut.xcfatest.model.XcfaRow;

/**
 * This AsyncTask descendant is responsible for filling up the adapter from another thread.
 * This is useful as the XCFA parsing and compilation is long-running and therefore it would lock up
 * the UI thread were it to run on that.
 */
public class AsyncFiller extends AsyncTask<Void, XcfaRow, Void> {
    private final MyRecyclerViewAdapter adapter;

    /**
     * @param adapter The adapter that will hold the values.
     */
    public AsyncFiller(MyRecyclerViewAdapter adapter) {
        this.adapter = adapter;
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
        for (int i = 0; i < 8; ++i) {
            publishProgress(new XcfaRow("something/long/filename" + 2 * i, true, 2, 4));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            publishProgress(new XcfaRow("something/long/filename" + (2 * i + 1), false, 2, 4));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
}
