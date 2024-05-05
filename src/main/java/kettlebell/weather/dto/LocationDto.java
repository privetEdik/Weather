package kettlebell.weather.dto;

import lombok.*;

@Data
@Builder
public class LocationDto {
    private String id;
    private String name;
    private String country;
    private String state;
    private String lat;
    private String lon;
    private String temp;
    private String description;
}
