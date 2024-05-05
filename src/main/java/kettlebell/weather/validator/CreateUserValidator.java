package kettlebell.weather.validator;

import kettlebell.weather.dto.CreateUserDto;
import kettlebell.weather.exception.validator.ValidationException;
import kettlebell.weather.exception.validator.heirs.PasswordValidationException;

public class CreateUserValidator implements Validator<CreateUserDto> {
    private static final CreateUserValidator INSTANCE = new CreateUserValidator();
    private static final String FOR_PASSWORD = "[0-9A-Za-zА-Яа-я]{1,20}";

    @Override
    public void isValid(CreateUserDto userDto) throws ValidationException {
        LoginValidator.getInstance().isValid(userDto.getLogin());

        if (userDto.getPassword() == null || userDto.getPassword().isBlank() || !userDto.getPassword().replaceFirst(FOR_PASSWORD, "").isBlank()) {

            throw new PasswordValidationException(Error.of("401", "Password is invalid. Latin or Russian letters and numbers are acceptable"));
        }


    }

    public static CreateUserValidator getInstance() {
        return INSTANCE;
    }
}
