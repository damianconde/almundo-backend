/**
 * Director Call by Call Center
 */
package almundo.com.backend.model;

import org.springframework.stereotype.Component;

/**
 * @author Damian Conde
 *
 */
@Component("director")
public class Director extends Employee{

	public Director(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "Muchas gracias por comunicarse con Almundo, mi nombre es " + super.getName() + ", director de la empresa. ¿En que puedo serle util?";
	}
}
