package pwr.smart.home.common.error;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ErrorDTO {
    private String message;
    private int status;

    public static class ErrorDTOBuilder {
        public ErrorDTOBuilder status(HttpStatus status) {
            this.status = status.value();
            return this;
        }

        public ErrorDTOBuilder status(int status) {
            this.status = status;
            return this;
        }
    }
}
