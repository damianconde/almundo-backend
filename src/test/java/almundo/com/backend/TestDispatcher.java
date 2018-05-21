package almundo.com.backend;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import junit.framework.Assert;
import org.junit.Test;

import almundo.com.backend.business.Dispatcher;
import almundo.com.backend.model.Call;
import almundo.com.backend.model.Director;
import almundo.com.backend.model.Employee;
import almundo.com.backend.model.Operator;
import almundo.com.backend.model.Response;
import almundo.com.backend.model.Supervisor;
import almundo.com.backend.model.Response.Status;
import almundo.com.backend.queue.DirectorQueue;
import almundo.com.backend.queue.OperatorQueue;
import almundo.com.backend.queue.SupervisorQueue;

public class TestDispatcher {

	@Test
	public void orderTakenCalls() {

		DirectorQueue directorQueue = new DirectorQueue();
		Director director = new Director("Director Matutino 1");
    	directorQueue.add(director);
    	
    	SupervisorQueue supervisorQueue = new SupervisorQueue(directorQueue);
    	Supervisor supervisor = new Supervisor("Supervisor Matutino 1");
    	supervisorQueue.add(supervisor);
    	
        OperatorQueue operatorQueue = new OperatorQueue(supervisorQueue);
        Operator operator = new Operator("Operador Matutino 1");
    	operatorQueue.add(operator);
    	
    	final Dispatcher dispatcher = new Dispatcher(operatorQueue, supervisorQueue, directorQueue);
    	
    	ExecutorService executor = Executors.newFixedThreadPool(10);
    	
    	Future<Response> call1 = executor.submit(new Callable<Response>() {
            public Response call() throws Exception {
            	return dispatcher.dispatchCall(new Call(UUID.randomUUID(), "Test 1"));
            }
        });
    	
    	Future<Response> call2 = executor.submit(new Callable<Response>() {
            public Response call() throws Exception {
            	return dispatcher.dispatchCall(new Call(UUID.randomUUID(), "Test 2"));
            }
        });
    	
    	Future<Response> call3 = executor.submit(new Callable<Response>() {
            public Response call() throws Exception {
            	return dispatcher.dispatchCall(new Call(UUID.randomUUID(), "Test 3"));
            }
        });
    	
    	Future<Response> call4 = executor.submit(new Callable<Response>() {
            public Response call() throws Exception {
            	return dispatcher.dispatchCall(new Call(UUID.randomUUID(), "Test 4"));
            }
        });
    	
    	Future<Response> call5 = executor.submit(new Callable<Response>() {
            public Response call() throws Exception {
            	return dispatcher.dispatchCall(new Call(UUID.randomUUID(), "Test 5"));
            }
        });
    	
    	Future<Response> call6 = executor.submit(new Callable<Response>() {
            public Response call() throws Exception {
            	return dispatcher.dispatchCall(new Call(UUID.randomUUID(), "Test 6"));
            }
        });
    	
    	Future<Response> call7 = executor.submit(new Callable<Response>() {
            public Response call() throws Exception {
            	return dispatcher.dispatchCall(new Call(UUID.randomUUID(), "Test 7"));
            }
        });
    	
    	Future<Response> call8 = executor.submit(new Callable<Response>() {
            public Response call() throws Exception {
            	return dispatcher.dispatchCall(new Call(UUID.randomUUID(), "Test 8"));
            }
        });
    	
    	Future<Response> call9 = executor.submit(new Callable<Response>() {
            public Response call() throws Exception {
            	return dispatcher.dispatchCall(new Call(UUID.randomUUID(), "Test 9"));
            }
        });
    	
    	Future<Response> call10 = executor.submit(new Callable<Response>() {
            public Response call() throws Exception {
            	return dispatcher.dispatchCall(new Call(UUID.randomUUID(), "Test 10"));
            }
        });
    	        
    	executor.shutdown();
    	
    	while(executor.isTerminated()) {};
    	
        try {        	
        	Employee attended1 = call1.get().getCall().getAttended();
        	System.out.println(attended1);
			Assert.assertEquals(true, ( call1.get().getStatus() == Status.Attended ));
			Assert.assertTrue( attended1.equals(operator));
			
			Employee attended2 = call2.get().getCall().getAttended();
        	System.out.println(attended2);
			Assert.assertEquals(true, ( call2.get().getStatus() == Status.Attended ));
			Assert.assertTrue( attended2.equals(supervisor));
			
			Employee attended3 = call3.get().getCall().getAttended();			
        	System.out.println(attended3);
			Assert.assertEquals(true, ( call3.get().getStatus() == Status.Attended ));
			Assert.assertTrue( attended3.equals(director));
			
			Employee attended4 = call4.get().getCall().getAttended();			
        	System.out.println(attended4);
			Assert.assertEquals(true, ( call4.get().getStatus() == Status.Attended ));
			
			Employee attended5 = call5.get().getCall().getAttended();			
        	System.out.println(attended5);
			Assert.assertEquals(true, ( call5.get().getStatus() == Status.Attended ));
		} catch (Exception e) {
			Assert.fail();
		}     
	}
}
