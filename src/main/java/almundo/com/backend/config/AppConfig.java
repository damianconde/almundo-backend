package almundo.com.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import almundo.com.backend.business.Dispatcher;
import almundo.com.backend.exception.ServiceNotAvailableException;
import almundo.com.backend.model.AttendedSizes;
import almundo.com.backend.queue.DirectorQueue;
import almundo.com.backend.queue.OperatorQueue;
import almundo.com.backend.queue.SupervisorQueue;

@Configuration
public class AppConfig {

	@Bean
	public AttendedSizes attendedSizes() {
		return new AttendedSizes();
	}
	
	@Bean
	public DirectorQueue directorQueue() {
		return new DirectorQueue();
	}
	
	@Bean
	public SupervisorQueue supervisorQueue() {
		return new SupervisorQueue(directorQueue());
	}
	
	@Bean
	public OperatorQueue operatorQueue() {
		return new OperatorQueue(supervisorQueue());
	}
	
	@Bean
	public Dispatcher dispatcher() throws ServiceNotAvailableException {
		return new Dispatcher(operatorQueue(), supervisorQueue(), directorQueue());
	}
}
