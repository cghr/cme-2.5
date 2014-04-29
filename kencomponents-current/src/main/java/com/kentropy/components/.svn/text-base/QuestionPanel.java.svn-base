/*      */ package com.kentropy.components;
/*      */ 
/*      */ import com.kentropy.db.TestXUIDB;
/*      */ import com.kentropy.flow.Age;
/*      */ import com.kentropy.flow.QuestionFlowManager;
/*      */ import com.kentropy.gps.GPStest;
/*      */ import com.kentropy.iterators.Iterator;
import com.theotherbell.ui.DateField;
import com.theotherbell.ui.DatePicker;

/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.ItemEvent;
/*      */ import java.awt.event.ItemListener;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.KeyListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
/*      */ import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;

/*      */ import net.xoetrope.awt.XButton;
/*      */ import net.xoetrope.awt.XComboBox;
/*      */ import net.xoetrope.awt.XEdit;
/*      */ import net.xoetrope.awt.XLabel;
/*      */ import net.xoetrope.awt.XMessageBox;
/*      */ import net.xoetrope.awt.XPanel;
/*      */ import net.xoetrope.awt.XTable;
/*      */ import net.xoetrope.awt.XTextArea;
/*      */ import net.xoetrope.xui.PageSupport;
/*      */ import net.xoetrope.xui.XPageManager;
/*      */ import net.xoetrope.xui.XProjectManager;
/*      */ import net.xoetrope.xui.XTextHolder;
/*      */ import net.xoetrope.xui.data.XBaseModel;
/*      */ import net.xoetrope.xui.data.XListBinding;
import net.xoetrope.xui.data.XModel;
/*      */ 
/*      */ public class QuestionPanel extends XPanel
/*      */   implements ItemListener, KeyListener, MouseListener, ActionListener, FocusListener
/*      */ {
/*   44 */   XTable view = null;
/*   45 */   XModel viewModel = null;
/*   46 */   XModel rootModel = null;
/*   47 */   String currentContextType = "";
/*      */ 
/*  389 */   public Component[] gpsComps = new Component[2];
/*      */ 
/*  481 */   String qtype = "";
/*  482 */   String label = "";
/*  483 */   String test = "";
/*  484 */   StringBuffer value = new StringBuffer();
/*  485 */   XBaseModel dataM = new XBaseModel();
/*  486 */   XModel xm = new XBaseModel();
/*  487 */   XModel qModel = null;
/*  488 */   Component comp = null;
/*  489 */   String selected = "";
/*      */ 
/*  511 */   public XModel context = null;
/*  512 */   public XModel selectedContext = null;
/*      */ 
/*  996 */   public Object[] viewElements = new Object[3];
/*      */ 
/*      */   public boolean checkNumeric(String value1)
/*      */   {
/*   52 */     for (int i = 0; i < value1.length(); i++)
/*      */     {
/*   54 */       String val = value1.substring(i, i + 1);
/*      */       try {
/*   56 */         Integer.parseInt(val);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*   60 */         e.printStackTrace();
/*   61 */         return false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*   67 */     return true;
/*      */   }
/*      */ 
public boolean checkDate(String value1)
/*      */   {
/*   52 */     
/*      */    
	/*  117 */       
	String format=this.qModel.get("@format")!=null?this.qModel.get("@format").toString():"dd/MM/yyyy";
		SimpleDateFormat sdf= new SimpleDateFormat(format);
		try {
			sdf.setLenient(false);
			
			sdf.parse(value.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	/*      */ 
	/*  125 */     //  return (val.compareTo(range1[0]) > 0) && (val.compareTo(range1[1]) < 0);
	/*      */     
	/*      */ 
/*      */ 
/*   67 */     return true;
/*      */   }
/*      */ 
/*      */   public void focusGained(FocusEvent e)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void focusLost(FocusEvent e)
/*      */   {
/*   80 */     System.out.println("Focus " + e.getSource() + " ");
/*      */   }
/*      */ 
/*      */   public void addValue(Object comp)
/*      */   {
/*   85 */     String tmpValue = "";
/*   86 */     if ((comp instanceof XComboBox))
/*      */     {
/*   88 */       tmpValue = ((XComboBox)comp).getSelectedItem();
/*      */     }
/*   90 */     if ((comp instanceof XEdit))
/*      */     {
/*   92 */       tmpValue = ((XEdit)comp).getText();
/*   93 */       System.out.println("Comp " + ((Component)comp).getName() + " " + tmpValue);
/*      */     }
/*   95 */     if ((comp instanceof XTextArea))
/*      */     {
/*   97 */       tmpValue = ((XTextArea)comp).getText();
/*      */     }
if ((comp instanceof DateField))
/*      */     {
/*   97 */       tmpValue = ((DateField)comp).getDate().toString();
/*      */     }
/*   99 */     if (!tmpValue.equals(""))
/*      */     {
/*  101 */       System.out.println(this.value + " " + tmpValue);
/*  102 */       this.value.append((this.value.toString().equals("") ? "" : ",") + tmpValue);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean rangeCheck()
/*      */   {
/*  110 */     String range = this.qModel.get("@range").toString();
/*  111 */     String[] range1 = range.split("-");
/*      */ 
/*  113 */     if (this.qModel.get("@type").equals("age"))
/*      */     {
/*  117 */       if (this.value.toString().split(",").length < 2)
/*      */       {
/*  119 */         return false;
/*      */       }
/*  121 */       Age val = new Age(this.value.toString());
/*      */ 
/*  125 */       return (val.compareTo(range1[0]) >= 0) && (val.compareTo(range1[1]) <= 0);
/*      */     }


/*  133 */     if ((this.qModel.get("@inputtype") != null) && (this.qModel.get("@inputtype").equals("numeric")))
/*      */     {
/*  135 */       int lower = Integer.parseInt(range1[0]);
/*  136 */       int upper = Integer.parseInt(range1[1]);
/*  137 */       int val1 = Integer.parseInt(this.value.toString());
/*      */ 
/*  139 */       return (val1 >= lower) && (val1 <= upper);
/*      */     }
/*  133 */     if ((this.qModel.get("@inputtype") != null) && (this.qModel.get("@inputtype").equals("date")))
/*      */     {
	try{
	SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd");
	String format=(String)this.qModel.get("@format");
	format=format==null?"dd/MM/yyyy":format;
	SimpleDateFormat sdf1= new SimpleDateFormat(format);
	
/*  135 */       Date lower = sdf.parse(range1[0].equals("Now")?sdf.format(new Date()):range1[0]);
/*  136 */       Date upper = sdf.parse(range1[1].equals("Now")?sdf.format(new Date()):range1[1]);
/*  137 */       Date val1 = sdf1.parse(this.value.toString());
/*      */ 
/*  139 */       return (val1.compareTo(lower)>=0) && (val1.compareTo(upper)<=0);
	}
	catch(Exception e)
	{
		e.printStackTrace();
		return true;
	}
/*      */     }
/*      */ 
/*  148 */     return (this.value.toString().compareTo(range1[0]) > 0) && (this.value.toString().compareTo(range1[1]) < 0);
/*      */   }
/*      */ 
public String rangeToStr(String range,String type)
{
	
	try{
		if(type!=null && type.equals("date"))
		{
	String[] range1 = range.split("-");
	SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd");
	String format=(String)this.qModel.get("@format");
	format=format==null?"dd/MM/yyyy":format;
	SimpleDateFormat sdf1= new SimpleDateFormat(format);
	
/*  135 */       Date lower = sdf.parse(range1[0].equals("Now")?sdf.format(new Date()):range1[0]);
/*  136 */       Date upper = sdf.parse(range1[1].equals("Now")?sdf.format(new Date()):range1[1]);
	
return sdf1.format(lower)+"-"+sdf1.format(upper);
		}
		else
			return range;

	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	return range;
}
/*      */   public void saveValues()
/*      */   {
/*  158 */     this.value = new StringBuffer();
/*  159 */     for (int i = 0; i < getComponentCount(); i++)
/*      */     {
/*  161 */       addValue(getComponent(i));
/*      */     }
/*  163 */     XModel dataModel = (XModel)((QuestionFlowPanel)getParent()).qfm.dataModel.get(this.qModel.get("@qno").toString());
/*  164 */     dataModel.set(this.value.toString());
/*      */   }
/*      */ 
/*      */   public void actionPerformed(ActionEvent e)
/*      */   {
/*  172 */     System.out.println(" Actuon " + e.getSource() + " " + e.getActionCommand());
/*  173 */     if (e.getActionCommand().equals("GetGPS"))
/*      */     {
/*  175 */       getGpsCoordinates1();
/*      */     }
/*      */ 
/*  178 */     if (e.getActionCommand().equals("Refresh"))
/*      */     {
/*  180 */       if (this.qModel.get("@type").equals("view"))
/*      */       {
/*      */         try
/*      */         {
/*  184 */           readViewModel();
/*      */         }
/*      */         catch (Exception e1) {
/*  187 */           e1.printStackTrace();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  195 */     if (e.getActionCommand().equals("Next"))
/*      */     {
/*  197 */       saveValues();
/*  198 */       if (this.qModel.get("@range") != null)
/*      */       {
/*  200 */         if (!rangeCheck())
/*      */         {
/*  202 */           displayError("Value out of range (" + this.rangeToStr(this.qModel.get("@range").toString(),this.qModel.get("@inputtype").toString())+")");
/*  203 */           return;
/*      */         }
/*      */ 
/*      */       }


if (this.qModel.get("@regex") != null)
/*      */       {
/*  200 */       if(!value.toString().matches(qModel.get("@regex").toString()))
{
			
/*  202 */           displayError("Value not in correct format (" + this.qModel.get("@formatstr")+")");
/*  203 */           return;
}
/*      */         
/*      */ 
/*      */       }
/*      */ 
/*  208 */       if (this.qModel.get("@minchars") != null)
/*      */       {
/*  210 */         int minchars = Integer.parseInt(this.qModel.get("@minchars").toString());
/*  211 */         if (minchars > this.value.length())
/*      */         {
/*  213 */           displayError("Please enter at least " + minchars + " characters ");
/*  214 */           return;
/*      */         }
/*      */       }
/*  217 */       if (this.qModel.get("@inputtype") != null)
/*      */       {
/*  219 */         String inputtype = this.qModel.get("@inputtype").toString();
/*  220 */         if (inputtype.equals("numeric"))
/*      */         {
/*  222 */           if (!checkNumeric(this.value.toString()))
/*      */           {
/*  224 */             displayError("Please enter only numbers ");
/*  225 */             return;
/*      */           }
/*      */         }
if (inputtype.equals("date"))
/*      */         {
/*  222 */           if (!checkDate(this.value.toString()))
/*      */           {
	String format =this.qModel.get("@format")==null?"dd/MM/yyyy":this.qModel.get("@format").toString();
/*  224 */             displayError("Please enter valid date value  ("+format+")");
/*  225 */             return;
/*      */           }
/*      */         }
/*      */       }

/*  229 */       if (this.qModel.get("@maxtext") != null)
/*      */       {
/*  231 */         String max = "";
/*  232 */         if (this.qModel.get("@maxno") != null)
/*      */         {
/*  234 */           max = this.qModel.get("@maxno").toString();
/*      */         }
/*      */         else
/*      */         {
/*  239 */           max = ((XTextHolder)((Object[])this.viewElements[1])[1]).getText();
/*      */         }
/*      */ 
/*  242 */         if ((this.view.getModel() != null) && (Integer.parseInt(max) > this.view.getModel().getNumChildren() - 1))
/*      */         {
/*  244 */           displayError("You have not added all the rows");
/*  245 */           return;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  250 */       save();
/*      */ 
/*  252 */       ((QuestionFlowPanel)getParent()).actionPerformed(e);
/*      */ 
/*  255 */     //  (e.getSource() instanceof XEdit);
/*      */     }
/*      */ 
/*  261 */     if (e.getActionCommand().equals("Add"))
/*      */     {
/*      */       try
/*      */       {
/*  265 */         if (this.qModel.get("@maxtext") != null)
/*      */         {
/*  267 */           String max = "";
/*  268 */           if (this.qModel.get("@maxno") != null)
/*      */           {
/*  270 */             max = this.qModel.get("@maxno").toString();
/*      */           }
/*      */           else
/*      */           {
/*  275 */             max = ((XTextHolder)((Object[])this.viewElements[1])[1]).getText();
/*      */           }
/*      */ 
/*  278 */           if ((this.view.getModel() != null) && (Integer.parseInt(max) == this.view.getModel().getNumChildren() - 1))
/*      */           {
/*  280 */             displayError("MAX Rows reached");
/*  281 */             return;
/*      */           }
/*      */         }
/*      */ 
/*  285 */         saveValues();
/*      */ 
/*  289 */         String subflow = this.qModel.get("@subflow").toString();
/*  290 */         XModel flowM = (XModel)this.rootModel.get("flows/" + subflow);
/*  291 */         String contextType = flowM.get("@context").toString();
/*  292 */         String id = TestXUIDB.getInstance().getMaxId(this.context, contextType);
/*  293 */         System.out.println(" next context " + contextType + " " + id);
/*  294 */         this.selectedContext = new XBaseModel();
/*  295 */         this.selectedContext.setId(contextType);
/*  296 */         this.selectedContext.set(id);
/*  297 */         ((QuestionFlowPanel)getParent()).actionPerformed(e);
/*      */       }
/*      */       catch (Exception e1) {
/*  300 */         e1.printStackTrace();
/*      */       }
/*      */ 
/*      */     }
/*  305 */     else if (e.getActionCommand().equals("Edit"))
/*      */     {
/*  307 */       saveValues();
/*  308 */       int row = this.view.getSelectedRow();
/*  309 */       String id = this.view.getModel().get(row + 1).getId();
/*  310 */       System.out.println("id =" + id + " row=" + row);
/*      */       try
/*      */       {
/*  313 */         String subflow = this.qModel.get("@subflow").toString();
/*  314 */         XModel flowM = (XModel)this.rootModel.get("flows/" + subflow);
/*  315 */         String contextType = flowM.get("@context").toString();
/*      */ 
/*  317 */         this.selectedContext = new XBaseModel();
/*  318 */         this.selectedContext.setId(contextType);
/*  319 */         this.selectedContext.set(id);
/*  320 */         ((QuestionFlowPanel)getParent()).actionPerformed(e);
/*      */       }
/*      */       catch (Exception e1) {
/*  323 */         e1.printStackTrace();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void mouseClicked(MouseEvent e)
/*      */   {
/*  334 */     System.out.println(e.getSource());
/*      */   }
/*      */ 
/*      */   public void mouseEntered(MouseEvent e)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void mouseExited(MouseEvent e)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void mousePressed(MouseEvent e)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void mouseReleased(MouseEvent e)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void save()
/*      */   {
/*  370 */     String fieldStr = (String)this.qModel.get("@fields");
/*  371 */     System.out.println(" Fields =" + fieldStr + " " + this.value);
/*  372 */     if (fieldStr == null)
/*  373 */       return;
/*  374 */     String[] fields = fieldStr.split(",");
/*  375 */     String[] values = this.value.toString().split(",");
/*  376 */     for (int i = 0; (i < fields.length) && (fields.length == values.length); i++)
/*      */     {
/*  378 */       ((XModel)this.dataM.get(fields[i])).set(values[i]);
/*  379 */       System.out.println(fields[i] + " " + values[i]);
/*      */     }
/*      */     try
/*      */     {
/*  383 */       TestXUIDB.getInstance().saveEnumData(this.context, this.currentContextType, this.dataM);
/*      */     }
/*      */     catch (Exception e) {
/*  386 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void getGpsCoordinates1()
/*      */   {
/*      */     try
/*      */     {
/*  396 */       String[] gps = GPStest.getGPS("127.0.0.1");
/*      */ 
/*  398 */       ((XEdit)this.gpsComps[0]).setText(gps[0]);
/*  399 */       ((XEdit)this.gpsComps[1]).setText(gps[1]);
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  409 */       displayError("GPS is not fixed, do you want to continue without GPS?");
/*  410 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void read()
/*      */   {
/*  416 */     String fieldStr = (String)this.qModel.get("@fields");
/*  417 */     System.out.println(" Fields =" + fieldStr + " " + this.value);
/*  418 */     if (fieldStr == null) {
/*  419 */       return;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  425 */       XModel dataM = TestXUIDB.getInstance().getEnumData(this.context, this.currentContextType, fieldStr);
/*  426 */       System.out.println("No of values read" + dataM.getNumChildren());
/*  427 */       if (dataM.getNumChildren() > 0) {
/*  428 */         int index = 0;
/*  429 */         System.out.println("No of fields read" + dataM.get(0).getNumChildren());
/*  430 */         for (int i = 0; i < dataM.get(0).getNumChildren(); i++)
/*      */         {
/*  432 */           String value1 = dataM.get(0).get(i).get().toString();
/*  433 */           System.out.println("Value " + value1);
/*  434 */           index = setNextValue(index + 1, value1);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception e) {
/*  439 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int setNextValue(int index, String value)
/*      */   {
/*  445 */     Component[] comps = getComponents();
/*      */ 
/*  447 */     for (int i = index; i < comps.length; i++)
/*      */     {
/*  449 */       boolean flg = setValue(comps[i], value);
/*  450 */       if (flg) {
/*  451 */         return i;
/*      */       }
/*      */     }
/*  454 */     return index;
/*      */   }
/*      */ 
/*      */   public boolean setValue(Object comp, String value)
/*      */   {
/*  460 */     boolean set = false;
/*      */ 
/*  462 */     if ((comp instanceof XComboBox))
/*      */     {
/*  464 */       ((XComboBox)comp).select(value);
/*  465 */       set = true;
/*      */     }
/*  467 */     if ((comp instanceof XEdit))
/*      */     {
/*  469 */       ((XEdit)comp).setText(value);
/*  470 */       set = true;
/*      */     }
/*  472 */     if ((comp instanceof XTextArea))
/*      */     {
/*  474 */       ((XTextArea)comp).setText(value);
/*  475 */       set = true;
/*      */     }
/*      */ 
/*  478 */     return set;
/*      */   }
/*      */ 
/*      */   public QuestionPanel()
/*      */   {
/*      */   }
/*      */ 
/*      */   public QuestionPanel(boolean arg0)
/*      */   {
/*  495 */     super(arg0);
/*      */   }
/*      */ 
/*      */   public void createQuestion()
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setQuestionModel(XModel xm)
/*      */   {
/*  515 */     this.qModel = xm;
				String formatStr=(String)xm.get("@formatstr");
				formatStr=formatStr!=null?("("+formatStr+")"):"";
/*  516 */     String label = ((XModel)xm.get("text")).get().toString()+" "+formatStr;
/*  517 */     String controltype = xm.get("@type").toString();
/*  518 */     String qno = xm.get("@qno").toString();
/*  519 */     XLabel qLabel = new XLabel();
/*  520 */     qLabel.setText(label);
/*  521 */     qLabel.setName(qno + "Lbl");
/*  522 */     qLabel.setBounds(10, 10, label.length() * 8, 30);
/*  523 */     qLabel.setAttribute("style", "Panel/Caption");
/*  524 */     add(qLabel);
/*  525 */     Object readonly = xm.get("@readonly");
/*  526 */     if ((readonly != null) && (!controltype.equals("view")))
/*      */     {
/*  528 */       XLabel vLabel = new XLabel();
/*  529 */       vLabel.setText(xm.get("@value").toString());
/*  530 */       vLabel.setBounds(10, 50, 100, 30);
/*  531 */       add(vLabel);
/*  532 */       return;
/*      */     }
/*      */ 
/*  535 */     if (controltype.equals("context")) {
/*  536 */       String val = "";
/*  537 */       for (int i = 0; i < this.context.getNumChildren(); i++)
/*      */       {
/*  539 */         String id = this.context.get(i).getId();
/*  540 */         Object tmpVal = (String)this.context.get(i).get();
/*  541 */         val = val + (tmpVal != null ? " " + id + ":" + tmpVal : "");
/*      */       }
/*      */ 
/*  544 */       XLabel vLabel = new XLabel();
/*  545 */       vLabel.setText(val);
/*  546 */       vLabel.setBounds(10, 50, getWidth(), 30);
/*  547 */       add(vLabel);
/*  548 */       return;
/*      */     }
/*      */ 
/*  551 */     if (controltype.equals("choice"))
/*      */     {
/*  553 */       XModel choices = (XModel)xm.get("options");
/*  554 */       if (choices.get("@lookupfunc") != null)
/*      */       {
/*  556 */         Object test = XProjectManager.getPageManager().getCurrentPage(null).evaluateAttribute(choices.get("@lookupfunc").toString());
/*      */ 
/*  558 */         if (test != null)
/*      */         {
/*  560 */           choices = (XModel)test;
/*  561 */           System.out.println(" Lookup " + test + " " + choices.getNumChildren());
/*      */         }
/*      */       }
/*      */ 
/*  565 */       this.xm = choices;
/*  566 */       XComboBox combo = new XComboBox();
/*  567 */       int w = 100;
/*  568 */       if (this.qModel.get("@width") != null)
/*  569 */         w = Integer.parseInt(this.qModel.get("@width").toString());
/*  570 */       combo.setName(qno);
/*  571 */       combo.setBounds(10, 50, w, 30);
/*  572 */       XListBinding xl = new XListBinding();
/*      */ 
/*  578 */       for (int i = 0; i < choices.getNumChildren(); i++)
/*      */       {
/*  580 */         String item = choices.get(i).get().toString();
/*      */ 
/*  582 */         combo.add(item);
/*      */       }
/*  584 */       combo.addKeyListener(this);
/*  585 */       combo.select(xm.get("@value"));
/*  586 */       combo.addFocusListener(this);
/*      */ 
/*  588 */       add(combo);
/*  589 */       transferFocus();
/*      */     }
/*  593 */     else if (controltype.equals("view"))
/*      */     {
/*  597 */       displayView(xm);
/*      */     }
/*  601 */     else if (controltype.equals("gps"))
/*      */     {
/*  605 */       displayGPS(xm);
/*      */     }
/*  609 */     else if (controltype.equals("age"))
/*      */     {
/*  613 */       displayAge(xm);
/*      */     }
else if (controltype.equals("date"))
/*      */     {
/*  613 */       displayDate(xm);
/*      */     }
else if (controltype.equals("narrative"))
/*      */     {
/*  613 */       displayNarrative(xm);
/*      */     }
/*  617 */     else if (controltype==null || controltype.equals("text"))
/*      */     {
/*  620 */       XEdit edit = new XEdit();
/*  621 */       int width = 100;
/*  622 */       if (this.qModel.get("@width") != null)
/*      */       {
/*  624 */         width = Integer.parseInt(this.qModel.get("@width").toString());
/*      */       }
/*  626 */       edit.setBounds(10, 50, width, 30);
/*  627 */       edit.addFocusListener(this);
/*  628 */       edit.addKeyListener(this);
/*  629 */       edit.setName(qno);
/*  630 */       add(edit);
/*  631 */       transferFocus();
/*      */     }
else if ( controltype.equals("textarea"))
/*      */     {
	/*  620 */       XTextArea edit = new XTextArea();
	/*  621 */       int width = 100;
	/*  622 */       if (this.qModel.get("@width") != null)
	/*      */       {
	/*  624 */         width = Integer.parseInt(this.qModel.get("@width").toString());
	/*      */       }
	/*  626 */       edit.setBounds(10, 50, width, 80);
	/*  627 */       edit.addFocusListener(this);
	/*  628 */       edit.addKeyListener(this);
	/*  629 */       edit.setName(qno);
	/*  630 */       add(edit);
	/*  631 */       transferFocus();
	/*      */     }

/*      */   }
/*      */ 
/*      */   public void displayAge(XModel m)
/*      */   {
/*  638 */     int y = 40;
/*  639 */     XLabel ageLbl = new XLabel();
/*  640 */     ageLbl.setText("age");
/*  641 */     ageLbl.setBounds(10, y, 50, 20);
/*  642 */     add(ageLbl);
/*      */ 
/*  644 */     XEdit age = new XEdit();
/*  645 */     age.setName("age");
/*  646 */     age.setBounds(70, y, 50, 20);
/*  647 */     add(age);
/*  648 */     XLabel ageUnitLbl = new XLabel();
/*  649 */     ageUnitLbl.setText("unit");
/*  650 */     ageUnitLbl.setBounds(130, y, 50, 20);
/*  651 */     add(ageUnitLbl);
/*  652 */     XComboBox ageUnit = new XComboBox();
/*  653 */     if (this.qModel.get("@units") != null)
/*      */     {
/*  655 */       String[] units1 = this.qModel.get("@units").toString().split(",");
/*  656 */       for (int i = 0; i < units1.length; i++)
/*      */       {
/*  658 */         ageUnit.add(units1[i]);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  663 */       ageUnit.add("Y");
/*  664 */       ageUnit.add("M");
/*  665 */       ageUnit.add("D");
/*  666 */       ageUnit.setName("ageUnit");
/*      */     }
/*      */ 
/*  669 */     ageUnit.setBounds(190, y, 50, 20);
/*  670 */     add(ageUnit);
/*      */ 
/*  676 */     XButton xb2 = new XButton();
/*  677 */     xb2.setLabel("Next");
/*  678 */     xb2.setBounds(310, y, 50, 20);
/*  679 */     add(xb2);
/*  680 */     xb2.addActionListener(this);
/*      */   }
public void displayDate(XModel m)
/*      */   {
/*  638 */ 
	
	//com.theotherbell.ui.DatePicker
	XEdit df= new XEdit();
	df.setBounds(50,10,200,40);
	df.setName(m.getId()+"_dt");
	df.setVisible(true);
//	df.s
	
	//df.addKeyListener(this);
add(df);
XButton xb2 = new XButton();
/*  713 */     xb2.setLabel("Next");
/*  714 */     xb2.setBounds(260, 10, 50, 20);
/*  715 */     add(xb2);
xb2.addActionListener(this);

/*      */   }



public void displayNarrative(XModel m)
/*      */   {
/*  638 */ 
	
	//df.addKeyListener(this);
    XButton xb1 = new XButton();
    /*  713 */     xb1.setLabel("Open Text Narrative");
    /*  714 */     xb1.setBounds(50, 10, 150, 20);
    /*  715 */     add(xb1);
    xb1.addActionListener(new ActionListener()
    {

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			openNarrative();
		}
		public void openNarrative()
		{
			JFrame frame = new JTextAreaDemo();
		    frame.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {System.exit(0);}
		    });

		    frame.pack();
		    frame.setVisible(true);
		    frame.setSize( 800, 600);
		}
    	
    }
    
    );
    XButton xb11 = new XButton();
    /*  713 */     xb11.setLabel("Open Image Narrative");
    /*  714 */     xb11.setBounds(150, 10, 250, 20);
    /*  715 */     add(xb11);
    xb11.addActionListener(new ActionListener()
    {

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			openNarrative();
		}
		public void openNarrative()
		{
			
		    new Scribble("test");
		}
    	
    }
    
    );
XButton xb2 = new XButton();
/*  713 */     xb2.setLabel("Next");
/*  714 */     xb2.setBounds(260, 10, 50, 20);
/*  715 */     add(xb2);
xb2.addActionListener(this);

/*      */   }

/*      */ 
/*      */   public void displayGPS(XModel m)
/*      */   {
/*  687 */     int y = 40;
/*  688 */     XLabel latLbl = new XLabel();
/*  689 */     latLbl.setText("lat");
/*  690 */     latLbl.setBounds(10, y, 50, 20);
/*  691 */     add(latLbl);
/*      */ 
/*  693 */     XEdit lat = new XEdit();
/*  694 */     lat.setName("lat");
/*  695 */     lat.setBounds(70, y, 50, 20);
/*  696 */     add(lat);
/*  697 */     this.gpsComps[0] = lat;
/*  698 */     XLabel longLbl = new XLabel();
/*  699 */     longLbl.setText("long");
/*  700 */     longLbl.setBounds(130, y, 50, 20);
/*  701 */     add(longLbl);
/*  702 */     XEdit longi = new XEdit();
/*  703 */     longi.setName("long");
/*  704 */     longi.setBounds(190, y, 50, 20);
/*  705 */     add(longi);
/*  706 */     this.gpsComps[1] = longi;
/*  707 */     XButton xb1 = new XButton();
/*  708 */     xb1.setLabel("GetGPS");
/*  709 */     xb1.setBounds(250, y, 50, 20);
/*  710 */     xb1.addActionListener(this);
/*  711 */     add(xb1);
/*  712 */     XButton xb2 = new XButton();
/*  713 */     xb2.setLabel("Next");
/*  714 */     xb2.setBounds(310, y, 50, 20);
/*  715 */     add(xb2);
/*  716 */     xb2.addActionListener(this);
/*      */   }
/*      */ 
/*      */   public void setAttribute(String arg0, Object arg1)
/*      */   {
/*  727 */     arg0.equals("question");
/*      */ 
/*  731 */     super.setAttribute(arg0, arg1);
/*      */   }
/*      */ 
/*      */   public void readViewModel()
/*      */     throws Exception
/*      */   {
/*  737 */     String iterator = this.qModel.get("@iterator").toString();
/*  738 */     Iterator itera = (Iterator)Class.forName(iterator).newInstance();
/*  739 */     itera.setContext(this.context);
/*  740 */     if (this.qModel.get("@cols") != null)
/*      */     {
/*  742 */       String cols = this.qModel.get("@cols").toString();
/*  743 */       itera.setFields(cols);
/*      */     }
/*  745 */     String subflow = this.qModel.get("@subflow").toString();
/*  746 */     XModel flowM = (XModel)this.rootModel.get("flows/" + subflow);
/*  747 */     String contextType = flowM.get("@context").toString();
/*  748 */     itera.setNextContextType(contextType);
/*  749 */     String constraints = (String)this.qModel.get("@constraints");
/*      */ 
/*  751 */     itera.setConstraints(constraints);
/*  752 */     if (this.qModel.get("@autoupdate") != null)
/*  753 */       itera.setAutoUpdate(true);
/*  754 */     itera.init();
/*      */ 
/*  756 */     XModel tbModel = new XBaseModel();
/*  757 */     tbModel.setTagName("table");
/*      */ 
/*  759 */     XModel headers = itera.getHeaders();
/*  760 */     if (headers != null)
/*  761 */       tbModel.append(headers);
/*  762 */     itera.first();
/*  763 */     XModel xm1 = itera.getData();
/*      */ 
/*  765 */     if (xm1 != null) {
/*  766 */       tbModel.append(xm1);
/*  767 */       while (itera.next())
/*      */       {
/*  769 */         xm1 = itera.getData();
/*  770 */         if (xm1 == null)
/*      */           break;
/*  772 */         tbModel.append(xm1);
/*      */       }
/*      */ 
/*  780 */       if (tbModel.getNumChildren() > 0)
/*      */       {
/*  782 */         this.view.setModel(tbModel);
/*  783 */         if (this.qModel.get("@colwidths") != null)
/*      */         {
/*  785 */           String[] colWidths = this.qModel.get("@colwidths").toString().split(",");
/*  786 */           for (int i = 0; i < colWidths.length; i++) {
/*  787 */             System.out.println(" col width " + i + " " + colWidths[i]);
/*  788 */             this.view.setColWidth(i, Integer.parseInt(colWidths[i]));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void displayView(XModel xm)
/*      */   {
/*  806 */     int y = 40;
/*      */ 
/*  809 */     if (this.qModel.get("@selector") != null)
/*      */     {
/*  811 */       XLabel selectorLbl = new XLabel();
/*  812 */       System.out.println(this.qModel.get("@selector"));
/*  813 */       System.out.println(this.qModel.get("@selectortext"));
/*  814 */       selectorLbl.setText(this.qModel.get("@selectortext").toString());
/*  815 */       selectorLbl.setBounds(10, y, 250, 30);
/*  816 */       add(selectorLbl);
/*  817 */       XComboBox selector = new XComboBox();
/*      */ 
/*  819 */       selector.setName("selectorno");
/*  820 */       selector.setBounds(260, y, 150, 30);
/*  821 */       selector.addItem(" Choose Any One");
/*  822 */       selector.addItem("Yes");
/*  823 */       selector.addItem("No");
/*  824 */       add(selector);
/*  825 */       Object[] tt = { selectorLbl, selector };
/*  826 */       this.viewElements[0] = tt;
/*  827 */       selector.addKeyListener(this);
/*  828 */       selector.addFocusListener(this);
/*  829 */       y += 40;
/*      */     }
/*      */ 
/*  834 */     String qno = xm.get("@qno").toString();
/*      */     try
/*      */     {
/*  839 */       if (this.qModel.get("@maxtext") != null)
/*      */       {
/*  841 */         XLabel maxnoLbl = new XLabel();
/*  842 */         maxnoLbl.setText(this.qModel.get("@maxtext").toString());
/*  843 */         maxnoLbl.setBounds(10, y, 100, 30);
/*  844 */         add(maxnoLbl);
/*      */ 
/*  846 */         if (this.qModel.get("@maxrows") != null)
/*      */         {
/*  848 */           XLabel maxno = new XLabel();
/*  849 */           maxno.setText(this.qModel.get("@maxrows").toString());
/*  850 */           maxno.setName("maxno");
/*  851 */           maxno.setBounds(110, y, 50, 30);
/*  852 */           add(maxno);
/*  853 */           maxno.addKeyListener(this);
/*  854 */           Object[] tt = { maxnoLbl, maxno };
/*  855 */           this.viewElements[1] = tt;
/*      */ 
/*  857 */           y += 40;
/*      */         }
/*      */         else
/*      */         {
/*  861 */           XEdit maxno = new XEdit();
/*      */ 
/*  863 */           maxno.setBounds(110, y, 50, 20);
/*  864 */           maxno.setName("maxno");
/*  865 */           add(maxno);
/*  866 */           maxno.addKeyListener(this);
/*  867 */           Object[] tt = { maxnoLbl, maxno };
/*  868 */           this.viewElements[1] = tt;
/*  869 */           y += 40;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  874 */       XTable table = new XTable();
/*  875 */       int h = 200;
/*  876 */       if (this.qModel.get("@viewheight") != null)
/*      */       {
/*  878 */         h = Integer.parseInt(this.qModel.get("@viewheight").toString());
/*      */       }
/*  880 */       table.setBounds(10, y, getWidth() - 30, h);
/*  881 */       table.setInteractiveTable(true);
/*  882 */       table.setSelectedStyle("selectedRow");
/*  883 */       table.setName(qno);
/*  884 */       table.addItemListener(this);
/*      */ 
/*  888 */       y = y + h + 10;
/*  889 */       XButton xb = new XButton();
/*  890 */       xb.setLabel("Add");
/*  891 */       xb.setBounds(10, y, 50, 20);
/*  892 */       xb.addActionListener(this);
/*  893 */       if (this.qModel.get("@add") != null) {
/*  894 */         add(xb);
/*      */       }
/*  896 */       XButton xb1 = new XButton();
/*  897 */       xb1.setLabel("Edit");
/*  898 */       xb1.setBounds(70, y, 50, 20);
/*  899 */       xb1.addActionListener(this);
/*  900 */       add(xb1);
/*      */ 
/*  902 */       XButton xb2 = new XButton();
/*  903 */       xb2.setLabel("Next");
/*  904 */       xb2.setBounds(120, y, 50, 20);
/*  905 */       xb2.addActionListener(this);
/*  906 */       add(xb2);
/*  907 */       XButton xb3 = new XButton();
/*  908 */       xb3.setLabel("Refresh");
/*  909 */       xb3.setBounds(190, y, 50, 20);
/*  910 */       xb3.addActionListener(this);
/*  911 */       add(xb3);
/*      */ 
/*  913 */       add(table);
/*  914 */       this.view = table;
/*  915 */       readViewModel();
/*  916 */       table.addItemListener(this);
/*  917 */       table.addKeyListener(this);
/*  918 */       if (this.qModel.get("@add") != null)
/*      */       {
/*  920 */         Object[] tt = { xb, xb1, xb2, xb3 };
/*  921 */         this.viewElements[2] = tt;
/*      */       }
/*      */       else
/*      */       {
/*  925 */         Object[] tt = { xb1, xb2, xb3 };
/*  926 */         this.viewElements[2] = tt;
/*      */       }
/*  928 */       if (this.qModel.get("@selector") != null)
/*      */       {
/*  930 */         hideViewElements(1, false);
/*  931 */         hideViewElements(2, false);
/*  932 */         this.view.setVisible(false);
/*      */       }
/*      */ 
/*  935 */       transferFocus();
/*      */     }
/*      */     catch (InstantiationException e)
/*      */     {
/*  940 */       e.printStackTrace();
/*      */     }
/*      */     catch (IllegalAccessException e) {
/*  943 */       e.printStackTrace();
/*      */     }
/*      */     catch (ClassNotFoundException e) {
/*  946 */       e.printStackTrace();
/*      */     }
/*      */     catch (Exception e) {
/*  949 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void hideViewElements(int index, boolean flg)
/*      */   {
/*  956 */     Object[] elementsToHide = (Object[])this.viewElements[index];
/*  957 */     if (elementsToHide == null)
/*  958 */       return;
/*  959 */     System.out.println("Elements to hide =" + elementsToHide.length);
/*  960 */     for (int i = 0; i < elementsToHide.length; i++)
/*      */     {
/*  962 */       ((Component)elementsToHide[i]).setVisible(flg);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void itemStateChanged(ItemEvent e)
/*      */   {
/*  971 */     System.out.println(e.getID() + " " + e.getItem());
/*  972 */     this.selected = e.getItem().toString();
/*      */   }
/*      */ 
/*      */   public void keyPressed(KeyEvent e)
/*      */   {
/*  983 */     if ((e.getComponent() instanceof XComboBox))
/*      */     {
/*  985 */       for (int i = 0; i < this.xm.getNumChildren(); i++)
/*      */       {
/*  987 */         System.out.println(" item pressed" + this.xm.get(i).getId());
/*  988 */         if ((!this.xm.get(i).get().toString().startsWith(e.getKeyChar()+"")) && (!this.xm.get(i).getId().equals(e.getKeyChar()+"")))
/*      */           continue;
/*  990 */         ((XComboBox)e.getComponent()).select(this.xm.get(i).get().toString());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void viewExit(Component c)
/*      */     throws Exception
/*      */   {
/*  998 */     System.out.println(" View Exit" + c);
/*  999 */     if ((c instanceof XEdit))
/*      */     {
/* 1001 */       String value = ((XEdit)c).getText();
/*      */       try {
/* 1003 */         Integer.parseInt(value);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/* 1007 */         e.printStackTrace();
/* 1008 */         displayError("You have to enter a number ");
/* 1009 */         hideViewElements(2, false);
/* 1010 */         return;
/*      */       }
/*      */ 
/* 1013 */       if ((this.view.getModel() != null) && (Integer.parseInt(value) < this.view.getModel().getNumChildren() - 1))
/*      */       {
/* 1015 */         displayError("Cannot make the value lesser than the number of rows ");
/* 1016 */         hideViewElements(2, false);
/*      */       }
/*      */       else
/*      */       {
/* 1021 */         hideViewElements(2, true);
/* 1022 */         ((XEdit)c).transferFocus();
/*      */       }
/*      */ 
/*      */     }
/* 1026 */     else if ((c instanceof XComboBox))
/*      */     {
/* 1028 */       String value = ((XComboBox)c).getSelectedItem();
/* 1029 */       if (value.equals("No"))
/*      */       {
/* 1031 */         if (!((XEdit)((Object[])this.viewElements[1])[1]).getText().equals(""))
/*      */         {
/* 1033 */           displayError("Cannot change to No because you have already added a few rows");
/* 1034 */           ((XComboBox)c).select("Yes");
/*      */         }
/*      */         else
/*      */         {
/* 1038 */           hideViewElements(1, false);
/* 1039 */           hideViewElements(2, false);
/* 1040 */           ActionEvent e1 = new ActionEvent(c, 0, "Next");
/* 1041 */           Rectangle r = getBounds();
/*      */ 
/* 1044 */           setBounds(r.x, r.y, r.width, 200);
/* 1045 */           getParent().doLayout();
/* 1046 */           actionPerformed(e1);
/*      */         }
/*      */ 
/*      */       }
/* 1050 */       else if (((XComboBox)c).getSelectedItem().equals(" Choose Any One")) {
/* 1051 */         displayError("Please select a value");
/*      */       }
/* 1053 */       else if (((XComboBox)c).getSelectedItem().equals("Yes"))
/*      */       {
/* 1055 */         hideViewElements(1, true);
/* 1056 */         this.view.setVisible(true);
/*      */ 
/* 1058 */         ((XComboBox)c).transferFocus();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void displayError(String err)
/*      */   {
/* 1073 */     XMessageBox mbox = new XMessageBox();
/* 1074 */     Dimension size = getSize();
/* 1075 */     Point location = getLocationOnScreen();
/* 1076 */     size = new Dimension(size.width + 2 * location.x, size.height + 2 * location.y);
/*      */ 
/* 1078 */     mbox.setup("Error", err, size, this);
/*      */   }
/*      */ 
/*      */   public void keyReleased(KeyEvent e)
/*      */   {
/* 1086 */     System.out.println(" Key released" + e.getKeyChar() + " " + e.getComponent().getName() + " " + e.isConsumed());
/* 1087 */     if ((e.getKeyCode() == 10) && (!e.isConsumed()))
/*      */       try
/*      */       {
/* 1090 */         System.out.println("Enter released");
/*      */ 
/* 1092 */         if (this.qModel.get("@type").equals("view"))
/*      */         {
/* 1094 */           viewExit((Component)e.getSource());
/* 1095 */           e.consume();
/* 1096 */           return;
/*      */         }
/*      */ 
/* 1099 */         ActionEvent e1 = new ActionEvent(e.getSource(), 0, "Next");
/*      */ 
/* 1101 */         actionPerformed(e1);
/* 1102 */         e.consume();
/* 1103 */         return;
/*      */       }
/*      */       catch (Exception e1)
/*      */       {
/* 1107 */         e.consume();
/*      */       }
/*      */   }
/*      */ 
/*      */   public void keyTyped(KeyEvent e)
/*      */   {
/*      */   }
/*      */ }

/* Location:           D:\enumeration\bin\
 * Qualified Name:     com.kentropy.components.QuestionPanel
 * JD-Core Version:    0.6.0
 */