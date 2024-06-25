package kettlebell.weather.exception;

public class APIException extends AppException {
    public APIException(Error error) {
        super(error);
    }
}
