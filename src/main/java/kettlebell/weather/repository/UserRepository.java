package kettlebell.weather.repository;

import java.util.Optional;

import kettlebell.weather.dto.db.User;
import kettlebell.weather.exception.EntityAlreadyExistsException;
import kettlebell.weather.exception.UserNotFoundException;
import kettlebell.weather.exception.Error;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRepository extends EntityRepository/* implements BaseDao<User>*/ {
    private static final UserRepository INSTANCE = new UserRepository();

    public static UserRepository getInstance() {
        return INSTANCE;
    }

   // @Override
    public void save(User entity) throws EntityAlreadyExistsException {
        try {
            executeInTransaction(session -> session.persist(entity));
        } catch (ConstraintViolationException e) {
            throw new EntityAlreadyExistsException(Error.of("400", "User with this login already exists"));
        }
    }


    public User findByLogin(String login) {

        return Optional.ofNullable(
                        executeWithoutTransactionAndReturn(
                                session -> session.createQuery("from User where login = :login", User.class)
                                        .setParameter("login", login)
                                        .getSingleResultOrNull()))
                .orElseThrow(() -> new UserNotFoundException(Error.of("This user is not registered")));

    }

    public User findById(Long id) {

        return executeWithoutTransactionAndReturn(session -> session.find(User.class, id));

    }
}
