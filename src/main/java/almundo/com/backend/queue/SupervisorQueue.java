package almundo.com.backend.queue;

import java.util.concurrent.LinkedBlockingQueue;

import almundo.com.backend.model.Employee;

public class SupervisorQueue extends LinkedBlockingQueue<Employee> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DirectorQueue directorQueue;
	
	public SupervisorQueue(DirectorQueue directorQueue) {
		this.directorQueue = directorQueue;
	}
	
	@Override
	public Employee take() throws InterruptedException {
		if(isEmpty())
			return directorQueue.take();
		
		return super.take();
	}
}
