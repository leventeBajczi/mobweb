package hu.bme.aut.xcfatest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import hu.bme.aut.xcfatest.R;
import hu.bme.aut.xcfatest.model.XcfaRow;

/**
 * Adapter to be used for the RecyclerView declared in content_main.xml
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    /**
     * The data currently displayed.
     */
    private List<XcfaRow> data;

    /**
     * Take data to be displayed.
     *
     * @param data the list of data needing to be displayed
     */
    public MyRecyclerViewAdapter(XcfaRow[] data) {
        this.data = Arrays.asList(data);
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
        TextView name = v.findViewById(R.id.name);
        TextView xcfaOk = v.findViewById(R.id.xcfa_ok);
        TextView noOfVars = v.findViewById(R.id.no_of_vars);
        TextView noOfThreads = v.findViewById(R.id.no_of_threads);
        return new MyViewHolder(v, name, xcfaOk, noOfVars, noOfThreads);
    }

    /**
     * Filling in the data from an XcfaRow
     *
     * @param holder   The ViewHolder to be filled in
     * @param position The position of the object to be displayed (to be looked up from data : XcfaRow [0..*])
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Context context = holder.cardView.getContext();
        holder.name.setText(data.get(position).getName());
        holder.xcfaOk.setText(data.get(position).isOk() ? context.getString(R.string.card_status_ok) : context.getString(R.string.card_status_error));
        holder.xcfaOk.setTextColor(data.get(position).isOk() ? context.getResources().getColor(R.color.ok, context.getTheme()) : context.getResources().getColor(R.color.error, context.getTheme()));
        holder.noOfVars.setText(context.getString(R.string.card_var_display, data.get(position).getVars()));
        holder.noOfThreads.setText(context.getString(R.string.card_var_display, data.get(position).getVars()));
        holder.noOfThreads.setText(context.getString(R.string.card_thread_display, data.get(position).getThreads()));

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
        private final TextView name;
        private final TextView xcfaOk;
        private final TextView noOfVars;
        private final TextView noOfThreads;

        private MyViewHolder(CardView v, TextView name, TextView xcfaOk, TextView noOfVars, TextView noOfThreads) {
            super(v);
            cardView = v;
            this.name = name;
            this.xcfaOk = xcfaOk;
            this.noOfVars = noOfVars;
            this.noOfThreads = noOfThreads;
        }
    }
}