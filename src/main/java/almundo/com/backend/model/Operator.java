/**
 * Operator Class by Call Center
 */
package almundo.com.backend.model;

import org.springframework.stereotype.Component;

/**
 * @author Damian Conde
 *
 */
@Component("operator")
public class Operator extends Employee{

	public Operator(String name) {
		super(name);
	}	
}
