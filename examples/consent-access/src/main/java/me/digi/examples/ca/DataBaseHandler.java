package me.digi.examples.ca;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Danni on 07/10/2017.
 */

public class DataBaseHandler {

    private DatabaseReference mDatabase;
    private DatabaseReference profileEndPoint;
    private ProfileStorage profileStorage;
    private String MESSAGE_CONSTANT = "messages";
    private String PROFILE_CONSTANT = "profiles";
    private Context ctx;

    public DataBaseHandler(Context ctx){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        profileEndPoint = mDatabase.child(PROFILE_CONSTANT);
        this.ctx = ctx;
        profileStorage = ((ProfileStorage) ctx);

    }

    public void addProfile(Profile profile){
        String key = profileEndPoint.push().getKey();
        profile.setId(key);
        Map<String, Message> messages = profile.getMessages();
        profile.setMessages(null);
        profileEndPoint.child(key).setValue(profile);
        DatabaseReference messageNode = profileEndPoint.child(key).child("messages");
        //Iterator it = messages.entrySet().iterator();

        for(Map.Entry<String, Message> message : messages.entrySet())
        {
            String messageKey = messageNode.push().getKey();
            message.getValue().setId(messageKey);
            messageNode.child(messageKey).setValue(message.getValue());
        }


    }

    public void loadProfiles(){
        final List<Profile> profiles = new ArrayList<>();
        profileEndPoint.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot profileSnapshot: dataSnapshot.getChildren()){
                    Profile profile = profileSnapshot.getValue(Profile.class);
                    Log.d("pepe", "name: " + profile.getName());
                    Log.d("pepe", "id: " + profile.getId());
                    Log.d("pepe", "id: " + profile.getGoalAmount());
                    if(profileSnapshot.hasChild(MESSAGE_CONSTANT))
                    {
                        Log.d("pepe", "has messages");
                        Map<String, Message> messages = profile.getMessages();

                        for(Map.Entry<String, Message> message : messages.entrySet())
                        {
                            Log.d("pepe message", message.getValue().getMessage());
                        }
                        /*DataSnapshot messageSnapshots = profileSnapshot.child(MESSAGE_CONSTANT);
                        for(DataSnapshot messageSnapshot: messageSnapshots.getChildren()){
                            Message message = messageSnapshot.getValue(Message.class);
                            Log.d("pepe", "name: " + message.getName());
                            Log.d("pepe", "message: " + message.getMessage());
                        }*/

                    }

                    else{
                        Log.d("pepe", "has no messages");
                    }
                    profileStorage.addProfile(profile);
                }

                Log.d("pepe", "name from barstorage: " + profileStorage.getProfiles().get(0).getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ERROR", databaseError.getMessage());
            }
        });

    }




}
