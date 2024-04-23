package kettlebell.weather.mapper;

import java.util.Comparator;
import java.util.List;

import kettlebell.weather.dto.LocationDto;
import kettlebell.weather.dto.UserDto;
import kettlebell.weather.entity.Location;
import kettlebell.weather.entity.User;
import kettlebell.weather.model.weather.WeatherModel;
import kettlebell.weather.model.weather.components.Coord;
import kettlebell.weather.repository.http.LocationRepositoryHttp;

public class UserToUserDtoMapper implements Mapper<UserDto, User>{
	private static final UserToUserDtoMapper INSTANCE = new UserToUserDtoMapper();

	private final LocationRepositoryHttp locationRepositoryHttp = LocationRepositoryHttp.getInstance();

	private final WeatherModelToLocationDto weatherModelToLocationDto = new WeatherModelToLocationDto(); 
	@Override
	public UserDto mapFrom(User user) {

        List<LocationDto> loc = user.getLocations().stream()
				.sorted(Comparator.comparingLong(Location::getId))
				.map(location -> { 
					WeatherModel model = new WeatherModel();
					model.setIdLocation(location.getId());
					model.setCoord(
								Coord.builder()
										.lat(location.getLatitude().toString())
										.lon(location.getLongitude().toString())
										.build()
										);
					model.setName(location.getName());
					return model;			
				})
				.map(locationRepositoryHttp::loadingWeatherFromTheOpenWeatherAPI)
				.map(weatherModelToLocationDto::mapFrom)
				.toList();

		return UserDto.builder()
				.id(user.getId())
				.login(user.getLogin())
				.locationDtos(loc)
				.build();
	}
	public static UserToUserDtoMapper getInstance() {

		return INSTANCE;
	} 
}
