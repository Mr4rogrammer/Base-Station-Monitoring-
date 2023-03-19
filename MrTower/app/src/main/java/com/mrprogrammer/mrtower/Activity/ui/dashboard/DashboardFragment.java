package com.mrprogrammer.mrtower.Activity.ui.dashboard;

import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mrprogrammer.mrtower.Interface.CompleteHandler;
import com.mrprogrammer.mrtower.R;
import com.mrprogrammer.mrtower.Realm.SaveLocation;
import com.mrprogrammer.mrtower.Realm.Tower;
import com.mrprogrammer.mrtower.Retrofit.ApiCall;
import com.mrprogrammer.mrtower.Utils.RealmManager;
import com.mrprogrammer.shop.MrToast.MrToast;

import java.util.List;

import io.realm.Realm;

public class DashboardFragment extends Fragment implements OnMapReadyCallback{
    private static View view;

    SupportMapFragment supportMapFragment;
    private GoogleMap googleMapView = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }
        try {
            supportMapFragment=(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
            assert supportMapFragment != null;
            supportMapFragment.getMapAsync(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


    private void addLocations(){
        Realm realm = RealmManager.getInstance();

       List<Tower> data =  realm.where(Tower.class).findAll();

        for(int i= 0 ;i< data.size();i++){
            LatLng location = new LatLng(data.get(i).getLat(), data.get(i).getLon());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(location)
                    .title(data.get(i).getRadio());
            googleMapView.addMarker(markerOptions);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMapView = googleMap;
        addLocations();
    }
}