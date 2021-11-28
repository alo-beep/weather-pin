package alopez188.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.OnLocationCameraTransitionListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.List;

/**
 * MainActivity class, deals with Map navigation and pinning locations
 * @author Angel Lopez
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity implements
        PermissionsListener {

    private PermissionsManager permissionsManager;
    private MapView mapView;
    private Marker marker;
    private Button btn_showWeather;
    private MapboxMap mapboxMap;
    private Style styleMapBox;
    private Location lastLocation;
    public static PinLocation pinLocation;
    public static PinLocation currentLocation;

    // Run once activity has been created
    // , serves as main functionality for MapBox
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // gathers access token from META_DATA for using MapBox API
        @Nullable ApplicationInfo appInfo = null;
        try {
            appInfo = MainActivity.this.getPackageManager()
                    .getApplicationInfo(MainActivity.this.getPackageName()
                            , PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        @Nullable String mapboxAPIKey = null;
        if (appInfo != null) {
            mapboxAPIKey = appInfo.metaData.getString("MapBox_KEY");
        }

        // Start MapView from MapBoxSDK if an API key was gathered
        if (mapboxAPIKey != null) {
            Mapbox.getInstance(this, mapboxAPIKey);
            setContentView(R.layout.activity_main);

            btn_showWeather = findViewById(R.id.btn_showWeather);
            pinLocation = new PinLocation();

            // assigns MapView object
            mapView = (MapView) findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(new OnMapReadyCallback() {
                // once MapView object is ready, begin loading
                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap) {

                    MainActivity.this.mapboxMap = mapboxMap;

                    mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {

                            styleMapBox = style;

                            // ask for permission to view user's current location
                            enableLocationComponent(styleMapBox);

                            // Map is set up and the style has loaded, now
                            // set starting camera centered in the Atlantic Ocean
                            CameraPosition position = new CameraPosition.Builder()
                                    .target(new LatLng(31.5, -40.5))
                                    .zoom(1)
                                    .tilt(20)
                                    .build();
                            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 10);

                            // gather longitude and latitude from point click
                            mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                                @Override
                                public boolean onMapClick(@NonNull LatLng point) {
                                    mapboxMap.clear();  // to limit markers (pins) to one
                                    marker = mapboxMap.addMarker(new MarkerOptions().position(point));  // add marker (pin)

                                    // set lat and long into pinLocation object
                                    pinLocation.setLatitude(point.getLatitude());
                                    pinLocation.setLongitude(point.getLongitude());

                                    // enable button once marker set
                                    btn_showWeather.setEnabled(true);
                                    btn_showWeather.setText(R.string.weather_button_location_set);
                                    return true;
                                }
                            });

                        }
                    });
                }
            });
        }
    }

    // necessary for mapView functioning
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    // necessary for mapView functioning
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    // necessary for mapView functioning
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    // necessary for mapView functioning
    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    // necessary for mapView functioning
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    // necessary for mapView functioning
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    // necessary for mapView functioning
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build()
            );

            // enable to make component visible
            locationComponent.setLocationComponentEnabled(true);
            
            // set the component's camera mode
            // , and zoom into the user's location
            OnLocationCameraTransitionListener tmp = null;
            locationComponent.setCameraMode(CameraMode.TRACKING, 
                    1000, 6.0, 0.0, 0.0, tmp);
            
            // zoom closer towards the user's current location
            locationComponent.zoomWhileTracking(8, 1000);
            
            // set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

            lastLocation = locationComponent.getLastKnownLocation();

        } else {

            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    /**
     * Button click function trigger for main screen button, opens WeatherActivity
     * @param view View
     */
    public void openWeatherScreen(View view) {
        if (btn_showWeather.isEnabled()) {
            startActivity(new Intent(MainActivity.this, WeatherActivity.class));
        }
    }

    /**
     * Button click function which centers camera onto user's current location
     * , and set weather button for current location
     * @param view View
     */
    public void showCurrentLocation(View view) {
        // ask for permission for current location and find current location
        enableLocationComponent(styleMapBox);

        // store coordinates for current location
        pinLocation.setLatitude(lastLocation.getLatitude());
        pinLocation.setLongitude(lastLocation.getLongitude());

        // update button text for current location and enable
        btn_showWeather.setEnabled(true);
        btn_showWeather.setText(R.string.weather_button_current_location_set);

        // remove current pins
        mapboxMap.clear();
    }


}