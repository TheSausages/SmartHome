package pwr.smart.home.selenium;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonPropertyOrder({"dataServiceStart", "dataServiceEnd", "dataServiceDifference", "renderStart", "renderEnd", "renderDifference"})
@AllArgsConstructor
public class HistoryTestCSVSchema {
    public LocalDate fromDate;
    public Double dataServiceStart;
    public Double dataServiceEnd;
    public Double dataServiceDifference;
    public LocalDateTime renderStart;
    public LocalDateTime renderEnd;
    public Long renderDifference;
}
