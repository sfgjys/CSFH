package com.minji.cufcs.widget;

import com.minji.cufcs.observer.Observers;

import android.media.MediaPlayer;

public class MyMediaPlayer extends MediaPlayer implements Observers {

	public MyMediaPlayer() {
		super();
	}

	@Override
	public void update(int mPosition, int areaOrautonomously) {
		if(mPosition==3){
			this.pause();	
		}
	}
	
}
