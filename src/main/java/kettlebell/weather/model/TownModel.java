package kettlebell.weather.model;

import java.util.Map;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TownModel {
    private String name;
    private Map<String, String> local_names;
    private String lat;
    private String lon;
    private String country;
    private String state;
}
