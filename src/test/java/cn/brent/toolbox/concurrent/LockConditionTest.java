package cn.brent.toolbox.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockConditionTest {
	
	private transient final ReentrantLock lock = new ReentrantLock();

	private final Condition available = lock.newCondition();
	
	private String t=null;
	
	public String get() throws InterruptedException{
		lock.lockInterruptibly();
		try {
			while (t==null){
				System.out.println("等待set值。。。");
				available.await();
			}
			String t1=t;
			return t1;
		} catch (Exception e) {
			return null;
		} finally{
			t=null;
			lock.unlock();
		}
	}
	
	public String getIfNull() throws InterruptedException{
		lock.lockInterruptibly();
		try {
			if (t==null){
				System.out.println("等待set值.。。。");
				available.awaitNanos(1000*1000*1000*2);
			}
			String t1=t;
			return t1;
		} catch (Exception e) {
			return null;
		} finally{
			t=null;
			lock.unlock();
		}
	}
	
	public void set(String t){
		lock.lock();
		this.t=t;
		available.signal();
		System.out.println("set值完成");
		lock.unlock();
	}
	
	public static void main(String[] args) throws InterruptedException {
		final LockConditionTest lt=new LockConditionTest();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				String t=null;
				try {
					t = lt.getIfNull();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(t);
			}
		}).start();
		
	
		new Thread(new Runnable() {
			@Override
			public void run() {
				lt.set("dog");
			}
		}).start();
	}
}
