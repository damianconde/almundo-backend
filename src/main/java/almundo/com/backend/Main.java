package almundo.com.backend;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Running tests!");

        JUnitCore engine = new JUnitCore();
        engine.addListener(new TextListener(System.out));
        engine.run(DispatcherTest.class);
        
        System.out.println("Final tests!");
	}
}
