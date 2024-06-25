package kettlebell.weather.service;


import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kettlebell.weather.dto.user.LocationDto;
import kettlebell.weather.dto.db.Location;
import kettlebell.weather.dto.db.User;
import kettlebell.weather.exception.AppException;
import kettlebell.weather.exception.JsonException;
import kettlebell.weather.mapper.LocationDtoToLocationMapper;
import kettlebell.weather.mapper.TownModelToLocationDtoMapper;
import kettlebell.weather.dto.api.TownModel;
import kettlebell.weather.repository.LocationRepository;
import kettlebell.weather.repository.SeanceRepository;
import kettlebell.weather.storage.LocationStorageInstance;
import kettlebell.weather.exception.Error;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class LocationService {
//    private final NameTownValidator nameTownValidator = NameTownValidator.getInstance();

//    private LocationRepositoryDb locationRepositoryDb;
//
//    private LocationRepositoryHttp locationRepositoryHttp;

    private LocationRepository locationRepository;

    private SeanceRepository seanceRepositoryDb;

    private final TownModelToLocationDtoMapper townModelToLocationDtoMapper = TownModelToLocationDtoMapper.getInstance();

    private final LocationDtoToLocationMapper locationDtoToLocationMapper = LocationDtoToLocationMapper.getInstance();

    private final ObjectMapper mapper = new ObjectMapper();


    public LocationService(LocationRepository locationRepositoryFacade) {
        this.locationRepository = locationRepositoryFacade;
    }

    //    public LocationService(LocationRepositoryHttp locationRepositoryHttp) {
//        this.locationRepositoryHttp = locationRepositoryHttp;
//    }
//
//    public LocationService(LocationRepositoryDb locationRepositoryDb) {
//        this.locationRepositoryDb = locationRepositoryDb;
//    }
//
    public LocationService(LocationRepository locationRepository, SeanceRepository seanceRepositoryDb) {
        this.locationRepository = locationRepository;
        this.seanceRepositoryDb = seanceRepositoryDb;
    }


    public List<LocationDto> getListOfLocationsByCityName(String keySeance, String nameTownParam) throws AppException {

        try {

            String jsonResponseString = locationRepository/*Http*/.findJsonLocations(nameTownParam.replaceAll(" ", "-"));

            if (jsonResponseString.equals("[]")) {
                throw new RuntimeException();
            }
            List<TownModel> listTown = mapper.readValue(jsonResponseString, new TypeReference<>() {
            });

            List<LocationDto> result = listTown.stream().map(townModelToLocationDtoMapper::mapFrom).toList();

            LocationStorageInstance.getInstance(keySeance).addAll(result);

            return result;
        } catch (JsonProcessingException | RuntimeException e) {
            log.error(e.getMessage());
            throw new JsonException(Error.of("400", "Empty list locations. Incorrect name location "));
        }

    }

    public void addLocationForUser(String keySeance, Integer indexLocation) throws AppException {

        LocationDto locationDto = LocationStorageInstance.getInstance(keySeance).get(indexLocation);

        Location location = locationDtoToLocationMapper.mapFrom(locationDto);
        LocationStorageInstance.clearStorage(keySeance);

        User user = seanceRepositoryDb.findByIdWithUser(keySeance).getUser();

        location.setUser(user);

        locationRepository/*Db*/.save(location);

    }

    public void removeLocationWithUser(Long idLocation) {

        Location location = Location.builder().id(idLocation).build();
        locationRepository/*Db*/.delete(location);

    }

}
