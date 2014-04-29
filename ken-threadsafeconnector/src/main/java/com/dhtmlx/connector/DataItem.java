/*   1:    */ package com.dhtmlx.connector;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashMap;
/*   5:    */ 
/*   6:    */ public class DataItem
/*   7:    */ {
/*   8:    */   protected HashMap<String, String> data;
/*   9:    */   protected DataConfig config;
/*  10:    */   protected int index;
/*  11:    */   protected boolean skip;
/*  12:    */   
/*  13:    */   public DataItem(HashMap<String, String> data, DataConfig config, int index)
/*  14:    */   {
/*  15: 36 */     this.skip = false;
/*  16: 37 */     this.index = index;
/*  17: 38 */     this.data = data;
/*  18: 39 */     this.config = config;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public String get_value(String name)
/*  22:    */   {
/*  23: 50 */     return (String)this.data.get(name);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void set_value(String name, String value)
/*  27:    */   {
/*  28: 60 */     this.data.put(name, value);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public String get_id()
/*  32:    */   {
/*  33: 69 */     return get_value(this.config.id.name);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void set_id(String value)
/*  37:    */   {
/*  38: 78 */     set_value(this.config.id.name, value);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public int get_index()
/*  42:    */   {
/*  43: 87 */     return this.index;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void skip()
/*  47:    */   {
/*  48: 94 */     this.skip = true;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void to_xml(StringBuffer out)
/*  52:    */   {
/*  53:103 */     to_xml_start(out);
/*  54:104 */     to_xml_end(out);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void to_xml_start(StringBuffer out)
/*  58:    */   {
/*  59:114 */     out.append("{id:");
/*  60:115 */     for (int i = 0; i < this.config.data.size(); i++)
/*  61:    */     {
/*  62:116 */       out.append(" ");
/*  63:117 */       out.append(((ConnectorField)this.config.data.get(i)).name);
/*  64:118 */       out.append("='");
/*  65:119 */       out.append(get_value(((ConnectorField)this.config.data.get(i)).name));
/*  66:120 */       out.append("'");
/*  67:    */     }
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void to_xml_end(StringBuffer out)
/*  71:    */   {
/*  72:132 */     out.append("</item>");
/*  73:    */   }
/*  74:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.DataItem
 * JD-Core Version:    0.7.0.1
 */