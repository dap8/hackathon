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
import android.widget.TextView;

import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;
import me.digi.examples.ca.adapters.donationListAdapter;

public class ProfileActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private LinearLayout blurredBg;
    private ProgressBar progressBar;
    private ListView donationList;
    private TextView diagnosis;
    private TextView amountRaised;
    private TextView amountGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        blurredBg = (LinearLayout) findViewById(R.id.blurredBackground);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        donationList = (ListView) findViewById(R.id.donationList);
        diagnosis = (TextView) findViewById(R.id.profileDiagnosis);
        amountRaised = (TextView) findViewById(R.id.amountRaised);
        amountGoal = (TextView) findViewById(R.id.amountGoal);

        String name = getIntent().getExtras().getString("name");
        Profile mProfile = ProfileStorage.getProfile(name);


        ArrayList<Message> values = new ArrayList<Message>();
        // Fyrir öll mProfile.messages, gera values.add(aksdjskaf);


        ArrayAdapter<Message> adapter = new donationListAdapter(this,
                android.R.layout.simple_list_item_1, values);


        // Assign adapter to ListView
        donationList.setAdapter(adapter);

        setListViewHeightBasedOnChildren(donationList);

        amountRaised.setText(mProfile.getRaisedAmount() + "$");
        amountGoal.setText(mProfile.getGoalAmount() + "$");
        diagnosis.setText(mProfile.getDiagnosis());

        progressBar.setMax(mProfile.getGoalAmount());
        progressBar.setProgress(mProfile.getRaisedAmount());

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
