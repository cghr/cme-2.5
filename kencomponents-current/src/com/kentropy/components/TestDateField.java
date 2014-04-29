package com.kentropy.components;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.im.InputContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JPanel;
import javax.swing.border.Border;

import net.xoetrope.swing.XPanel;

import com.theotherbell.ui.DateField;
import com.theotherbell.ui.DatePicker;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

public final class TestDateField {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	
	
	
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
Frame frm = new Frame("Test Date Field");
frm.setBounds(0,0,800,600);
frm.addWindowListener(new WindowAdapter(){

	/* (non-Javadoc)
	 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		super.windowClosing(arg0);
		System.exit(0);
	}
	
	
});
Locale locale= new Locale("mr","IN");

InputContext.getInstance().selectInputMethod(locale);
//DateField df= new DateField((new SimpleDateFormat("yyyy-MM-dd")).parse("2004-01-01"));
JDateChooser df= new JDateChooser();
df.setDate((new SimpleDateFormat("yyyy-MM-dd")).parse("2004-01-01"));
df.setSize(400, 100);
//df.
frm.setLayout(null);
XPanel jp0= new XPanel();
jp0.setBounds(10,10,500,100);
jp0.setAttribute("border", "1");
frm.add(jp0);
XPanel jp= new XPanel();
jp.setBounds(10,210,500,200);
jp.setAttribute("border", "1");
jp.setBackground(Color.red);
//jp.setBorder();
df.setBounds(10,60,200,30);
jp.add(df);

frm.add(jp);
//df.getDate()
frm.setVisible(true);
System.out.println(InputContext.getInstance().getLocale());

	}
	
	
	
}
