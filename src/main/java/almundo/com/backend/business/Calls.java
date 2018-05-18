package almundo.com.backend.business;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import almundo.com.backend.model.Call;
import almundo.com.backend.model.Employee;

public class Calls implements BlockingQueue<Call>{
	private BlockingQueue<Employee> employees;
	
	public Calls(BlockingQueue<Employee> employees) {
		this.employees = employees;	
	}
	
	public Call remove() {
		// TODO Auto-generated method stub
		return null;
	}

	public Call poll() {
		// TODO Auto-generated method stub
		return null;
	}

	public Call element() {
		// TODO Auto-generated method stub
		return null;
	}

	public Call peek() {
		// TODO Auto-generated method stub
		return null;
	}

	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public Iterator<Call> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean addAll(Collection<? extends Call> c) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	public void clear() {
		// TODO Auto-generated method stub
		
	}

	public boolean add(Call e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean offer(Call e) {
		// TODO Auto-generated method stub
		return false;
	}

	public void put(Call e) throws InterruptedException {
		
	}

	public boolean offer(Call e, long timeout, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	public Call take() throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	public Call poll(long timeout, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	public int remainingCapacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	public int drainTo(Collection<? super Call> c) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int drainTo(Collection<? super Call> c, int maxElements) {
		// TODO Auto-generated method stub
		return 0;
	}

}
