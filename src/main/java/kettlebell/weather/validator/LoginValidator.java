	package kettlebell.weather.validator;

	import kettlebell.weather.exception.validator.ValidationException;
	import kettlebell.weather.exception.validator.heirs.LoginValidatorException;

	public class LoginValidator implements Validator<String>{
	private static final LoginValidator INSTANCE = new LoginValidator();
	private static final String FOR_LOGIN = "[0-9A-Za-zА-Яа-я[\\-][\\.][\\_][\\@]]{1,10}";

	@Override
	public void isValid(String login) throws ValidationException {

		if(login == null || login.isBlank() || !login.replaceFirst(FOR_LOGIN, "").isBlank()) {
			throw new LoginValidatorException(Error.of("400", "Login is invalid. Length from 1 to 10 characters. Letters of the Latin or Russian alphabet are acceptable, as well as the dot, dash, underscore and dog symbols"));
		}

	}

	public static LoginValidator getInstance() {
		return INSTANCE;
	}

}
