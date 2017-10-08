package me.digi.examples.ca;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;
import me.digi.examples.ca.adapters.donationListAdapter;

public class ProfileActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private LinearLayout blurredBg;
    private ProgressBar progressBar;
    private ListView donationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        blurredBg = (LinearLayout) findViewById(R.id.blurredBackground);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        donationList = (ListView) findViewById(R.id.donationList);

        ArrayList<String> values = new ArrayList<String>();
        values.add("Epli");
        values.add("Epli");
        values.add("Epli");
        values.add("Epli");
        values.add("Epli");
        values.add("Epli");
        values.add("Epli");
        values.add("Epli");
        values.add("Epli");
        values.add("Epli");
        values.add("Epli");
        values.add("Epli");
        values.add("Epli");
        values.add("Epli");
        values.add("Epli");


        ArrayAdapter<String> adapter = new donationListAdapter(this,
                android.R.layout.simple_list_item_1, values);


        // Assign adapter to ListView
        donationList.setAdapter(adapter);

        setListViewHeightBasedOnChildren(donationList);

        progressBar.setMax(10000);
        progressBar.setProgress(6322);

        scrollView.setVerticalScrollbarPosition(0);

        // Blurry.with(getApplicationContext()).radius(25).sampling(2).onto(blurredBg);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ActionBar.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
