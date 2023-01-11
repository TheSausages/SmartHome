package pwr.smart.home.selenium;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class representing the Resource Timing API results
 * https://developer.mozilla.org/en-US/docs/Web/API/PerformanceResourceTiming
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ResourceTimings {
    private List<PerformanceResourceTiming> timings = new ArrayList<>();

    /**
     * The resource name is the address of the resource on the server.
     * For example, for an image this can be http://localhost:3000/logo.svg
     * or for ann API can it will be the API adress (ex. http://localhost:8080/api/control/turnOn)
     */
    public List<PerformanceResourceTiming> getTimingsForResources(String name) {
        return timings.stream().filter(timing -> timing.getName().contains(name)).collect(Collectors.toList());
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class PerformanceResourceTiming {
        private Double connectEnd;
        private Double connectStart;
        private Double decodedBodySize;
        private Double domainLookupEnd;
        private Double domainLookupStart;
        private Double duration;
        private Double encodedBodySize;
        private String entryType;
        private Double fetchStart;
        private String initiatorType;
        private String name;
        private String nextHopProtocol;
        private Double redirectEnd;
        private Double redirectStart;
        private String renderBlockingStatus;
        private Double requestStart;
        private Double responseEnd;
        private Double responseStart;
        private Double secureConnectionStart;
        private Object[] serverTiming;
        private Double startTime;
        private Double transferSize;
        private Double workerStart;

        public static class PerformanceResourceTimingBuilder {
            private Double connectEnd;
            private Double connectStart;
            private Double decodedBodySize;
            private Double domainLookupEnd;
            private Double domainLookupStart;
            private Double duration;
            private Double encodedBodySize;
            private String entryType;
            private Double fetchStart;
            private String initiatorType;
            private String name;
            private String nextHopProtocol;
            private Double redirectEnd;
            private Double redirectStart;
            private String renderBlockingStatus;
            private Double requestStart;
            private Double responseEnd;
            private Double responseStart;
            private Double secureConnectionStart;
            private Object[] serverTiming;
            private Double startTime;
            private Double transferSize;
            private Double workerStart;

            public PerformanceResourceTimingBuilder workerStart(Object workerStart) {
                if (workerStart instanceof Double) {
                    this.workerStart = ((Double) workerStart);
                }

                if (workerStart instanceof Long) {
                    this.workerStart = ((Long) workerStart).doubleValue();
                }

                return this;
            }

            public PerformanceResourceTimingBuilder transferSize(Object transferSize) {
                if (transferSize instanceof Double) {
                    this.transferSize = (Double) transferSize;
                }

                if (transferSize instanceof Long) {
                    this.transferSize = ((Long) transferSize).doubleValue();
                }

                return this;
            }

            public PerformanceResourceTimingBuilder startTime(Object startTime) {
                if (startTime instanceof Double) {
                    this.startTime = (Double)startTime;
                }

                if (startTime instanceof Long) {
                    this.startTime = ((Long) startTime).doubleValue();
                }

                return this;
            }

            public PerformanceResourceTimingBuilder secureConnectionStart(Object secureConnectionStart) {
                if (secureConnectionStart instanceof Double) {
                    this.secureConnectionStart = (Double) secureConnectionStart;
                }

                if (secureConnectionStart instanceof Long) {
                    this.secureConnectionStart = ((Long) secureConnectionStart).doubleValue();
                }

                return this;
            }

            public PerformanceResourceTimingBuilder responseStart(Object responseStart) {
                if (responseStart instanceof Double) {
                    this.responseStart = (Double) responseStart;
                }

                if (responseStart instanceof Long) {
                    this.responseStart = ((Long) responseStart).doubleValue();
                }

                return this;
            }

            public PerformanceResourceTimingBuilder responseEnd(Object responseEnd) {
                if (responseEnd instanceof Double) {
                    this.responseEnd = (Double) responseEnd;
                }

                if (responseEnd instanceof Long) {
                    this.responseEnd = ((Long) responseEnd).doubleValue();
                }

                return this;
            }

            public PerformanceResourceTimingBuilder requestStart(Object requestStart) {
                if (requestStart instanceof Double) {
                    this.requestStart = (Double) requestStart;
                }

                if (requestStart instanceof Long) {
                    this.requestStart = ((Long) requestStart).doubleValue();
                }

                return this;
            }

            public PerformanceResourceTimingBuilder redirectStart(Object redirectStart) {
                if (redirectStart instanceof Double) {
                    this.redirectStart = (Double) redirectStart;
                }

                if (redirectStart instanceof Long) {
                    this.redirectStart = ((Long) redirectStart).doubleValue();
                }

                return this;
            }

            public PerformanceResourceTimingBuilder redirectEnd(Object redirectEnd) {
                if (redirectEnd instanceof Double) {
                    this.redirectEnd = (Double) redirectEnd;
                }

                if (redirectEnd instanceof Long) {
                    this.redirectEnd = ((Long) redirectEnd).doubleValue();
                }

                return this;
            }

            public PerformanceResourceTimingBuilder fetchStart(Object fetchStart) {
                if (fetchStart instanceof Double) {
                    this.fetchStart = (Double) fetchStart;
                }

                if (fetchStart instanceof Long) {
                    this.fetchStart = ((Long) fetchStart).doubleValue();
                }

                return this;
            }

            public PerformanceResourceTimingBuilder encodedBodySize(Object encodedBodySize) {
                if (encodedBodySize instanceof Double) {
                    this.encodedBodySize = (Double) encodedBodySize;
                }

                if (encodedBodySize instanceof Long) {
                    this.encodedBodySize = ((Long) encodedBodySize).doubleValue();
                }

                return this;
            }

            public PerformanceResourceTimingBuilder duration(Object duration) {
                if (duration instanceof Double) {
                    this.duration = (Double) duration;
                }

                if (duration instanceof Long) {
                    this.duration = ((Long) duration).doubleValue();
                }

                return this;
            }

            public PerformanceResourceTimingBuilder domainLookupStart(Object domainLookupStart) {
                if (domainLookupStart instanceof Double) {
                    this.domainLookupStart = (Double) domainLookupStart;
                }

                if (domainLookupStart instanceof Long) {
                    this.domainLookupStart = ((Long) domainLookupStart).doubleValue();
                }

                return this;
            }

            public PerformanceResourceTimingBuilder domainLookupEnd(Object domainLookupEnd) {
                if (domainLookupEnd instanceof Double) {
                    this.domainLookupEnd = (Double) domainLookupEnd;
                }

                if (domainLookupEnd instanceof Long) {
                    this.domainLookupEnd = ((Long) domainLookupEnd).doubleValue();
                }

                return this;
            }

            public PerformanceResourceTimingBuilder connectEnd(Object connectEnd) {
                if (connectEnd instanceof Double) {
                    this.connectEnd = (Double) connectEnd;
                }

                if (connectEnd instanceof Long) {
                    this.connectEnd = ((Long) connectEnd).doubleValue();
                }

                return this;
            }

            public PerformanceResourceTimingBuilder connectStart(Object connectStart) {
                if (connectStart instanceof Double) {
                    this.connectStart = (Double) connectStart;
                }

                if (connectStart instanceof Long) {
                    this.connectStart = ((Long) connectStart).doubleValue();
                }

                return this;
            }

            public PerformanceResourceTimingBuilder decodedBodySize(Object decodedBodySize) {
                if (decodedBodySize instanceof Double) {
                    this.decodedBodySize = (Double) decodedBodySize;
                }

                if (decodedBodySize instanceof Long) {
                    this.decodedBodySize = ((Long) decodedBodySize).doubleValue();
                }

                return this;
            }
        }
    }
}
