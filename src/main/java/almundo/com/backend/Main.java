package almundo.com.backend;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import almundo.com.backend.business.Dispatcher;
import almundo.com.backend.config.AppConfig;
import almundo.com.backend.model.Call;
import almundo.com.backend.model.Response;

public class Main {

	private static Logger logger;
	
	public static void main(String[] args) throws InterruptedException {
		
		System.setProperty("log4j.configurationFile",  "almundo/com/backend/config/log4j2.xml");
	    System.setProperty("logFilename", "logs/Main.log");
	    
	    logger = LogManager.getLogger(Logger.class.getName());
	    
		ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		
		Dispatcher dispatcher = context.getBean(Dispatcher.class);
		
		dispatcher.setOperators(null);
		dispatcher.setSupervisors(null);
		dispatcher.setDirectors(null);
		
		Response response = dispatcher.dispatchCall(new Call(UUID.randomUUID(), "Call 1"));

		logger.info(response);
		
		context.close();
	}
}
