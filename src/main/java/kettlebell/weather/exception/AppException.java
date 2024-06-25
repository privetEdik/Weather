package kettlebell.weather.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final Error error;

    public AppException(Error error) {

        this.error = error;
    }

}
