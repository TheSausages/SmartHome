package pwr.smart.home.data.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "home")
public class Home {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private String hours;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Home)) return false;
        Home home = (Home) o;
        return Objects.equals(id, home.getId());
    }

    public Set<Integer> getHours() {
        List<String> stringList = Arrays.asList(hours.split(";"));
        if(stringList.size() == 1 && stringList.get(0).isEmpty())
            return new HashSet<>();
        return stringList.stream().map(Integer::parseInt).collect(Collectors.toSet());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
