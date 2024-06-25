package kettlebell.weather.exception;

public class SeanceEndedException extends AppException {
    public SeanceEndedException(Error error) {
        super(error);
    }

}
