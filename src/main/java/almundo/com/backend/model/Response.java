package almundo.com.backend.model;

public class Response {
	public static enum Status{
		Attended, // La llama fue realizada correctamente
		Interrupted, //Se envia a la cola de espera
		OnHold, //Se envia a la cola de espera
		Error,
		Success, //Finalizo la ejecucion de todas las llamadas de forma exitosa.
		WithoutService //Es cuando no hay ningun empleado en la instancia inicial
	}
	
	private Status status;
	private String message;
	private Call call;
	
	public Response(Status status, String message, Call call) {
		this.status = status;
		this.message = message;
		this.call = call;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public Call getCall() {
		return call;
	}
	
	public String getMessage() {
		return message;
	}
	
	@Override
	public String toString() {
		String response;
		//if(call == null) return "Ningun Llamado para ser Atendido.";
		switch(status) {
			case OnHold:
				response = "Call: " + call.getName() + " | Status: " + status + " | Message: " + message;
				break;
			case Attended:
				response = "Call: " + call.getName() + " | Attended: " + call.getAttended().getName() + " | Status: " + status + " | Message: " + message;
				break;
			case Interrupted:
				response = "Call: " + call.getName() + " | Attended: " + call.getAttended().getName() + " | Status: " + status + " | Message: " + message;
				break;
			case Success:
				response = "Todas las llamas han sido costestadas correctamente.";
				break;
		default:
			response = "Por el momento no estamos atendiendo llamados, vuelva a intentar mas tarde.";
		}
		
		return response;
	}
}
