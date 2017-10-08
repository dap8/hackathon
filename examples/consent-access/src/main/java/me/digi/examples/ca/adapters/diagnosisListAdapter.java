package me.digi.examples.ca.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import me.digi.examples.ca.R;

/**
 * Created by steinar on 08/10/2017.
 */

public class diagnosisListAdapter extends ArrayAdapter<String> {

    public diagnosisListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public diagnosisListAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.donation_list_item, null);
        }

        String p = getItem(position);

        if (p != null) {
            /* TextView tt1 = (TextView) v.findViewById(R.id.id);
            TextView tt2 = (TextView) v.findViewById(R.id.categoryId);
            TextView tt3 = (TextView) v.findViewById(R.id.description);

            if (tt1 != null) {
                tt1.setText(p.getId());
            }

            if (tt2 != null) {
                tt2.setText(p.getCategory().getId());
            }

            if (tt3 != null) {
                tt3.setText(p.getDescription());
            } */
        }

        return v;
    }

}