package almundo.com.backend.business;

import java.lang.reflect.Method;
import java.util.Observable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import almundo.com.backend.contract.IDispatcher;
import almundo.com.backend.exception.WithoutEmployeeException;
import almundo.com.backend.model.Call;
import almundo.com.backend.model.Director;
import almundo.com.backend.model.Employee;
import almundo.com.backend.model.Operator;
import almundo.com.backend.model.Response;
import almundo.com.backend.model.Response.Status;
import almundo.com.backend.model.Supervisor;
import almundo.com.backend.queue.DirectorQueue;
import almundo.com.backend.queue.OperatorQueue;
import almundo.com.backend.queue.SupervisorQueue;

public class Dispatcher extends Observable implements IDispatcher{
	private LinkedBlockingQueue<Employee> operators;
	private LinkedBlockingQueue<Employee> supervisors;
	private LinkedBlockingQueue<Employee> directors;
	//private WaitCallQueue waitCallQueue;
	private LinkedBlockingQueue<Call> waitCallQueue;
	private AtomicInteger priority;
	
	public Dispatcher(OperatorQueue operators, SupervisorQueue supervisors, DirectorQueue directors) {	
		this.directors = directors == null ? new DirectorQueue() : directors;
		this.supervisors = supervisors == null ? new SupervisorQueue(directors) : supervisors;
		this.operators = operators == null ? new OperatorQueue(supervisors) : operators;
		waitCallQueue = new LinkedBlockingQueue<Call>();
		priority = new AtomicInteger(0);
	}
	
	public void addWaitQueue(Call call) {
		if(call == null) return;
		waitCallQueue.add(call);
	}
	
	public Call getWaitCall() throws InterruptedException {
		//Uso Take para evitar posibles nulos, ya que de no encontrar datos, espera a que los haya.
		return waitCallQueue.take();
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
	
	public Response dispatchCall(Call call) {
		Response response = null;
		
		try {
			if(call == null) 
				throw new NullPointerException("La Llamada no puede ser nula.");
			
			if(call.havePriority())
				call.setPriority(priority.incrementAndGet());
			
			call.setAttended(operators.take());
			
			// Intervalo de duracion del Llamado entre 5 y 10 segundos. 
			int value = 5000 +  (int)(Math.random() * 5000);
			Thread.sleep(value);
			//Dejo disponible al empleado.
			employeeQueueOrchestation(call.getAttended());
			response = new Response(Status.Attended, call.toString(), call);
		} catch (WithoutEmployeeException e) {
			addWaitQueue(call);
			response = new Response(Status.OnHold, e.getMessage(), call);
		} catch (InterruptedException e) {
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
