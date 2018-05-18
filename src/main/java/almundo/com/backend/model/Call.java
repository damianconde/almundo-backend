package almundo.com.backend.model;

import java.util.UUID;

public class Call {
	private UUID id = UUID.randomUUID();
	private Employee attended = null;
	
	public UUID getId() {
		return id;
	}
	
	public Employee getAttended() {
		return attended;
	}

	public void setAttended(Employee attended) {
		this.attended = attended;
	}
}
