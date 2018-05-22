package almundo.com.backend.business;

import java.util.Observable;
import java.util.Observer;

import almundo.com.backend.exception.WithoutWaitCallException;
import almundo.com.backend.model.Call;

public class WaitCallQueueObserver implements Observer{

   private Dispatcher dispatcher;
   public ProcessCall processCall;
   
   public WaitCallQueueObserver(Dispatcher dispatcher)
   {
      this.dispatcher = dispatcher;
   }
   
   public void update(Observable obs, Object obj)
   {
      if (obs == dispatcher)
      {
		try {
			processCall.call = dispatcher.getWaitCall();
			processCall.call();	
		} catch (WithoutWaitCallException e) {
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
      }
   }
}
