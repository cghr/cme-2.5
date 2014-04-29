/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.HashMap;
/*  5:   */ 
/*  6:   */ public class FormDataItem
/*  7:   */   extends DataItem
/*  8:   */ {
/*  9:   */   public FormDataItem(HashMap<String, String> data, DataConfig config, int index)
/* 10:   */   {
/* 11: 9 */     super(data, config, index);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public void to_xml(StringBuffer out)
/* 15:   */   {
/* 16:13 */     if (!this.skip) {
/* 17:14 */       for (int i = 0; i < this.config.text.size(); i++)
/* 18:   */       {
/* 19:15 */         String tag = ((ConnectorField)this.config.text.get(i)).name;
/* 20:16 */         out.append("<" + tag + "><![CDATA[" + get_value(tag) + "]]></" + tag + ">");
/* 21:   */       }
/* 22:   */     }
/* 23:   */   }
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.FormDataItem
 * JD-Core Version:    0.7.0.1
 */