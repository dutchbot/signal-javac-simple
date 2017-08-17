package main;
import java.io.File;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Config {
	
	static final String config = "config_2.json";

	public static String getPhoneNumber() {
		try {
			ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
			Map<String, Object> json = mapper.readValue(new File(config), Map.class);

			return (String) json.get("phoneNumber");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getToPhoneNumber() {
		try {
			ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
			Map<String, Object> json = mapper.readValue(new File(config), Map.class);

			return (String) json.get("sendToPhoneNumber");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
