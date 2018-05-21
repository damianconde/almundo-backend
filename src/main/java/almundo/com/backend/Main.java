package almundo.com.backend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;

import almundo.com.backend.business.Dispatcher;
import almundo.com.backend.contract.IDispatcher;
import almundo.com.backend.model.Call;
import almundo.com.backend.model.Director;
import almundo.com.backend.model.Employee;
import almundo.com.backend.model.Operator;
import almundo.com.backend.model.Supervisor;
import almundo.com.backend.queue.DirectorQueue;
import almundo.com.backend.queue.OperatorQueue;
import almundo.com.backend.queue.SupervisorQueue;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		LinkedBlockingQueue<Director> directorsQ = new LinkedBlockingQueue<Director>();
		directorsQ.add(new Director("Damian Conde"));
		DirectorQueue directors = new DirectorQueue(directorsQ);
		LinkedBlockingQueue<Supervisor> supervisorsQ = new LinkedBlockingQueue<Supervisor>();
		//supervisorsQ.add(new Supervisor("Ariana Conde"));
		SupervisorQueue supervisors = new SupervisorQueue(supervisorsQ, directors);
		LinkedBlockingQueue<Operator> operatorsQ = new LinkedBlockingQueue<Operator>();
		//operatorsQ.add(new Operator("Sabrina Dana"));
		OperatorQueue operators = new OperatorQueue(operatorsQ, supervisors);
    	
    	IDispatcher dispatcher = new Dispatcher(operators);
    	
    	dispatcher.dispatchCall(new Call("Yooooo"));
    	dispatcher.dispatchCall(new Call("Seeeee"));
	}
}
