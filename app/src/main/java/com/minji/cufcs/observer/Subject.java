package com.minji.cufcs.observer;


public interface Subject {
	/* 增加观察者 */
	public void add(Observers observer);

	/* 删除观察者 */
	public void del(Observers observer);

	/* 通知所有的观察者 */
	public void notifyObservers(int mPosition, int areaOrautonomously);

	/* 自身的操作 */
	public void operation(int mPosition, int areaOrautonomously);
}
