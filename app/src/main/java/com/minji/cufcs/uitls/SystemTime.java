package com.minji.cufcs.uitls;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemTime {


	public static String getTimer(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss:SSS");
		long currentTimeMillis = System.currentTimeMillis();
		Date date = new Date(currentTimeMillis);

		return simpleDateFormat.format(date);
	}
	
}
