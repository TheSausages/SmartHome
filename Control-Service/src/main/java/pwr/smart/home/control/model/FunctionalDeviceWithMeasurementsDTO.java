package pwr.smart.home.control.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pwr.smart.home.common.model.enums.MeasurementType;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FunctionalDeviceWithMeasurementsDTO {
    private FunctionalDevice device;
    private Map<MeasurementType, List<Measurement>> measurements;
}
