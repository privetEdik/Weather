package kettlebell.weather.exception;

import kettlebell.weather.validator.Error;

public class SeanceEndedException extends AppException{
	public SeanceEndedException(Error error) {
		super(error);
	}

}
