package com.jun90.projects.scan.web;

import java.util.Random;

public class KeyUtils {

	private static final Random random = new Random();
	
	public static String getStringKey() {
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < 32; i++) {
			int v = random.nextInt(62);
			if(v < 10) buffer.append((char) ('0' + v));
			else if(v < 36) buffer.append((char) ('A' - 10 + v));
			else buffer.append((char) ('a' - 36 + v));
		}
		return buffer.toString();
	}
	
	public static int getIntegerKey() {
		return 1 + random.nextInt(999999);
	}
			
}
