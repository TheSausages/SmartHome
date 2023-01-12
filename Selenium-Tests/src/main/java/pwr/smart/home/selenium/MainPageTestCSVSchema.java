package pwr.smart.home.selenium;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@JsonPropertyOrder({"fullyLoadTime", "timeToTitle", "timeToFirstByteHomeImage", "timeToFirstByteSite"})
@AllArgsConstructor
public class MainPageTestCSVSchema {
    public Long fullyLoadTime;
    public Long timeToTitle;
    public Double timeToFirstByteHomeImage;
    public Double timeToFirstByteSite;
}
