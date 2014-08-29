package com.flightcom.kudosu;

import java.util.ArrayList;

public final class MyArrayList<Integer> extends ArrayList<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String toString() {
		
		String result = "";
		
		for ( Integer x : this) {
			
			result += x;
			result += result.length() % 5 == 0 ? "\n" : " ";
			
		}
		return result;
		
	}

}
