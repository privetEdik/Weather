package kettlebell.weather.dto.user;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    Long id;
    String login;
    List<LocationDto> locationDtos;
}
