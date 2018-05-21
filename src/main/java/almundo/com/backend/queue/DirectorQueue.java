package almundo.com.backend.queue;

import java.util.concurrent.LinkedBlockingQueue;

import almundo.com.backend.model.Director;
import almundo.com.backend.model.Employee;

public class DirectorQueue  extends LinkedBlockingQueue<Employee> {
	private LinkedBlockingQueue<Director> directors;
	
	public DirectorQueue(LinkedBlockingQueue<Director> directors) {
		this.directors = directors;
	}

	@Override
	public Employee take() throws InterruptedException {
		if(directors.isEmpty())
			return null;
		
		return directors.take();
	}

}
