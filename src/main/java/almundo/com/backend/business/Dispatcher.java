package almundo.com.backend.business;

import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import almundo.com.backend.config.Config;
import almundo.com.backend.contract.IDispatcher;
import almundo.com.backend.exception.ServiceNotAvailableException;
import almundo.com.backend.exception.WithoutEmployeeException;
import almundo.com.backend.model.Call;
import almundo.com.backend.model.Director;
import almundo.com.backend.model.Employee;
import almundo.com.backend.model.Operator;
import almundo.com.backend.model.Response;
import almundo.com.backend.model.Response.Status;
import almundo.com.backend.model.Supervisor;
import almundo.com.backend.observer.WaitCallQueueObserver;
import almundo.com.backend.queue.DirectorQueue;
import almundo.com.backend.queue.OperatorQueue;
import almundo.com.backend.queue.SupervisorQueue;

@Component("dispatcher")
public class Dispatcher extends Observable implements IDispatcher{
	@Autowired
	@Qualifier(value="operatorQueue")
	private LinkedBlockingQueue<Employee> operators;
	@Autowired
	@Qualifier(value="supervisorQueue")
	private LinkedBlockingQueue<Employee> supervisors;
	@Autowired
	@Qualifier(value="directorQueue")
	private LinkedBlockingQueue<Employee> directors;
	private Queue<Call> waitCallQueue;
	private AtomicInteger priority;
	private WaitCallQueueObserver observer;
	private Config config;
	
	public Dispatcher(OperatorQueue operators, SupervisorQueue supervisors, DirectorQueue directors) throws ServiceNotAvailableException {	
		initQueuesIfNull(true);
		waitCallQueue = new ConcurrentLinkedQueue<Call>();
		priority = new AtomicInteger(0);
		observer = new WaitCallQueueObserver(this);
    	addObserver(observer);    
    	config = new Config();
	}
	
	public void addWaitQueue(Call call) {
		if(call == null) return;
		waitCallQueue.add(call);
	}
	
	public Call getWaitCall() throws NoSuchElementException {
		return waitCallQueue.remove();		
	}
	
	public AtomicInteger getPriority()
	{
		return priority;
	}
	
	public LinkedBlockingQueue<Employee> getOperators()
	{
		return operators;
	}
	
	public void setOperators(LinkedBlockingQueue<Employee> operators)
	{
		this.operators = operators;
	}
	
	public LinkedBlockingQueue<Employee> getSupervisors()
	{
		return supervisors;
	}
	
	public void setSupervisors(LinkedBlockingQueue<Employee> supervisors)
	{
		this.supervisors = supervisors;
	}
	
	public LinkedBlockingQueue<Employee> getDirectors()
	{
		return directors;
	}
	
	public void setDirectors(LinkedBlockingQueue<Employee> directors)
	{
		this.directors = directors;
	}
	
	public void addEmployee(Operator operator)
	{
		if(operator == null) return;
		this.operators.add(operator);
	}
	
	public void addEmployee(Supervisor supervisor)
	{
		if(supervisor == null) return;
		this.supervisors.add(supervisor);
	}
	
	public void addEmployee(Director director)
	{
		if(director == null) return;
		this.directors.add(director);
	}
	
	private void initQueuesIfNull(boolean fromConstructor) throws ServiceNotAvailableException {
		if(!fromConstructor && this.directors == null && this.supervisors == null && this.operators == null)
			throw new ServiceNotAvailableException();
			
		this.directors = directors == null ? new DirectorQueue() : directors;
		this.supervisors = supervisors == null ? new SupervisorQueue((DirectorQueue)this.directors) : supervisors;
		this.operators = operators == null ? new OperatorQueue((SupervisorQueue)this.supervisors) : operators;
	}
	
	public Response dispatchCall(Call call) {
		Response response = null;
		
		try {
			if(call == null) 
				throw new NoSuchElementException("La Llamada no puede ser nula.");
			
			if(call.havePriority())
				call.setPriority(priority.incrementAndGet());
			
			initQueuesIfNull(false);
			
			call.setAttended(operators.take());
			
			// Intervalo de duracion del Llamado entre 5 y 10 segundos. 
			int timeCalledMin = Integer.parseInt(config.properties.getProperty("TIME_CALLED_MIN", "5000"));
			int timeCalledMax = Integer.parseInt(config.properties.getProperty("TIME_CALLED_MAX", "10000"));
			
			int value = timeCalledMin + (int)(Math.random() * (timeCalledMax - timeCalledMin));
			Thread.sleep(value);
			//Dejo disponible al empleado.
			employeeQueueOrchestation(call.getAttended());
			response = new Response(Status.Attended, call.toString(), call);
		} catch (ServiceNotAvailableException e) {			
			response = new Response(Status.WithoutService, e.getMessage(), call);
		} catch (WithoutEmployeeException e) {
			addWaitQueue(call);
			response = new Response(Status.OnHold, e.getMessage(), call);
		} catch (NoSuchElementException e) {
			response = new Response(Status.Success, e.getMessage(), call);
		}
		catch (InterruptedException e) {
			addWaitQueue(call);
			response = new Response(Status.Interrupted, e.getMessage(), call);
		} catch (NullPointerException e) {
			System.err.println(e.getMessage());
			response = new Response(Status.Error, "No se ha podido interpretar la llamada.", call);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			response = new Response(Status.Error, "La llamada se perdio, el cliente debera volver a llamar.", call);
		}
		
		return response;
	}	
	
	public void updateObservable() {		
		setChanged();
	}
	
	private void employeeQueueOrchestation(Employee employee) throws Exception
	{	
		//Uso reflection para orquestar de manera dinamica segun la clase principal y no la heredada.
		Method method = this.getClass().getMethod("addEmployee", employee.getClass());
		method.invoke(this, employee);
	}
}
