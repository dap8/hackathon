package me.digi.examples.ca.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import me.digi.examples.ca.Message;
import me.digi.examples.ca.R;

/**
 * Created by steinar on 08/10/2017.
 */

public class donationListAdapter extends ArrayAdapter<Message> {

    public donationListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public donationListAdapter(Context context, int resource, List<Message> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("cub","ofc");
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.donation_list_item, null);
        }

        Message p = getItem(position);
        if(p==null) Log.d("cub", "p is null" );

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.donationAmount);
            TextView tt2 = (TextView) v.findViewById(R.id.donationMessage);
            TextView tt3 = (TextView) v.findViewById(R.id.donationName);

            if (tt1 != null) {
                tt1.setText(p.getAmount() + "$");
            }

            if (tt2 != null) {
                tt2.setText(p.getMessage());
            }

            if (tt3 != null) {
                tt3.setText(p.getName());
            }

        }

        return v;
    }

}