package kettlebell.weather.mapper;

import kettlebell.weather.dto.CreateUserDto;
import kettlebell.weather.entity.User;
import org.mindrot.jbcrypt.BCrypt;

public class CreateUserToUserMapper implements Mapper<User, CreateUserDto>{
	private static final CreateUserToUserMapper INSTANCE = new CreateUserToUserMapper();
	@Override
	public User mapFrom(CreateUserDto object) {
		String hashedPassword = BCrypt.hashpw(object.getPassword(), BCrypt.gensalt());

		return User.builder()
				.login(object.getLogin())
				.password(hashedPassword)
				.build();
	}
	
	public static CreateUserToUserMapper getInstance() {
		return INSTANCE;
	} 

}
