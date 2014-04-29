/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.HashMap;
/*  5:   */ 
/*  6:   */ public class TreeGridDataItem
/*  7:   */   extends GridDataItem
/*  8:   */ {
/*  9:14 */   private int kids = -1;
/* 10:   */   
/* 11:   */   public TreeGridDataItem(HashMap<String, String> data, DataConfig config, int index)
/* 12:   */   {
/* 13:24 */     super(data, config, index);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public String get_parent_id()
/* 17:   */   {
/* 18:33 */     return get_value(this.config.relation_id.name);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void set_image(String image_path)
/* 22:   */   {
/* 23:42 */     set_cell_attribute(((ConnectorField)this.config.text.get(0)).name, "image", image_path);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public int has_kids()
/* 27:   */   {
/* 28:51 */     return this.kids;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void set_kids(int count)
/* 32:   */   {
/* 33:60 */     this.kids = count;
/* 34:61 */     if (count != 0) {
/* 35:62 */       set_row_attribute("xmlkids", Integer.toString(count));
/* 36:   */     }
/* 37:   */   }
/* 38:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.TreeGridDataItem
 * JD-Core Version:    0.7.0.1
 */