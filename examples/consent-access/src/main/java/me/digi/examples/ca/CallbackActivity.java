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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list = (ListView) findViewById(R.id.listView1);
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
        list.setAdapter(adapter);



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

        final Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setVisibility(View.GONE);
                DigiMeClient.getInstance().createSession(cb);
            }
        });

        statusText = (TextView) findViewById(R.id.callback_status);

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
                writeStatus("Session authorized!");
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
                writeStatus("Failed to fetch list" + exception.getMessage());

            }
        });
    }

    public void getFileContent(List<String> fileIds) {
        for (final String fileId : fileIds) {
            DigiMeClient.getInstance().getFileContent(fileId, new SDKCallback<CAFileResponse>() {
                @Override
                public void succeeded(SDKResponse<CAFileResponse> result) {
                    //writeStatus("Content retrieved");
                    if (!result.body.fileContent.toString().contains("null")){
                        Pattern pattern = Pattern.compile("'(.*?)'");
                        Matcher matcher = pattern.matcher(result.body.fileContent.toString());
                        if (matcher.find())
                        {
                            arrayList.add(matcher.group(1));
                            arrayList.add("stuff");
                            arrayList.add("stuff");
                            arrayList.add("stuff");
                            arrayList.add("stuff");
                            arrayList.add("stuff");
                            arrayList.add("stuff");
                            arrayList.add("stuff");
                            arrayList.add("stuff");
                            arrayList.add("stuff");
                            arrayList.add(matcher.group(1));
                            adapter.notifyDataSetChanged();
                            writeStatus(matcher.group(1));
                        }
                    }

                    Log.d(TAG, "Content for file " + fileId + ": " + result.body.fileContent.toString());
                }

                @Override
                public void failed(SDKException exception) {
                    Log.e(TAG, "Failed to retrieve file content for file: " + fileId + "; Reason: " + exception);
                }
            });
        }
    }

    private void writeStatus(String status) {
        statusText.setText(status);
        Log.d(TAG, status);
    }

}
