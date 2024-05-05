package kettlebell.weather.validator;

import kettlebell.weather.exception.validator.ValidationException;

public interface Validator<T> {

    void isValid(T object) throws ValidationException;
}
