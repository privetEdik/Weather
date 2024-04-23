package kettlebell.weather.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import jakarta.persistence.Query;
import kettlebell.weather.repository.localdb.EntityRepositoryDb;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoadingBasicDataService extends EntityRepositoryDb {
    private static final LoadingBasicDataService INSTANCE = new LoadingBasicDataService();

    public static LoadingBasicDataService getInstance() {
        return INSTANCE;
    }

	public void loadingBasicData() {
        // formatter:off
        final String sql;
        try {
            sql = new String(Files.readAllBytes(Paths.get(
                    Objects.requireNonNull(
                            LoadingBasicDataService.class.getClassLoader().getResource("data.sql"))
                            .toURI())));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        // formatter:on

        executeInTransaction((session) -> {
                 Query query = session.createNativeQuery(sql);
                    query.executeUpdate();
        });

		}
		
	}

