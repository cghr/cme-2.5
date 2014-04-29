package com.kentropy.components;

import java.awt.Color;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.FileReader;

import net.xoetrope.awt.XButton;
import net.xoetrope.awt.XPanel;
import net.xoetrope.awt.XScrollPane;
import net.xoetrope.awt.XTextArea;
import net.xoetrope.data.XDataSource;
import net.xoetrope.xui.XProjectManager;
import net.xoetrope.xui.data.XModel;
import net.xoetrope.xui.data.XModelListener;

public class ChatPanel extends XPanel implements ActionListener {

	/**
	 * @param args
	 */
	XTextArea xt= new XTextArea();
	XTextArea xt1= new XTextArea();
	public int currentSection=-1;
	public int currentQ=-1;
	//String [] questions= {"----Initial Section----\r\nQ :hello","---------name-------------\r\nQ :what is your name?"," Where are you from?","Good bye"};
	public XModel questions;
	int count=0;
	public void askQuestion(String question)
	{
		xt.setForeground(Color.red);
	xt.append(question+"\r\n");	
		
	}
	public void nextQuestion()
	{
	if(	currentSection==-1 )
	{
		this.askFilterQuestion(questions.get(0).getId());
		currentSection=0;
	}
	else		
		if(currentQ==-1)
		{
			this.askQuestion(questions.get(currentSection).get(0).get("@question").toString());
			currentQ++;
		}
		else
		{
			if(currentQ ==questions.get(currentSection).getNumChildren())
			{
				currentSection++;
				
				currentQ=-1;
				if(currentSection<questions.getNumChildren() )
				{
				this.askFilterQuestion(questions.get(currentSection).getId());
				}
			}
			else
			{
				this.askQuestion(questions.get(currentSection).get(currentQ).get("@question").toString());
				currentQ++;
			}
		}
			
			
			
	
	/*	if(currentSecton < questions)
	{
		this.askQuestion(questions[count]);
		count++;
	}*/
	}
	
	public void askFilterQuestion(String filter)
	{
		this.askQuestion("Did he have "+filter+"?");
	}
	public void setAnswer(String answer)
	{
		//xt.setForeground(Color.blue);
	xt.append("A :"+answer+"\r\n\r\n");	
	this.nextQuestion();
		
	}
	public void init()
	{
		//XScrollPane sp= new XScrollPane();
		
		
		
		xt.setBounds(10, 10, this.getWidth()-20, 400);
		xt.setEditable(false);
		xt.setText("");
		this.add(xt);
		
		xt1.setBounds(10, 420, this.getWidth()-20, 100);
		
		this.add(xt1);
		XButton b= new XButton();
		b.setBounds(10, 520, 100, 20);
		b.setName("Submit");
		b.setLabel("Submit");
		b.addActionListener(this);
		this.add(b);
		this.nextQuestion();
		
	
		
	}
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		XDataSource ds= new XDataSource();
		ds.read(new FileReader("e:/workspace/components/datasets.xml"));
		
	XModel xm=	XProjectManager.getModel();//.getCurrentProject().getModel();
	/*xm.addModelListener(new XModelListener(){

		public void modelUpdated(String arg0, XModel arg1) {
			// TODO Auto-generated method stub
			System.out.println(" Received model updated");
		}
		
		
	}
	}, arg1)*/
	XModel xm1=((XModel)xm.get("symptomlist"));
	System.out.println("List no "+xm1.getNumChildren());
		//ds.
		Frame frame= new Frame("test");
		ChatPanel cp= new ChatPanel();
		cp.setBounds(10,10,700,500);
		frame.setBounds(10,10,800,600);
		frame.add(cp);
		frame.show();
		cp.questions=xm1;
		cp.init();
		frame.addWindowListener(new WindowListener(){

			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
				
			}

			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			
			
		});
		
		

	}
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	
		if(e.getActionCommand().equals("Submit"))
		{
			setAnswer(xt1.getText());
			xt1.setText("");
		}
	}

}
