/*   1:    */ package com.dhtmlx.connector;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import java.util.Set;
/*   7:    */ 
/*   8:    */ public class TreeDataItem
/*   9:    */   extends DataItem
/*  10:    */ {
/*  11: 15 */   private int kids = -1;
/*  12: 18 */   private boolean check = false;
/*  13: 21 */   private String im0 = null;
/*  14: 24 */   private String im1 = null;
/*  15: 27 */   private String im2 = null;
/*  16: 29 */   protected HashMap<String, String> userdata = new HashMap();
/*  17:    */   
/*  18:    */   public TreeDataItem(HashMap<String, String> data, DataConfig config, int index)
/*  19:    */   {
/*  20: 39 */     super(data, config, index);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public String get_parent_id()
/*  24:    */   {
/*  25: 48 */     return get_value(this.config.relation_id.name);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public boolean get_check_state()
/*  29:    */   {
/*  30: 57 */     return this.check;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void set_check_state(boolean new_state)
/*  34:    */   {
/*  35: 66 */     this.check = new_state;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void set_image(String image_path)
/*  39:    */   {
/*  40: 75 */     set_image(image_path, image_path, image_path);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void set_image(String folder_closed, String folder_open, String leaf)
/*  44:    */   {
/*  45: 86 */     this.im0 = folder_closed;
/*  46: 87 */     this.im1 = folder_open;
/*  47: 88 */     this.im2 = leaf;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void set_user_data(String name, String value)
/*  51:    */   {
/*  52: 93 */     this.userdata.put(name, value);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public String get_user_data(String name)
/*  56:    */   {
/*  57: 97 */     return (String)this.userdata.get(name);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public int has_kids()
/*  61:    */   {
/*  62:106 */     return this.kids;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void set_kids(int count)
/*  66:    */   {
/*  67:115 */     this.kids = count;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void to_xml_end(StringBuffer out)
/*  71:    */   {
/*  72:123 */     if (!this.skip) {
/*  73:124 */       out.append("</item>");
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void to_xml_start(StringBuffer out)
/*  78:    */   {
/*  79:132 */     if (!this.skip)
/*  80:    */     {
/*  81:133 */       out.append("<item id='" + get_id() + "' text='" + get_value(((ConnectorField)this.config.text.get(0)).name) + "' ");
/*  82:134 */       if (this.kids > 0) {
/*  83:135 */         out.append("child='" + Integer.toString(this.kids) + "' ");
/*  84:    */       }
/*  85:136 */       if (this.im0 != null) {
/*  86:137 */         out.append("im0='" + this.im0 + "' ");
/*  87:    */       }
/*  88:138 */       if (this.im1 != null) {
/*  89:139 */         out.append("im1='" + this.im1 + "' ");
/*  90:    */       }
/*  91:140 */       if (this.im2 != null) {
/*  92:141 */         out.append("im2='" + this.im2 + "' ");
/*  93:    */       }
/*  94:143 */       if (this.check) {
/*  95:144 */         out.append("checked='true' ");
/*  96:    */       }
/*  97:146 */       out.append(">");
/*  98:    */       
/*  99:148 */       Iterator<String> uitc = this.userdata.keySet().iterator();
/* 100:149 */       while (uitc.hasNext())
/* 101:    */       {
/* 102:150 */         String userdata_key = ((String)uitc.next()).toString();
/* 103:151 */         out.append("<userdata name='" + userdata_key + "'>");
/* 104:152 */         out.append((String)this.userdata.get(userdata_key));
/* 105:153 */         out.append("</userdata>");
/* 106:    */       }
/* 107:    */     }
/* 108:    */   }
/* 109:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.TreeDataItem
 * JD-Core Version:    0.7.0.1
 */