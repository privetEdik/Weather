package kettlebell.weather.exception;

public class UserNotFoundException extends AppException {
    public UserNotFoundException(Error error) {
        super(error);
    }

}
