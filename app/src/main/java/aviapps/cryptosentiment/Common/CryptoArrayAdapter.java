package aviapps.cryptosentiment.Common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import aviapps.cryptosentiment.GetSet.Crypto;
import aviapps.cryptosentiment.R;

/**
 * Created by avije on 14-Dec-17.
 */

public class CryptoArrayAdapter extends ArrayAdapter<Crypto> {
    private final Context context;
    private final ArrayList<Crypto> values;

    public CryptoArrayAdapter(Context context, ArrayList<Crypto> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listview, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
        TextView description = (TextView) rowView.findViewById(R.id.secondLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        textView.setText(values.get(position).getTitle());
        description.setText(values.get(position).getDescription());
        // change the icon for Windows and iPhone
//        String s = values.get(position).getDescription();
//        if (s.startsWith("iPhone")) {
//            imageView.setImageResource(R.drawable.no);
//        } else {
//            imageView.setImageResource(R.drawable.ok);
//        }

        return rowView;
    }
}
