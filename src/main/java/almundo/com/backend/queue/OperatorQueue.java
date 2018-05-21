package almundo.com.backend.queue;

import java.util.concurrent.LinkedBlockingQueue;

import almundo.com.backend.model.Employee;
import almundo.com.backend.model.Operator;

public class OperatorQueue extends LinkedBlockingQueue<Employee> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public LinkedBlockingQueue<Operator> operators;
	public LinkedBlockingQueue<Employee> supervisors;
	
	public OperatorQueue(LinkedBlockingQueue<Operator> operators, SupervisorQueue supervisors) {
		this.operators = operators;
		this.supervisors = supervisors;
	}
	
	@Override
	public Employee take() throws InterruptedException {
		if(operators.isEmpty())
			return supervisors.take();
		
		return operators.take();			
	}
}
