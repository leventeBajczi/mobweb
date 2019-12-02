package hu.bme.aut.xcfatest.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.xcfatest.R;
import hu.bme.aut.xcfatest.data.listeners.XcfaRowListener;
import hu.bme.aut.xcfatest.data.model.XcfaRow;

/**
 * Adapter to be used for the RecyclerView declared in content_main.xml
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    /**
     * The data currently displayed.
     */
    private List<XcfaRow> data;

    /**
     * Initialize the object. We don't take any values just yet, because we will update them
     * asynchronously.
     * @see hu.bme.aut.xcfatest.tasks.AsyncFiller
     */
    public MyRecyclerViewAdapter() {
        this.data = new ArrayList<>();
    }

    /**
     * Creating the ViewHolder for later use.
     *
     * @param parent   The parent view to get the context from.
     * @param viewType NC
     * @return The ViewHolder holding a row, which has a CardView as its root layout element
     */
    @NonNull
    @Override
    public MyRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_row, parent, false);
        XcfaRowListener listener = new XcfaRowListener();
        v.setOnClickListener(listener);
        TextView name = v.findViewById(R.id.name);
        TextView xcfaOk = v.findViewById(R.id.xcfa_ok);
        TextView noOfVars = v.findViewById(R.id.no_of_vars);
        TextView noOfThreads = v.findViewById(R.id.no_of_threads);
        return new MyViewHolder(v, listener, name, xcfaOk, noOfVars, noOfThreads);
    }

    /**
     * Filling in the data from an XcfaRow
     *
     * @param holder   The ViewHolder to be filled in
     * @param position The position of the object to be displayed (to be looked up from data : XcfaRow [0..*])
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        XcfaRow xcfaRow = data.get(position);
        Context context = holder.cardView.getContext();
        holder.listener.setXcfaRow(xcfaRow);
        holder.name.setText(xcfaRow.getName());
        holder.xcfaOk.setText(xcfaRow.isOk() ? context.getString(R.string.card_status_ok) : context.getString(R.string.card_status_error));
        holder.xcfaOk.setTextColor(xcfaRow.isOk() ? context.getResources().getColor(R.color.ok, context.getTheme()) : context.getResources().getColor(R.color.error, context.getTheme()));
        holder.noOfVars.setText(context.getString(R.string.card_var_display, xcfaRow.getVars()));
        holder.noOfThreads.setText(context.getString(R.string.card_thread_display, xcfaRow.getThreads()));

    }

    /**
     * @return The number of items in the adapter.
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * The ViewHolder to be used for the adapter
     */
    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final XcfaRowListener listener;
        private final TextView name;
        private final TextView xcfaOk;
        private final TextView noOfVars;
        private final TextView noOfThreads;

        private MyViewHolder(CardView v, XcfaRowListener listener, TextView name, TextView xcfaOk, TextView noOfVars, TextView noOfThreads) {
            super(v);
            cardView = v;
            this.listener = listener;
            this.name = name;
            this.xcfaOk = xcfaOk;
            this.noOfVars = noOfVars;
            this.noOfThreads = noOfThreads;
        }
    }

    /**
     * Add new data to the adapter
     *
     * @param newElement the new element to be added
     */
    public void addData(XcfaRow newElement) {
        data.add(newElement);
        notifyDataSetChanged();
    }
}