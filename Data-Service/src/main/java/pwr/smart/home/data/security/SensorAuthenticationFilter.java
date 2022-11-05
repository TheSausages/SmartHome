package pwr.smart.home.data.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import pwr.smart.home.data.dao.SensorDao;
import pwr.smart.home.data.repository.SensorRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SensorAuthenticationFilter extends OncePerRequestFilter {
    private final SensorRepository sensorRepository;

    SensorAuthenticationFilter(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String serialNumber = request.getHeader("Serial-Number");

        Optional<SensorDao> sensor = sensorRepository.findBySerialNumber(serialNumber);

        if (sensor.isEmpty()) {
            throw new SecurityException("A sensor with serial number " + serialNumber + " does not exist!");
        }

        Authentication auth = UsernamePasswordAuthenticationToken.authenticated(serialNumber, null, List.of(new SimpleGrantedAuthority("Sensor")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}
