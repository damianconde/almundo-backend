package almundo.com.backend;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

import almundo.com.backend.business.Dispatcher;
import almundo.com.backend.business.ProcessCall;
import almundo.com.backend.business.WaitCallQueueObserver;
import almundo.com.backend.model.Call;
import almundo.com.backend.model.Director;
import almundo.com.backend.model.Operator;
import almundo.com.backend.model.Supervisor;
import almundo.com.backend.queue.DirectorQueue;
import almundo.com.backend.queue.OperatorQueue;
import almundo.com.backend.queue.SupervisorQueue;

public class Main {

	public static void main(String[] args) throws InterruptedException {
	
	}
}
