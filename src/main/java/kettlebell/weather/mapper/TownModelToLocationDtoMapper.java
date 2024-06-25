package kettlebell.weather.mapper;

import java.util.Locale;

import kettlebell.weather.dto.user.LocationDto;
import kettlebell.weather.dto.api.TownModel;

public class TownModelToLocationDtoMapper implements Mapper<LocationDto, TownModel> {
    private static final TownModelToLocationDtoMapper INSTANCE = new TownModelToLocationDtoMapper();

    @Override
    public LocationDto mapFrom(TownModel townModel) {
        String nameCountry = getNameCountry(townModel.getCountry());
        return LocationDto.builder()
                .name(townModel.getName())
                .country(nameCountry)
                .state(townModel.getState())
                .lat(townModel.getLat())
                .lon(townModel.getLon())
                .build();
    }

    private String getNameCountry(String codeCountryISO) {
        return new Locale("", codeCountryISO).getDisplayCountry(Locale.ENGLISH);
    }

    public static TownModelToLocationDtoMapper getInstance() {
        return INSTANCE;
    }
}
