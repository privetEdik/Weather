package kettlebell.weather.validator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Error {
    private String code;
    private String message;

    private Error(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private Error(String message) {
        this.message = message;
    }


    public static Error of(String code, String message) {
        return new Error(code, message);
    }

    public static Error of(String message) {
        return new Error(message);
    }
}
