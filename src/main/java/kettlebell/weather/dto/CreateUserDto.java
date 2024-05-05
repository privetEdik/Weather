package kettlebell.weather.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateUserDto {
    String login;
    String password;
}
