package kettlebell.weather.exception;

public class JsonException extends AppException {
    public JsonException(Error error) {
        super(error);
    }
}
