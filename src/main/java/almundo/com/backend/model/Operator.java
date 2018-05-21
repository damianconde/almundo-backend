/**
 * Operator Class by Call Center
 */
package almundo.com.backend.model;

/**
 * @author Damian Conde
 *
 */
public class Operator extends Employee{

	public Operator(String name) {
		super(name);
	}

	public Employee HoldOn() {
		return new Supervisor("Supervisor Test");
	}

	public Employee holdOn() {
		// TODO Auto-generated method stub
		return null;
	}	
}
