package com.minji.cufcs.observer;


public class MySubject extends AbstractSubject {

	private static volatile MySubject mySubject = null;

	private MySubject() {
	}

	public static MySubject getInstance() {
		if (mySubject == null) {
			synchronized (MySubject.class) {
				if (mySubject == null) {
					mySubject = new MySubject();
				}
			}
		}
		return mySubject;
	}

	@Override
	public void operation(int mPosition, int areaOrautonomously) {
		System.out.println("update self!");
		notifyObservers(mPosition,areaOrautonomously);
	}

}
