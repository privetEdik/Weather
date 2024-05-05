package kettlebell.weather.exception;

import kettlebell.weather.validator.Error;

public class JsonException extends AppException {
    public JsonException(Error error) {
        super(error);
    }
}
