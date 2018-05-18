/**
 * 
 */
package almundo.com.backend.contract;

import almundo.com.backend.model.Call;

/**
 * @author user
 *
 */
public interface IDispatcher {
	public void dispatchCall(Call call) throws InterruptedException;
}
