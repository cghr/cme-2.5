package com.kentropy.components.swing;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Label;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.Vector;

import com.kentropy.components.TransferPanel;
import com.kentropy.components.TransferPanel1;
import com.kentropy.db.TestXUIDB;
import com.kentropy.transfer.Discovery;
import com.kentropy.transfer.Server2;
import com.kentropy.security.client.UserAuthentication;

import net.xoetrope.swing.XButton;
import net.xoetrope.swing.XLabel;
import net.xoetrope.swing.XPanel;
import net.xoetrope.xui.data.XModel;

public class NetworkPanel extends XPanel implements Observer ,ActionListener {

	/**
	 * @param args
	 */
	int [] counters=null;
	java.util.ArrayDeque<String> q= new ArrayDeque<String>();
	String username=null;
	String passwd=null;
	String recepients=null;
	String id=null;
public 	static int sleepTime=10000;
public static int noOfCycles=20;
	public NetworkPanel(String uname,String p,String r,String id1)
	{
		username=uname;
		passwd=p;
	recepients=r;
	id=id1;
	}
	
	public TransferPanel1 tfp = new TransferPanel1();
//	Queue sync=new Queue(queue);
	public void sync(String node)
	{
	
		try {
			UserAuthentication ua= new UserAuthentication();
			ua.authenticate(username, passwd);
			
		
		
				tfp.actionStr = "upload";
				tfp.currentUser = id;//TestXUIDB.getInstance().getCurrentUser();
				tfp.recepients = recepients;
				tfp.run();
				
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public void init()
	{
		Vector networks=Discovery.network;
		counters=new int[networks.size()];
		for(int i=0; i< networks.size();i++)
		{
			XLabel label= new XLabel();
			label.setBounds(10,10+(i*30), 350,20);
			label.setText(networks.get(i).toString());
			this.add(label);
			XButton btn= new XButton();
			btn.setBounds(360,10+(i*30), 150,20);
			String [] tt=networks.get(i).toString().split(",");
			if(tt[2].equals("server"))
			btn.setLabel("Get and Send");
			else
				btn.setLabel("Send");
			btn.setEnabled(false);
			btn.addActionListener(this);
			this.add(btn);
			counters[i]=0;
			
		}
		
		XButton btn= new XButton();
		btn.setBounds(360,300, 150,20);
		
			btn.setLabel("Close");
		//btn.setEnabled(false);
		btn.addActionListener(this);
		this.add(btn);
		this.add(tfp, 0);
		tfp.display("Upload", "Starting upload ");
		tfp.setVisible(true);
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		DatagramSocket d;
		//try {
	//		d = new DatagramSocket(8888);
		//	System.out.println(		d.getBroadcast());
		//d.s
	//		d.send(new DatagramPacket(" testtt".getBytes(),0, InetAddress.),2));
			//d.getChannel().
	//	} catch (SocketException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
	//	}
		
		//System.in.read();
	
		final Frame frm = new Frame("tet");
		frm.setBounds(10,10,800,600);
		Image img=Toolkit.getDefaultToolkit().createImage("resources/cghr_logo1.gif");
		TrayIcon t=new TrayIcon(img);
		t.setToolTip("Hello world");
		PopupMenu p=new PopupMenu();
		p.add("Maximize");
		p.add("Minimize");
		p.add("Close");
		p.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println(arg0.getActionCommand());
				if(arg0.getActionCommand().equals("Maximize"))
				frm.setState(Frame.NORMAL);
				else
					if(arg0.getActionCommand().equals("Minimize"))
					frm.setState(Frame.ICONIFIED);
					else
					{
						frm.dispose();
						System.exit(0);
				
		
						
					}
			}
			
		});
		
		
		t.setPopupMenu(p);

		
		SystemTray.getSystemTray().add(t);
		frm.setState(Frame.ICONIFIED);
		/*SystemTray.getSystemTray().addPropertyChangeListener("Test", new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println(arg0.getPropertyName());
			}
			
		
		});*/
		
		NetworkPanel np= new NetworkPanel("test","test","admin","9");
		np.setBounds(10,10,600,400);
		np.init();
		frm.add(np);
		
		frm.addWindowStateListener(
				new WindowStateListener()
		{

			public void windowStateChanged(WindowEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getNewState()==arg0.WINDOW_CLOSING)
					SystemTray.getSystemTray().remove(	SystemTray.getSystemTray().getTrayIcons()[0]);
			}
			
		});
		
		frm.show();
		
		Thread th= new Thread(new Discovery(np));
		th.start();
		np.stop();
		Thread.currentThread().sleep(sleepTime);
		np.start();
	//	Server2.main(new String[0]);

	}
	int counter=0;
	
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		System.out.println(" Status is "+status);
		if(status.equals("NotStarted"))
			return;
		counter++;
		System.out.println(" Counter is "+counter);
		for(int i=0 ;i<this.getComponentCount();i++)
		{
			if(this.getComponent(i) instanceof XLabel){
			XLabel label=(XLabel)this.getComponent(i);
			String [] tt= arg1.toString().split(" ");
			String [] tt1=label.getText().split(" ");
			if(tt[0].trim().equals(tt1[0]))
					{
				label.setText(arg1.toString());
				if(tt[1].equals("alive"))
				{
					this.getComponent(i+1).setEnabled(true);
				//	counters[i]++;
					//if(counter>4) 
				if(!q.contains(arg1.toString()))
					{
					System.out.println("Adding to queue "+q.size());
					//	counters[i]+=0;
						q.push(arg1.toString());
						//sync(arg1.toString());
					}
				}
				else
					this.getComponent(i+1).setEnabled(false);
				
					}
			
			}
		}
		if(counter>noOfCycles)
		{
			processQueue();
		counter=0;	
		}
		
	}
	String status="Started";
	public void stop()
	{
		System.out.println(" Stopping ");
		status="NotStarted";
	}
	public void start()
	{
		System.out.println(" Starting ");
		status="Started";
		//counter=25;
	}
	public void start(int counter1)
	{
		System.out.println(" Starting ");
		status="Started";
		counter=counter1;
	}
	

	private synchronized void processQueue() {
		// TODO Auto-generated method stub
		while(!q.isEmpty())
		sync(	q.pop());
		
	}
	

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getActionCommand().equals("Close"))
		{
			this.setVisible(false);
			
		}
		else
		{
		q.add("");
		processQueue();
		counter=0;
		
		}
		
	}

}
