package com.kentropy.components;

import java.io.IOException;

public class ExecuteScript {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] command = {"krita","test.jpg"};
		try {
			if(System.getProperty("os.name").equals("Linux")) {
				Process proc = Runtime.getRuntime().exec(command);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
