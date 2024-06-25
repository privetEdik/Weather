package kettlebell.weather.repository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import jakarta.persistence.Query;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoadingBasicDataRepository extends EntityRepository {
    private static final LoadingBasicDataRepository INSTANCE = new LoadingBasicDataRepository();

    public static LoadingBasicDataRepository getInstance() {
        return INSTANCE;
    }

    public void loadingBasicData() {
        // formatter:off
        final String sql;
        try {
            sql = new String(Files.readAllBytes(Paths.get(
                    Objects.requireNonNull(
                                    LoadingBasicDataRepository.class.getClassLoader().getResource("data.sql"))
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

