package pwr.smart.home.selenium;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;

@JsonPropertyOrder({"dataServiceStart", "dataServiceEnd", "dataServiceDifference", "renderStart", "renderEnd", "renderDifference"})
public class MainPageTestCSVSchema {
    public LocalDateTime fromDate;
    public Double dataServiceStart;
    public Double dataServiceEnd;
    public Double dataServiceDifference;
    public LocalDateTime renderStart;
    public LocalDateTime renderEnd;
    public Double renderDifference;
}
