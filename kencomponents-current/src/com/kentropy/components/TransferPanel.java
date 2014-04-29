package com.kentropy.components;

 import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import com.kentropy.db.TestXUIDB;
import com.kentropy.transfer.Client;
import com.kentropy.util.NetworkUtil;

import net.xoetrope.awt.XButton;
import net.xoetrope.awt.XLabel;
import net.xoetrope.awt.XPanel;
import net.xoetrope.awt.XTextArea;
import net.xoetrope.xui.PageSupport;
import net.xoetrope.xui.data.XModel;
import net.xoetrope.xui.*;

/**
 * Accomplishes Front-end Business Logic for CME System. Methods in this class
 * are used to accomplish abstracted Business Logic for the front-end.
 * 
 * @author Rajeev.K
 * @author Sagar.P
 * @author Navaneetha.K
 * 
 */

public class TransferPanel extends XPanel implements ActionListener,
		ComponentListener {

	Thread th=null;
	public class Task implements Runnable{
		
		/**
		 * This method is used to perform uploading task. 
		 * @throws Exception
		 */
		
		public void upload() throws Exception {
		int changes=	TestXUIDB.getInstance().sendLogs(currentUser, recepients);
		tfp.ta.append("\n"+changes+" changes uploaded");
		
		}
		
		/**
		 * This method is used to perform downloading task.
		 */
		
		public void download() throws Exception {

			Client cl = new Client();
			cl.participant=currentUser;
			cl.run(); 
			for(int i=0;i< cl.messages.size();i++)
			{
				tfp.ta.append("\n"+cl.messages.get(i));
			}
			
			
		}
		int codeCount=0;
		public void syncCode() throws Exception {

			Client cl = new Client();
			cl.participant=currentUser;
			 codeCount=cl.runRepoSync(); 
			for(int i=0;i< cl.messages.size();i++)
			{
				tfp.ta.append("\n"+cl.messages.get(i));
			}
			
			
		}

		/**
		 * This method is used to perform synchronization of both upload and download process.
		 */
		
		public void run() {
			// TODO Auto-generated method stub
			try {
				String curAction="";
				tfp.ta.append("\nChecking connection to server");
				//update code
				tfp.ta.append("\n-------------------");
				tfp.ta.append("\nStarting Code Update... It might take some time.");
				curAction="download";
				syncCode();
				//
				
				tfp.ta.append("\nStarting Data Synchronization...");

				curAction="upload";
				tfp.ta.append("\n-------------------");
				tfp.ta.append("\nStarting Upload...");
				upload();

				tfp.ta.append("\nUpload Completed");



				tfp.ta.append("\n-------------------");
				tfp.ta.append("\nStarting Download... It might take some time.");
				curAction="download";
				download();
		


				tfp.action.getActionListeners()[0].actionPerformed(new ActionEvent(tfp.action, 1, "download"));

				tfp.ta.append("\nDownload Completed");
				tfp.ta.append("\nSynchronization Completed");



			} catch (Exception e) {
				// TODO Auto-generated catch block


				tfp.ta.append(e.toString());
				e.printStackTrace();
				tfp.action.getActionListeners()[0].actionPerformed(new ActionEvent(tfp.action, 1, "abort"));
			}
		}
		public String currentUser;
		public String recepients;
		
		public TransferPanel tfp=null;
		
		
	}
	public int columns = 1;
	public String heading = "Testing a long heading";
	private java.awt.Color color = java.awt.Color.blue;

	

	public XModel list = null;
	XLabel header = null;
	XLabel lbl = null;
	XTextArea ta=null;
	XButton close = null;
	public XButton action = new XButton();;
	
	/**
	 * This method is used to display the message upload and download process in the panel.
	 * @param htext
	 * 			name of the header.
	 * @param msg
	 * 			content of the message which is shown in the panel.
	 */
	
	public void display(String htext, String msg) {

		this.setBounds(10, 50, 300, 130);
		setName("error");
		setBackground(new Color(50, 50, 255));
		

		close = new XButton();
		close.setText("close");
		close.setForeground(Color.white);
		close.setBackground(Color.red);
		close.setBounds(259, 2, 40, 18);
		close.setFont(new Font("Verdana", Font.BOLD, 12));
		// addActionHandler(close, "hideMessagePanel");
		close.addActionListener(this);
		add(close);

		//action = new XButton();
		action.setName("action");
		action.setText("");
		action.setForeground(Color.white);
		action.setBackground(Color.red);
		action.setBounds(20, 100, 40, 18);
		action.setFont(new Font("Verdana", Font.BOLD, 12));
		// addActionHandler(action, handler);
		add(action);
		action.setVisible(false);


		
		ta = new XTextArea();
		ta.setName("errorContainer");
		ta.setBackground(new Color(240, 240, 240));
		ta.setForeground(Color.black);
		ta.setBounds(3, 22, 294, 100);
		ta.setEditable(false);
		ta.setText(msg);

		// lbl.setVisible(true);
		header = new XLabel();
		header.setName("errorHeader");
		header.setBackground(new Color(50, 50, 255));
		header.setForeground(Color.WHITE);
		header.setBounds(1, 1, 230, 18);

		header.setFont(new Font("Verdana", Font.BOLD, 12));
		header.setText(htext);

		add(header);
		add(ta);
	
		Task task= new Task();
		task.currentUser=this.currentUser;
		task.recepients=this.recepients;
		task.tfp=this;
		Thread th= new Thread(task);
		th.start();
		

	}

	/**
	 * This method is used to set the color.
	 * @param color
	 * 			which has to set.
	 */
	
	public void setColor(java.awt.Color color) {
		this.color = color;
	}

	public String actionStr = null;
	public String currentUser = null;
	public String recepients = null;

	

	
	public TransferPanel() {
		// TODO Auto-generated constructor stub
	}

	public TransferPanel(boolean arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getActionCommand().equals("close")) {
			this.setVisible(false);
		}
		
	}

	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void componentResized(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Component Shown");
		try {
			//this.upload();
			
			
			//lbl.setText("Upload completed");
			//this.
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
