package pwr.smart.home.selenium;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TimingsWithAnimationTimings {
    private ResourceTimings resourceTimings;
    private LocalDateTime animationStart;
    private LocalDateTime animationEnd;
}
