package org.dev4u.hv.my_diagnostic.Fragments;
import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.dev4u.hv.my_diagnostic.CustomInfoWindowAdapter;
import org.dev4u.hv.my_diagnostic.MyPlacesUI.GooglePlacesReadTask;
import org.dev4u.hv.my_diagnostic.MyPlacesUI.NearbyPlaces;
import org.dev4u.hv.my_diagnostic.MyPlacesUI.Place;
import org.dev4u.hv.my_diagnostic.MyPlacesUI.PlacesException;
import org.dev4u.hv.my_diagnostic.MyPlacesUI.PlacesListener;
import org.dev4u.hv.my_diagnostic.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.internal.zzagz.runOnUiThread;


public class MapFragment extends BaseFragment implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,
        PlacesListener, GoogleMap.OnMarkerClickListener,GoogleMap.OnInfoWindowClickListener,Thread.UncaughtExceptionHandler


{

    View rootView;
    private GoogleMap googleMap;
    private String SgoogleMap;
    private static final String GOOGLE_API_KEY = "AIzaSyCPNL19Statnfac_LR6sE9Hf42b77Uk_c0";
    private LocationRequest mLocationRequest;
    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private static final String TAG = android.support.v4.app.Fragment.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    // The entry point to Google Play services, used by the MyPlacesJson API and Fused Location Provider.
    private GoogleApiClient mGoogleApiClient;

    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private static Marker myMarker;
    private static ArrayList<Marker> markerArrayList = new ArrayList<>();
    private static int radius = 2500;//2500 meters - 2.5km

    private SharedPreferences savedData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();




        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        SgoogleMap="other_map";
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval( 1000)
                .setFastestInterval( 1000);

    }


    private boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            //we are connected to a network
            return true;
        } else
            return false;

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    getDeviceLocation();
                    updateLocationUI();
                }
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_maps, container, false);
        setHasOptionsMenu(true);


        savedData       = getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);

        radius          = (int) (savedData.getFloat("DISTANCE",3)*1000);

        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if(!isNetworkAvailable(getContext()))
            Snackbar.make(rootView, R.string.not_network, Snackbar.LENGTH_LONG)
                    .show();

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        MapsInitializer.initialize(this.getContext());
        mMapView.getMapAsync(this);
        return rootView;
    }
    @Override
    public void onPlacesFailure(PlacesException e) {
    }

    @Override
    public void onPlacesStart() {

    }
    @Override
    public void onResume() {
        super.onResume();
        radius          = (int) (savedData.getFloat("DISTANCE",3)*1000);
        myMarker=null;
        mGoogleApiClient.connect();
        mMapView.onResume();
        setUpMap();

    }
    @Override
    public void onPause() {
        super.onPause();
        myMarker=null;
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        mMapView.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION))
            return;

        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null)
        {    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Snackbar.make(rootView, getString(R.string.gps_disable), Snackbar.LENGTH_LONG)
                .show();}
        else
            handleNewLocation(location);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    private void setUpMap() {
        if (mGoogleMap == null)
            mMapView.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mGoogleMap = googleMap;
                }
            });
    }
    private void getDeviceLocation() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);

        }

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
        } else {
            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }

        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }
    }

    private void handleNewLocation(Location location) {
        mMap = googleMap;
        getDeviceLocation();
        updateLocationUI();
        // Add a marker in Sydney and move the camera
        LatLng MyLatLeng = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.clear();

        if(myMarker==null){
            //TODO icono personalizado
            Drawable circleDrawable = getResources().getDrawable(R.drawable.circle_shape);
            BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);

            myMarker = mMap.addMarker(new MarkerOptions()
                    .position(MyLatLeng).title(getString(R.string.i_am_here))
                    .icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));

        }else{
            myMarker.setPosition(MyLatLeng);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MyLatLeng, 15));
        new NearbyPlaces.Builder()
                .listener(this)
                .key("KEY")
                .latlng(location.getLatitude(),location.getLongitude())
                .radius(radius)
                .keyword("clinic")
                .build()
                .execute();
    }
    @Override
    public void onPlacesSuccess(final List<Place> places) {
        Log.i("PlacesAPI", "onPlacesSuccess()");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                markerArrayList.clear();
                for (Place place : places) {
                    LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
                    markerArrayList.add(mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(place.getName())
                            .snippet(place.getVicinity()+"<"+place.getPlaceId())

                    ));
                }
            }
        });
    }
    private boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
    @Override
    public void onPlacesFinished() {

    }
    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(LayoutInflater.from(getActivity())));

        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        try {
            String[] split = marker.getSnippet().split("<");
            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
            googlePlacesUrl.append("placeid=" + split[1]);
            googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);
            GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
            googlePlacesReadTask.setMyothercontext(getContext());
            Object[] toPass = new Object[2];
            toPass[0] = SgoogleMap;
            toPass[1] = googlePlacesUrl.toString();
            googlePlacesReadTask.execute(toPass);
        }
        catch (Exception e)
        {
        }
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        Toast.makeText(getContext(),"Ocurrio un error",Toast.LENGTH_SHORT);
    }
}