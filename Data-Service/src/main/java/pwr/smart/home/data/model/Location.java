package pwr.smart.home.data.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Location {
    float latitude;
    float longitude;

    public Location(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
