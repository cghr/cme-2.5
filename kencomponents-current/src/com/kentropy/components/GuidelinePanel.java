package com.kentropy.components;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import net.xoetrope.awt.XPanel;

public class GuidelinePanel extends XPanel {

	
	public GuidelinePanel() {
		// TODO Auto-generated constructor stub
		guideLabel.setVerticalAlignment(SwingConstants.TOP);
		this.add(guideLabel);
	}

	public GuidelinePanel(boolean arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	String guide=null;
	String g=null;
	JLabel guideLabel=new JLabel();
	public void addText(String guidelines,boolean stage) {
		// TODO Auto-generated method stub
		
		System.out.println("Mult info panel method called");
		System.out.println("Guideline::::"+guidelines);
		int w=(this.getWidth()-10);
		int h=(this.getHeight()-10);
		guideLabel.setBounds(this.getX()+5,this.getY(),w,h);
		if(guide!=null && stage==true){
			guide=g+"\n"+guidelines;
			System.out.println("Second guide***********"+guide);
		} else{
			guide=guidelines;
			g=guidelines;
			System.out.println("First guide***********"+guide);
		}
		guideLabel.setText("<html>"+guide+"</html>");
		
	}
	
	public void setFont(Font font) {
		guideLabel.setFont(font);
	}
	
	public void clearGuide(){
		System.out.println("Entered clear guide");
		guideLabel.setText("");
	}
	
}
