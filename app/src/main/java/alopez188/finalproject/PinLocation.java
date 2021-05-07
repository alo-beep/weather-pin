package alopez188.finalproject;

public final class PinLocation {
    private double latitude;
    private double longitude;

    PinLocation() {
        latitude = 0.0;
        longitude = 0.0;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
