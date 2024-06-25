package kettlebell.weather.exception;

public class KeyForApiWeatherException extends APIException{
    public KeyForApiWeatherException(Error error) {
        super(error);
    }
}
