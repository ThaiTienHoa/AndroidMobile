package com.example.loginandregister.acitivites.home;

import androidx.lifecycle.ViewModel;

import com.example.loginandregister.model.Location;

public class HomeActivityViewModel extends ViewModel {

    private Location sourceLocation;
    private Location destinationLocation;

    public Location getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(Location sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public Location getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(Location destinationLocation) {
        this.destinationLocation = destinationLocation;
    }
}
