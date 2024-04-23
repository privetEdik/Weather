package kettlebell.weather.exception;

import kettlebell.weather.validator.Error;

public class UserNotFoundException extends AppException{
	public UserNotFoundException(Error error) {
		super(error);
	}

}
