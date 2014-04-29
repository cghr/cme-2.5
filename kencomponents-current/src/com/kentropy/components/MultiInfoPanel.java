package com.kentropy.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import com.kentropy.db.TestXUIDB;

import net.xoetrope.awt.XLabel;
import net.xoetrope.awt.XPanel;
import net.xoetrope.xui.data.XBaseModel;
import net.xoetrope.xui.data.XModel;

public class MultiInfoPanel extends XPanel {

	
	public int columns=1;
	public String heading="";
	private java.awt.Color color = java.awt.Color.BLACK;
	private String style = "head123";
	public void setStyle(String style) {
		this.style = style;
	}


	
	
	public XModel list=null;
	
	public void setList(XModel list1)
	{
		Component [] comps=this.getComponents();
		for(int i=0; i< comps.length;i++)
		{
			this.remove(comps[i]);
		}
		this.list=list1;
		System.out.println("---"+list.getNumChildren());
		int rowcount=0;
		XLabel item = new XLabel();
		//System.out.println("---"+key +" "+value);
		int w=(this.getWidth()-10);
		item.setBounds(5,10,w,15);
		item.setText(heading);
//		item.setAttribute("style",style);
	//	item.setAttribute("border", "1");
		//item.setForeground(java.awt.Color.cyan);
		
		Font font=new Font("Arial",Font.BOLD,12);
		item.setForeground(color);
		
		item.setFont(font);
		this.add(item);
		item.setVisible(true);
		Font font1=new Font("SANS_SERIF",Font.BOLD,10);
//		item.setForeground(new Color(0x000000));
		for(int i=0;i<list.getNumChildren();)
		{// item = new XLabel();
			for(int j=0;j<columns;j++)
			{
			String key =list.get(i).getId();
			key=key.substring(key.indexOf(" ")+1);
			String value=(String)list.get(i).get();
		 item = new XLabel();
		 item.setFont(font1);
		 w=(this.getWidth()-10)/columns;
		 System.out.println("---"+key +" "+value+" "+columns+" "+w);
			item.setBounds(5+(j*w),25+(rowcount*12),w,15);
			if(key.length()>20 && key.contains("Pregnancy")) {
				key = key.replace("Pregnancy", "Preg.");
			}
			item.setText((key.length()>20?key.substring(0,20):key)+": "+value);
			item.setAttribute("style","Table/SmallHeader2");
		//	item.setAttribute("border", "1");
			//item.setForeground(java.awt.Color.red);
			item.setFont(font1);
			this.add(item);
			item.setVisible(true);
			i++;
			if(i==list.getNumChildren())
				return;;
			}
			rowcount++;
			
		}
	}
	
	Font headingFont=new Font("Arial",Font.BOLD,12);
	Font textFont=new Font("Arial", Font.PLAIN, 12);
	
	public void displayItem(String text, String style,int row)
	{
		XLabel item = new XLabel();
		if(style.equals("h2")) {
			item.setFont(headingFont);
		} else {
			item.setFont(textFont);
		}
		
		if(style.equals("p")) {
			text = text.substring(text.indexOf(' ')+1);
		}
		item.setText(text);
		//System.out.println("---"+key +" "+value);
		int w=(this.getWidth()-10);
		item.setBounds(5,5+15*(row-1),w,15);
//		item.setAttribute("style",style);
		//	item.setAttribute("border", "1");
			//item.setForeground(java.awt.Color.cyan);
			
			item.setForeground(color);
			
//			item.setFont(textFont);
			this.add(item);
			item.setVisible(true);

	}
	public void setList1(XModel list1,String report)
	{
		System.out.println("---Children");
//		Component [] comps=this.getComponents();
//		for(int i=0; i< comps.length;i++)
//		{
//			this.remove(comps[i]);
//		}
		this.list=list1;
		System.out.println("Children---"+list1.getNumChildren());
		int rowcount=0;
		for(int i=0; i< list1.getNumChildren();i++)
		{
			for(int j=0; j< list1.get(i).getNumChildren();j++)
			{
				XModel itemM=list1.get(i).get(j);
//				String style=itemM.getTagName().equals("h2")?"heading":"item";
				String style = itemM.getTagName();
				String src=(String)itemM.get("@src");
				String value=(String)itemM.get();
				String text= null;
				if(src!=null)
				{
					if(src.contains("*"))
					{
						XModel itemsM= new XBaseModel();
						TestXUIDB.getInstance().getKeyValues(itemsM, "keyvalue", "/va/"+report+"/"+src.substring(0,src.indexOf("*")-1));
						System.out.println("Path11: va/"+report+"/"+src.substring(0,src.indexOf("*")-1));
						for(int k=0; k< itemsM.getNumChildren();k++)
						{
							text=itemsM.get(k).getId()+": "+itemsM.get(k).get().toString();
							rowcount++;
							this.displayItem(text, style,rowcount);
						}
					}
					else
					{
					text=TestXUIDB.getInstance().getValue("keyvalue", "/va/"+report+"/"+src);
					System.out.println("Path11: va/"+report+"/"+src+" "+text);
					rowcount++;
					this.displayItem(text, style,rowcount);
					}
				
				}
			else
			{
				text=value;
				rowcount++;
				this.displayItem(text, style,rowcount);
			}
				

				
		}
			rowcount++;
	}
		
		
			

	}

		public void setColor(java.awt.Color color) {
		this.color = color;
	}


		public XModel getList() {
		return list;
	}


		public MultiInfoPanel() {
		// TODO Auto-generated constructor stub
	}

	public MultiInfoPanel(boolean arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

}