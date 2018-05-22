package almundo.com.backend.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {

	public Properties properties = new Properties();
	
	public Config() {
		try {			
			Class<Config> clazz = Config.class;
			InputStream inputStream = clazz.getResourceAsStream("config.properties");
			properties.load(inputStream);
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
