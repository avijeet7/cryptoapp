package aviapps.cryptosentiment.Custom;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

import aviapps.cryptosentiment.R;

/*
 * Created by Avijeet on 30-Dec-17.
 */

public class RVCryptoAdapter extends RecyclerView.Adapter<RVCryptoAdapter.ViewHolder> {
    private List<JSONObject> values;

    // Provide a suitable constructor (depends on the kind of dataset)
    public RVCryptoAdapter(List<JSONObject> myData) {
        values = myData;
    }

    public void add(int position, JSONObject item) {
        values.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        values.remove(position);
        notifyItemRemoved(position);
    }

    public void update(int position, JSONObject tick) {
        values.set(position, tick);
        notifyItemChanged(position);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RVCryptoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.listview, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final JSONObject name = values.get(position);
        holder.txtHeader.setText(name.optString("symbol"));
        holder.txtHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });

        holder.txtFooter.setText(name.optString("chanId"));
        holder.tv_ltp.setText(name.optString("ltp"));
        holder.tv_pc.setText(name.optString("pc"));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        public View layout;
        // each data item is just a string in this case
        TextView txtHeader, txtFooter, tv_ltp, tv_pc;

        ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = v.findViewById(R.id.firstLine);
            txtFooter = v.findViewById(R.id.secondLine);
            tv_ltp = v.findViewById(R.id.tv_second);
            tv_pc = v.findViewById(R.id.tv_first);
        }
    }
}
