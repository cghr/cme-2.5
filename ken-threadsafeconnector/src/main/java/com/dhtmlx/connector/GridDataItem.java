/*   1:    */ package com.dhtmlx.connector;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import java.util.Set;
/*   7:    */ 
/*   8:    */ public class GridDataItem
/*   9:    */   extends DataItem
/*  10:    */ {
/*  11: 15 */   protected HashMap<String, String> row_attrs = new HashMap();
/*  12: 18 */   protected HashMap<String, HashMap<String, String>> cell_attrs = new HashMap();
/*  13: 20 */   protected HashMap<String, String> userdata = new HashMap();
/*  14:    */   
/*  15:    */   public GridDataItem(HashMap<String, String> data, DataConfig config, int index)
/*  16:    */   {
/*  17: 30 */     super(data, config, index);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public void set_row_color(String color)
/*  21:    */   {
/*  22: 39 */     this.row_attrs.put("bgColor", color);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void set_row_style(String style)
/*  26:    */   {
/*  27: 48 */     this.row_attrs.put("style", style);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void set_cell_style(String name, String value)
/*  31:    */   {
/*  32: 58 */     set_cell_attribute(name, "style", value);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void set_cell_attribute(String name, String attr, String value)
/*  36:    */   {
/*  37: 69 */     if (this.cell_attrs.get(name) == null) {
/*  38: 70 */       this.cell_attrs.put(name, new HashMap());
/*  39:    */     }
/*  40: 72 */     ((HashMap)this.cell_attrs.get(name)).put(attr, value);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void set_row_attribute(String name, String value)
/*  44:    */   {
/*  45: 82 */     this.row_attrs.put(name, value);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void set_cell_class(String name, String value)
/*  49:    */   {
/*  50: 92 */     set_cell_attribute(name, "class", value);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void set_user_data(String name, String value)
/*  54:    */   {
/*  55: 96 */     this.userdata.put(name, value);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public String get_user_data(String name)
/*  59:    */   {
/*  60:100 */     return (String)this.userdata.get(name);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void to_xml_start(StringBuffer out)
/*  64:    */   {
/*  65:108 */     out.append("<row id='");out.append(get_id());out.append("'");
/*  66:    */     
/*  67:110 */     Iterator<String> it = this.row_attrs.keySet().iterator();
/*  68:111 */     for (; it.hasNext(); out.append("'"))
/*  69:    */     {
/*  70:112 */       String key = ((String)it.next()).toString();
/*  71:113 */       out.append(" ");out.append(key);out.append("='");out.append((String)this.row_attrs.get(key));
/*  72:    */     }
/*  73:115 */     out.append(">");
/*  74:    */     
/*  75:117 */     Iterator<String> uitc = this.userdata.keySet().iterator();
/*  76:118 */     while (uitc.hasNext())
/*  77:    */     {
/*  78:119 */       String userdata_key = ((String)uitc.next()).toString();
/*  79:120 */       out.append("<userdata name='" + userdata_key + "'>");
/*  80:121 */       out.append((String)this.userdata.get(userdata_key));
/*  81:122 */       out.append("</userdata>");
/*  82:    */     }
/*  83:125 */     for (int i = 0; i < this.config.data.size(); i++)
/*  84:    */     {
/*  85:126 */       out.append("<cell");
/*  86:    */       
/*  87:128 */       HashMap<String, String> current_cell_attrs = (HashMap)this.cell_attrs.get(((ConnectorField)this.config.data.get(i)).name);
/*  88:129 */       if (current_cell_attrs != null)
/*  89:    */       {
/*  90:130 */         Iterator<String> itc = current_cell_attrs.keySet().iterator();
/*  91:131 */         for (; itc.hasNext(); out.append("'"))
/*  92:    */         {
/*  93:132 */           String key_c = ((String)itc.next()).toString();
/*  94:133 */           out.append(" ");out.append(key_c);out.append("='");out.append((String)current_cell_attrs.get(key_c));
/*  95:    */         }
/*  96:    */       }
/*  97:137 */       out.append("><![CDATA[");
/*  98:138 */       out.append(get_value(((ConnectorField)this.config.data.get(i)).name));
/*  99:139 */       out.append("]]></cell>");
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void to_xml_end(StringBuffer out)
/* 104:    */   {
/* 105:148 */     out.append("</row>");
/* 106:    */   }
/* 107:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.GridDataItem
 * JD-Core Version:    0.7.0.1
 */