/**
 * Employee Class by Call Center
 */
package almundo.com.backend.model;

import java.util.UUID;

import almundo.com.backend.contract.IEmployee;

/**
 * @author Damian Conde
 *
 */
public abstract class Employee implements IEmployee{	
	private UUID id;
	private String name;
	
	public Employee(String name)
	{
		id = UUID.randomUUID();
		this.name = name;
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "Muchas gracias por comunicarse con Almundo, mi nombre es " + name + ". ¿En que puedo ayudarlo?";
	}
	
	public void log(){
		System.out.println( this );
	}
}