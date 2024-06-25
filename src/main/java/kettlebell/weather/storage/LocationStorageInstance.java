package kettlebell.weather.storage;

import java.util.*;

import kettlebell.weather.dto.user.LocationDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationStorageInstance {
   // INSTANCE;

    private static volatile LocationStorageInstance INSTANCE;

    private static  Map<String, List<LocationDto>> map/* = new HashMap<>()*/;

//    public static List<LocationDto> getInstance(String uuid) {
//        if (map == null) {
//            map = new HashMap<>();
//
//        }
//        if (map.containsKey(uuid)) {
//            return map.get(uuid);
//        } else {
//            List<LocationDto> locationDtos = new ArrayList<>();
//            map.put(uuid, locationDtos);
//
//            return locationDtos;
//        }
//
//    }

    public static List<LocationDto> getInstance(String uuid) {
        if (INSTANCE == null) {
            synchronized (LocationStorageInstance.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocationStorageInstance();
                    map = Collections.synchronizedMap(new HashMap<>());
                }
            }
        }

        if (map.containsKey(uuid)) {
            return map.get(uuid);
        } else {
            List<LocationDto> locationDtos = new ArrayList<>();
            map.put(uuid, locationDtos);

            return locationDtos;
        }

       // return null;
    }

    public static void clearStorage(String uuid) {

        if (INSTANCE != null) {
            synchronized (LocationStorageInstance.class) {
                if (INSTANCE != null) {
                    map.remove(uuid);
                }
            }
        }
    }

}
