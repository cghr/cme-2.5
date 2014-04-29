package com.kentropy.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.SystemTray;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.Hashtable;

import com.kentropy.components.swing.NetworkPanel;
import com.kentropy.transfer.Discovery;

import net.xoetrope.awt.XLabel;
import net.xoetrope.awt.XPanel;



public class StatusIndicator extends XPanel {

	/**
	 * @param args
	 * 
	 */
	
	public class StatusControl extends XPanel{
	/**
		 * 
		 */
		String name;
		public String status="NotStarted";
		public StatusControl(String name)
		{
			this.name=name;
			
			
		}
		public void setStatus(String stat)
		{
			this.status=stat;
			if(status.equals("Processing"))
			{
				indic.setBackground(Color.yellow);
			}
			else
			if(status.equals("Complete"))
			{
				indic.setBackground(Color.green);
			}
			else
			if(status.equals("Failed"))
			{
				indic.setBackground(Color.red);
			}
			else
				indic.setBackground(Color.gray);
			indic.setText(status);
		}
		XLabel lbl= new XLabel();
		XLabel indic= new XLabel();
		public void display()
		{
			
			lbl.setName(name+"_lbl");
			lbl.setText(name);
			lbl.setForeground(Color.white);
			indic.setName(name+"_indic");
			indic.setText(status);
			indic.setBackground(Color.gray);
			lbl.setBounds(2,2,this.getWidth()-4,20);
			indic.setBounds(2,25,this.getWidth()-4,30);
			this.add(lbl);
			this.add(indic);
			
			
			
		}
		
	
		
	}
	public String[] params={"Code Sync","Upload","Download"};
	Hashtable ht= new Hashtable();
	public void init()
	{
	
		for(int i=0;i<params.length;i++)
		{
			StatusControl st=new StatusControl(params[i]);
			st.setBounds(10+(i*80),10,80,80);
			this.add(st);
			st.display();
			ht.put(params[i], st);
		
		}
	
		
	}
	
	public void setStatus(String name, String status)
	{
		StatusControl sc=(StatusControl)ht.get(name);
		sc.setStatus(status);
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		final Frame frm = new Frame("tet");
		frm.setBounds(10,10,800,600);
	StatusIndicator si= new StatusIndicator();
	si.setBounds(10,10,600,400);
	si.init();
	frm.add(si);
	frm.addWindowStateListener(
			new WindowStateListener()
	{

		public void windowStateChanged(WindowEvent arg0) {
			// TODO Auto-generated method stub
			if(arg0.getNewState()==arg0.WINDOW_CLOSING);
				//SystemTray.getSystemTray().remove(	SystemTray.getSystemTray().getTrayIcons()[0]);
		}
		
	});
	
	
	frm.show();
	Thread.currentThread().sleep(1000);
	si.setStatus("Upload", "Processing");
	si.setStatus("Code Sync", "Complete");
	Thread.currentThread().sleep(1000);
	si.setStatus("Upload", "Complete");
	si.setStatus("Download", "Processing");
	}

}
