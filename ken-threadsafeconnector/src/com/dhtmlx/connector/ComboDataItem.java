/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.HashMap;
/*  5:   */ 
/*  6:   */ public class ComboDataItem
/*  7:   */   extends DataItem
/*  8:   */ {
/*  9:   */   private boolean selected;
/* 10:   */   
/* 11:   */   public ComboDataItem(HashMap<String, String> data, DataConfig config, int index)
/* 12:   */   {
/* 13:27 */     super(data, config, index);
/* 14:28 */     this.selected = false;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public void to_xml_start(StringBuffer out)
/* 18:   */   {
/* 19:36 */     if (!this.skip)
/* 20:   */     {
/* 21:37 */       out.append("<option");
/* 22:38 */       if (this.selected) {
/* 23:39 */         out.append(" selected='true'");
/* 24:   */       }
/* 25:40 */       out.append(" value='");
/* 26:41 */       out.append(get_id());
/* 27:42 */       out.append("' >");
/* 28:43 */       out.append(get_value(((ConnectorField)this.config.text.get(0)).name));
/* 29:   */     }
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void to_xml_end(StringBuffer out)
/* 33:   */   {
/* 34:52 */     if (!this.skip) {
/* 35:53 */       out.append("</option>");
/* 36:   */     }
/* 37:   */   }
/* 38:   */   
/* 39:   */   public void select()
/* 40:   */   {
/* 41:60 */     this.selected = true;
/* 42:   */   }
/* 43:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.ComboDataItem
 * JD-Core Version:    0.7.0.1
 */