package com.antbean.pay;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class SystemHolder {

	private static ScheduledThreadPoolExecutor schedulerPool;
	private static ExecutorService threadPool;

	private static final Object lock1 = new Object();
	private static final Object lock2 = new Object();

	public static ScheduledThreadPoolExecutor getSchedulerPool() {
		if (null == schedulerPool) {
			synchronized (lock1) {
				if (null == schedulerPool) {
					schedulerPool = new ScheduledThreadPoolExecutor(10);
				}
			}
		}
		return schedulerPool;
	}

	public static ExecutorService getThreadPool() {
		if (null == threadPool) {
			synchronized (lock2) {
				if (null == schedulerPool) {
					threadPool = Executors.newFixedThreadPool(10);
				}
			}
		}
		return threadPool;
	}
}