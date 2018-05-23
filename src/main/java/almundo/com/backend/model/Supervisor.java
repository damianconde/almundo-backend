/**
 * Supervisor Class by Call Center
 */
package almundo.com.backend.model;

import org.springframework.stereotype.Component;

/**
 * @author Damian Conde
 *
 */
@Component("supervisor")
public class Supervisor extends Employee{

	public Supervisor(String name) {
		super(name);
	}
	
	@Override
	public String toString() {
		return "Muchas gracias por comunicarse con Almundo, mi nombre es " + super.getName() + ", supervisor del area. ¿En que puedo servirle?";
	}
}
