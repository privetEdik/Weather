package kettlebell.weather.mapper;

import kettlebell.weather.dto.user.CreateUserDto;
import kettlebell.weather.dto.db.User;

public class CreateUserToUserMapper implements Mapper<User, CreateUserDto> {
    private static final CreateUserToUserMapper INSTANCE = new CreateUserToUserMapper();

    @Override
    public User mapFrom(CreateUserDto object) {
//        String hashedPassword = BCrypt.hashpw(object.getPassword(), BCrypt.gensalt());

        return User.builder()
                .login(object.getLogin())
                .password(object.getPassword())
                .build();
    }

    public static CreateUserToUserMapper getInstance() {
        return INSTANCE;
    }

}
