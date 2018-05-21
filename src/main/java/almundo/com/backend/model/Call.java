package almundo.com.backend.model;

import java.util.UUID;

public class Call {
	private UUID id;
	private String name;
	private Employee attended = null;	
	
	public Call(String name)
	{
		this.name = name;
		this.id = UUID.randomUUID();
	}
	
	public UUID getId() {
		return id;
	}
	
	public Employee getAttended() {
		return attended;
	}

	public void setAttended(Employee attended) {
		
		this.attended = attended;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {					
		return "Call: " + name + " | Message: " + (attended == null ? "Todos nuestros operadores se encuentran ocupados, aguarde en linea, que sera atendido a la brevedad. Muchas gracias." : attended) ;
	}
	
	public void log() {
		System.out.println(this);
	}
}
