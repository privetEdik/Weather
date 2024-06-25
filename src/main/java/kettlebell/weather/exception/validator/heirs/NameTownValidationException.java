package kettlebell.weather.exception.validator.heirs;

import kettlebell.weather.exception.validator.ValidationException;
import kettlebell.weather.exception.Error;

public class NameTownValidationException extends ValidationException {
    public NameTownValidationException(Error error) {
        super(error);
    }
}
