package alopez188.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

/**
 * MainActivity class, deals with Map navigation and pinning locations
 * @author Angel Lopez
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private Marker marker;
    private Button btn_showWeather;
    private MapboxMap mapboxMap;
    public static PinLocation pinLocation;

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

                    mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {

                            // Map is set up and the style has loaded. Now you can add data or make other map adjustments

                            // set starting camera centered in the United States
                            CameraPosition position = new CameraPosition.Builder()
                                    .target(new LatLng(31.5, -98.5))
                                    .zoom(3)
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
                                    btn_showWeather.setText("Show Weather at Pin");
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
    protected void onSaveInstanceState(Bundle outState) {
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

    /**
     * Button click function trigger for main screen button, opens WeatherActivity
     * @param view View
     */
    public void openWeatherScreen(View view) {
        if (btn_showWeather.isEnabled() == true) {
            startActivity(new Intent(MainActivity.this, WeatherActivity.class));
        }
    }
}