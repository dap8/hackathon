/*
 * Copyright © 2017 digi.me. All rights reserved.
 */

package me.digi.examples.ca;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import me.digi.examples.ca.searchData.ColorSuggestion;
import me.digi.examples.ca.searchData.ColorWrapper;
import me.digi.examples.ca.searchData.DataHelper;
import me.digi.sdk.core.session.CASession;
import me.digi.sdk.core.DigiMeClient;
import me.digi.sdk.core.SDKException;
import me.digi.sdk.core.SDKListener;
import me.digi.sdk.core.entities.CAFileResponse;
import me.digi.sdk.core.entities.CAFiles;
import me.digi.sdk.core.internal.AuthorizationException;

public class MainActivity extends AppCompatActivity implements SDKListener {

    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;
    private static final String TAG = "DemoActivity";
    private TextView statusText;
    private Button gotoCallback;
    private TextView downloadedCount;
    private FloatingActionButton actionButton;
    private DigiMeClient dgmClient;
    private FloatingSearchView mSearchView;
    private final AtomicInteger counter = new AtomicInteger(0);
    private final AtomicInteger failedCount = new AtomicInteger(0);
    private int allFiles = 0;
    private String mLastQuery = "";
    private boolean mIsDarkSearchTheme = false;
    private DataBaseHandler databasehandler;
    private ProfileStorage profileStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dgmClient = DigiMeClient.getInstance();

        profileStorage = ((ProfileStorage)getApplicationContext());
        profileStorage.init();
        databasehandler = new DataBaseHandler(getApplicationContext());
        //databasehandler.loadProfiles();
        //addTestData();
        //List<Message> messages = new ArrayList<>();
        //databasehandler.addProfile(new Profile("bob","sick","1111",1000));

        actionButton = (FloatingActionButton) findViewById(R.id.actionButton);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });

        /* statusText = (TextView) findViewById(R.id.status_text);
        gotoCallback = (Button) findViewById(R.id.go_to_callback);
        gotoCallback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dgmClient.removeListener(MainActivity.this);
                startActivity(new Intent(MainActivity.this, CallbackActivity.class));
            }
        });
        gotoCallback.setVisibility(View.GONE);
        downloadedCount = (TextView) findViewById(R.id.counter);*/

        // Search Bar stuff //

        mSearchView = (FloatingSearchView) findViewById(R.id.search_bar);
        setupSearchBar();

        ////////////

        //Add this activity as a listener to DigiMeClient and start the auth flow
        dgmClient.addListener(this);
        // dgmClient.authorize(this, null);
    }

    private void setupSearchBar() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {

                    //this shows the top left circular progress
                    //you can call it where ever you want, but
                    //it makes sense to do it when loading something in
                    //the background.
                    mSearchView.showProgress();

                    //simulates a query call to a data source
                    //with a new query.
                    DataHelper.findSuggestions(getApplicationContext(), newQuery, 5,
                            FIND_SUGGESTION_SIMULATED_DELAY, new DataHelper.OnFindSuggestionsListener() {

                                @Override
                                public void onResults(List<ColorSuggestion> results) {

                                    //this will swap the data and
                                    //render the collapse/expand animations as necessary
                                    mSearchView.swapSuggestions(results);

                                    //let the users know that the background
                                    //process has completed
                                    mSearchView.hideProgress();
                                }
                            });
                }

                Log.d(TAG, "onSearchTextChanged()");
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

                ColorSuggestion colorSuggestion = (ColorSuggestion) searchSuggestion;
                DataHelper.findColors(getApplicationContext(), colorSuggestion.getBody(),
                        new DataHelper.OnFindColorsListener() {

                            @Override
                            public void onResults(List<ColorWrapper> results) {
                                //show search results
                            }

                        });
                Log.d(TAG, "onSuggestionClicked()");

                mLastQuery = searchSuggestion.getBody();
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                i.putExtra("name", mLastQuery);
                startActivity(i);
            }

            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;

                DataHelper.findColors(getApplicationContext(), query,
                        new DataHelper.OnFindColorsListener() {

                            @Override
                            public void onResults(List<ColorWrapper> results) {
                                //show search results
                            }

                        });
                Log.d(TAG, "onSearchAction()");
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

                //show suggestions when search bar gains focus (typically history suggestions)
                mSearchView.swapSuggestions(DataHelper.getHistory(getApplicationContext(), 3));

                Log.d(TAG, "onFocus()");
            }

            @Override
            public void onFocusCleared() {

                //set the title of the bar so that when focus is returned a new query begins
                mSearchView.setSearchBarTitle(mLastQuery);

                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
                //mSearchView.setSearchText(searchSuggestion.getBody());

                Log.d(TAG, "onFocusCleared()");
            }
        });

        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {
                ColorSuggestion colorSuggestion = (ColorSuggestion) item;

                String textColor = mIsDarkSearchTheme ? "#ffffff" : "#000000";
                String textLight = mIsDarkSearchTheme ? "#bfbfbf" : "#787878";

                if (colorSuggestion.getIsHistory()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_history_black_24dp, null));

                    Util.setIconColor(leftIcon, Color.parseColor(textColor));
                    leftIcon.setAlpha(.36f);
                } else {
                    leftIcon.setAlpha(0.0f);
                    leftIcon.setImageDrawable(null);
                }

                textView.setTextColor(Color.parseColor(textColor));
                String text = colorSuggestion.getBody()
                        .replaceFirst(mSearchView.getQuery(),
                                "<font color=\"" + textLight + "\">" + mSearchView.getQuery() + "</font>");
                textView.setText(Html.fromHtml(text));
            }

        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dgmClient.getAuthManager().onActivityResult(requestCode, resultCode, data);

    }

    /**
     *
     * SDKListener overrides for DiGiMeClient
     */

    @Override
    public void sessionCreated(CASession session) {
        Log.d(TAG, "Session created with token " + session.getSessionKey());
        statusText.setText(R.string.session_created);
    }

    @Override
    public void sessionCreateFailed(SDKException reason) {
        Log.d(TAG, reason.getMessage());
        gotoCallback.setVisibility(View.VISIBLE);
    }

    @Override
    public void authorizeSucceeded(CASession session) {
        Log.d(TAG, "Session created with token " + session.getSessionKey());
        statusText.setText(R.string.session_authorized);
        DigiMeClient.getInstance().getFileList(null);
    }

    @Override
    public void authorizeDenied(AuthorizationException reason) {
        Log.d(TAG, "Failed to authorize session; Reason " + reason.getThrowReason().name());
        statusText.setText(R.string.auth_declined);
        gotoCallback.setVisibility(View.VISIBLE);
    }

    @Override
    public void authorizeFailedWithWrongRequestCode() {

    }

    @Override
    public void clientRetrievedFileList(CAFiles files) {
        downloadedCount.setText(String.format(Locale.getDefault(), "Downloaded : %d/%d", 0, files.fileIds.size()));
        allFiles = files.fileIds.size();
        for (final String fileId :
                files.fileIds) {
            counter.incrementAndGet();
            DigiMeClient.getInstance().getFileContent(fileId, null);
        }
        String progress = getResources().getQuantityString(R.plurals.files_retrieved, files.fileIds.size(), files.fileIds.size());
        statusText.setText(progress);
        gotoCallback.setVisibility(View.VISIBLE);
    }

    @Override
    public void clientFailedOnFileList(SDKException reason) {
        Log.d(TAG, "Failed to retrieve file list: " + reason.getMessage());
        gotoCallback.setVisibility(View.VISIBLE);
    }

    @Override
    public void contentRetrievedForFile(String fileId, CAFileResponse content) {
        Log.d(TAG, content.fileContent.toString());
        updateCounters();
    }

    @Override
    public void jsonRetrievedForFile(String fileId, JsonElement content) {
    }

    @Override
    public void contentRetrieveFailed(String fileId, SDKException reason) {
        Log.d(TAG, "Failed to retrieve file content for file: " + fileId + "; Reason: " + reason);
        failedCount.incrementAndGet();
        updateCounters();
    }

    private void updateCounters() {
        int current = counter.decrementAndGet();
        if (failedCount.get() > 0) {
            downloadedCount.setText(String.format(Locale.getDefault(), "Downloaded : %d/%d", allFiles - current, allFiles));
        } else {
            downloadedCount.setText(String.format(Locale.getDefault(), "Downloaded : %d/%d; Failed: %d", allFiles - current, allFiles, failedCount.get()));
        }
        if (current == 0) {
            statusText.setText(R.string.data_retrieved);
        }
    }

    private void addTestData() {
        String[] greetings = {"Get better", "Stay strong", "You're a hero!", "I'm praying for you"};
        String[] names = {"Tatum", "Lovella", "Jasmin", "Ofelia", "Ellena","Annemarie","Roy","Trey","Consuela", "Laurice","Klara","Martin","Natosha", "Vera","Robbin","Eusebio", "Conchita","Lakia","Florine","Fred"};
        String[] diseases = {"Leukemia", "Alzheimer's", "Parkinson's", "Hepatitis C"};
        int NUMBER_OF_PROFILES = 10;
        int NUMBER_OF_MESSAGES = 10;
        final Random rand = new Random();
        int messageId = 0;

        for(int i = 0; i<NUMBER_OF_PROFILES; i++)
        {
            String account = rand.nextInt(9999) + "-" + rand.nextInt(99) + "-" + rand.nextInt(9999);
            int amount = rand.nextInt(100000);
            int raisedAmount = rand.nextInt(amount);

            String name = names[rand.nextInt(names.length-1)];
            String diagnosis = diseases[rand.nextInt(diseases.length-1)];

            Map<String, Message> messages = new HashMap<>();
            //List<Message> messages = new ArrayList<>();
            for(int k = 0; k<NUMBER_OF_MESSAGES; k++)
            {
                String messageName = names[rand.nextInt(names.length-1)];
                String greeting = greetings[rand.nextInt(greetings.length-1)];
                int donationAmount = rand.nextInt(amount/10);
                Message message = new Message(messageName,greeting,donationAmount);
                messages.put(messageId+"",message);
                messageId++;
            }
            Profile profile = new Profile(name,diagnosis,account,amount,raisedAmount,messages);
            //profileStorage.addProfile(profile);
            databasehandler.addProfile(profile);
        }
    }
}
