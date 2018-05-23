package almundo.com.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Assertions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import almundo.com.backend.business.Dispatcher;
import almundo.com.backend.business.Utils;
import almundo.com.backend.config.AppConfig;
import almundo.com.backend.exception.ServiceNotAvailableException;
import almundo.com.backend.model.AttendedSizes;
import almundo.com.backend.model.Call;
import almundo.com.backend.model.Director;
import almundo.com.backend.model.Operator;
import almundo.com.backend.model.Response;
import almundo.com.backend.model.Supervisor;
import almundo.com.backend.observable.ProcessCall;

public class DispatcherTest {

	ConfigurableApplicationContext context;
	Dispatcher dispatcher = null;
	private Logger logger;
	
	@BeforeEach
	void init() throws ServiceNotAvailableException {	
		System.setProperty("log4j.configurationFile",  "almundo/com/backend/config/log4j2.xml");
	    System.setProperty("logFilename", "logs/Main.log");
	    
	    logger = LogManager.getLogger(Logger.class.getName());
	    
		context = new AnnotationConfigApplicationContext(AppConfig.class);
		dispatcher = context.getBean(Dispatcher.class);
		//Agrego directores a la cola
		dispatcher.addEmployee(new Director("Director 1"));
		
		//Agrego supervisores a la cola
		for(int i =1; i <= 2; i++)
			dispatcher.addEmployee(new Supervisor("Supervisor " + i));
		
		//Agrego operadores a la cola
		for(int i =1; i <= 4; i++)
			dispatcher.addEmployee(new Operator("Operador " + i)); 	
	}
	
	@DisplayName("Test of 10 calls Concurrents")	
	@Test
	public void TenConcurrentsCalls() {
		//El orden de prioridades es Operador, Supervisor y Director, respectivamente. 
		//De no encontrar alguien que atienda el llamado, pasa a estar en la cola espera.
		//Luego queda a la escucha de que se libere alguien, como la duracion de la llamada es Random, entre 5 y 10 segundos
		//es probable que deje de respetar dicho orden (porque se puede dar que se libere primero el Director, por ejemplo.) hasta que se liberen mas de uno al mismo tiempo, en ese caso respeta el orden principal.
		
		//Se corren los Tests
		
		logger.info("Inicia el Test de 10 Llamados concurrentes!!!");
		
		//Creo el pool de hilos, hasta 100 Concurrentes 
    	ExecutorService executor = Executors.newFixedThreadPool(100);
    	List<Future<Response>> responses = new ArrayList<Future<Response>>();
    	
    	for(int i = 1; i <= 10; i++)
	    	responses.add(executor.submit(new ProcessCall(dispatcher, new Call(UUID.randomUUID(), "Incomming Call " + i))));
    	
    	executor.shutdown();
    	
    	//Finalizo el envio de Threads.
		while(!executor.isTerminated()) { }
		
		logger.info("Finaliza el Test de 10 Llamados concurrentes!!!");
    	
		//Valido los Response
    	AttendedSizes attendedSizes = Utils.responseSizes(responses);
    	
    	long employeesCount = dispatcher.getOperators().size() + dispatcher.getSupervisors().size() + dispatcher.getDirectors().size();
    	//Estado inicial de las 10 llamadas concurrentes
    	Assertions.assertEquals(employeesCount, attendedSizes.getAttendedSize());
    	Assertions.assertEquals(responses.size() - employeesCount, attendedSizes.getOnHoldSize());
    	//Los Supervisores debieran ser los unicos en atender
    	
    	//Debieran haber atendido 4 operadores como minimo 	
    	Assertions.assertTrue(attendedSizes.getOperatorSize() >= 4);
    	//Debieran haber atendido 4 operadores como minimo 	
    	Assertions.assertTrue(attendedSizes.getSupervisorSize() >= 2);
    	//Debieran haber atendido 4 operadores como minimo 	
    	Assertions.assertTrue(attendedSizes.getDirectorSize() >= 1);
    	//Valido que no haya Errores ni Interrupciones
    	Assertions.assertEquals(0, attendedSizes.getErrorSize());
    	Assertions.assertEquals(0, attendedSizes.getInterruptedSize());
	}
	
	@AfterEach
	void dispose() {
		dispatcher = null;
	}
}
