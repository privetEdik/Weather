package kettlebell.weather.validator;

import kettlebell.weather.exception.Error;
import kettlebell.weather.exception.validator.ValidationException;
import kettlebell.weather.exception.validator.heirs.NameTownValidationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NameTownValidator implements Validator<String> {
    private static final NameTownValidator INSTANCE = new NameTownValidator();
    private static final String FOR_NAME_TOWN_RUS = "[А-Яа-я[\\-][\\.][\\ ]]{1,170}";
    private static final String FOR_NAME_TOWN_ENG = "[A-Za-z[\\-][\\.][\\ ]]{1,170}";

    @Override
    public void isValid(String nameTown) throws ValidationException {

        if (nameTown == null || nameTown.isBlank()) {
            log.info("work validator null+empty");
            throw new NameTownValidationException(Error.of("400", "field is empty"));
        }
        if (!(nameTown.replaceFirst(FOR_NAME_TOWN_ENG, "").isBlank() |
                nameTown.replaceFirst(FOR_NAME_TOWN_RUS, "").isBlank())) {
            log.info("the name did not pass the pattern");
            throw new NameTownValidationException(Error.of("400", "Letters of the Latin alphabet are acceptable, as well as the dot and dash symbols. Replace spaces with dashes"));

        }

    }


    public static NameTownValidator getInstance() {
        return INSTANCE;
    }

}
