package kettlebell.weather.exception;

import kettlebell.weather.validator.Error;

public class EntityAlreadyExistsException extends AppException{
    public EntityAlreadyExistsException(Error error) {
        super(error);
    }
}
