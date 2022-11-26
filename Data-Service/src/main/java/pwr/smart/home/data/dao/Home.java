package pwr.smart.home.data.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "home")
public class Home {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Home)) return false;
        Home home = (Home) o;
        return Objects.equals(id, home.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
