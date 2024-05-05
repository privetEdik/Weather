package kettlebell.weather.service;

import kettlebell.weather.dto.LocationDto;
import kettlebell.weather.entity.Location;
import kettlebell.weather.entity.Seance;
import kettlebell.weather.entity.User;
import kettlebell.weather.exception.JsonException;
import kettlebell.weather.exception.validator.heirs.NameTownValidationException;
import kettlebell.weather.repository.http.LocationRepositoryHttp;
import kettlebell.weather.util.HibernateRunner;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    private LocationService locationService;

    @BeforeAll
    static void setUp() {
        Configuration configuration = new Configuration();
        configuration.configure();
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Location.class);
        configuration.addAnnotatedClass(Seance.class);
        HibernateRunner.getInstanceSessionFactory(configuration);

    }

    @Test
    void getListOfLocationsByCityName_Valid_NameTown() {
        LocationRepositoryHttp repositoryHttp = Mockito.mock(LocationRepositoryHttp.class);

        String nameTown = "London";
        String keySeance = UUID.randomUUID().toString();
        String jsonResponseString = "[{\"name\":\"London\",\"local_names\":{\"br\":\"Londrez\",\"kv\":\"Лондон\",\"ne\":\"?????\",\"he\":\"??????\",\"ur\":\"????? ????\",\"ca\":\"Londres\",\"yo\":\"L?nd?nu\",\"ab\":\"Лондон\",\"oc\":\"Londres\",\"ay\":\"London\",\"sm\":\"Lonetona\",\"lv\":\"Londona\",\"fo\":\"London\",\"sq\":\"Londra\",\"gd\":\"Lunnainn\",\"ga\":\"Londain\",\"yi\":\"??????\",\"jv\":\"London\",\"gl\":\"Londres\",\"sr\":\"Лондон\",\"ka\":\"???????\",\"wa\":\"Londe\",\"de\":\"London\",\"es\":\"Londres\",\"th\":\"??????\",\"av\":\"Лондон\",\"bg\":\"Лондон\",\"ml\":\"?????\",\"bn\":\"?????\",\"eu\":\"Londres\",\"rm\":\"Londra\",\"so\":\"London\",\"ff\":\"London\",\"hr\":\"London\",\"eo\":\"Londono\",\"mt\":\"Londra\",\"bm\":\"London\",\"ku\":\"London\",\"kl\":\"London\",\"sk\":\"Londyn\",\"nl\":\"Londen\",\"cv\":\"Лондон\",\"ta\":\"???????\",\"ps\":\"????\",\"qu\":\"London\",\"it\":\"Londra\",\"mn\":\"Лондон\",\"lt\":\"Londonas\",\"az\":\"London\",\"bo\":\"????????\",\"ascii\":\"London\",\"mk\":\"Лондон\",\"bs\":\"London\",\"ie\":\"London\",\"ug\":\"??????\",\"ky\":\"Лондон\",\"si\":\"??????\",\"fj\":\"Lodoni\",\"mi\":\"Ranana\",\"om\":\"Landan\",\"su\":\"London\",\"sh\":\"London\",\"bi\":\"London\",\"se\":\"London\",\"et\":\"London\",\"vi\":\"Luan Don\",\"ny\":\"London\",\"ig\":\"London\",\"my\":\"???????????\",\"is\":\"London\",\"cu\":\"Лондонъ\",\"kn\":\"?????\",\"gv\":\"Lunnin\",\"el\":\"???????\",\"tl\":\"Londres\",\"km\":\"?????\",\"pt\":\"Londres\",\"st\":\"London\",\"ko\":\"??\",\"fa\":\"????\",\"ln\":\"Lond?l?\",\"io\":\"London\",\"ht\":\"Lonn\",\"uk\":\"Лондон\",\"sl\":\"London\",\"tr\":\"Londra\",\"lo\":\"??????\",\"kk\":\"Лондон\",\"to\":\"Lonitoni\",\"pa\":\"????\",\"os\":\"Лондон\",\"ja\":\"????\",\"an\":\"Londres\",\"sn\":\"London\",\"fr\":\"Londres\",\"hi\":\"????\",\"wo\":\"Londar\",\"sv\":\"London\",\"sc\":\"Londra\",\"cy\":\"Llundain\",\"zu\":\"ILondon\",\"en\":\"London\",\"ba\":\"Лондон\",\"na\":\"London\",\"li\":\"Londe\",\"nn\":\"London\",\"am\":\"????\",\"pl\":\"Londyn\",\"sw\":\"London\",\"hy\":\"??????\",\"tk\":\"London\",\"gn\":\"Londyre\",\"bh\":\"????\",\"ha\":\"Landan\",\"ee\":\"London\",\"fi\":\"Lontoo\",\"vo\":\"London\",\"mg\":\"Londona\",\"sa\":\"??????\",\"co\":\"Londra\",\"tg\":\"Лондон\",\"kw\":\"Loundres\",\"gu\":\"????\",\"feature_name\":\"London\",\"cs\":\"Londyn\",\"be\":\"Лондан\",\"uz\":\"London\",\"ro\":\"Londra\",\"tt\":\"Лондон\",\"lb\":\"London\",\"id\":\"London\",\"mr\":\"????\",\"da\":\"London\",\"ar\":\"????\",\"no\":\"London\",\"ms\":\"London\",\"sd\":\"????\",\"nv\":\"Tooh Dine?e Bikin Haal?a\",\"ia\":\"London\",\"zh\":\"??\",\"ce\":\"Лондон\",\"af\":\"Londen\",\"te\":\"?????\",\"tw\":\"London\",\"hu\":\"London\",\"fy\":\"Londen\",\"ru\":\"Лондон\",\"or\":\"?????\"},\"lat\":51.5073219,\"lon\":-0.1276474,\"country\":\"GB\",\"state\":\"England\"},{\"name\":\"City of London\",\"local_names\":{\"lt\":\"Londono Sitis\",\"uk\":\"Лондонське Сіті\",\"pt\":\"Cidade de Londres\",\"ko\":\"?? ?? ??\",\"es\":\"City de Londres\",\"ru\":\"Сити\",\"he\":\"????? ?? ??????\",\"zh\":\"???\",\"en\":\"City of London\",\"fr\":\"Cite de Londres\",\"hi\":\"???? ??? ????\",\"ur\":\"???? ???\"},\"lat\":51.5156177,\"lon\":-0.0919983,\"country\":\"GB\",\"state\":\"England\"},{\"name\":\"London\",\"local_names\":{\"ja\":\"????\",\"yi\":\"??????\",\"oj\":\"Baketigweyaang\",\"ug\":\"??????\",\"ga\":\"Londain\",\"ka\":\"???????\",\"en\":\"London\",\"bn\":\"?????\",\"be\":\"Лондан\",\"ru\":\"Лондон\",\"lt\":\"Londonas\",\"lv\":\"Landona\",\"he\":\"??????\",\"ar\":\"????\",\"ko\":\"??\",\"cr\":\"?????\",\"fr\":\"London\",\"fa\":\"????\",\"iu\":\"????\",\"th\":\"??????\",\"el\":\"??????\",\"hy\":\"??????\"},\"lat\":42.9832406,\"lon\":-81.243372,\"country\":\"CA\",\"state\":\"Ontario\"},{\"name\":\"Chelsea\",\"local_names\":{\"az\":\"Celsi\",\"no\":\"Chelsea\",\"af\":\"Chelsea, Londen\",\"ja\":\"?????\",\"sh\":\"Chelsea, London\",\"zh\":\"???\",\"hu\":\"Chelsea\",\"nl\":\"Chelsea\",\"tr\":\"Chelsea, Londra\",\"ko\":\"??\",\"pt\":\"Chelsea\",\"da\":\"Chelsea\",\"eu\":\"Chelsea\",\"de\":\"Chelsea\",\"id\":\"Chelsea, London\",\"el\":\"??????\",\"en\":\"Chelsea\",\"pl\":\"Chelsea\",\"uk\":\"Челсі\",\"hi\":\"??????, ????\",\"sv\":\"Chelsea, London\",\"he\":\"?'???\",\"ar\":\"??????\",\"et\":\"Chelsea\",\"fr\":\"Chelsea\",\"it\":\"Chelsea\",\"sk\":\"Chelsea\",\"fa\":\"????\",\"ru\":\"Челси\",\"ga\":\"Chelsea\",\"es\":\"Chelsea\",\"ur\":\"?????? ????\",\"vi\":\"Chelsea, Luan Don\"},\"lat\":51.4875167,\"lon\":-0.1687007,\"country\":\"GB\",\"state\":\"England\"},{\"name\":\"London\",\"lat\":37.1289771,\"lon\":-84.0832646,\"country\":\"US\",\"state\":\"Kentucky\"}]\n";
        when(repositoryHttp.loadingLocationsFromTheOpenWeatherAPI(anyString())).thenReturn(jsonResponseString);
        locationService = new LocationService(repositoryHttp);

        List<LocationDto> list = locationService.getListOfLocationsByCityName(keySeance, nameTown);

        String nameLocation = list.stream()
                .filter(location -> location.getName().contains("London"))
                .findFirst()
                .map(LocationDto::getName)
                .orElse("");

        assertTrue(nameLocation.contains("London"));
    }

    @Test
    void getListOfLocationsByCityName_Empty_field_NameTown() {
        locationService = new LocationService();
        NameTownValidationException exception = assertThrows(NameTownValidationException.class, () -> locationService.getListOfLocationsByCityName("jjhj", ""));
        String expectedMessage = "field is empty";
        String actualMessage = exception.getError().getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void getListOfLocationsByCityName_Error_NameTown() {
        locationService = new LocationService();
        NameTownValidationException exception = assertThrows(NameTownValidationException.class, () -> locationService.getListOfLocationsByCityName("jjhj", "/"));
        String expectedMessage = "Letters of the Latin alphabet are acceptable, as well as the dot and dash symbols. Replace spaces with dashes";
        String actualMessage = exception.getError().getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void getListOfLocationsByCityName_Correct_Non_Existent_City_Name() {
        LocationRepositoryHttp repositoryHttp = Mockito.mock(LocationRepositoryHttp.class);
        String jsonResponseString = "{{}}";
        when(repositoryHttp.loadingLocationsFromTheOpenWeatherAPI("qqqqqqqqqqqq")).thenReturn(jsonResponseString);


        locationService = new LocationService(repositoryHttp);
        JsonException exception = assertThrows(JsonException.class, () -> locationService.getListOfLocationsByCityName("jjhj", "qqqqqqqqqqqq"));
        String expectedCode = "400";
        String actualCode = exception.getError().getCode();

        assertEquals(expectedCode, actualCode);
    }

}

