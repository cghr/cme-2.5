package com.kentropy.components;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import net.xoetrope.awt.XEdit;
import net.xoetrope.xui.XAttributedComponent;

public class KenEdit extends XEdit implements XAttributedComponent ,KeyListener{

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	String restrictDigit="";
	public void setAttribute(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		if(arg0.equals("restrictDigit"))
		{
			restrictDigit=(String)arg1;
			
		}
		

	}
	public Object getAttribute(String arg0)
	{
		if(arg0.equals("restrictDigit"))
		{
return restrictDigit;
			
		}
		return "";
	}

	public KenEdit() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
