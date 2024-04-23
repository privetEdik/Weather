package kettlebell.weather.exception.validator.heirs;

import kettlebell.weather.exception.validator.ValidationException;
import kettlebell.weather.validator.Error;

public class PasswordValidationException extends ValidationException {
    public PasswordValidationException(Error error) {
        super(error);
    }
}
