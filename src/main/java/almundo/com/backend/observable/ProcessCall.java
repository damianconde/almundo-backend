package almundo.com.backend.observable;

import java.util.Observable;
import java.util.concurrent.Callable;

import almundo.com.backend.business.Dispatcher;
import almundo.com.backend.model.Call;
import almundo.com.backend.model.Response;
import almundo.com.backend.model.Response.Status;

public class ProcessCall extends Observable implements Callable<Response>{

	Dispatcher dispatcher;
	Call call;
	
	public ProcessCall(Dispatcher dispatcher, Call call) {
		this.dispatcher = dispatcher;
		this.call = call;
	}

	@Override
	public Response call() throws Exception {
		Response response = dispatcher.dispatchCall(call);
		System.out.println(response);
		
		//Como se libero un empleado, lo notifico al observer
		if(response.getStatus() == Status.Attended) {
			dispatcher.updateObservable();
			dispatcher.notifyObservers();
		}
		
		return response;
	}
}
