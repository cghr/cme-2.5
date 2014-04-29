package com.kentropy.components;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.StringTokenizer;

import net.xoetrope.awt.XEdit;
import net.xoetrope.awt.XLabel;
import net.xoetrope.awt.XPanel;
import net.xoetrope.awt.XTextArea;
import net.xoetrope.xui.XPageManager;
import net.xoetrope.xui.XProjectManager;
import net.xoetrope.xui.validation.XBaseValidator;

public class TestComp extends XPanel implements KeyListener {

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		restrictDigits2(arg0);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
	
	}
	public String getValue() {
		System.out.println("Set called");
		//XPanel tt= (XPanel)comp;
		String val="";//(String)outputModel.get();
		System.out.println("Val is "+val);
		
		
		Component [] test=this.getComponents();
		int count=0;
		for (int i=0; i<test.length ;i++)
		{
		if(test[i] instanceof XEdit)
		{
			
			System.out.println("comp called"+test[i].getName());
			
			
			val=val+((count==0)?"":":")+((XEdit)test[i]).getText();
			
		count++;
		}
		}
	
		return val;
		//outputModel.set(val);
		// TODO Auto-generated method stub
		//super.set();
	}
	
	public void setValue(String val) {
		// TODO Auto-generated method stub
		//XModel oo= this.
	//	System.out.println("Get called"+comp+" "+this.getOutputPath()+" "+this.outputModel);
	//	XPanel tt= (XPanel)comp;
		//String val=(String)outputModel.get();
		System.out.println("Val is "+val);
		StringTokenizer valTok=null;
		if(val!=null)
		valTok=new StringTokenizer(val,":");
		Component [] test=this.getComponents();
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
			((XEdit)test[i]).setText("00");
		}
		}
		//super.get();
	}
	public void restrictDigits2(KeyEvent evt) {
	//	KeyEvent evt = (KeyEvent) getCurrentEvent();
		System.out.println("restrict digits2");
		String  digitsToRestrictStr="2";
		if (evt.getID() == KeyEvent.KEY_PRESSED) {
			// System.out.println("Restrict digits called. Key pressed");
			Component source1 = (Component) evt.getSource();
			if (source1 instanceof KenEdit) {
				KenEdit source = (KenEdit) source1;
			  digitsToRestrictStr=source.restrictDigit;
				if(source.getText()!=null){
				switch (evt.getKeyCode()) {
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_BACK_SPACE:
				case KeyEvent.VK_DELETE:
				case KeyEvent.VK_TAB:
					break;
				default:
					int level = XBaseValidator.LEVEL_IGNORE;
				
					if (digitsToRestrictStr != null) {
						try {
							int digitsToRestrict = Integer
									.parseInt(digitsToRestrictStr);

							if (source.getSelectedText().length() == digitsToRestrict)
								source.setText("");

							if (source.getText().length() >= digitsToRestrict) {
								evt.consume();
								java.awt.Toolkit.getDefaultToolkit().beep();
							}
							break;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	String fields="";
	String labels="";
	public TestComp() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see net.xoetrope.awt.XPanel#setAttribute(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setAttribute(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		System.out.println("Set attribute called");
		if(arg0.equals("fields"))
		{
			fields=(String)arg1;
			System.out.println("fields " +arg1);
		StringTokenizer st= new StringTokenizer(fields,",");
		int count=0;
	while(st.hasMoreTokens())
		{
		String fld=st.nextToken();
		StringTokenizer st1= new StringTokenizer(fld,":");
		String lbl1=st1.nextToken();
		XLabel lbl=new XLabel();
		lbl.setBounds( 10+(count*55),2, 50,20);
		//ed.setName(st.nextToken());
		lbl.setText(lbl1);
		this.add(lbl);
		String fld1=st1.nextToken();
		KenEdit ed=new KenEdit();
		ed.addKeyListener(this);
		ed.setAttribute("restrictDigit", "2");
	//	ed.set
	//	XAttributedComponentHelper.
		ed.setBounds( 10+(count*55),22, 30,20);
		ed.setName(fld1);
		ed.restrictDigit=st1.nextToken();
		this.add(ed);
	XPageManager pm=XProjectManager.getCurrentProject().getPageManager();
	System.out.println("Targets="+pm.getNumTargets()+" "+pm.getTarget(0));
	//String target=(Spm.getTarget(0);
//	pm.getCurrentPage("content").setAttribute("restrictDigits", "2", fld1);
		System.out.println("Adding "+ed.getName());
		count++;
		};
		}
	/*	else	if(arg0.equals("labels"))
		{
			labels=(String)arg1;
			System.out.println("labels " +arg1);
		StringTokenizer st= new StringTokenizer(labels,",");
		int count=0;
	while(st.hasMoreTokens())
		{
		XLabel ed=new XLabel();
		ed.setBounds( 10+(count*55),2, 50,20);
		//ed.setName(st.nextToken());
		ed.setText(st.nextToken());
		this.add(ed);
		System.out.println("Adding "+ed.getName());
		count++;
		};
		}*/
		else
		super.setAttribute(arg0, arg1);
		
	}

	/* (non-Javadoc)
	 * @see java.awt.Container#doLayout()
	 */
	@Override
	public void doLayout() {
		// TODO Auto-generated method stub
		super.doLayout();
	}

	public TestComp(boolean arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	public void setup()
	{
		
	}

}
