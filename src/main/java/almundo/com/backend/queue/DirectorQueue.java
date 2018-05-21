package almundo.com.backend.queue;

import java.util.concurrent.LinkedBlockingQueue;

import almundo.com.backend.exception.WithoutEmployeeException;
import almundo.com.backend.model.Employee;

public class DirectorQueue extends LinkedBlockingQueue<Employee> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DirectorQueue() {
		
	}

	@Override
	public Employee take() throws InterruptedException {
		if(isEmpty())
			throw new WithoutEmployeeException("Todos nuestros operadores se encuentran ocupados, aguarde en linea, que sera atendido a la brevedad. Muchas gracias.");
		
		return super.take();
	}

}