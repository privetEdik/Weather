package kettlebell.weather.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "seance")
@Builder
@Entity
@Table(name = "users", schema = "weather", indexes = @Index(columnList = "login", unique = true))
public class User implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Location> locations = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private Seance seance;

    public void addLocation(Location location) {
        getLocations().add(location);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

}
