package com.kentropy.bindings;

import java.awt.Component;
import java.util.StringTokenizer;

import net.xoetrope.awt.XEdit;
import net.xoetrope.awt.XPanel;
import net.xoetrope.xml.XmlElement;
import net.xoetrope.xui.data.XCustomDataBinding;
import net.xoetrope.xui.data.XModel;
import net.xoetrope.xui.data.XStateBinding;

public class KenPanelBinding  extends  XStateBinding implements XCustomDataBinding {

	public void setup(Object arg0, XmlElement arg1) {
		// TODO Auto-generated method stub
		System.out.println("setup called");
		comp=arg0;
		this.setSourcePath(arg1.getAttribute("source"));
		this.setOutputPath(arg1.getAttribute("output"));
	System.out.println(arg1.getAttribute("output"));
		//super.setup( arg0, arg1);
	}
	

	public KenPanelBinding() {
		super();
		System.out.println("1>>");
	
		// TODO Auto-generated constructor stub
	}


	public KenPanelBinding(Object arg0, String arg1, XModel arg2, String arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}


	public KenPanelBinding(Object arg0, String arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}


	/* (non-Javadoc)
	 * @see net.xoetrope.xui.data.XStateBinding#get()
	 */
	@Override
	public void get() {
		// TODO Auto-generated method stub
		//XModel oo= this.
		System.out.println("Get called"+comp+" "+this.getOutputPath()+" "+this.outputModel);
		XPanel tt= (XPanel)comp;
		String val=(String)outputModel.get();
		System.out.println("Val is "+val);
		StringTokenizer valTok=null;
		if(val!=null)
		valTok=new StringTokenizer(val,":");
		Component [] test=tt.getComponents();
		for (int i=0; i<test.length ;i++)
		{
		if(test[i] instanceof XEdit)
		{
			
			System.out.println("comp called"+test[i].getName());
			if(valTok!=null && valTok.hasMoreTokens())
			{
				((XEdit)test[i]).setText(valTok.nextToken());
			}
			else
			((XEdit)test[i]).setText("test");
		}
		}
		//super.get();
	}


	/* (non-Javadoc)
	 * @see net.xoetrope.xui.data.XStateBinding#setOutput(net.xoetrope.xui.data.XModel, java.lang.String)
	 */
	@Override
	public void setOutput(XModel arg0, String arg1) {
		// TODO Auto-generated method stub
		System.out.println("Set o1 called");
		try{
		super.setOutput(arg0, arg1);
		}
		catch(Exception e)
		{
			
		}
	}


	/* (non-Javadoc)
	 * @see net.xoetrope.xui.data.XStateBinding#setOutputPath(java.lang.String)
	 */
	@Override
	public void setOutputPath(String arg0) {
		System.out.println("Set o2 called");
		// TODO Auto-generated method stub
		outputPath=arg0;
		try{
		super.setOutputPath(arg0);
		}
		catch(Exception e)
		{
			
		}
	}


	/* (non-Javadoc)
	 * @see net.xoetrope.xui.data.XStateBinding#setSource(net.xoetrope.xui.data.XModel)
	 */
	@Override
	public void setSource(XModel arg0) {
		// TODO Auto-generated method stub
		System.out.println("Set s1 called");
		//super.setSource(arg0);
	}


	/* (non-Javadoc)
	 * @see net.xoetrope.xui.data.XStateBinding#setSourcePath(java.lang.String)
	 */
	@Override
	public void setSourcePath(String arg0) {
		// TODO Auto-generated method stub
		System.out.println("Set s2 called");
		//super.setSourcePath(arg0);
	}


	/* (non-Javadoc)
	 * @see net.xoetrope.xui.data.XStateBinding#set()
	 */
	@Override
	public void set() {
		System.out.println("Set called");
		XPanel tt= (XPanel)comp;
		String val="";//(String)outputModel.get();
		System.out.println("Val is "+val);
		
		Component [] test=tt.getComponents();
		for (int i=0; i<test.length ;i++)
		{
		if(test[i] instanceof XEdit)
		{
			
			System.out.println("comp called"+test[i].getName());
			
			
			val=val+((i==0)?"":":")+((XEdit)test[i]).getText();
			
		
		}
		}
		outputModel.set(val);
		// TODO Auto-generated method stub
		//super.set();
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
