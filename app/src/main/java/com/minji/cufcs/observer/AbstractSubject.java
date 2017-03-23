package com.minji.cufcs.observer;

import java.util.Enumeration;
import java.util.Vector;


public abstract class AbstractSubject implements Subject {
	private Vector<Observers> vector = new Vector<Observers>();

	/**
	 * 现在已经存储   mPosition来区分  1为FragmentArea 2为FragmentAutonomously 3为AlarmServicer 
	 * */
	@Override
	public void add(Observers observer) {
		vector.add(observer);
		
	}

	@Override
	public void del(Observers observer) {
		vector.remove(observer);
		
	}

	@Override
	public void notifyObservers(int mPosition,int areaOrautonomously) {
		Enumeration<Observers> enumo = vector.elements();
		while (enumo.hasMoreElements()) {
			enumo.nextElement().update(mPosition,areaOrautonomously);
		}
	}

}
