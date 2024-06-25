package kettlebell.weather.exception.validator;

import kettlebell.weather.exception.AppException;
import kettlebell.weather.exception.Error;

public class ValidationException extends AppException {

    public ValidationException(Error error) {
        super(error);
    }

}
