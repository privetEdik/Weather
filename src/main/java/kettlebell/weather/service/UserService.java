package kettlebell.weather.service;


import kettlebell.weather.dto.CreateUserDto;
import kettlebell.weather.entity.User;
import kettlebell.weather.exception.AppException;
import kettlebell.weather.mapper.CreateUserToUserMapper;
import kettlebell.weather.repository.localdb.UserRepositoryDb;
import kettlebell.weather.validator.CreateUserValidator;
import kettlebell.weather.validator.LoginValidator;

public class UserService {
    private final CreateUserToUserMapper createUserToUserMapper = CreateUserToUserMapper.getInstance();
    private final UserRepositoryDb userRepositoryDb;
    private final CreateUserValidator createUserValidator = CreateUserValidator.getInstance();
    private final LoginValidator loginValidator = LoginValidator.getInstance();


    public UserService(UserRepositoryDb userRepositoryDb) {
        this.userRepositoryDb = userRepositoryDb;
    }

    public Long authenticateByLogin(String login) throws AppException {
        loginValidator.isValid(login);

        User user = userRepositoryDb.findByLogin(login);

        return user.getId();
    }

    public void create(CreateUserDto createUserDto) throws AppException {
        createUserValidator.isValid(createUserDto);

        User user = createUserToUserMapper.mapFrom(createUserDto);
        userRepositoryDb.save(user);
    }

}
