package me.digi.examples.ca;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danni on 07/10/2017.
 */

public class ProfileStorage {
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
}
