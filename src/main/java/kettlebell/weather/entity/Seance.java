package kettlebell.weather.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "seance",schema = "weather")
public class Seance implements BaseEntity<String>{

	@Id
	private String id;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "time")
	private LocalDateTime time;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id=id;
	}
}
