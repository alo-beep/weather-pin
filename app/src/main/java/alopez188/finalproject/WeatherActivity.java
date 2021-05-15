package alopez188.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * WeatherActivity is main weather information screen for a given pin location set
 * @author Angel Lopez
 * @version 1.0
 */
public class WeatherActivity extends AppCompatActivity {

    private Button btn_returnToLocation;
    private String access_key = "b99121ecce36058a1c22db935b8f0a72";
    public WeatherData weatherData;
    private TextView sunsetTextView;
    private TextView sunriseTextView;
    private TextView temperatureTextView;
    private TextView weatherDescriptionTextView;
    private ImageView iconImageView;

    // Run once activity has been created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn_returnToLocation = findViewById(R.id.btn_returnToLocation);
        setContentView(R.layout.activity_weather);
        weatherData = new WeatherData();

        // gather API values from OpenWeatherMap and assign to WeatherData object
        getValuesFromLocation(weatherData, MainActivity.pinLocation.getLatitude(), MainActivity.pinLocation.getLongitude());
    }

    /**
     * Function deals API calls and assignment of values into WeatherData object
     * @param weatherData WeatherData
     * @param lat double
     * @param lon double
     */
    private void getValuesFromLocation(WeatherData weatherData, double lat, double lon) {
        Handler handler = new Handler();

        // conduct API connection
        handler.postDelayed(new Runnable() {
            // API used for gathering weather data
            String url = String.format("http://api.openweathermap.org/data/2.5/weather?lat="
                    + String.valueOf(lat) + "&lon=" + String.valueOf(lon) +"&appid=" + access_key + "&units=imperial");

            AsyncHttpClient client = new AsyncHttpClient();
            // Run upon a connection established
            @Override
            public void run() {
                client.get(url, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            // gather temperature
                            JSONObject json = response.getJSONObject("main");
                            double tmpTemp = json.getDouble("temp");
                            // gather weather description and icon name
                            JSONArray jsonArray = response.getJSONArray("weather");
                            json = jsonArray.getJSONObject(0);
                            String tmpDescription = json.getString("description");
                            String tmpIcon = json.getString("icon");
                            // gather sunrise/sunset time
                            json = response.getJSONObject("sys");
                            int tmpSunrise = json.getInt("sunrise");
                            int tmpSunset = json.getInt("sunset");

                            // place values within WeatherData object for activity
                            weatherData.setSunriseTime(tmpSunrise);
                            weatherData.setSunsetTime(tmpSunset);
                            weatherData.setTemperature(tmpTemp);
                            weatherData.setWeatherDescription(tmpDescription);
                            weatherData.setIconLink(tmpIcon);

                            // execute function to insert values into this activity
                            insertValuesToActivity(weatherData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, 500);
    }

    /**
     * Inserts values into this activity's elements
     * @param weatherData WeatherData object
     */
    public void insertValuesToActivity(WeatherData weatherData) {
        // set all elements to their respective ID's
        sunsetTextView = findViewById(R.id.txt_sunset);
        sunriseTextView = findViewById(R.id.txt_sunrise);
        temperatureTextView = findViewById(R.id.txt_temperature);
        weatherDescriptionTextView = findViewById(R.id.txt_description);
        iconImageView = findViewById(R.id.img_icon);
        // set, and download from API, icon ImageView
        new DownloadImageTask((ImageView)findViewById(R.id.img_icon))
                .execute(weatherData.getIconLink());

        // set text for each element
        sunriseTextView.setText(weatherData.getSunriseTime());
        sunsetTextView.setText(weatherData.getSunsetTime());
        weatherDescriptionTextView.setText(weatherData.getWeatherDescription());
        temperatureTextView.setText(weatherData.getTemperature());
    }

    /**
     * Function will download an image based on the URL inputted
     *  , utilized for the icon download
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    /**
     * button click function, triggers to send user back to main map screen
     * @param view View
     */
    public void openLocationScreen(View view) {
        startActivity(new Intent(WeatherActivity.this, MainActivity.class));
    }
}