package kettlebell.weather.entity;

public interface BaseEntity <T> {
	T getId();
	
	void setId(T id);
}
