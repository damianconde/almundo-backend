package almundo.com.backend;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class DispatcherTest {
    
    @Test
    public void orderEmployees()
    {
    	DirectorQueue directorQueue = new DirectorQueue();
    	directorQueue.add(new Director("Director Matutino 1"));
    	
    	SupervisorQueue supervisorQueue = new SupervisorQueue(directorQueue);
    	supervisorQueue.add(new Supervisor("Supervisor Matutino 1"));
    	
        OperatorQueue operatorQueue = new OperatorQueue(supervisorQueue);
        Operator operator = new Operator("Operador Matutino 1");
    	operatorQueue.add(operator);
    	
    	Dispatcher dispatcher = new Dispatcher(operatorQueue, supervisorQueue, directorQueue);
    	
    	ExecutorService executor = Executors.newFixedThreadPool(3);
    	
    	executor.execute(new ProcessCall(dispatcher, new Call(UUID.randomUUID(), "Test 1")));
    	 //= dispatcher.dispatchCall();
    	 
    	Response response1 = dispatcher.dispatchCall(new Call(UUID.randomUUID(), "Test 2"));
    	Response response3 = dispatcher.dispatchCall(new Call(UUID.randomUUID(), "Test 3"));
    	
        Assert.assertTrue( response1.getStatus() == Status.Attended );
        Assert.assertTrue( response1.getCall().getAttended().equals(operator));
    }
}
