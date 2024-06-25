package kettlebell.weather.dto.user;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import lombok.Value;

@Builder
@Data
public class CreateUserDto {
    String login;
    String password;
}
