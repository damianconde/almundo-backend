package almundo.com.backend.queue;

import java.util.concurrent.LinkedBlockingQueue;

import almundo.com.backend.model.Employee;

public class OperatorQueue extends LinkedBlockingQueue<Employee> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SupervisorQueue supervisorQueue;
	
	public OperatorQueue(SupervisorQueue supervisorQueue) {
		this.supervisorQueue = supervisorQueue;
	}		
	
	@Override
	public Employee take() throws InterruptedException {
		if(isEmpty())
			return supervisorQueue.take();
		
		return super.take();			
	}
}
