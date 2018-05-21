package almundo.com.backend;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import almundo.com.backend.business.Dispatcher;
import almundo.com.backend.business.ProcessCall;
import almundo.com.backend.business.WaitCallQueueObserver;
import almundo.com.backend.model.Call;
import almundo.com.backend.model.Director;
import almundo.com.backend.model.Operator;
import almundo.com.backend.model.Supervisor;
import almundo.com.backend.queue.DirectorQueue;
import almundo.com.backend.queue.OperatorQueue;
import almundo.com.backend.queue.SupervisorQueue;
import junit.framework.Assert;
import org.junit.Test;

public class DispatcherManyCalls {

	@Test
	public void manyOptions() throws InterruptedException {
		//El orden de prioridades es Operador, Supervisor y Director, respectivamente. 
		//De no encontrar alguien que atienda el llamado, pasa a estar en la cola espera.
		//Luego queda a la escucha de que se libere alguien, como la duracion de la llamada es Random, entre 3 y 5 segundos
		//es probable que deje de respetar dicho orden (porque se puede dar que se libere primero el Director) hasta que se liberen mas de uno al mismo tiempo, en ese caso respeta el orden principal.
		
		//Se corren los Tests
		/*System.out.println("Running tests!");

        JUnitCore engine = new JUnitCore();
        engine.addListener(new TextListener(System.out)); // required to print reports
        engine.run(DispatcherTest.class);
        
        System.out.println("Finish tests!");*/
		//Se emula dicho proceso para que ingrese un empleado mas tarde y en la siguiente etapa sea con cambio de turno 
		
		System.out.println("Inicia el turno Matutino!!!");
		//Creo la cola de Directores Matutinos
		DirectorQueue directorQueue = new DirectorQueue();
		directorQueue.add(new Director("Director Matutino 1"));
		
		//Creo la cola de Supervisores Matutinos
		SupervisorQueue supervisorQueue = new SupervisorQueue(directorQueue);
		supervisorQueue.add(new Supervisor("Supervisor Matutino 1"));
		supervisorQueue.add(new Supervisor("Supervisor Matutino 2"));
		supervisorQueue.add(new Supervisor("Supervisor Matutino 3"));
		supervisorQueue.add(new Supervisor("Supervisor Matutino 4"));	
		
		//Creo la cola de Operadores Matutinos
		OperatorQueue operatorQueue = new OperatorQueue(supervisorQueue);
		operatorQueue.add(new Operator("Operador Matutino 1"));
		operatorQueue.add(new Operator("Operador Matutino 2"));
		
		//Creo el pool de hilos, hasta 1000 Concurrentes 
    	ExecutorService executor = Executors.newFixedThreadPool(1000);
    	
    	//Instancio el Dispatcher con las colas de los empleados disponibles.
    	Dispatcher dispatcher = new Dispatcher(operatorQueue, supervisorQueue, directorQueue);
    	//Observer para los llamados en lista de espera
    	WaitCallQueueObserver to = new WaitCallQueueObserver(dispatcher);
    	dispatcher.addObserver(to);
    	
    	//Envio diez llamados en la primera instancia
    	for(int i = 1; i <= 10; i++)
    		executor.execute(new ProcessCall(dispatcher, new Call(UUID.randomUUID(), "Llamado Entrante " + i)));
    	
    	//Aguardo cinco segundos para lanzar la proxima camada de llamados
		executor.awaitTermination(15, TimeUnit.SECONDS);    	
		
		System.out.println("LLega tarde un empleado y se alista a la cola de Operadores!!!");
    	dispatcher.addEmployee(new Operator("Operador Demoron"));
    	
		for(int i = 11; i <= 100; i++)
    		executor.execute(new ProcessCall(dispatcher, new Call(UUID.randomUUID(), "Llamado Entrante " + i)));
		
		executor.awaitTermination(1, TimeUnit.MINUTES);   
		
		System.out.println("Comienza el turno Vespertino!!!");
		
		//Cambio de turno a Vespertino
		
		//Creo la cola de Directores Vespertino
		directorQueue = new DirectorQueue();
		directorQueue.add(new Director("Director Vespertino 1"));
		
		//Creo la cola de Supervisores Vespertino
		supervisorQueue = new SupervisorQueue(directorQueue);
		supervisorQueue.add(new Supervisor("Supervisor Vespertino 1"));
		supervisorQueue.add(new Supervisor("Supervisor Vespertino 2"));
		supervisorQueue.add(new Supervisor("Supervisor Vespertino 3"));
		supervisorQueue.add(new Supervisor("Supervisor Vespertino 4"));	
		
		//Creo la cola de Operadores Vespertino
		operatorQueue = new OperatorQueue(supervisorQueue);
		operatorQueue.add(new Operator("Operador Vespertino 1"));
		operatorQueue.add(new Operator("Operador Vespertino 2"));
		
		//Cambio a todos los empleados por el turno vespertino
		dispatcher.setOperators(operatorQueue);
		dispatcher.setSupervisors(supervisorQueue);
		dispatcher.setDirectors(directorQueue);
		
		executor.awaitTermination(30, TimeUnit.SECONDS);
		
		//Envio diez llamados en la primera instancia
    	for(int i = 101; i <= 110; i++)
    		executor.execute(new ProcessCall(dispatcher, new Call(UUID.randomUUID(), "Llamado Entrante " + i)));
    	
    	executor.awaitTermination(15, TimeUnit.SECONDS);
    	
		executor.execute(new ProcessCall(dispatcher, new Call(UUID.randomUUID(), "Ultimo Llamado Entrante")));
		
		System.out.println("Finaliza el turno Vespertino!!!");
		System.out.println("Queda a la escucha de proximos llamados...");
		
		//Finalizo el envio de Threads.
		executor.shutdown();		
	}
}
