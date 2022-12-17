package pwr.smart.home.control.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import pwr.smart.home.common.model.enums.DeviceType;
import pwr.smart.home.control.model.Endpoint;
import pwr.smart.home.control.model.FunctionalDevice;
import pwr.smart.home.control.model.FunctionalDeviceWithMeasurementsDTO;
import pwr.smart.home.control.model.Home;
import pwr.smart.home.common.weather.model.response.AirQualityResponse;
import pwr.smart.home.common.weather.model.response.ForecastWeatherResponse;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class ActionsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActionsService.class);

    @Autowired
    private DataEmitter dataEmitter;

    @Autowired
    private DataService dataService;

    @Autowired
    private Endpoint endpoint;

    @Autowired
    private OpenMeteoAsyncMethods openMeteoAsyncMethods;

    @Autowired
    private FunctionalDevicesAsyncMethods functionalDevicesAsyncMethods;

    public String doActionsForDeviceWithType(String target, DeviceType deviceType, Jwt jwt) throws ExecutionException, InterruptedException {
        Home home = dataService.getHome(UUID.fromString(jwt.getSubject()));

        Future<List<FunctionalDeviceWithMeasurementsDTO>> devices = functionalDevicesAsyncMethods.getFunctionalDevicesWithMeasurementsForHome(home);
        Future<ForecastWeatherResponse> weather = openMeteoAsyncMethods.getWeatherForecast(home);
        Future<AirQualityResponse> air = openMeteoAsyncMethods.getAirData(home);

        // For now we only get the selected machine
        // In a more complicated model, more than one device may be changed in a single action
        List<FunctionalDeviceWithMeasurementsDTO> device = devices.get().stream()
                .filter(dev -> deviceType.equals(dev.getDevice().getType()))
                .collect(Collectors.toList());

        for (FunctionalDeviceWithMeasurementsDTO devi : device) {
            LOGGER.info("Try to set target {} for device with serial number {}", target, devi.getDevice().getSerialNumber());

            if (devi.getDevice().isConnected()) {
                switch (devi.getDevice().getType()) {
                    case AIR_FILTER:
                        return functionalDevicesAsyncMethods.handleFilter(devi, target, home, air.get()).get();
                    case AIR_HUMIDIFIER:
                        return functionalDevicesAsyncMethods.handleHumidity(devi, target, home, weather.get()).get();
                    case AIR_CONDITIONER:
                        return functionalDevicesAsyncMethods.handleTemperature(devi, target, home, weather.get()).get();
                    default:
                        LOGGER.info("Device of unknown type found: {}", devi.getDevice().getType());
                        return "";
                }
            } else {
                LOGGER.info("Device {} disconnected - passing", devi.getDevice().getSerialNumber());
            }
        }

        LOGGER.info("Null Device");
        return "";
    }

    public boolean tryToActivate(String serialNumber) {
        FunctionalDevice device = dataService.getFunctionalDevice(serialNumber);

        if (Objects.nonNull(device)) {
            LOGGER.info(device.toString());

            String endpointStr = null;
            switch (device.getType()) {
                case AIR_FILTER:
                    endpointStr = endpoint.getAirFilterUrl(serialNumber);
                    break;
                case AIR_HUMIDIFIER:
                    endpointStr = endpoint.getAirHumidifierUrl(serialNumber);
                    break;
                case AIR_CONDITIONER:
                    endpointStr = endpoint.getAirConditionerUrl(serialNumber);
                    break;
                default:
                    LOGGER.info("Device of unknown type found: {} - could not activate", device.getType());
                    return false;
            }

            if (Objects.nonNull(endpointStr)) {
                return dataEmitter.tryToActivate(serialNumber, endpointStr);
            }
        }

        return false;
    }

    public boolean tryToDeactivate(String serialNumber) {
        FunctionalDevice device = dataService.getFunctionalDevice(serialNumber);

        if (Objects.nonNull(device)) {
            String endpointStr = null;
            switch (device.getType()) {
                case AIR_FILTER:
                    endpointStr = endpoint.getAirFilterUrl(serialNumber);
                    break;
                case AIR_HUMIDIFIER:
                    endpointStr = endpoint.getAirHumidifierUrl(serialNumber);
                    break;
                case AIR_CONDITIONER:
                    endpointStr = endpoint.getAirConditionerUrl(serialNumber);
                    break;
                default:
                    LOGGER.info("Device of unknown type found: {} - could not activate", device.getType());
                    return false;
            }

            if (Objects.nonNull(endpointStr)) {
                return dataEmitter.tryToDeactivate(serialNumber, endpointStr);
            }
        }

        return false;
    }
}
