/**
 * 
 */
package almundo.com.backend.contract;

import almundo.com.backend.model.Call;
import almundo.com.backend.model.Response;

/**
 * @author user
 *
 */
public interface IDispatcher {
	Response dispatchCall(Call call) throws InterruptedException, Exception;
}
