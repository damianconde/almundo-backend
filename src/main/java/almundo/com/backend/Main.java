package almundo.com.backend;

import java.util.UUID;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import almundo.com.backend.business.Dispatcher;
import almundo.com.backend.config.AppConfig;
import almundo.com.backend.model.AttendedSizes;
import almundo.com.backend.model.Call;
import almundo.com.backend.model.Director;
import almundo.com.backend.model.Employee;
import almundo.com.backend.model.Operator;
import almundo.com.backend.model.Response;
import almundo.com.backend.model.Supervisor;
import almundo.com.backend.queue.DirectorQueue;
import almundo.com.backend.queue.OperatorQueue;
import almundo.com.backend.queue.SupervisorQueue;

//@Configuration
//@EnableAutoConfiguration
//@Import({ Config.class })
public class Main {

	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

//		DirectorQueue directors = context.getBean(DirectorQueue.class);
//		directors.add(new Director("Director 1"));
//		
//		SupervisorQueue supervisors = context.getBean(SupervisorQueue.class);
//		//supervisors.add(new Supervisor("Supervisor 1"));
//		
//		OperatorQueue operators = context.getBean(OperatorQueue.class);
//		//operators.add(new Operator("Operador 1"));
		
		Dispatcher dispatcher = context.getBean(Dispatcher.class);
		
		Response response = dispatcher.dispatchCall(new Call(UUID.randomUUID(), "Call 1"));
		
		context.close();
	}
}
