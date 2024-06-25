package kettlebell.weather.mapper;

import java.math.BigDecimal;

import kettlebell.weather.dto.user.LocationDto;
import kettlebell.weather.dto.db.Location;

public class LocationDtoToLocationMapper implements Mapper<Location, LocationDto> {
    private static final LocationDtoToLocationMapper INSTANCE = new LocationDtoToLocationMapper();

    @Override
    public Location mapFrom(LocationDto f) {

        return Location.builder()
                .name(f.getName())
                .latitude(new BigDecimal(f.getLat()))
                .longitude(new BigDecimal(f.getLon()))
                .build();
    }

    public static LocationDtoToLocationMapper getInstance() {
        return INSTANCE;
    }
}
