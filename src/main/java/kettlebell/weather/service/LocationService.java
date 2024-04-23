package kettlebell.weather.service;


import java.util.List;

import kettlebell.weather.dto.LocationDto;
import kettlebell.weather.entity.Location;
import kettlebell.weather.entity.User;
import kettlebell.weather.exception.APIException;
import kettlebell.weather.exception.AppException;
import kettlebell.weather.exception.validator.ValidationException;
import kettlebell.weather.mapper.LocationDtoToLocationMapper;
import kettlebell.weather.mapper.TownModelToLocationDtoMapper;
import kettlebell.weather.model.TownModel;
import kettlebell.weather.repository.http.LocationRepositoryHttp;
import kettlebell.weather.repository.localdb.LocationRepositoryDb;
import kettlebell.weather.repository.localdb.SeanceRepositoryDb;
import kettlebell.weather.storage.LocationStorageInstance;
import kettlebell.weather.validator.NameTownValidator;

import kettlebell.weather.validator.Error;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationService {
	private final NameTownValidator nameTownValidator = NameTownValidator.getInstance();

	private final LocationRepositoryDb locationRepositoryDb = LocationRepositoryDb.getInstance();

	private final LocationRepositoryHttp locationRepositoryHttp = LocationRepositoryHttp.getInstance();

	private final SeanceRepositoryDb seanceRepositoryDb = SeanceRepositoryDb.getInstance();

	private final TownModelToLocationDtoMapper townModelToLocationDtoMapper = TownModelToLocationDtoMapper.getInstance();

	private final LocationDtoToLocationMapper locationDtoToLocationMapper = LocationDtoToLocationMapper.getInstance();
	
	private static final LocationService INSTANCE = new LocationService();
	
	
	public List<LocationDto> getListOfLocationsByCityName(String keySeance, String nameTownParam) throws ValidationException,APIException {

		nameTownValidator.isValid(nameTownParam);
		
		List<TownModel> dto = locationRepositoryHttp.loadingLocationsFromTheOpenWeatherAPI(nameTownParam.replaceAll(" ", "-"));

		List<LocationDto> result = dto.stream().map(townModelToLocationDtoMapper::mapFrom).toList();

		LocationStorageInstance.INSTANCE.getInstance(keySeance).addAll(result);

		return result;
	}
	
	public void addLocationForUser(String keySeance, Integer indexLocation) throws AppException {

		LocationDto locationDto = LocationStorageInstance.INSTANCE.getInstance(keySeance).get(indexLocation);

		Location location = locationDtoToLocationMapper.mapFrom(locationDto);
		LocationStorageInstance.INSTANCE.clearStorage(keySeance);

		User user = seanceRepositoryDb.findById(keySeance).getUser();

		location.setUser(user);

		locationRepositoryDb.save(location);

	}
	
	public void removeLocationWithUser(Long idLocation) {

		Location location = Location.builder().id(idLocation).build();
		locationRepositoryDb.delete(location);

	}

	
	public static LocationService getInstance() {
		return INSTANCE;
	}

}
