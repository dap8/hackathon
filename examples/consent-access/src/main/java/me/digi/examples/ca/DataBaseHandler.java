package me.digi.examples.ca;

import android.content.Context;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.digi.examples.ca.searchData.ColorSuggestion;
import me.digi.examples.ca.searchData.DataHelper;

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
        try
        {
            Log.d("pepe", "About to add with this name: " + profile.getName());
            Log.d("pepe", "Size of message array: " + profile.getMessages().size());
            String key = profileEndPoint.push().getKey();
            Log.d("pepe", "got this key from firebase: " + key);
            profile.setId(key);
            Map<String, Message> messages = profile.getMessages();
            profile.setMessages(null);
            try
            {
                profileEndPoint.child(key).setValue(profile).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("onfailure_log", e.getLocalizedMessage());
                    }
                });;
            }

            catch (DatabaseException e){
                Log.e("DATABASE_ERROR","ERROR GUNGA GINGA inside profilenedpoint error:",e);
            }

            DatabaseReference messageNode = profileEndPoint.child(key).child(MESSAGE_CONSTANT);
            //Iterator it = messages.entrySet().iterator();

            for(Map.Entry<String, Message> message : messages.entrySet())
            {
                String messageKey = messageNode.push().getKey();
                message.getValue().setId(messageKey);
                messageNode.child(messageKey).setValue(message.getValue());
            }
            profileStorage.addProfile(profile);
        }

        catch (DatabaseException e){
            Log.e("DATABASE_ERROR","ERROR GUNGA GINGA:",e);
        }

    }

    public void addMessage(Message message, String profileId){
        DatabaseReference messageNode = profileEndPoint.child(profileId).child(MESSAGE_CONSTANT);
        String key = messageNode.push().getKey();
        messageNode.child(key).setValue(message);
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
                    Map<String, Message> messages = null;
                    if(profileSnapshot.hasChild(MESSAGE_CONSTANT))
                    {
                        Log.d("pepe", "has messages");
                         messages = profile.getMessages();

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
                    profile.setMessages(messages);
                    profileStorage.addProfile(profile);
                }
                DataHelper.makeSuggestionList(getColorSuggestions(profileStorage.getProfiles()));

                Log.d("pepe", "name from barstorage: " + profileStorage.getProfiles().get(0).getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ERROR", databaseError.getMessage());
            }
        });

    }

    public List<ColorSuggestion> getColorSuggestions(List<Profile> profiles){
        List<ColorSuggestion> colorSuggestions = new ArrayList<>();

        for(Profile profile : profiles)
        {
            colorSuggestions.add(new ColorSuggestion(profile.getName()));
        }

        return  colorSuggestions;
    }




}
