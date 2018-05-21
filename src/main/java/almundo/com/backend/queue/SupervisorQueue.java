package almundo.com.backend.queue;

import java.util.concurrent.LinkedBlockingQueue;

import almundo.com.backend.model.Director;
import almundo.com.backend.model.Employee;
import almundo.com.backend.model.Supervisor;

public class SupervisorQueue extends LinkedBlockingQueue<Employee> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LinkedBlockingQueue<Supervisor> supervisors;
	private LinkedBlockingQueue<Employee> directors;
	
	public SupervisorQueue(LinkedBlockingQueue<Supervisor> supervisors, DirectorQueue directors) {
		this.supervisors = supervisors;
		this.directors = directors;
	}
	
	@Override
	public Employee take() throws InterruptedException {
		if(supervisors.isEmpty())
			return directors.take();
		
		return supervisors.take();
	}
}
