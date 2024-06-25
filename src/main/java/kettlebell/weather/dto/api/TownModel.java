package kettlebell.weather.dto.api;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TownModel {
    private String name;
    @JsonProperty("local_names")
    private Map<String, String> localNames;
    private String lat;
    private String lon;
    private String country;
    private String state;
}
