package kettlebell.weather.service;

import kettlebell.weather.dto.CreateUserDto;
import kettlebell.weather.entity.Location;
import kettlebell.weather.entity.Seance;
import kettlebell.weather.entity.User;
import kettlebell.weather.exception.EntityAlreadyExistsException;
import kettlebell.weather.exception.SeanceEndedException;
import kettlebell.weather.exception.UserNotFoundException;
import kettlebell.weather.exception.validator.heirs.LoginValidatorException;
import kettlebell.weather.exception.validator.heirs.PasswordValidationException;
import kettlebell.weather.repository.localdb.SeanceRepositoryDb;
import kettlebell.weather.repository.localdb.UserRepositoryDb;
import kettlebell.weather.util.HibernateRunner;
import kettlebell.weather.util.ScheduledExecutorServiceUtil;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;


import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private static UserService userService;

    @BeforeAll
    static void setUp() {
        Configuration configuration = new Configuration();
        configuration.configure();
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Location.class);
        configuration.addAnnotatedClass(Seance.class);
        HibernateRunner.getInstanceSessionFactory(configuration);

        userService = new UserService(UserRepositoryDb.getInstance());
        userService.create(CreateUserDto.builder().login("Bob").password("Hello").build());
    }


    @Test
    void authenticateByLoginSuccess() {
        assertSame(Long.class, userService.authenticateByLogin("Bob").getClass());
    }

    @Test
    void authenticateByLoginInvalid() {
        LoginValidatorException exception = assertThrows(LoginValidatorException.class, () -> userService.authenticateByLogin("/"));
        String expectedCode = "400";
        String actualCode = exception.getError().getCode();

        assertTrue(actualCode.contains(expectedCode));

    }

    @Test
    void authenticateByLoginNotFound() {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.authenticateByLogin("Vasya"));
        String expectedMessage = "This user is not registered";
        String actualMessage = exception.getError().getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void createSuccess() {
        String login = "Frederick";
        userService.create(CreateUserDto.builder().login(login).password("World").build());
        Long id = userService.authenticateByLogin(login);

        assertEquals(login, UserRepositoryDb.getInstance().findById(id).getLogin());

    }

    @Test
    void createInvalidLogin() {
        String login = "/";
        String password = "world";
        LoginValidatorException exception = assertThrows(LoginValidatorException.class,
                () -> userService.create(CreateUserDto.builder().login(login).password(password).build()));
        String expectedCode = "400";
        String actualCode = exception.getError().getCode();

        assertTrue(actualCode.contains(expectedCode));

    }

    @Test
    void createInvalidPassword() {
        String login = "Edik";
        String password = "7777777777777777777...????00000000000000222";
        PasswordValidationException exception = assertThrows(PasswordValidationException.class,
                () -> userService.create(CreateUserDto.builder().login(login).password(password).build()));
        String expectedCode = "401";
        String actualCode = exception.getError().getCode();

        assertTrue(actualCode.contains(expectedCode));

    }

    @Test
    void createViolationLoginUniqueness() {
        String login = "Jack";
        String password = "77777";
        userService.create(CreateUserDto.builder().login(login).password(password).build());
        userService = new UserService(UserRepositoryDb.getInstance());
        EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class,
                () -> userService.create(CreateUserDto.builder().login(login).password(password).build()));
        String expectedCode = "400";
        String actualCode = exception.getError().getCode();

        assertTrue(actualCode.contains(expectedCode));

    }

    @Test
    void expiredSeance() {
        User user = UserRepositoryDb.getInstance().findByLogin("Bob");
        String keySeance = new SeanceService(SeanceRepositoryDb.getInstance(), UserRepositoryDb.getInstance()).startSeanceAndGetKey(user.getId());
        Seance seance = SeanceRepositoryDb.getInstance().findById(keySeance);

        assertEquals(user.getLogin(), seance.getUser().getLogin());

        ScheduledExecutorServiceUtil.getInstance().schedule(new SeanceObserver(), 4, TimeUnit.SECONDS);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        SeanceEndedException exception = assertThrows(SeanceEndedException.class, () -> SeanceRepositoryDb.getInstance().updateOrSeanceIsOver(seance));
        String expectedMessage = "Your session has ended please authenticate";
        String actualMessage = exception.getError().getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

}