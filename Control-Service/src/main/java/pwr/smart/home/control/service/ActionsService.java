package pwr.smart.home.control.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.smart.home.control.model.Endpoint;
import pwr.smart.home.control.model.FunctionalDeviceWithMeasurementsDTO;
import pwr.smart.home.control.model.Home;
import pwr.smart.home.common.weather.model.response.AirQualityResponse;
import pwr.smart.home.common.weather.model.response.ForecastWeatherResponse;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

    public String doActionsForDeviceWithSerialNumber(String target, String serialNumber) throws ExecutionException, InterruptedException {
        Home home = dataService.getHome(serialNumber);

        Future<List<FunctionalDeviceWithMeasurementsDTO>> devices = functionalDevicesAsyncMethods.getFunctionalDevicesWithMeasurementsForHome(home);
        Future<ForecastWeatherResponse> weather = openMeteoAsyncMethods.getWeatherForecast(home);
        Future<AirQualityResponse> air = openMeteoAsyncMethods.getAirData(home);

        // For now we only get the selected machine
        // In a more complicated model, more than one device may be changed in a single action
        FunctionalDeviceWithMeasurementsDTO device = devices.get().stream()
                .filter(dev -> serialNumber.equals(dev.getDevice().getSerialNumber()))
                .findAny()
                .orElse(null);

        LOGGER.info("Try to set target {} for device with serial number {}", target, serialNumber);

        if (Objects.nonNull(device) && devices.isDone() && weather.isDone() && air.isDone()) {
            switch (device.getDevice().getType()) {
                case AIR_FILTER:
                    return functionalDevicesAsyncMethods.handleFilter(device, target, home, air.get()).get();
                case AIR_HUMIDIFIER:
                    return functionalDevicesAsyncMethods.handleHumidity(device, target, home, weather.get()).get();
                case AIR_CONDITIONER:
                    return functionalDevicesAsyncMethods.handleTemperature(device, target, home, weather.get()).get();
                default:
                    LOGGER.info("Device of unknown type found: {}", device.getDevice().getType());
                    return "";
            }
        }

        LOGGER.info("Null Device");
        return "";
    }

    public boolean tryToActivate(String serialNumber) throws ExecutionException, InterruptedException {
        Home home = dataService.getHome(serialNumber);

        Future<List<FunctionalDeviceWithMeasurementsDTO>> devices = functionalDevicesAsyncMethods.getFunctionalDevicesWithMeasurementsForHome(home);

        FunctionalDeviceWithMeasurementsDTO device = devices.get().stream()
                .filter(dev -> serialNumber.equals(dev.getDevice().getSerialNumber()))
                .findAny()
                .orElse(null);

        if (Objects.nonNull(device)) {
            String endpointStr = null;
            switch (device.getDevice().getType()) {
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
                    LOGGER.info("Device of unknown type found: {} - could not activate", device.getDevice().getType());
                    return false;
            }

            if (Objects.nonNull(endpointStr)) {
                return dataEmitter.tryToActivate(serialNumber, endpointStr);
            }
        }

        return false;
    }

    public boolean tryToDeactivate(String serialNumber) throws ExecutionException, InterruptedException {
        Home home = dataService.getHome(serialNumber);

        Future<List<FunctionalDeviceWithMeasurementsDTO>> devices = functionalDevicesAsyncMethods.getFunctionalDevicesWithMeasurementsForHome(home);

        FunctionalDeviceWithMeasurementsDTO device = devices.get().stream()
                .filter(dev -> serialNumber.equals(dev.getDevice().getSerialNumber()))
                .findAny()
                .orElse(null);

        if (Objects.nonNull(device)) {
            String endpointStr = null;
            switch (device.getDevice().getType()) {
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
                    LOGGER.info("Device of unknown type found: {} - could not activate", device.getDevice().getType());
                    return false;
            }

            if (Objects.nonNull(endpointStr)) {
                return dataEmitter.tryToDeactivate(serialNumber, endpointStr);
            }
        }

        return false;
    }
}
