package almundo.com.backend.queue;

import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import almundo.com.backend.model.Employee;

@Component("operatorQueue")
public class OperatorQueue extends LinkedBlockingQueue<Employee> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	@Qualifier(value="supervisorQueue")
	private SupervisorQueue supervisorQueue;
	
	public OperatorQueue(SupervisorQueue supervisorQueue) {
		super();
		this.supervisorQueue = supervisorQueue;
	}		
	
	@Override
	public Employee take() throws InterruptedException {
		if(isEmpty())
			return supervisorQueue.take();
		
		return super.take();			
	}
}
