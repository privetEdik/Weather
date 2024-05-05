package kettlebell.weather.service;


import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kettlebell.weather.dto.LocationDto;
import kettlebell.weather.entity.Location;
import kettlebell.weather.entity.User;
import kettlebell.weather.exception.AppException;
import kettlebell.weather.exception.JsonException;
import kettlebell.weather.mapper.LocationDtoToLocationMapper;
import kettlebell.weather.mapper.TownModelToLocationDtoMapper;
import kettlebell.weather.model.TownModel;
import kettlebell.weather.repository.http.LocationRepositoryHttp;
import kettlebell.weather.repository.localdb.LocationRepositoryDb;
import kettlebell.weather.repository.localdb.SeanceRepositoryDb;
import kettlebell.weather.storage.LocationStorageInstance;
import kettlebell.weather.validator.Error;
import kettlebell.weather.validator.NameTownValidator;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class LocationService {
    private final NameTownValidator nameTownValidator = NameTownValidator.getInstance();

    private LocationRepositoryDb locationRepositoryDb;

    private LocationRepositoryHttp locationRepositoryHttp;

    private SeanceRepositoryDb seanceRepositoryDb;

    private final TownModelToLocationDtoMapper townModelToLocationDtoMapper = TownModelToLocationDtoMapper.getInstance();

    private final LocationDtoToLocationMapper locationDtoToLocationMapper = LocationDtoToLocationMapper.getInstance();

    private final ObjectMapper mapper = new ObjectMapper();


    public LocationService(LocationRepositoryHttp locationRepositoryHttp) {
        this.locationRepositoryHttp = locationRepositoryHttp;
    }

    public LocationService(LocationRepositoryDb locationRepositoryDb) {
        this.locationRepositoryDb = locationRepositoryDb;
    }

    public LocationService(LocationRepositoryDb locationRepositoryDb, SeanceRepositoryDb seanceRepositoryDb) {
        this.locationRepositoryDb = locationRepositoryDb;
        this.seanceRepositoryDb = seanceRepositoryDb;
    }


    public List<LocationDto> getListOfLocationsByCityName(String keySeance, String nameTownParam) throws AppException {

        nameTownValidator.isValid(nameTownParam);

        String jsonResponseString = locationRepositoryHttp.loadingLocationsFromTheOpenWeatherAPI(nameTownParam.replaceAll(" ", "-"));

        try {

            List<TownModel> listTown = mapper.readValue(jsonResponseString, new TypeReference<>() {
            });

            List<LocationDto> result = listTown.stream().map(townModelToLocationDtoMapper::mapFrom).toList();

            LocationStorageInstance.INSTANCE.getInstance(keySeance).addAll(result);

            return result;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new JsonException(Error.of("400", "Empty list locations. Incorrect name location "));
        }

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

}
