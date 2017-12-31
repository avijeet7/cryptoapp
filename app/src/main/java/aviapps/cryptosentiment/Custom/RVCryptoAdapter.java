package aviapps.cryptosentiment.Custom;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import aviapps.cryptosentiment.GetSet.GetSetStream;
import aviapps.cryptosentiment.R;

/*
 * Created by Avijeet on 30-Dec-17.
 */

public class RVCryptoAdapter extends RecyclerView.Adapter<RVCryptoAdapter.ViewHolder> {
    private List<GetSetStream> values;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public RVCryptoAdapter(List<GetSetStream> myData, Context context) {
        values = myData;
        this.context = context;
    }

    public void add(int position, GetSetStream item) {
        values.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        values.remove(position);
        notifyItemRemoved(position);
    }

    public void update(int position, GetSetStream tick) {
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
        final GetSetStream object = values.get(position);
        holder.txtHeader.setText(object.getPair());
        holder.txtHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                remove(position);
            }
        });

        DecimalFormat df = new DecimalFormat("#.##");
        DecimalFormat ltpformat = new DecimalFormat("#.####");
        if (object.getLtp() > 10000)
            ltpformat = new DecimalFormat("#");
        else if (object.getLtp() > 100)
            ltpformat = new DecimalFormat("#.##");

        if (object.getPc() < 0) {
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.colorNegativeRed));
            holder.tv_pc.setTextColor(Color.RED);
        } else {
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.colorPositiveGreen));
            holder.tv_pc.setTextColor(Color.GREEN);
        }

        String img_name = object.getPair();
        img_name = img_name.replace("USD", "");
        img_name = img_name.toLowerCase();

        // Handling special cases
        if (img_name.equalsIgnoreCase("qtm")) {
            img_name = "qtum";
        }

        int id = context.getResources().getIdentifier(img_name, "drawable", context.getPackageName());

        holder.iv_main.setImageResource(id);
        holder.txtFooter.setText("bitfinex");
        holder.tv_ltp.setText("$ " + ltpformat.format(object.getLtp()));
        holder.tv_pc.setText(df.format(object.getPc()) + " %");
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
        ImageView iv_main;

        ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = v.findViewById(R.id.firstLine);
            txtFooter = v.findViewById(R.id.secondLine);
            tv_ltp = v.findViewById(R.id.tv_second);
            tv_pc = v.findViewById(R.id.tv_first);
            iv_main = v.findViewById(R.id.iv_main);
        }
    }
}
