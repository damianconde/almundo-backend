package almundo.com.backend.business;

import java.util.List;
import java.util.concurrent.Future;

import almundo.com.backend.model.AttendedSizes;
import almundo.com.backend.model.Director;
import almundo.com.backend.model.Operator;
import almundo.com.backend.model.Response;
import almundo.com.backend.model.Supervisor;
import almundo.com.backend.model.Response.Status;

public class Utils {

	public static AttendedSizes responseSizes(List<Future<Response>> responses)
	{
		AttendedSizes attendedSizes = new AttendedSizes();
		
		attendedSizes.setOperatorSize(responses
			.stream()
			.filter(element -> {
				try {
					//Casteo a Operador para saber cuantos Operadores Atendieron
					Operator operator = (Operator)element.get().getCall().getAttended();
					return operator != null;
				} catch (Exception e) {						
					return false;
				}					
			})
			.count());
		
		attendedSizes.setSupervisorSize(responses
				.stream()
				.filter(element -> {
					try {
						//Casteo a Supervisor para saber cuantos Supervisores Atendieron
						Supervisor supervisor = (Supervisor)element.get().getCall().getAttended();
						return supervisor != null;
					} catch (Exception e) {						
						return false;
					}					
				})
				.count());
		
		attendedSizes.setDirectorSize(responses
				.stream()
				.filter(element -> {
					try {
						//Casteo a Director para saber cuantos Directores Atendieron
						Director director = (Director)element.get().getCall().getAttended();
						return director != null;
					} catch (Exception e) {						
						return false;
					}					
				})
				.count());
		
		attendedSizes.setAttendedSize(responses
	    		.stream()
	    		.filter(element -> {
					try {
						return element.get().getStatus() == Status.Attended;
					} catch (Exception e) {
						return false;
					}
				})
	    		.count());

		attendedSizes.setOnHoldSize(responses
				.stream()
				.filter(element -> {
					try {
						return element.get().getStatus() == Status.OnHold;
					} catch (Exception e) {
						return false;
					}					
				})
				.count());
		
		attendedSizes.setInterruptedSize(responses
				.stream()
				.filter(element -> {
					try {
						return element.get().getStatus() == Status.Interrupted;
					} catch (Exception e) {
						return false;
					}					
				})
				.count());
		
		attendedSizes.setErrorSize(responses
				.stream()
				.filter(element -> {
					try {
						return element.get().getStatus() == Status.Error;
					} catch (Exception e) {
						return false;
					}					
				})
				.count());
		
		return attendedSizes;
	}
}
