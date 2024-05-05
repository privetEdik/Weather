package kettlebell.weather.repository.localdb;

import kettlebell.weather.dao.BaseDao;
import kettlebell.weather.entity.Location;
import kettlebell.weather.entity.User;
import kettlebell.weather.exception.EntityAlreadyExistsException;
import kettlebell.weather.validator.Error;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationRepositoryDb extends EntityRepositoryDb implements BaseDao<Long, Location> {
    private static final LocationRepositoryDb INSTANCE = new LocationRepositoryDb();

    public static LocationRepositoryDb getInstance() {
        return INSTANCE;
    }

    @Override
    public void save(Location entity) throws EntityAlreadyExistsException {
        try {
            executeInTransaction(session -> {
                User user = session.find(User.class, entity.getUser().getId());

                user.addLocation(entity);

                session.persist(entity);
            });
        } catch (ConstraintViolationException e) {

            throw new EntityAlreadyExistsException(Error.of("Location already exists"));
        }

    }

    @Override
    public void delete(Location entity) {
        executeInTransaction(session -> {
            Location location = session.find(Location.class, entity.getId());
            session.remove(location);
        });
    }

}
