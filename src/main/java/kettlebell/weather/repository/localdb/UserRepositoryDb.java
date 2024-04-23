package kettlebell.weather.repository.localdb;

import java.util.Optional;
import kettlebell.weather.dao.BaseDao;
import kettlebell.weather.entity.User;
import kettlebell.weather.exception.EntityAlreadyExistsException;
import kettlebell.weather.exception.UserNotFoundException;
import kettlebell.weather.validator.Error;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRepositoryDb extends EntityRepositoryDb implements BaseDao<Long,User> {
	private static final UserRepositoryDb INSTANCE = new UserRepositoryDb();

	public static UserRepositoryDb getInstance() {
		return INSTANCE;
	}
	
	@Override
	public void save(User entity) throws EntityAlreadyExistsException{
		try {
			executeInTransaction(session -> session.persist(entity));
		}catch (ConstraintViolationException e){
			throw new EntityAlreadyExistsException(Error.of("400","User with this login already exists"));
		}
	}

	@Override
	public void delete(User entity) {}


	public User findByLogin(String login) {

		return Optional.ofNullable(
					executeInTransactionAndReturn(
						session -> session.createQuery("from User where login = :login",User.class)
				.setParameter("login",login)
				.getSingleResultOrNull()))
				.orElseThrow(()-> new UserNotFoundException(Error.of("This user is not registered")));

	}
	
	public User findById(Long id) {

		return executeInTransactionAndReturn(session -> session.find(User.class, id));

	}
}
