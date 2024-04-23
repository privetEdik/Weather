package kettlebell.weather.dao;

import kettlebell.weather.entity.BaseEntity;
import kettlebell.weather.exception.EntityAlreadyExistsException;

public interface BaseDao <K , E extends BaseEntity<K>> {
	void save(E entity) throws EntityAlreadyExistsException;
	void delete(E entity);
	
}
