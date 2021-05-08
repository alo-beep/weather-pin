package alopez188.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class WeatherActivity extends AppCompatActivity {

    Button btn_returnToLocation;
    String access_key = "b99121ecce36058a1c22db935b8f0a72";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn_returnToLocation = findViewById(R.id.btn_returnToLocation);

        setContentView(R.layout.activity_weather);

        getTemp(19.00, -119.00);
    }

    private void getTemp(double lat, double lon) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            // API used for gathering crypto currency prices
            //String url = "http://api.openweathermap.org/data/2.5/weather?lat=16.00&lon=-119.00&appid=b99121ecce36058a1c22db935b8f0a72&units=imperial";
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
                            // gather sunrise/sunset time
                            json = response.getJSONObject("sys");
                            int tmpSunrise = json.getInt("sunrise");
                            int tmpSunset = json.getInt("sunset");
                            Log.d("desc: ", tmpDescription);
                            Log.d("sunset: ", String.valueOf(tmpSunrise));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, 500);
    }

    public void openLocationScreen(View view) {
        startActivity(new Intent(WeatherActivity.this, MainActivity.class));

    }
}