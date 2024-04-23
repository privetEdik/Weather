package kettlebell.weather.exception.validator.heirs;

import kettlebell.weather.exception.validator.ValidationException;
import kettlebell.weather.validator.Error;

public class LoginValidatorException extends ValidationException {
    public LoginValidatorException(Error error) {
        super(error);
    }
}
