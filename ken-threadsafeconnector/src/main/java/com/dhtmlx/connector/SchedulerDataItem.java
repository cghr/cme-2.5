/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.HashMap;
/*  5:   */ 
/*  6:   */ public class SchedulerDataItem
/*  7:   */   extends DataItem
/*  8:   */ {
/*  9:   */   public SchedulerDataItem(HashMap<String, String> data, DataConfig config, int index)
/* 10:   */   {
/* 11:21 */     super(data, config, index);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public void to_xml(StringBuffer out)
/* 15:   */   {
/* 16:29 */     if (!this.skip)
/* 17:   */     {
/* 18:31 */       out.append("<event id='" + get_id() + "' >");
/* 19:32 */       out.append("<start_date><![CDATA[" + get_value(((ConnectorField)this.config.text.get(0)).name) + "]]></start_date>");
/* 20:33 */       out.append("<end_date><![CDATA[" + get_value(((ConnectorField)this.config.text.get(1)).name) + "]]></end_date>");
/* 21:34 */       out.append("<text><![CDATA[" + get_value(((ConnectorField)this.config.text.get(2)).name) + "]]></text>");
/* 22:36 */       for (int i = 3; i < this.config.text.size(); i++)
/* 23:   */       {
/* 24:37 */         String tag = ((ConnectorField)this.config.text.get(i)).name;
/* 25:38 */         out.append("<" + tag + "><![CDATA[" + get_value(tag) + "]]></" + tag + ">");
/* 26:   */       }
/* 27:41 */       out.append("</event>");
/* 28:   */     }
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.SchedulerDataItem
 * JD-Core Version:    0.7.0.1
 */