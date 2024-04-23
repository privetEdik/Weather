package kettlebell.weather.validator;

import lombok.Getter;
import lombok.Value;

//@Value(staticConstructor = "of")
@Getter
public class Error {
	private  String code;
	private  String message;

	private Error(){}
	private Error (String code, String message){
		this.code = code;
		this.message = message;
	}
	private Error (String message){
		this.message = message;
	}


	public static Error of(String code, String message){
		return new Error(code,message);
	}
	public static Error of(String message){
		return new Error(message);
	}
}
