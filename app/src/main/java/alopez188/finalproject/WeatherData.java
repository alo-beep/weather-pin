package alopez188.finalproject;

import com.loopj.android.http.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.entity.mime.Header;

public class WeatherData {
    String access_key = "b99121ecce36058a1c22db935b8f0a72";
    double temperature;
    int sunriseTime;
    int sunsetTime;
    String description;
    String icon;

    WeatherData() {}

    public String getAccess_key() {
        return access_key;
    }

    public void setAccess_key(String access_key) {
        this.access_key = access_key;
    }

    public String getTemperature() {
        return String.valueOf(temperature + "°F");
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getSunriseTime() {
        return convertTime(sunriseTime);
    }

    public void setSunriseTime(int sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public String getSunsetTime() {

        return convertTime(sunsetTime);
    }

    public void setSunsetTime(int sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String convertTime(int UNIXtime) {
        Date date = new java.util.Date(Long.valueOf(UNIXtime) * 1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd HH:mm");
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public void setIconLink(String tmpIcon) { this.icon = tmpIcon; }

    public String getIconLink() {
        String url = "https://openweathermap.org/img/wn/" + this.icon + "@2x" + ".png";
        return url;
    }
}
