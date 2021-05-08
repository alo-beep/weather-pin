package alopez188.finalproject;

import com.loopj.android.http.*;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.entity.mime.Header;

public class WeatherData {
    String access_key = "b99121ecce36058a1c22db935b8f0a72";
    double temp;

    WeatherData() {}

    public void getTemp(double lat, double lon) {
        String url = String.format("https://pi.openweathermap.org/data/2.5/weather?lat=" +
                lat + "&lon=" + lon +"&appid=" + access_key + "&units=imperial");

        AsyncHttpClient client = new AsyncHttpClient();

            client.get(url, new JsonHttpResponseHandler(){

                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        // deals with gathering prices
                        JSONObject json = response.getJSONObject("main");
                        double tmpTemp = json.getDouble("temp");
                        //coin.setCurValue(tmpPrice);
                        System.out.print("temp: " + tmpTemp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
    }
}
