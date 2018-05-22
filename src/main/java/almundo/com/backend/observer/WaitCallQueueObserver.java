package almundo.com.backend.observer;

import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Observer;

import almundo.com.backend.business.Dispatcher;
import almundo.com.backend.observable.ProcessCall;

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
		try {
			new ProcessCall(dispatcher, dispatcher.getWaitCall()).call();
		} catch (NoSuchElementException e) {
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
      }
   }
}
