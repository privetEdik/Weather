package kettlebell.weather.mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

import kettlebell.weather.dto.LocationDto;
import kettlebell.weather.model.weather.WeatherModel;

public class WeatherModelToLocationDto implements Mapper<LocationDto, WeatherModel>{

	@Override
	public LocationDto mapFrom(WeatherModel f) {

		return LocationDto.builder()
				.id(f.getIdLocation().toString())
				.name(f.getName())
				.country( new Locale(   "" ,f.getSys().getCountry() ).getDisplayCountry(Locale.ENGLISH)    )
				.description(f.getWeatherList().get(0).getDescription())
				.lat(f.getCoord().getLat())
				.lon(f.getCoord().getLon())
				
				.temp(convertTemp(f))
				.build();
	}

	private String convertTemp(WeatherModel weatherModel){
		String result = new BigDecimal(weatherModel.getMain().getTemp()).setScale(0,RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
		char c = result.charAt(0);
		if((c!='-')&(c!='0')){
			result = "+" + result;
		}
		return result;
	}

}
