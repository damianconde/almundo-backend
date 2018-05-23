package almundo.com.backend.queue;

import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import almundo.com.backend.model.Employee;

@Component("supervisorQueue")
public class SupervisorQueue extends LinkedBlockingQueue<Employee> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	@Qualifier(value="directorQueue")
	private DirectorQueue directorQueue;
	
	public SupervisorQueue(DirectorQueue directorQueue) {
		super();
		this.directorQueue = directorQueue;
	}
	
	@Override
	public Employee take() throws InterruptedException {
		if(isEmpty())
			return directorQueue.take();
		
		return super.take();
	}
}
