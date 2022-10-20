package alopez188.finalproject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Class deals with weather data organizing
 */
public class WeatherData {
    private String access_key;
    private double temperature;
    private double maxTemperature;
    private double minTemperature;
    private int sunriseTime;
    private int sunsetTime;
    private String weatherDescription;
    private String iconName;
    private double windSpeed;
    private int windDirection;
    private int humidity;
    private String wind;

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
        return capitalizeWords(weatherDescription);
    }

    /**
     * Setter, for description
     * @param weatherDescription String
     */
    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    /**
     * function that capitalizes the first character for each word in a string
     * @param str String
     * @return string with each word's first character capitalized
     */
    public String capitalizeWords(String str) {
        String words[] = str.split("\\s");
        String capitalizedWords="";
        for (String w:words) {
            String first = w.substring(0,1);
            String afterFirst = w.substring(1);
            capitalizedWords+=first.toUpperCase()+afterFirst+" ";
        }
        return capitalizedWords.trim();
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

    /**
     * Setter, for wind speed
     * @param tmpWindSpeed double
     */
    public void setWindSpeed(double tmpWindSpeed) { this.windSpeed = tmpWindSpeed; };

    /**
     * Getter, for wind speed
     * @return string of formatted wind speed value
     */
    public String getWindSpeed() { return String.valueOf(windSpeed); }

    /**
     * Setter, for wind direction
     * @param tmpWindDirection int
     */
    public void setWindDirection(int tmpWindDirection) { this.windDirection = tmpWindDirection; };

    /**
     * Getter, for wind direction
     * @return formatted wind direction using compass cardinal points, divided in 16 parts
     */
    public String getWindDirection() { return degreeToCompass(windDirection); }

    /**
     * function to convert degrees to compass cardinal points in 16 parts
     * @param degree int
     * @return compass cardinal point corresponding to the degree inputted
     */
    public String degreeToCompass(int degree) {
        String direction = null;
        if ((degree >= 345 && degree <= 360) || (degree >= 0 && degree <= 15)) { direction = "N"; }
        else if (degree > 15 && degree < 30) { direction = "NNE"; }
        else if (degree >= 30 && degree <= 60) { direction = "NE"; }
        else if (degree > 60 && degree < 75) { direction = "ENE"; }
        else if (degree >= 75 && degree <= 105) { direction = "E"; }
        else if (degree > 105 && degree < 120) { direction = "ESE"; }
        else if (degree >= 120 && degree <= 150) { direction = "SE"; }
        else if (degree > 150 && degree < 165) { direction = "SSE"; }
        else if (degree >= 165 && degree <= 195) { direction = "S"; }
        else if (degree > 195 && degree < 210) { direction = "SSW"; }
        else if (degree >= 210 && degree <= 240) { direction = "SW"; }
        else if (degree > 240 && degree < 255) { direction = "WSW"; }
        else if (degree >= 255 && degree <= 285) { direction = "W"; }
        else if (degree > 285 && degree < 300) { direction = "WNW"; }
        else if (degree >= 300 && degree < 330) { direction = "NW"; }
        else if (degree >= 330 && degree <= 345) { direction = "NNW"; }
        return direction;
    }

    /**
     * Getter, for the complete wind string
     * @return formatted wind text with wind speed and direction
     */
    public String getWind() { return getWindSpeed() + "mph " + getWindDirection(); }

    /**
     * Setter, for humidity
     * @param tmpHumidity int
     */
    public void setHumidity(int tmpHumidity) { this.humidity = tmpHumidity; }

    /**
     * Getter, for humidity
     * @return
     */
    public String getHumidity() { return String.valueOf(this.humidity) + "%"; }
}
