/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.HashMap;
/*  5:   */ 
/*  6:   */ public class CommonDataItem
/*  7:   */   extends DataItem
/*  8:   */ {
/*  9:   */   public CommonDataItem(HashMap<String, String> data, DataConfig config, int index)
/* 10:   */   {
/* 11: 9 */     super(data, config, index);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public void to_xml_start(StringBuffer out)
/* 15:   */   {
/* 16:14 */     out.append("<item>");
/* 17:15 */     xml_field(out, "id", get_id());
/* 18:16 */     for (int i = 0; i < this.config.data.size(); i++) {
/* 19:17 */       xml_field(out, ((ConnectorField)this.config.data.get(i)).name, get_value(((ConnectorField)this.config.data.get(i)).name));
/* 20:   */     }
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void xml_field(StringBuffer out, String name, String value)
/* 24:   */   {
/* 25:22 */     out.append("<");
/* 26:23 */     out.append(name);
/* 27:24 */     out.append("><![CDATA[");
/* 28:25 */     out.append(value);
/* 29:26 */     out.append("]]></");
/* 30:27 */     out.append(name);
/* 31:28 */     out.append(">");
/* 32:   */   }
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.CommonDataItem
 * JD-Core Version:    0.7.0.1
 */