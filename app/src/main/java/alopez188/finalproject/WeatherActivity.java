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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class WeatherActivity extends AppCompatActivity {

    private Button btn_returnToLocation;
    private String access_key = "b99121ecce36058a1c22db935b8f0a72";
    public WeatherData weatherData;
    private TextView sunsetTextView;
    private TextView sunriseTextView;
    private TextView temperatureView;
    private TextView descriptionView;
    private ImageView iconImageView;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn_returnToLocation = findViewById(R.id.btn_returnToLocation);
        setContentView(R.layout.activity_weather);
        weatherData = new WeatherData();

        getValuesFromLocation(weatherData, MainActivity.pinLocation.getLatitude(), MainActivity.pinLocation.getLongitude());

    }

    private void getValuesFromLocation(WeatherData weatherData, double lat, double lon) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            // API used for gathering crypto currency prices
            String url = String.format("http://api.openweathermap.org/data/2.5/weather?lat="
                    + String.valueOf(lat) + "&lon=" + String.valueOf(lon) +"&appid=" + access_key + "&units=imperial");

            AsyncHttpClient client = new AsyncHttpClient();
            @Override
            public void run() {
                client.get(url, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            // gather temperature
                            JSONObject json = response.getJSONObject("main");
                            double tmpTemp = json.getDouble("temp");
                            Log.d("temp: ", String.valueOf(tmpTemp));
                            // gather weather description
                            JSONArray jsonArray = response.getJSONArray("weather");
                            json = jsonArray.getJSONObject(0);
                            String tmpDescription = json.getString("description");
                            String tmpIcon = json.getString("icon");
                            // gather sunrise/sunset time
                            json = response.getJSONObject("sys");
                            int tmpSunrise = json.getInt("sunrise");
                            int tmpSunset = json.getInt("sunset");
                            //Log.d("desc: ", tmpDescription);
                            Log.d("sunset: ", String.valueOf(tmpSunrise));

                            // place values within WeatherData object for activity
                            weatherData.setSunriseTime(tmpSunrise);
                            weatherData.setSunsetTime(tmpSunset);
                            weatherData.setTemperature(tmpTemp);
                            weatherData.setDescription(tmpDescription);
                            weatherData.setIconLink(tmpIcon);

                            Log.d("temp1.5: ", String.valueOf(weatherData.getTemperature()));
                            insertValuesToActivity(weatherData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, 500);
    }

    public void insertValuesToActivity(WeatherData weatherData) {
        sunsetTextView = findViewById(R.id.txt_sunset);
        sunriseTextView = findViewById(R.id.txt_sunrise);
        temperatureView = findViewById(R.id.txt_temperature);
        descriptionView = findViewById(R.id.txt_description);
        iconImageView = findViewById(R.id.img_icon);
        new DownloadImageTask((ImageView)findViewById(R.id.img_icon))
                .execute(weatherData.getIconLink());

        sunriseTextView.setText(weatherData.getSunriseTime());
        sunsetTextView.setText(weatherData.getSunsetTime());
        descriptionView.setText(weatherData.getDescription());
        temperatureView.setText(weatherData.getTemperature());

        sunriseTextView.getPaint().setUnderlineText(false);
        sunsetTextView.getPaint().setUnderlineText(false);
        descriptionView.getPaint().setUnderlineText(false);
        temperatureView.getPaint().setUnderlineText(false);

    }

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
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    public void openLocationScreen(View view) {
        startActivity(new Intent(WeatherActivity.this, MainActivity.class));

    }
}