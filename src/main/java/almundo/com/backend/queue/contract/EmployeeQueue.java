package almundo.com.backend.queue.contract;

import java.util.concurrent.LinkedBlockingQueue;

import almundo.com.backend.model.Employee;

public class EmployeeQueue extends LinkedBlockingQueue<Employee>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LinkedBlockingQueue<Employee> employees;
	
	public EmployeeQueue(LinkedBlockingQueue<Employee> employees) {
		this.employees = employees;
	}
	
	public Employee take() throws InterruptedException {
		return employees.take();
	}

}
