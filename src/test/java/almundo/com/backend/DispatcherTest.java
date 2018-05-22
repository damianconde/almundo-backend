package almundo.com.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import almundo.com.backend.business.Dispatcher;
import almundo.com.backend.business.ProcessCall;
import almundo.com.backend.model.Call;
import almundo.com.backend.model.Director;
import almundo.com.backend.model.Operator;
import almundo.com.backend.model.Response;
import almundo.com.backend.model.Response.Status;
import almundo.com.backend.model.Supervisor;
import almundo.com.backend.queue.DirectorQueue;
import almundo.com.backend.queue.OperatorQueue;
import almundo.com.backend.queue.SupervisorQueue;
import junit.framework.Assert;
import org.junit.Test;

public class DispatcherTest {

	@Test
	public void TenConcurrentsCalls() {
		//El orden de prioridades es Operador, Supervisor y Director, respectivamente. 
		//De no encontrar alguien que atienda el llamado, pasa a estar en la cola espera.
		//Luego queda a la escucha de que se libere alguien, como la duracion de la llamada es Random, entre 3 y 5 segundos
		//es probable que deje de respetar dicho orden (porque se puede dar que se libere primero el Director) hasta que se liberen mas de uno al mismo tiempo, en ese caso respeta el orden principal.
		
		//Se corren los Tests
		
		System.out.println("Inicia el Test de 10 Llamados concurrentes!!!");
		//Creo la cola de Directores
		DirectorQueue directorQueue = new DirectorQueue();
		directorQueue.add(new Director("Director 1"));
		
		//Creo la cola de Supervisores
		SupervisorQueue supervisorQueue = new SupervisorQueue(directorQueue);
		supervisorQueue.add(new Supervisor("Supervisor 1"));
		supervisorQueue.add(new Supervisor("Supervisor 2"));
		supervisorQueue.add(new Supervisor("Supervisor 3"));
		supervisorQueue.add(new Supervisor("Supervisor 4"));	
		
		//Creo la cola de Operadores
		OperatorQueue operatorQueue = new OperatorQueue(supervisorQueue);
		operatorQueue.add(new Operator("Operador 1"));
		operatorQueue.add(new Operator("Operador 2"));
		
		//Creo el pool de hilos, hasta 100 Concurrentes 
    	ExecutorService executor = Executors.newFixedThreadPool(100);
    	
    	//Instancio el Dispatcher con las colas de los empleados disponibles.
    	final Dispatcher dispatcher = new Dispatcher(operatorQueue, supervisorQueue, directorQueue);
    	
    	List<Future<Response>> responses = new ArrayList<Future<Response>>();
    	
    	for(int i = 1; i <= 10; i++)
	    	responses.add(executor.submit(new ProcessCall(dispatcher, new Call(UUID.randomUUID(), "Incomming Call " + i))));
    	
    	executor.shutdown();
    	
    	//Finalizo el envio de Threads.
		while(!executor.isTerminated()) { }
		
		System.out.println("Finaliza el Test de 10 Llamados concurrentes!!!");
    	
    	long attendedCount = responses
			    		.stream()
			    		.filter(element -> {
							try {
								return element.get().getStatus() == Status.Attended;
							} catch (InterruptedException | ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return false;
						})
			    		.count();
    	
    	long onHoldCount = responses
	    		.stream()
	    		.filter(element -> {
					try {
						return element.get().getStatus() == Status.OnHold;
					} catch (InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return false;
				})
	    		.count();
    	
    	int employeesCount = dispatcher.getOperators().size() + dispatcher.getSupervisors().size() + dispatcher.getDirectors().size();
    	//Estado inicial de las 10 llamadas concurrentes
    	Assert.assertEquals(employeesCount, attendedCount);
    	Assert.assertEquals(responses.size() - employeesCount, onHoldCount);   
	}
}
