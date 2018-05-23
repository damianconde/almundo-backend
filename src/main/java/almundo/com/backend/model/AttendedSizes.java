package almundo.com.backend.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value="attendedSizes")
public class AttendedSizes {
	private long operatorSize;
	private long supervisorSize;
	private long directorSize;
	private long attendedSize;
	private long onHoldSize;
	private long errorSize;
	private long interruptedSize;
	private long successSize;
	private long withoutServiceSize;
	
	@Autowired
	public AttendedSizes() {
		
	}
	
	public long getOnHoldSize() {
		return onHoldSize;
	}
	public void setOnHoldSize(long onHoldSize) {
		this.onHoldSize = onHoldSize;
	}
	public long getErrorSize() {
		return errorSize;
	}
	public void setErrorSize(long errorSize) {
		this.errorSize = errorSize;
	}
	public long getInterruptedSize() {
		return interruptedSize;
	}
	public void setInterruptedSize(long interruptedSize) {
		this.interruptedSize = interruptedSize;
	}	
	
	public long getOperatorSize() {
		return operatorSize;
	}
	public void setOperatorSize(long operatorSize) {
		this.operatorSize = operatorSize;
	}
	public long getSupervisorSize() {
		return supervisorSize;
	}
	public void setSupervisorSize(long supervisorSize) {
		this.supervisorSize = supervisorSize;
	}
	public long getDirectorSize() {
		return directorSize;
	}
	public void setDirectorSize(long directorSize) {
		this.directorSize = directorSize;
	}
	public long getAttendedSize() {
		return attendedSize;
	}
	public void setAttendedSize(long attendedSize) {
		this.attendedSize = attendedSize;
	}

	public long getSuccessSize() {
		return successSize;
	}

	public void setSuccessSize(long successSize) {
		this.successSize = successSize;
	}

	public long getWithoutServiceSize() {
		return withoutServiceSize;
	}

	public void setWithoutServiceSize(long withoutServiceSize) {
		this.withoutServiceSize = withoutServiceSize;
	}
}
