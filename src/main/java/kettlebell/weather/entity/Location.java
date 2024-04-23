 package kettlebell.weather.entity;

import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;


import jakarta.persistence.*;
//import jakarta.persistence.Table;
//import jakarta.persistence.UniqueConstraint;
import lombok.*;

 @Data
 @NoArgsConstructor
 @AllArgsConstructor
 @Builder
 @Entity
@Table(name = "locations", schema = "weather",uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","latitude","longitude"}))
public class Location implements BaseEntity<Long>{
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "user_id",referencedColumnName = "id")
	private User user;
	
	@Column(name = "latitude")
	private BigDecimal latitude;
	
	@Column(name = "longitude")
	private BigDecimal longitude; 

	@Override
	public Long getId() {return id;}

	@Override
	public void setId(Long id) {this.id=id;}

}
