package alopez188.finalproject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class deals with weather data organizing
 */
public class WeatherData {
    private String access_key;
    private double temperature;
    private int sunriseTime;
    private int sunsetTime;
    private String weatherDescription;
    private String iconName;

    /**
     * Default constructor
     */
    WeatherData() {}

    /**
     * Gathers access key for API (openweathermap)
     * @return access_key String
     */
    public String getAccess_key() {
        return access_key;
    }

    public void setAccess_key(String key) { access_key = key; }

    /**
     * Getter, for temperature
     * @return String with formatted temperature
     */
    public String getTemperature() {
        return String.valueOf(temperature + "°F");
    }

    /**
     * Setter, for temperature
     * @param temperature double
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    /**
     * Getter, for SunriseTime
     * @return String with formatted UNIX time
     */
    public String getSunriseTime() {
        return convertTime(sunriseTime);
    }

    /**
     * Setter, for SunriseTime
     * @param sunriseTime int
     */
    public void setSunriseTime(int sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    /**
     * Getter, for SunsetTime
     * @return String with formatted UNIX time
     */
    public String getSunsetTime() { return convertTime(sunsetTime); }

    /**
     * Setter, for SunsetTime
     * @param sunsetTime int
     */
    public void setSunsetTime(int sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    /**
     * Getter, for description
     * @return String weather description
     */
    public String getWeatherDescription() {
        return weatherDescription;
    }

    /**
     * Setter, for description
     * @param weatherDescription String
     */
    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    /**
     * Function converts UNIX time to a SimpleDateFormat
     * @param UNIXtime int
     * @return String with format = ex: Jul 21 12:01AM
     */
    public String convertTime(int UNIXtime) {
        Date date = new java.util.Date(Long.valueOf(UNIXtime) * 1000L);     // takes ms as input
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd hh:mma");   // format date
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    /**
     * Setter, for iconName
     * @param tmpIcon String
     */
    public void setIconLink(String tmpIcon) { this.iconName = tmpIcon; }

    /**
     * Getter, for iconName
     * @return String url which links to API's images
     */
    public String getIconLink() {
        String url = "https://openweathermap.org/img/wn/" + this.iconName + "@2x" + ".png";
        return url;
    }
}
