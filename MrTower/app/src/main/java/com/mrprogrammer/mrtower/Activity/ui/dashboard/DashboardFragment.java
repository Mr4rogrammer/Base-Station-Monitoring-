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
import com.mrprogrammer.mrtower.Interface.CompleteHandler;
import com.mrprogrammer.mrtower.R;
import com.mrprogrammer.mrtower.Realm.SaveLocation;
import com.mrprogrammer.mrtower.Retrofit.ApiCall;
import com.mrprogrammer.shop.MrToast.MrToast;

public class DashboardFragment extends Fragment {
    private static View view;
    SupportMapFragment supportMapFragment;

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
        Button m = view.findViewById(R.id.test);

        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiCall.Companion.getValue(10.4807146, 78.7701246, new CompleteHandler() {
                    @Override
                    public void onSuccess(String Message) {
                        System.out.println(Message);
                        SaveLocation.Companion.saveLocationData(Message);
                    }

                    @Override
                    public void onFailure(String e) {
                        MrToast.Companion.error(getActivity(),e);
                    }
                });
            }
        });

        try {
            supportMapFragment=(SupportMapFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.mapView);

            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

}