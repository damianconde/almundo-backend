package almundo.com.backend.business;

import java.util.concurrent.LinkedBlockingQueue;

import almundo.com.backend.contract.IDispatcher;
import almundo.com.backend.model.Call;
import almundo.com.backend.model.Employee;
import almundo.com.backend.queue.OperatorQueue;

public class Dispatcher implements IDispatcher{
	LinkedBlockingQueue<Employee> employees;
	
	public Dispatcher(OperatorQueue operators) {	
		this.employees = operators;
	}
	
	public void dispatchCall(Call call) throws InterruptedException {
		call.setAttended(employees.take());		
		call.log();
	}
}
