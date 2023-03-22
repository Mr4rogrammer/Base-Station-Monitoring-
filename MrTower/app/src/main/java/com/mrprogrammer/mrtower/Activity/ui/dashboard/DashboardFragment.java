package com.mrprogrammer.mrtower.Activity.ui.dashboard;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mrprogrammer.mrtower.Interface.CompleteHandler;
import com.mrprogrammer.mrtower.R;
import com.mrprogrammer.mrtower.Realm.SaveLocation;
import com.mrprogrammer.mrtower.Realm.Tower;
import com.mrprogrammer.mrtower.Retrofit.ApiCall;
import com.mrprogrammer.mrtower.Utils.CustomInfoWindow;
import com.mrprogrammer.mrtower.Utils.LocalSharedPreferences;
import com.mrprogrammer.mrtower.Utils.RealmManager;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class DashboardFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, RoutingListener {
    private static View view;
    SupportMapFragment supportMapFragment;
    private GoogleMap googleMapView = null;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    Location location = null;
    List<Marker> markers = new ArrayList<>();
    private List<Polyline> polylines = null;
    protected LatLng start = null;
    protected LatLng end = null;
    private ImageView reload;
    private TextView kms;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
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
            supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
            assert supportMapFragment != null;
            supportMapFragment.getMapAsync(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        reload = view.findViewById(R.id.reload);
        kms = view.findViewById(R.id.kms);
        reload.setOnClickListener(view -> {
            ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(reload, "rotation", 0f, 360f);
            rotateAnimator.setDuration(20000);
            rotateAnimator.start();
                    getLastLocation();
                    addLocations();
                }
        );
        enableWorkManager();
        return view;
    }

    private void addLocations() {
        Realm realm = RealmManager.getInstance();
        RealmResults<Tower> data = realm.where(Tower.class).findAll();
        updateLocationMarkerForTower(data);
        data.addChangeListener(new RealmChangeListener<RealmResults<Tower>>() {
            @Override
            public void onChange(RealmResults<Tower> towers) {
                updateLocationMarkerForTower(towers);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMapView = googleMap;
        googleMapView.setInfoWindowAdapter(new CustomInfoWindow(requireActivity()));
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
        getLastLocation();
        addLocations();
    }

    private void getLastLocation() {
        if (isLocationEnabled()) {
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions();
            }
            mFusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                Location location = task.getResult();
                if (location == null) {
                    requestNewLocationData();
                } else {
                    try {
                        requestNewLocationData();
                    } catch (Exception e) {
                        locationData(location);
                    }
                }
            });
        } else {
            Toast.makeText(requireActivity(), "Please turn on your location...", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                               @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            locationData(mLastLocation);
        }
    };

    private void updateLocationMarkerForTower(RealmResults<Tower> data) {
        googleMapView.clear();
        markers.clear();
        for (int i = 0; i < data.size(); i++) {
            String Message = "Radio :" + data.get(i).getRadio() + "\nArea Code : " + data.get(i).getArea() + "\nMCC : " + data.get(i).getMcc() + "\nNET : " + data.get(i).getNet();
            LatLng location = new LatLng(data.get(i).getLat(), data.get(i).getLon());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(location)
                    .snippet(Message)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.antenna))
                    .title(data.get(i).getCell().toString());
            markers.add(googleMapView.addMarker(markerOptions));
        }
        if (location != null) {
            updateLocationForUser();
            shortestDistanceMarker();
        }
    }

    private void updateLocationForUser() {
        LatLng locations = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions()
                .position(locations)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.walking))
                .title(LocalSharedPreferences.Companion.getLocalSavedUser(requireActivity()).get(0));
        googleMapView.animateCamera(CameraUpdateFactory.newLatLngZoom(locations, 16));
        googleMapView.addMarker(markerOptions).showInfoWindow();
        shortestDistanceMarker();
    }

    private void locationData(Location locations) {
        location = locations;
        updateLocationForUser();
        ApiCall.Companion.getValue(locations.getLatitude(), locations.getLongitude(), new CompleteHandler() {
            @Override
            public void onSuccess(String Message) {
                SaveLocation.Companion.saveLocationData(Message);
            }

            @Override
            public void onFailure(String e) {
                System.out.println(e);
            }
        });
    }

    private void shortestDistanceMarker() {
        if (location == null) return;
        double shortestDistance = Double.MAX_VALUE;
        Marker nearestMarker = null;
        LatLng startingPoint = new LatLng(location.getLatitude(), location.getLongitude());
        for (Marker marker : markers) {
            double distance = distance(startingPoint, marker.getPosition());
            if (distance < shortestDistance) {
                shortestDistance = distance;
                nearestMarker = marker;
            }
        }
        if (nearestMarker != null) {
            nearestMarker.showInfoWindow();
            LatLng shortestTower = new LatLng(nearestMarker.getPosition().latitude, nearestMarker.getPosition().longitude);


            float[] results = new float[1];
            Location.distanceBetween(startingPoint.latitude, startingPoint.longitude, shortestTower.latitude, shortestTower.longitude, results);

            float distanceInMeters = results[0];
            float distanceInKm = distanceInMeters / 1000;


            kms.setText(distanceInKm + " Total distance");

            PolylineOptions polylineOptions = new PolylineOptions()
                    .add(startingPoint)
                    .add(shortestTower);
            polylineOptions.color(Color.RED);
            polylineOptions.width(5);
            googleMapView.addPolyline(polylineOptions);
            //Findroutes(startingPlace, shortestTower);
        }
    }

    private static double distance(LatLng start, LatLng end) {
        double earthRadius = 6371; // in kilometers
        double lat1 = start.latitude;
        double lat2 = end.latitude;
        double lon1 = start.longitude;
        double lon2 = end.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c; // in kilometers
        return distance;
    }

    public void Findroutes(LatLng Start, LatLng End) {

        if (Start == null || End == null) {
            Toast.makeText(requireActivity(), "Unable to get location", Toast.LENGTH_LONG).show();
        } else {
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key("AIzaSyDvE3vZxumx0ywwAittiSrzdcYon-5laus")  //also define your api key here.
                    .build();
            routing.execute();
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        try {
            Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void onRoutingStart() {
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if (polylines != null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng = null;
        LatLng polylineEndLatLng = null;
        polylines = new ArrayList<>();
        for (int i = 0; i < route.size(); i++) {
            if (i == shortestRouteIndex) {
                polyOptions.color(getResources().getColor(R.color.black));
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = googleMapView.addPolyline(polyOptions);
                polylineStartLatLng = polyline.getPoints().get(0);
                int k = polyline.getPoints().size();
                polylineEndLatLng = polyline.getPoints().get(k - 1);
                polylines.add(polyline);
            }
        }
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("My Location");
        googleMapView.addMarker(startMarker);
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Destination");
        googleMapView.addMarker(endMarker);
    }

    @Override
    public void onRoutingCancelled() {
        //Findroutes(start, end);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Findroutes(start, end);
    }

    public void enableWorkManager() {
    }

}