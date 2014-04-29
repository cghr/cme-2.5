/*     */ package com.kentropy.components.swing;
/*     */ 
/*     */ import com.kentropy.db.TestXUIDB;
/*     */ import com.kentropy.flow.QuestionFlowManager;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Point;
import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
import java.awt.im.InputContext;
/*     */ import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/*     */ import java.util.Vector;

import javax.swing.JViewport;
import javax.swing.Scrollable;

/*     */ import net.xoetrope.swing.XMessageBox;
/*     */ import net.xoetrope.swing.XPanel;
/*     */ import net.xoetrope.swing.XScrollPane;
/*     */ import net.xoetrope.xui.data.XBaseModel;
import net.xoetrope.xui.data.XModel;
/*     */ 
/*     */ public class QuestionFlowPanel extends XPanel
/*     */   implements ItemListener, KeyListener, MouseListener, ActionListener
/*     */ {
	       
/*  37 */   public QuestionPanel selectedQp = null;
/*  38 */   public QuestionFlowManager qfm = new QuestionFlowManager();
/*  39 */   public Vector qps = new Vector();
/*  40 */   public XModel rootModel = null;
/*  41 */   public QuestionFlowPanel parent = null;
/*  42 */   public XModel context = null;
			public String lang="en";
/*     */ 
/* 278 */   String qtype = "";
/* 279 */   String label = "";
/* 280 */   String test = "";
/* 281 */   String value = "";
/* 282 */   XModel xm = new XBaseModel();
/* 283 */   XModel qModel = null;
/* 284 */   Component comp = null;
/* 285 */   String selected = "";
/*     */ 
/* 306 */   int count = 0;
/* 307 */   public String currentContextType = "";
/*     */ 
/*     */   public void save()
/*     */   {
/*  47 */     String table = this.qfm.flowModel.get("@table").toString();
/*  48 */     String where = "";
/*  49 */     for (int i = 0; i < this.context.getNumChildren(); i++)
/*     */     {
/*  51 */       String key = this.context.get(i).getId();
/*  52 */       String value = this.context.get(i).get().toString();
/*  53 */       String tablekey = this.qfm.flowModel.get("@" + key + "Fld").toString();
/*  54 */       where = where + (i == 0 ? "" : " and ") + tablekey + "'" + value + "'";
/*     */     }
/*     */ 
/*  59 */     for (int i = 0; i < this.qps.size(); i++)
/*     */     {
/*  61 */       QuestionPanel qp = (QuestionPanel)this.qps.get(i);
/*  62 */       String field = qp.qModel.get("@field").toString();
/*  63 */       ((XModel)this.xm.get(field)).set(qp.value);
/*  64 */       System.out.println(qp.getName() + " " + qp.value);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  69 */       TestXUIDB.getInstance().saveDataM(table, where, this.xm);
/*     */     }
/*     */     catch (Exception e) {
/*  72 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void read()
/*     */   {
/*  79 */     XModel xm = new XBaseModel();
/*  80 */     String table = this.qfm.flowModel.get("@table").toString();
/*  81 */     String where = "";
/*  82 */     for (int i = 0; i < this.context.getNumChildren(); i++)
/*     */     {
/*  84 */       String key = this.context.get(i).getId();
/*  85 */       String value = this.context.get(i).get().toString();
/*  86 */       String tablekey = this.qfm.flowModel.get("@" + key + "Fld").toString();
/*  87 */       where = where + (i == 0 ? "" : " and ") + tablekey + "'" + value + "'";
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 102 */       xm = TestXUIDB.getInstance().getDataM1(table, where);
/* 103 */       this.qfm.dataModel = xm;
/*     */     }
/*     */     catch (Exception e) {
/* 106 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
Date qStartTime=null;
Date qEndTime=null;

/*     */   public void displayQuestion(XModel xm) {
/* 111 */     System.out.println(" Displa q" + xm);
/* 112 */     if (xm != null)
/*     */     {
/* 114 */       QuestionPanel qp = new QuestionPanel();
qStartTime=new Date();
qp.lang=lang;
qp.qfm=qfm;
qp.flowparams=qfm.getFlowParams();

qp.inlineflow=qfm.inlineflow;
//Locale locale=getLocale();
//Locale locale1=new Locale(locale.getCountry(),lang);

//InputContext.getInstance().selectInputMethod(locale1);
				qp.setLayout(null);
/* 115 */       qp.context = this.context;
				qp.setAttribute("border", "1");
/* 116 */       qp.currentContextType = this.currentContextType;
/* 117 */       QuestionPanel lastqp = this.qps.size() > 0 ? (QuestionPanel)this.qps.get(this.qps.size() - 1) : null;
/* 118 */       int x = 10;
/* 119 */       int y = lastqp == null ? 30 : lastqp.getHeight() + lastqp.getY() + 10;
/* 120 */       int w = getWidth() - 30;
/* 121 */       int h = xm.get("@type").equals("view") ? 400 : (xm.get("@type").equals("textarea")?200:100);
					if(xm.get("@height")!=null)
					{
						h=Integer.parseInt(xm.get("@height").toString());
					}
/* 122 */       System.out.println(x + " " + y + " " + w + " " + h);
/* 123 */       qp.setBounds(x, y, w, h);
/* 124 */       qp.rootModel = this.rootModel;
/* 125 */       add(qp);
/* 126 */       qp.setQuestionModel(xm);
/* 127 */       qp.setName(xm.getId());
/* 128 */       qp.read();
/* 129 */       this.qps.add(qp);
/* 130 */       this.selectedQp = qp;
/* 131 */   //    ((XScrollPane)getParent()).setScrollPosition(x, y - 100);
//scrollRectToVisible(new Rectangle(x,y-100,1,1));
((JViewport)getParent()).setViewPosition(new Point(x, y - 100));

/*     */     }
/*     */   }

public void setSelectedQp(QuestionPanel qp)
{
	
	int i=qps.indexOf(qp)+1;
	qfm.goTo((i-2));
	System.out.println(" Current  "+qfm.current);
	
	while(qps.size() >i)
	{
	QuestionPanel qp1=	(QuestionPanel)(qps.get(i));
	//qp1.clearValues();
	qp1.setVisible(false);
	this.remove(qp1);
	qps.remove(qp1);
	
	}
	this.validate();
	this.repaint();
}
/*     */ 
/*     */   public void scrollToCurrent() {
/* 136 */     //((XScrollPane)getParent()).setScrollPosition(10, this.selectedQp.getY());
 //((JViewport)getParent()).scrollRectToVisible(new Rectangle(10,this.selectedQp.getY()-100,1,1));   
	//((JViewport)getParent()).s
	((JViewport)getParent()).setViewPosition(new Point(10,this.selectedQp.getY()-100));
	}
/*     */ 
/*     */   public void displayContext(XModel xm) {
/* 140 */     System.out.println(" Displa q" + xm);
/* 141 */     if (xm != null)
/*     */     {
/* 143 */       QuestionPanel qp = new QuestionPanel();
/*     */ qp.lang=lang;
/* 146 */       int x = 10;
/* 147 */       int y = 30;
/* 148 */       int w = getWidth() - 30;
/* 149 */       int h = 100;
/* 150 */       System.out.println(x + " " + y + " " + w + " " + h);
/* 151 */       qp.context = this.context;
/* 152 */       qp.setBounds(x, y, w, h);
/* 153 */       add(qp);
/* 154 */       qp.setQuestionModel(xm);
/* 155 */       qp.setName(xm.getId());
/*     */ 
/* 157 */       this.qps.add(qp);
/* 158 */       //((XScrollPane)getParent()).setScrollPosition(x, y - 100);
//scrollRectToVisible(new Rectangle(x,y-100,1,1));
((JViewport)getParent()).setViewPosition(new Point(x, y - 100));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void displaySubFlow(XModel xm) {
/* 163 */     System.out.println(" Displa q" + xm);
/* 164 */     if (xm != null)
/*     */     {
/* 166 */       QuestionFlowPanel qfp = new QuestionFlowPanel();
qfp.lang=lang;
/* 167 */       qfp.rootModel = this.rootModel;
/*     */ 
/* 169 */       XModel newContext = new XBaseModel();
/*     */ 
/* 171 */       for (int i = 0; i < this.context.getNumChildren(); i++)
/*     */       {
/* 173 */         newContext.append(this.context.get(i));
/*     */       }
/*     */ 
/* 176 */       newContext.append(this.selectedQp.selectedContext);
/* 177 */       ((XModel)this.rootModel.get("currentContext")).removeChildren();
/* 178 */       ((XModel)this.rootModel.get("currentContext")).append(this.context);
/*     */ 
/* 189 */       qfp.setBounds(getX(), getY(), getWidth(), getHeight());
qfp.setPreferredSize(new Dimension(getWidth(), getHeight()));
/*     */ 
/* 194 */      // getParent().add(qfp);
//getParent().add(qfp);
((JViewport)getParent()).setView(qfp);

/* 195 */       qfp.setName(xm.getId());
/* 196 */       this.rootModel.set("currentPanel", xm.getId());
/*     */ 
/* 198 */       qfp.parent = this;
/* 199 */       qfp.context = newContext;
/* 200 */       qfp.currentContextType = this.selectedQp.selectedContext.getId();
XModel flowParams=new XBaseModel();
try {
	flowParams = TestXUIDB.getInstance().getFlowParameters(newContext, qfp.currentContextType);
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
/* 201 */       qfp.setQuestionFlowModel(xm,flowParams);
/*     */     }
/*     */   }
public void displayInlineFlow(XModel xm) {
	/* 163 */     System.out.println(" Displa q" + xm);
	/* 164 */     if (xm != null)
	/*     */     {
	/* 166 */       QuestionFlowPanel qfp = new QuestionFlowPanel();
	/* 167 */       qfp.rootModel = this.rootModel;
	/*     */ 
	/* 169 */       XModel newContext = new XBaseModel();
	/*     */ 
	/* 171 */      for (int i = 0; i < this.context.getNumChildren(); i++)
	/*     */       {
	/* 173 */         newContext.append(this.context.get(i));
	/*     */       }
	/*     */ 
	/* 176 */       //newContext.append(this.selectedQp.selectedContext);
	/* 177 */       //((XModel)this.rootModel.get("currentContext")).removeChildren();
	/* 178 */       //((XModel)this.rootModel.get("currentContext")).append(this.context);
	/*     */ 
	/* 189 */       qfp.setBounds(getX(), getY(), getWidth(), getHeight());
	/*     */ 
	/* 194 */       getParent().add(qfp);
	/* 195 */       qfp.setName(xm.getId());
	/* 196 */       this.rootModel.set("currentPanel", xm.getId());
	/*     */ 
	/* 198 */       qfp.parent = this;
	/* 199 */       qfp.context = newContext;
	/* 200 */       qfp.currentContextType = this.selectedQp.selectedContext.getId();
/*	XModel flowParams=new XBaseModel();
	try {
		flowParams = TestXUIDB.getInstance().getFlowParameters(newContext, qfp.currentContextType);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
	/* 201 */       qfp.setQuestionFlowModel(xm,qfm.flowParams);
	/*     */     }
	/*     */   }
/*     */ 
/*     */   public void actionPerformed(ActionEvent e) {
/* 206 */     System.out.println(e.getSource() + " " + e.getActionCommand());
/* 207 */     if (e.getActionCommand().equals("Next"))
/*     */     {
/* 209 */       QuestionPanel qp = (QuestionPanel)this.qps.get(this.qps.size() - 1);
SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
try {
	TestXUIDB.getInstance().saveFlowQuestion(id, flow,(String)((XModel)context.get("surveyor")).get(), (String)qp.qModel.get("@qno"), sdf.format(qStartTime), sdf.format(new Date()));
} catch (Exception e1) {
	// TODO Auto-generated catch block
	e1.printStackTrace();
}
/* 210 */       String field = (String)qp.qModel.get("@field");
/* 211 */       String value1 = qp.value.toString();
/* 212 */       if (field != null)
/*     */       {
/* 214 */         this.qfm.dataModel.set("@" + field, this.value);
/*     */       }
/* 216 */       XModel nextQuestion = this.qfm.nextQuestion();
/* 217 */       if (nextQuestion != null)
{
	qp.catchAllFocusEvents(qp);
	
/* 218 */         displayQuestion(nextQuestion);
}
/*     */       else {
/* 220 */         displayMsg("Flow completed.Press the Close Button");
this.status="Complete";
}

/*     */     }
/*     */ 
/* 224 */     if (e.getActionCommand().equals("Add"))
/*     */     {
/* 226 */       QuestionPanel qp = (QuestionPanel)((Component)e.getSource()).getParent();
/* 227 */       displaySubFlow((XModel)this.rootModel.get("flows/" + qp.qModel.get("@subflow")));

/*     */     }
/*     */ 
/* 230 */     if (e.getActionCommand().equals("Edit"))
/*     */     {
/* 232 */       QuestionPanel qp = (QuestionPanel)((Component)e.getSource()).getParent();
/* 233 */       displaySubFlow((XModel)this.rootModel.get("flows/" + qp.qModel.get("@subflow")));
/*     */     }
/*     */   }
/*     */ 

public String id="";
public void close()
{
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	try {
		//SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		TestXUIDB.getInstance().saveFlow(id,flow, this.getContextStr(),(String)((XModel)context.get("surveyor")).get(), status ,sdf.format(startTime), sdf.format(new Date()));
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	/*     */       

	
}
public String status="In Process";
public String ilineflow=null;
private Date startTime;
private String flow;
/*     */   public void mouseClicked(MouseEvent e)
/*     */   {
/* 243 */     System.out.println(e.getSource());
/*     */   }
/*     */ 
/*     */   public void mouseEntered(MouseEvent e)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void mouseExited(MouseEvent e)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void mousePressed(MouseEvent e)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void mouseReleased(MouseEvent e)
/*     */   {
/*     */   }
/*     */ 
/*     */   public QuestionFlowPanel()
/*     */   {
	this.setAutoscrolls(true);
	//lang=InputContext.getInstance().getLocale().getLanguage();
/*     */   }
/*     */ 
/*     */   public QuestionFlowPanel(boolean arg0)
/*     */   {
/* 291 */     super();
/*     */   }
/*     */ 
/*     */   public void createQuestion()
/*     */   {
/*     */   }
/*     */ 
public String getContextStr()
{
	String val = "";
	/*  537 */       for (int i = 0; i < this.context.getNumChildren(); i++)
	/*      */       {
	/*  539 */         String id = this.context.get(i).getId();
	/*  540 */         Object tmpVal = (String)this.context.get(i).get();
	/*  541 */         val = val + (tmpVal != null ? " " + id + ":" + tmpVal : "");
	/*      */       }
	/*      */      val += " Language :"+lang;// InputContext.getInstance().getLocale().getDisplayLanguage()+"("+InputContext.getInstance().getLocale().getLanguage()+")";
return val;
}
/*     */   public void setQuestionFlowModel(XModel xm,XModel params)
/*     */   {
/* 310 */     if (this.count == 0)
/*     */     {
	this.startTime=new Date();
	this.flow=(String)xm.getId();
	SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	id="/va/"+TestXUIDB.getInstance().getCurrentUser()+sdf.format(new Date());
	
/* 312 */       XModel xm1 = new XBaseModel();
/* 313 */       xm1.setId("context");
/* 314 */       xm1.set("@type", "context");
/* 315 */       xm1.set("@qno", "");
/* 316 */       ((XModel)xm1.get("text")).set("context");
/*     */ 
/* 318 */       displayContext(xm1);
/* 319 */       this.count += 1;
/* 320 */       this.currentContextType = xm.get("@context").toString();
/*     */     }
/* 322 */     this.qfm.setFlowModel(xm);
this.qfm.setFlowParams(params);
qfm.context=context;
qfm.currentContextType=this.currentContextType;
/* 323 */     XModel next = this.qfm.nextQuestion();
/*     */ 
/* 325 */     displayQuestion(next);

/*     */   }
/*     */ 
/*     */   public void displayMsg(String msg)
/*     */   {
/* 331 */     XMessageBox mbox = new XMessageBox();
/* 332 */     Dimension size = getSize();
/* 333 */     Point location = getLocationOnScreen();
/* 334 */     size = new Dimension(size.width + 2 * location.x, size.height + 2 * location.y);
/*     */ 
/* 336 */     mbox.setup("Message", msg, size, this);
/*     */   }
/*     */ 
/*     */   public void setAttribute(String arg0, Object arg1)
/*     */   {
/* 345 */     arg0.equals("question");
/*     */ 
/* 349 */    super.setAttribute(arg0, arg1);
/*     */   }
/*     */ 
/*     */   public void itemStateChanged(ItemEvent e)
/*     */   {
/* 358 */     System.out.println(e.getID() + " " + e.getItem());
/* 359 */     this.selected = e.getItem().toString();
/*     */   }
/*     */ 
/*     */   public void keyPressed(KeyEvent e)
/*     */   {
/* 368 */     System.out.println("QFP Key pressed" + e.getKeyChar());
/*     */   }
/*     */ 
/*     */   public void keyReleased(KeyEvent e)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void keyTyped(KeyEvent e)
/*     */   {
/*     */   }
/*     */
public Dimension getPreferredScrollableViewportSize() {
	// TODO Auto-generated method stub
	Dimension d = new Dimension();
	d.setSize(800, 10000);
	return this.getPreferredSize();
}
public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2) {
	// TODO Auto-generated method stub
	return 10;
}
public boolean getScrollableTracksViewportHeight() {
	// TODO Auto-generated method stub
	return false;
}
public boolean getScrollableTracksViewportWidth() {
	// TODO Auto-generated method stub
	return false;
}
public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
	// TODO Auto-generated method stub
	return 10;
} }

/* Location:           D:\enumeration\bin\
 * Qualified Name:     com.kentropy.components.QuestionFlowPanel
 * JD-Core Version:    0.6.0
 */