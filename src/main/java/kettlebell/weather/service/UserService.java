package kettlebell.weather.service;


import kettlebell.weather.dto.user.CreateUserDto;
import kettlebell.weather.dto.db.User;
import kettlebell.weather.exception.AppException;
import kettlebell.weather.mapper.CreateUserToUserMapper;
import kettlebell.weather.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    private final CreateUserToUserMapper createUserToUserMapper = CreateUserToUserMapper.getInstance();
    private final UserRepository userRepositoryDb;

//    private final LoginValidator loginValidator = LoginValidator.getInstance();


    public UserService(UserRepository userRepositoryDb) {
        this.userRepositoryDb = userRepositoryDb;
    }

    public Long authenticateByLogin(String login) throws AppException {
//        loginValidator.isValid(login);

        User user = userRepositoryDb.findByLogin(login);

        return user.getId();
    }

    public void create(CreateUserDto createUserDto) throws AppException {
        String hashedPassword = BCrypt.hashpw(createUserDto.getPassword(), BCrypt.gensalt());
        createUserDto.setPassword(hashedPassword);
        User user = createUserToUserMapper.mapFrom(createUserDto);
        userRepositoryDb.save(user);
    }

}
