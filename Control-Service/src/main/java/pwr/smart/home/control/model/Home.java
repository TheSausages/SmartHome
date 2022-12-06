package pwr.smart.home.control.model;

import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Home {
    private Long id;
    private String name;
    private String street;
    private String city;
    private String postCode;
    private String country;
    private float longitude;
    private float latitude;
    private int preferredTemp;
    private int preferredHum;
    private Set<Integer> hours;
}
