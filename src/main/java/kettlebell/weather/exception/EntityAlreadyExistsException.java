package kettlebell.weather.exception;

public class EntityAlreadyExistsException extends AppException {
    public EntityAlreadyExistsException(Error error) {
        super(error);
    }
}
