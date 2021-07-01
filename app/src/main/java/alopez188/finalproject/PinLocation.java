package alopez188.finalproject;

/**
 * Simple PinLocation class, holds latitude and longitude values
 * @author Angel Lopez
 * @version 1.0
 */
public final class PinLocation {
    private double latitude;
    private double longitude;

    /**
     * Default constructor
     */
    PinLocation() {
        latitude = 0.0;
        longitude = 0.0;
    }

    /**
     * Getter, for latitude
     * @return latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Setter, for latitude
     * @param latitude double
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Getter, for longitude
     * @return longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Setter, for longitude
     * @param longitude double
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
