package kettlebell.weather.service;



import kettlebell.weather.dto.CreateUserDto;
import kettlebell.weather.entity.User;
import kettlebell.weather.exception.AppException;
import kettlebell.weather.exception.validator.ValidationException;
import kettlebell.weather.mapper.CreateUserToUserMapper;
import kettlebell.weather.repository.localdb.UserRepositoryDb;
import kettlebell.weather.validator.CreateUserValidator;
import kettlebell.weather.validator.LoginValidator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserService {
	private static final UserService INSTANCE = new UserService();
	private final CreateUserToUserMapper createUserToUserMapper = CreateUserToUserMapper.getInstance();
	private final UserRepositoryDb userRepositoryDb = UserRepositoryDb.getInstance();
	private final CreateUserValidator createUserValidator = CreateUserValidator.getInstance();
	private final LoginValidator loginValidator = LoginValidator.getInstance();
	
	public Long authenticateByLogin(String login) throws AppException {
		loginValidator.isValid(login);
	
		User user = userRepositoryDb.findByLogin(login);

		return user.getId() ;
	}
	
	public void create (CreateUserDto createUserDto) throws AppException{
		createUserValidator.isValid(createUserDto);

		User user = createUserToUserMapper.mapFrom(createUserDto);
		 userRepositoryDb.save(user);
	}
	public static UserService getInstance() {
		return INSTANCE;
	}

}
