/*
 * Copyright © 2017 digi.me. All rights reserved.
 */

package me.digi.examples.ca;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.digi.examples.ca.adapters.diagnosisListAdapter;
import me.digi.sdk.core.session.CASession;
import me.digi.sdk.core.DigiMeAuthorizationManager;
import me.digi.sdk.core.DigiMeClient;
import me.digi.sdk.core.SDKCallback;
import me.digi.sdk.core.SDKException;
import me.digi.sdk.core.SDKResponse;
import me.digi.sdk.core.entities.CAFileResponse;
import me.digi.sdk.core.entities.CAFiles;


public class CallbackActivity extends AppCompatActivity {

    private static final String TAG = "DemoCallbackActivity";
    private SDKCallback<CASession> cb;
    private DigiMeAuthorizationManager authManager;
    private TextView statusText;
    private RadioGroup radioGroup;
    private Button createAccountButton;
    private DataBaseHandler databasehandler;
    private String mDiagnosis;
    private EditText amountRequested;
    private EditText bankAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        amountRequested = (EditText) findViewById(R.id.requestedAmountInput);
        bankAccount = (EditText) findViewById(R.id.bankAccountInfo);

        createAccountButton = (Button) findViewById(R.id.createAccountButton);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databasehandler = new DataBaseHandler(getApplicationContext());
                if(mDiagnosis == null) mDiagnosis = "Herbert Guðmundsson";
                int requestedAmount = Integer.parseInt(amountRequested.getText().toString());
                String bankAccountInfo = bankAccount.getText().toString();
                databasehandler.addProfile(new Profile("Óskar Ólafsson", mDiagnosis, bankAccountInfo, requestedAmount));
                Intent i = new Intent(CallbackActivity.this, ProfileActivity.class);
                i.putExtra("name", "Óskar Ólafsson");
                startActivity(i);
            }
        });

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        this.cb = new SDKCallback<CASession>() {
            @Override
            public void succeeded(SDKResponse<CASession> result) {
                onSessionReceived();
                writeStatus("Session created!");
            }

            @Override
            public void failed(SDKException exception) {
                writeStatus("Session create failed!");
                Log.d(TAG, exception.getMessage());
            }
        };
        DigiMeClient.getInstance().createSession(cb);

        /*final Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setVisibility(View.GONE);
                DigiMeClient.getInstance().createSession(cb);
            }
        });

        statusText = (TextView) findViewById(R.id.callback_status);*/
    }

    public void addRadioButton(String text) {
        RadioButton btn = new RadioButton(this);
        btn.setText(text);
        radioGroup.addView(btn);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (authManager != null) {
            authManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onSessionReceived() {
        authManager = DigiMeClient.getInstance().authorizeInitializedSession(this, new SDKCallback<CASession>() {
            @Override
            public void succeeded(SDKResponse<CASession> result) {
                requestFileList();
            }

            @Override
            public void failed(SDKException exception) {
                writeStatus("Authorization failed!");
            }
        });
    }

    public void requestFileList() {
        DigiMeClient.getInstance().getFileList(new SDKCallback<CAFiles>() {
            @Override
            public void succeeded(SDKResponse<CAFiles> result) {
                CAFiles files = result.body;
                getFileContent(files.fileIds);
            }

            @Override
            public void failed(SDKException exception)  {

            }
        });
    }

    public void getFileContent(List<String> fileIds) {
        for (final String fileId : fileIds) {
            DigiMeClient.getInstance().getFileContent(fileId, new SDKCallback<CAFileResponse>() {
                @Override
                public void succeeded(SDKResponse<CAFileResponse> result) {
                    if (!result.body.fileContent.toString().contains("null")){
                        Pattern pattern = Pattern.compile("'(.*?)'");
                        Matcher matcher = pattern.matcher(result.body.fileContent.toString());
                        if (matcher.find())
                        {
                            Log.d(TAG, matcher.group(1));
                            addRadioButton(matcher.group(1));
                            mDiagnosis = matcher.group(1);
                        }
                    }
                }

                @Override
                public void failed(SDKException exception) {
                    Log.d(TAG, "Failed to retrieve file content for file: " + fileId + "; Reason: " + exception);
                }
            });
        }
    }

    private void writeStatus(String status) {
        Log.d(TAG, status);
    }

}
