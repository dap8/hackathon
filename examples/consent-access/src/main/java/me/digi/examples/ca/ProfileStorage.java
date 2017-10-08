package me.digi.examples.ca;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danni on 07/10/2017.
 */

public class ProfileStorage extends Application {
    private List<Profile> profiles;

    public void init()
    {
        this.profiles = new ArrayList<>();
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public void addProfile(Profile profile){
        profiles.add(profile);
    }

    public Profile getProfile(String name) {
        for(Profile profile : profiles)
        {
            if(profile.getName().toLowerCase().equals(name)) return profile;
        }
        return null;
    }
}
