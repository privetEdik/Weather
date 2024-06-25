package kettlebell.weather.service;

import kettlebell.weather.dto.user.LocationDto;
import kettlebell.weather.dto.db.Location;
import kettlebell.weather.dto.db.Seance;
import kettlebell.weather.dto.db.User;
import kettlebell.weather.dto.api.weather.WeatherModel;
import kettlebell.weather.repository.LocationRepository;
import kettlebell.weather.repository.SeanceRepository;
import kettlebell.weather.util.HibernatePropertiesFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SeanceServiceTest {
    @BeforeAll
    static void setUp() {
        Configuration configuration = new Configuration();
        configuration.configure();
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Location.class);
        configuration.addAnnotatedClass(Seance.class);
        HibernatePropertiesFactory.getInstanceSessionFactory(configuration);

    }

    @Test
    void findUserDtoFromSeance() {

        String jsonResponseStringMadrid = "{\"coord\":{\"lon\":-3.7018,\"lat\":40.419},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"base\":\"stations\",\"main\":{\"temp\":16.85,\"feels_like\":15.69,\"temp_min\":15.45,\"temp_max\":19.08,\"pressure\":1014,\"humidity\":42},\"visibility\":10000,\"wind\":{\"speed\":5.66,\"deg\":270},\"clouds\":{\"all\":40},\"dt\":1714403277,\"sys\":{\"type\":2,\"id\":2084029,\"country\":\"ES\",\"sunrise\":1714367765,\"sunset\":1714417674},\"timezone\":7200,\"id\":3117735,\"name\":\"Madrid\",\"cod\":200}\n";
        String jsonResponseStringBerlin = "{\"coord\":{\"lon\":13.3889,\"lat\":52.517},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"base\":\"stations\",\"main\":{\"temp\":21.45,\"feels_like\":20.98,\"temp_min\":20.55,\"temp_max\":22.22,\"pressure\":1000,\"humidity\":51},\"visibility\":10000,\"wind\":{\"speed\":2.68,\"deg\":30},\"clouds\":{\"all\":0},\"dt\":1714402972,\"sys\":{\"type\":2,\"id\":2009543,\"country\":\"DE\",\"sunrise\":1714361895,\"sunset\":1714415341},\"timezone\":7200,\"id\":7576815,\"name\":\"Alt-Kölln\",\"cod\":200}\n";
        String jsonResponseStringOdesa = "{\"coord\":{\"lon\":30.73,\"lat\":46.48},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"base\":\"stations\",\"main\":{\"temp\":19.56,\"feels_like\":18.77,\"temp_min\":19.56,\"temp_max\":19.56,\"pressure\":1025,\"humidity\":46,\"sea_level\":1025,\"grnd_level\":1021},\"visibility\":10000,\"wind\":{\"speed\":7.93,\"deg\":35,\"gust\":11.85},\"clouds\":{\"all\":100},\"dt\":1714403285,\"sys\":{\"country\":\"UA\",\"sunrise\":1714358732,\"sunset\":1714410182},\"timezone\":10800,\"id\":698740,\"name\":\"Odesa\",\"cod\":200}\n";


        List<Location> locations = new ArrayList<>(List.of(
                Location.builder().id(11L).name("Madrid").latitude(new BigDecimal("40.4167047")).longitude(new BigDecimal("-3.7035825")).build(),
                Location.builder().id(5L).name("Berlin").latitude(new BigDecimal("52.5170365")).longitude(new BigDecimal("13.3888599")).build(),
                Location.builder().id(2L).name("Odesa").latitude(new BigDecimal("46.4843023")).longitude(new BigDecimal("30.7322878")).build()
        ));

        SeanceRepository seanceRepositoryDbMock = Mockito.mock(SeanceRepository.class);
        User user = User.builder().id(10L).login("Gary").locations(locations).build();
        Seance seance = new Seance();
        seance.setUser(user);
        when(seanceRepositoryDbMock.findByIdWithUserAndLocations("keySeance")).thenReturn(seance);

        LocationRepository locationRepositoryMock = Mockito.mock(LocationRepository.class);

        when(locationRepositoryMock.findJsonWeather(any(WeatherModel.class))).thenReturn(jsonResponseStringMadrid);
        when(locationRepositoryMock.findJsonWeather(any(WeatherModel.class))).thenReturn(jsonResponseStringBerlin);
        when(locationRepositoryMock.findJsonWeather(any(WeatherModel.class))).thenReturn(jsonResponseStringOdesa);


        SeanceService seanceService = new SeanceService(seanceRepositoryDbMock, locationRepositoryMock);


        List<LocationDto> list = seanceService.findUserDtoFromSeance("keySeance").getLocationDtos();

        List<LocationDto> listResult = list.stream()
                .filter(location -> location.getName().equals("Madrid") || location.getName().equals("Berlin") || location.getName().equals("Odesa"))
                .toList();

        assertEquals(3, listResult.size());
        assertEquals("Odesa", listResult.get(0).getName());
        assertEquals("Berlin", listResult.get(1).getName());
        assertEquals("Madrid", listResult.get(2).getName());
    }

}