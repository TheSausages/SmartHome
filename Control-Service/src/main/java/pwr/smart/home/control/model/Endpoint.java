package pwr.smart.home.control.model;

public enum Endpoint {
    AIR_HUMIDIFIER_URL("http://localhost:8082/setTarget"),
    AIR_CONDITIONER_URL("http://localhost:8083/setTarget"),
    AIR_FILTER_URL("http://localhost:8084/setTarget");

    public final String url;

    Endpoint(String url) {
        this.url = url;
    }
}
