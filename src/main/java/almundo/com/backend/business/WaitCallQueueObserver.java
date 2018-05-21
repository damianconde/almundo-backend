package almundo.com.backend.business;

import java.util.Observable;
import java.util.Observer;

import almundo.com.backend.model.Call;

public class WaitCallQueueObserver implements Observer{

   private Dispatcher dispatcher;
   
   public WaitCallQueueObserver(Dispatcher dispatcher)
   {
      this.dispatcher = dispatcher;
   }
   
   public void update(Observable obs, Object obj)
   {
      if (obs == dispatcher)
      {
    	Call call;
    	
		try {
			call = dispatcher.getWaitCall();
		} catch (InterruptedException e) {
			System.out.println("Nada Pendiente por el momento");
			return;
		}
    	  
    	new ProcessCall(dispatcher, call).run();
      }
   }
}
