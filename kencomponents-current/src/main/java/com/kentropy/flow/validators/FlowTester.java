package com.kentropy.flow.validators;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JComboBox;

import net.xoetrope.data.XDataSource;
import net.xoetrope.xui.XProject;
import net.xoetrope.xui.XProjectManager;
import net.xoetrope.xui.XTextHolder;
import net.xoetrope.xui.data.XBaseModel;
import net.xoetrope.xui.data.XModel;

import com.kentropy.components.swing.QuestionFlowPanel;
import com.kentropy.db.TestXUIDB;
import com.kentropy.db.XDataModel;
import com.kentropy.flow.Age;
import com.kentropy.flow.QuestionFlowManager;

public class FlowTester {

	/**
	 * @param args
	 */
	XModel qModel;
	String value;
	XModel viewModel;
	XModel testModel;
	public Object[] viewElements = new Object[3];
	
/*      */   
	/*      */   public boolean rangeCheck()
	/*      */   {
	/*  110 */     String range = qModel.get("@range").toString();
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
	/*  133 */     if ((this.qModel.get("@inputtype") != null) && (this.qModel.get("@inputtype").equals("date")) &&  (this.qModel.get("@range")!=null) )
	/*      */     {
		try{
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd");//,getLocale());
		String format=(String)this.qModel.get("@format");
		format=format==null?"dd/MM/yyyy":format;
//		SimpleDateFormat sdf1= new SimpleDateFormat(format,getLocale());
		SimpleDateFormat sdf1= new SimpleDateFormat("yyyy-MM-dd");
		
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
	
	public void displayError(String err)
	{
		System.out.println("Error:"+err);
		
	}

public static boolean isEnglish(String str) {
	Charset asciiCharset = Charset.forName("US-ASCII");
	CharsetEncoder encoder = asciiCharset.newEncoder();
	
	encoder.onUnmappableCharacter(CodingErrorAction.REPORT);
	
	CharBuffer cb = CharBuffer.wrap(str);
	try {
		encoder.encode(cb);
		return true;
	} catch (CharacterCodingException e) {
		// TODO Auto-generated catch block
		System.out.println("Failed");
		return false;
	}
}
public boolean checkNumeric(String value1)
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

public boolean checkDate(String value1)
/*      */   {
/*   52 */     
/*      */    
	/*  117 */       
	String format=this.qModel.get("@format")!=null?this.qModel.get("@format").toString():"dd/MM/yyyy";
	//Locale locale=InputContext.getInstance().getLocale();
		//SimpleDateFormat sdf= new SimpleDateFormat(format,getLocale());
	SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
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
public String rangeToStr(String range,String type)
{
	
	try{
		Locale locale=Locale.ENGLISH;
		if(type!=null && type.equals("date"))
		{
	String[] range1 = range.split("-");
	SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd",locale);
	String format=(String)this.qModel.get("@format");
	format=format==null?"dd/MM/yyyy":format;
	SimpleDateFormat sdf1= new SimpleDateFormat(format,locale);
	
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
/*      */   
	public void actionPerformed(ActionEvent e)
	/*      */   {
	/*  172 */     System.out.println(" Actuon " + e.getSource() + " " + e.getActionCommand());
	/*if(e.getSource() instanceof JComboBox)
	{
		  this.selected = ((JComboBox)e.getSource()).getSelectedItem().toString();
	}*/
	/*  173 */     if (e.getActionCommand().equals("GetGPS"))
	/*      */     {
	/*  175 */      // getGpsCoordinates1();
	/*      */     }
	/*      */ 
	
QuestionFlowManager qfm= new QuestionFlowManager();
	if(!qfm.crossCheck(qModel))
	{
		displayError("Cross check failed!\r\n"+qModel.get("@crosscheckmsg"));
		return ;
	}
		
	/*if (this.qModel.get("@type").equals("narrative"))
	{
		//value=TestXUIDB.getInstance().getValue("resource", "/va/"+context.get("@area")+context.get("@member"));//(xm, "resources", parentPath);
		if(value==null || value.toString().length()==0)
		{
			displayError("Please select one of the listed options");
		return ;	
		}
			
	}*/
	if (this.qModel.get("@type").equals("choice"))
	{
		if(value==null || value.toString().length()==0)
		{
			displayError("Please select one of the listed options");
		return ;	
		}
			
	}
	/*
	if (this.qModel.get("@type").equals("narrative"))
	{
		if(!validateNarrative())
		{
			displayError("Please complete the narrative");
		return;
		}
	}*/

	if (this.qModel.get("@lang") != null)
	{
		if(!this.isEnglish(value.toString()))
				{
			displayError("Value is not in English ");
			return;
				}
				
	}

	/*  198 */       if (this.qModel.get("@range") != null)
	/*      */       {
	/*  200 */         if (!rangeCheck())
	/*      */         {
						String inptype=this.qModel.get("@inputtype")==null ?null:(String)this.qModel.get("@inputtype");
	/*  202 */         //  displayError("Value out of range (" + this.rangeToStr(this.qModel.get("@range").toString(),this.qModel.get("@inputtype").toString())+")");
	/*  203 */          displayError("Value out of range (" + this.rangeToStr(this.qModel.get("@range").toString(),inptype)+")"); 
						return;
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
		System.out.println("Inside michars");
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
	/*  224 */             displayError("Please enter valid date value");//  ("+format+")");
	/*  225 */             return;
	/*      */           }
	/*      */         }
	/*      */       }

	/*  229 */      if (this.qModel.get("@maxtext") != null)
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
	/*  242 */         if ((this.viewModel != null) && (Integer.parseInt(max) > this.viewModel.getNumChildren() - 1))
	/*      */         {
	/*  244 */           displayError("You have not added all the rows");
	/*  245 */           return;
	/*      */         }
	/*      */ 
	/*      */       }
	/*      */ 
	/*  250 */       
	/*      */ 
	/*  255 */     //  (e.getSource() instanceof XEdit);
	/*      */     }
	/*      */ 
	
	public void runFlow()
	{
		QuestionFlowManager qfm = new QuestionFlowManager();
		qfm.setFlowModel(qModel);
		qfm.dataModel=testModel;
		XModel context= new XBaseModel();
		((XModel)context.get("va")).set("1315032010008");
		qfm.context=context;
		
		XModel qm=qfm.nextQuestion();
		System.out.println(" Question "+qm.getId());
	}
	public void readFlows(String fileName) throws Exception
	{
		XProject xpm=XProjectManager.getCurrentProject();
		//xpm.
		XDataSource xd=new XDataSource();
		xd.read(new FileReader("e:/workspace_integration/va_integration/resources/adult.xml"));
		System.out.println("tytugu:"+ xpm.getModel().getNumChildren());
	}
	/*  261 */    

	/*      */   
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		XProject xpm=XProjectManager.getCurrentProject();
		//XProjectManager.getModel();
		//xpm.
		XDataSource xd=new XDataSource();
		
		xd.read(new FileReader("e:/workspace_integration/va_integration/resources/datasets.xml"));
	//	xd.notify();
		System.out.println("tytugu:"+ xpm.getModel().get(0).getId()+" "+	XProjectManager.getModel().getNumChildren()+" "+xd.toString());
		System.out.println(((XModel)XProjectManager.getModel().get("flows")).getNumChildren());
		String uniqno="/va/1315032010008/17";
		XModel xm = new XBaseModel();
		TestXUIDB.getInstance().getKeyValues(xm, "keyvalue", uniqno);
		System.out.println(" data "+xm.getNumChildren());
		FlowTester ft= new FlowTester();
		ft.qModel=((XModel)XProjectManager.getModel().get(0).get("va"));
		ft.testModel=xm;
		ft.runFlow();
	}

}
