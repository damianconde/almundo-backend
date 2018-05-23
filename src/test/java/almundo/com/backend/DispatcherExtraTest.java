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
import almundo.com.backend.config.Config;
import almundo.com.backend.model.AttendedSizes;
import almundo.com.backend.model.Call;
import almundo.com.backend.model.Director;
import almundo.com.backend.model.Operator;
import almundo.com.backend.model.Response;
import almundo.com.backend.model.Supervisor;
import almundo.com.backend.observable.ProcessCall;

public class DispatcherExtraTest {

	ConfigurableApplicationContext context;
	Dispatcher dispatcher = null;
	private Logger logger = null;
	
	@BeforeEach
	void init() {
		System.setProperty("log4j.configurationFile",  "almundo/com/backend/config/log4j2.xml");
	    System.setProperty("logFilename", "logs/Main.log");
	    
	    logger = LogManager.getLogger(Logger.class.getName());
	    
		context = new AnnotationConfigApplicationContext(AppConfig.class);
		dispatcher = context.getBean(Dispatcher.class);
		
		//Agrego directores a la cola
		for(int i =1; i <= 5; i++)
			dispatcher.addEmployee(new Director("Director " + i));
		
		//Agrego supervisores a la cola
		for(int i =1; i <= 5; i++)
			dispatcher.addEmployee(new Supervisor("Supervisor " + i));
		
		//Agrego operadores a la cola
		for(int i =1; i <= 5; i++)
			dispatcher.addEmployee(new Operator("Operador " + i));    	
	}
	
	@DisplayName("Test of Config setting correctly")
	@Test
	public void configTest() {
		Config config = new Config();
		List<String> propertiesName = new ArrayList<>();
		propertiesName.add("TIME_CALLED_MIN");
		propertiesName.add("TIME_CALLED_MAX");
		
		propertiesName.forEach(prop -> Assertions.assertFalse(config.properties.getProperty(prop).isEmpty()));
	}
	
	@DisplayName("Test of 5 calls Concurrents without employees")
	@Test
	public void fiveConcurrentsCallsWithoutEmployees() {
		//La idea de este Test uniario es de enviar llamados sin que haya empleados en la empresa, es decir esta cerrada.
		dispatcher.setDirectors(null);
		dispatcher.setSupervisors(null);
		dispatcher.setOperators(null);
		
		//Se corren los Tests
		
		logger.info("Inicia el Test de 5 Llamados concurrentes sin empleados!!!");
		
		//Creo el pool de hilos, hasta 5 Concurrentes 
    	ExecutorService executor = Executors.newFixedThreadPool(5);
    	List<Future<Response>> responses = new ArrayList<Future<Response>>();
    	
    	for(int i = 1; i <= 5; i++)
	    	responses.add(executor.submit(new ProcessCall(dispatcher, new Call(UUID.randomUUID(), "Incomming Call " + i))));
    	
    	executor.shutdown();
    	
    	//Finalizo el envio de Threads.
		while(!executor.isTerminated()) { }
		
		logger.info("Finaliza el Test de 5 Llamados concurrentes sin empleados!!!");
    	
		//Valido los Response    	    	
    	AttendedSizes attendedSizes = Utils.responseSizes(responses);    	
    	
    	//Estado inicial de las 5 llamadas concurrentes
    	Assertions.assertEquals(0, attendedSizes.getErrorSize()); 
    	Assertions.assertEquals(0, attendedSizes.getAttendedSize());
    	Assertions.assertEquals(0, attendedSizes.getOnHoldSize());
    	Assertions.assertEquals(0, attendedSizes.getInterruptedSize());
    	Assertions.assertEquals(0, attendedSizes.getSuccessSize());
    	Assertions.assertEquals(5, attendedSizes.getWithoutServiceSize());
	}	
	
	@DisplayName("Test of 5 calls Concurrents and lower number of calls to available employees")	
	@Test
	public void fiveConcurrentsCallsFullConstructor() {
		//La idea de este Test uniario es de enviar una cantidad inferior de llamados a la cantidad de empleados disponibles. 
		//No debiera existir ningun llamado en espera
		
		//Se corren los Tests
		
		System.out.println("Inicia el Test de 5 Llamados concurrentes con empleados de mas!!!");
		
		//Creo el pool de hilos, hasta 5 Concurrentes 
    	ExecutorService executor = Executors.newFixedThreadPool(5);
    	List<Future<Response>> responses = new ArrayList<Future<Response>>();
    	
    	for(int i = 1; i <= 5; i++)
	    	responses.add(executor.submit(new ProcessCall(dispatcher, new Call(UUID.randomUUID(), "Incomming Call " + i))));
    	
    	executor.shutdown();
    	
    	//Finalizo el envio de Threads.
		while(!executor.isTerminated()) { }
		
		System.out.println("Finaliza el Test de 5 Llamados concurrentes con empleados de mas!!!");
    	
		//Valido los Response    	    	
    	AttendedSizes attendedSizes = Utils.responseSizes(responses);    	
    	
    	//Estado inicial de las 5 llamadas concurrentes
    	Assertions.assertEquals(5, attendedSizes.getAttendedSize());
    	Assertions.assertEquals(0, attendedSizes.getOnHoldSize());  
    	//Los Supervisores debieran ser los unicos en atender
    	Assertions.assertEquals(5, attendedSizes.getOperatorSize());
    	//No debiera haber atendido ningun operador y ningun director, 
    	//ya que con la cantidad de supervisores bastaria para suplir la totalidad de llamados entrantes.
    	Assertions.assertEquals(0, attendedSizes.getDirectorSize());
    	Assertions.assertEquals(0, attendedSizes.getSupervisorSize());    	
    	//Valido que no haya Errores ni Interrupciones
    	Assertions.assertEquals(0, attendedSizes.getErrorSize());
    	Assertions.assertEquals(0, attendedSizes.getInterruptedSize());
	}	
	
	@DisplayName("Test of 100 calls Concurrents")	
	@Test	
	public void hundredConcurrentsCalls() {
		//El orden de prioridades es Operador, Supervisor y Director, respectivamente. 
		//De no encontrar alguien que atienda el llamado, pasa a estar en la cola espera.
		//Luego queda a la escucha de que se libere alguien, como la duracion de la llamada es Random, entre 5 y 10 segundos
		//es probable que deje de respetar dicho orden (porque se puede dar que se libere primero el Director, por ejemplo.) hasta que se liberen mas de uno al mismo tiempo, en ese caso respeta el orden principal.
		
		//Se corren los Tests
		
		System.out.println("Inicia el Test de 100 Llamados concurrentes!!!");
		
		//Creo el pool de hilos, hasta 100 Concurrentes 
    	ExecutorService executor = Executors.newFixedThreadPool(100);
    	List<Future<Response>> responses = new ArrayList<Future<Response>>();
    	
    	for(int i = 1; i <= 100; i++)
	    	responses.add(executor.submit(new ProcessCall(dispatcher, new Call(UUID.randomUUID(), "Incomming Call " + i))));
    	
    	executor.shutdown();
    	
    	//Finalizo el envio de Threads.
		while(!executor.isTerminated()) { }
		
		System.out.println("Finaliza el Test de 100 Llamados concurrentes!!!");
    	
		//Valido los Response    	    	
    	AttendedSizes attendedSizes = Utils.responseSizes(responses);
    	
    	long employeesCount = dispatcher.getOperators().size() + dispatcher.getSupervisors().size() + dispatcher.getDirectors().size();
    	//Estado inicial de las 100 llamadas concurrentes
    	Assertions.assertEquals(employeesCount, attendedSizes.getAttendedSize());
    	Assertions.assertEquals(responses.size() - employeesCount, attendedSizes.getOnHoldSize());   
    	//Valido que no haya Errores ni Interrupciones
    	Assertions.assertEquals(0, attendedSizes.getErrorSize());
    	Assertions.assertEquals(0, attendedSizes.getInterruptedSize());
	}
	
	@DisplayName("Test of 5 calls Concurrents without operators.")	
	@Test
	public void fiveConcurrentsCallsWithoutOperators() {
		//La idea de este Test uniario es de enviar 5 llamados teniendo solo supervisores y directores
		
		dispatcher.setOperators(null);
		
		//Se corren los Tests
		
		System.out.println("Inicia el Test de 5 Llamados concurrentes sin operadores!!!");
		
		//Creo el pool de hilos, hasta 100 Concurrentes 
    	ExecutorService executor = Executors.newFixedThreadPool(100);
    	List<Future<Response>> responses = new ArrayList<Future<Response>>();
    	
    	for(int i = 1; i <= 5; i++)
	    	responses.add(executor.submit(new ProcessCall(dispatcher, new Call(UUID.randomUUID(), "Incomming Call " + i))));
    	
    	executor.shutdown();
    	
    	//Finalizo el envio de Threads.
		while(!executor.isTerminated()) { }
		
		System.out.println("Finaliza el Test de 5 Llamados concurrentes sin operadores!!!");
    	
		//Valido los Response    	    	
    	AttendedSizes attendedSizes = Utils.responseSizes(responses);
    	
    	//Estado inicial de las 5 llamadas concurrentes
    	Assertions.assertEquals(5, attendedSizes.getAttendedSize());
    	Assertions.assertEquals(0, attendedSizes.getOnHoldSize());  
    	//Los Supervisores debieran ser los unicos en atender
    	Assertions.assertEquals(5, attendedSizes.getSupervisorSize());
    	//No debiera haber atendido ningun operador y ningun director, 
    	//ya que con la cantidad de supervisores bastaria para suplir la totalidad de llamados entrantes.
    	Assertions.assertEquals(0, attendedSizes.getDirectorSize());
    	Assertions.assertEquals(0, attendedSizes.getOperatorSize());    	
    	//Valido que no haya Errores ni Interrupciones
    	Assertions.assertEquals(0, attendedSizes.getErrorSize());
    	Assertions.assertEquals(0, attendedSizes.getInterruptedSize());
	}
	
	@DisplayName("Test of 5 calls Concurrents without supervisors.")	
	@Test
	public void fiveConcurrentsCallsWithoutSupervisors() {
		//La idea de este Test uniario es de enviar 5 llamados teniendo solo operadores y directores
		
		dispatcher.setSupervisors(null);
		//Se corren los Tests
		
		System.out.println("Inicia el Test de 5 Llamados concurrentes sin supervisores!!!");
		
		//Creo el pool de hilos, hasta 100 Concurrentes 
    	ExecutorService executor = Executors.newFixedThreadPool(100);
    	List<Future<Response>> responses = new ArrayList<Future<Response>>();
    	
    	for(int i = 1; i <= 5; i++)
	    	responses.add(executor.submit(new ProcessCall(dispatcher, new Call(UUID.randomUUID(), "Incomming Call " + i))));
    	
    	executor.shutdown();
    	
    	//Finalizo el envio de Threads.
		while(!executor.isTerminated()) { }
		
		System.out.println("Finaliza el Test de 5 Llamados concurrentes sin supervisores!!!");
    	
		//Valido los Response    	    	
    	AttendedSizes attendedSizes = Utils.responseSizes(responses);
    	
    	//Estado inicial de las 5 llamadas concurrentes
    	Assertions.assertEquals(5, attendedSizes.getAttendedSize());
    	Assertions.assertEquals(0, attendedSizes.getOnHoldSize());  
    	//Los Operadores debieran ser los unicos en atender
    	Assertions.assertEquals(5, attendedSizes.getOperatorSize());
    	//No debiera haber atendido ningun director y ningun supervisor, 
    	//ya que con la cantidad de operadores bastaria para suplir la totalidad de llamados entrantes.
    	Assertions.assertEquals(0, attendedSizes.getDirectorSize());
    	Assertions.assertEquals(0, attendedSizes.getSupervisorSize());
    	//Valido que no haya Errores ni Interrupciones
    	Assertions.assertEquals(0, attendedSizes.getErrorSize());
    	Assertions.assertEquals(0, attendedSizes.getInterruptedSize());
	}
	
	@DisplayName("Test of 5 calls Concurrents without directors.")	
	@Test
	public void fiveConcurrentsCallsWithoutDirectors() {
		//La idea de este Test uniario es de enviar 5 llamados teniendo solo operadores y supervisores
		
		dispatcher.setDirectors(null);
		
		//Se corren los Tests
		
		System.out.println("Inicia el Test de 5 Llamados concurrentes sin directores!!!");
		
		//Creo el pool de hilos, hasta 100 Concurrentes 
    	ExecutorService executor = Executors.newFixedThreadPool(100);
    	List<Future<Response>> responses = new ArrayList<Future<Response>>();
    	
    	for(int i = 1; i <= 5; i++)
	    	responses.add(executor.submit(new ProcessCall(dispatcher, new Call(UUID.randomUUID(), "Incomming Call " + i))));
    	
    	executor.shutdown();
    	
    	//Finalizo el envio de Threads.
		while(!executor.isTerminated()) { }
		
		System.out.println("Finaliza el Test de 5 Llamados concurrentes sin directores!!!");
    	
		//Valido los Response
    	AttendedSizes attendedSizes = Utils.responseSizes(responses);
    	
    	//Estado inicial de las 5 llamadas concurrentes
     	Assertions.assertEquals(5, attendedSizes.getAttendedSize());
    	Assertions.assertEquals(0, attendedSizes.getOnHoldSize());   
    	//Los Operadores debieran ser los unicos en atender
    	Assertions.assertEquals(5, attendedSizes.getOperatorSize());
    	//No debiera haber atendido ningun director y ningun supervisor, 
    	//ya que con la cantidad de operadores bastaria para suplir la totalidad de llamados entrantes.
    	Assertions.assertEquals(0, attendedSizes.getDirectorSize());
    	Assertions.assertEquals(0, attendedSizes.getSupervisorSize());
    	//Valido que no haya Errores ni Interrupciones
    	Assertions.assertEquals(0, attendedSizes.getErrorSize());
    	Assertions.assertEquals(0, attendedSizes.getInterruptedSize());
	}
	
	@AfterEach
	void dispose() {
		dispatcher = null;
	}
}
