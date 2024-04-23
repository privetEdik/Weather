package kettlebell.weather.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kettlebell.weather.dto.LocationDto;
import kettlebell.weather.dto.UserDto;

public enum LocationStorageInstance {
	INSTANCE;

    private static Map<String, List<LocationDto>> map;

	public List<LocationDto> getInstance(String uuid) {
		if(map == null) {
			map = new HashMap<>();
		}
		if ( map.containsKey(uuid)) {
			return map.get(uuid);
		} else {
            List<LocationDto> locationDtos = new ArrayList<>();
			map.put(uuid, locationDtos);
		  
			return locationDtos;
		}

	}
	
	public void clearStorage(String uuid) {
		if(map!=null){
		map.remove(uuid);
		}
	}

}
