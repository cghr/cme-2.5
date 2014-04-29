/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.HashMap;
/*  5:   */ 
/*  6:   */ public class SelectOptionsDataItem
/*  7:   */   extends DataItem
/*  8:   */ {
/*  9:   */   public SelectOptionsDataItem(HashMap<String, String> data, DataConfig config, int index)
/* 10:   */   {
/* 11: 9 */     super(data, config, index);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public void to_xml_start(StringBuffer out)
/* 15:   */   {
/* 16:14 */     out.append("<item");
/* 17:   */     
/* 18:16 */     out.append(" ");
/* 19:17 */     out.append("value='");
/* 20:18 */     out.append(get_value(((ConnectorField)this.config.data.get(0)).name));
/* 21:19 */     out.append("'");
/* 22:   */     
/* 23:21 */     out.append(" ");
/* 24:22 */     out.append("label='");
/* 25:23 */     out.append(get_value(((ConnectorField)this.config.data.get(1)).name));
/* 26:24 */     out.append("'");
/* 27:   */     
/* 28:26 */     out.append(">");
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.SelectOptionsDataItem
 * JD-Core Version:    0.7.0.1
 */