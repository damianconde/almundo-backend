package almundo.com.backend.business;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;

import almundo.com.backend.contract.IDispatcher;
import almundo.com.backend.model.Call;
import almundo.com.backend.model.Employee;

public class Dispatcher implements IDispatcher{
	private BlockingQueue<Employee> employeeQueue;
	public BlockingQueue<Call> callQueue;
	
	public Dispatcher(Collection<Employee> employees) {
		employeeQueue.addAll(employees);
	}
	
	public void dispatchCall(Call call) throws InterruptedException {
		callQueue.put(call);
	}
}
