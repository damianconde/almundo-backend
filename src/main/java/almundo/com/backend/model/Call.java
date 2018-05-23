package almundo.com.backend.model;

import java.util.UUID;

public class Call {
	private UUID id;
	private String name;
	private Employee attended = null;
	private Integer priority;
	
	public Call(UUID id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public boolean havePriority()
	{
		return priority == null;
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
		return "Position: " + priority + " | Message: " + attended;
	}
}
