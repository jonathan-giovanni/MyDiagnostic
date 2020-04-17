package org.dev4u.hv.my_diagnostic.MyPlacesUI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public interface PlacesListener {
    View onCreateView(LayoutInflater inflater, ViewGroup container,
                      Bundle savedInstanceState);

    void onPlacesFailure(PlacesException e);

    void onPlacesStart();

    void onPlacesSuccess(List<Place> places);

    void onPlacesFinished();
}