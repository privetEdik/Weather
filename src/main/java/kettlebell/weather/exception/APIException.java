package kettlebell.weather.exception;

import kettlebell.weather.validator.Error;

public class APIException extends AppException{
    public APIException(Error error) {
        super(error);
    }
}
