package pwr.smart.home.selenium;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Class representing the Navigation Timing API results
 * https://www.lambdatest.com/blog/how-to-measure-page-load-times-with-selenium/
 * https://developer.mozilla.org/en-US/docs/Web/API/Navigation_timing_API
 */
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class Performance {
    private Long timeOrigin;
    private Timing timing;
    private Memory memory;

    public static class PerformanceBuilder {
        private Long timeOrigin;

        public PerformanceBuilder timeOrigin(Object timeOriginObj) {
            if (timeOriginObj instanceof Double) {
                this.timeOrigin = ((Double) timeOriginObj).longValue();
            }

            if (timeOriginObj instanceof Long) {
                this.timeOrigin = (Long) timeOriginObj;
            }

            return this;
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Timing {
        private Long connectEnd;
        private Long connectStart;
        private Long domComplete;
        private Long domContentLoadedEventEnd;
        private Long domContentLoadedEventStart;
        private Long domInteractive;
        private Long domLoading;
        private Long domainLookupEnd;
        private Long domainLookupStart;
        private Long fetchStart;
        private Long loadEventEnd;
        private Long loadEventStart;
        private Long navigationStart;
        private Long redirectEnd;
        private Long redirectStart;
        private Long requestStart;
        private Long responseEnd;
        private Long responseStart;
        private Long secureConnectionStart;
        private Long unloadEventEnd;
        private Long unloadEventStart;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Memory {
        private Long jsHeapSizeLimit;
        private Long totalJSHeapSize;
        private Long usedJSHeapSize;
    }
}
