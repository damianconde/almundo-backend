package almundo.com.backend.business;

import java.util.Observable;

import almundo.com.backend.model.Call;
import almundo.com.backend.model.Response;
import almundo.com.backend.model.Response.Status;

public class ProcessCall extends Observable implements Runnable{

	Dispatcher dispatcher;
	Call call;
	
	public ProcessCall(Dispatcher dispatcher, Call call) {
		this.dispatcher = dispatcher;
		this.call = call;
	}
	
	public void run() {
		Response response = dispatcher.dispatchCall(call);
		//TODO
		System.out.println(response);
		
		//Como se libero un empleado, lo notifico al observer
		if(response.getStatus() == Status.Attended) {
			dispatcher.updateObservable();
			dispatcher.notifyObservers();
		}
	}
}
