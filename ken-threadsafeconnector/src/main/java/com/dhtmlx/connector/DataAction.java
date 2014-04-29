/*   1:    */ package com.dhtmlx.connector;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import java.util.Set;
/*   7:    */ 
/*   8:    */ public class DataAction
/*   9:    */ {
/*  10:    */   private String status;
/*  11:    */   private String id;
/*  12:    */   private String nid;
/*  13:    */   private HashMap<String, String> data;
/*  14:    */   private HashMap<String, String> attrs;
/*  15:    */   private String output;
/*  16:    */   private boolean ready;
/*  17:    */   private ArrayList<String> addf;
/*  18:    */   private ArrayList<String> delf;
/*  19:    */   
/*  20:    */   public DataAction(String status, String id, HashMap<String, String> data)
/*  21:    */   {
/*  22: 55 */     this.status = status;
/*  23: 56 */     this.id = id;
/*  24: 57 */     this.nid = id;
/*  25: 58 */     this.data = data;
/*  26:    */     
/*  27: 60 */     this.attrs = new HashMap();
/*  28: 61 */     this.ready = false;
/*  29:    */     
/*  30: 63 */     this.addf = new ArrayList();
/*  31: 64 */     this.delf = new ArrayList();
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void add_field(String name, String value)
/*  35:    */   {
/*  36: 74 */     this.data.put(name, value);
/*  37: 75 */     this.addf.add(name);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void remove_field(String name)
/*  41:    */   {
/*  42: 84 */     this.delf.add(name);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void sync_config(DataConfig config)
/*  46:    */     throws ConnectorConfigException
/*  47:    */   {
/*  48: 95 */     for (int i = 0; i < this.addf.size(); i++) {
/*  49: 96 */       config.add_field((String)this.addf.get(i));
/*  50:    */     }
/*  51: 97 */     for (int i = 0; i < this.delf.size(); i++) {
/*  52: 98 */       config.remove_field((String)this.addf.get(i));
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   public String get_value(String name)
/*  57:    */   {
/*  58:109 */     String value = (String)this.data.get(name);
/*  59:110 */     if (value == null)
/*  60:    */     {
/*  61:111 */       LogManager.getInstance().log("Missed field in DB action:" + name);
/*  62:112 */       return "";
/*  63:    */     }
/*  64:115 */     return value;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public String set_value(String name, String value)
/*  68:    */   {
/*  69:127 */     return (String)this.data.put(name, value);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public String get_id()
/*  73:    */   {
/*  74:136 */     return this.id;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public HashMap<String, String> get_data()
/*  78:    */   {
/*  79:145 */     return this.data;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public String get_userdata_value(String name)
/*  83:    */   {
/*  84:156 */     return get_value(name);
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void set_userdata_value(String name, String value)
/*  88:    */   {
/*  89:166 */     set_value(name, value);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public String get_status()
/*  93:    */   {
/*  94:175 */     return this.status;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void set_response_text(String text)
/*  98:    */   {
/*  99:184 */     set_response_xml("<![CDATA[" + text + "]]>");
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void set_response_xml(String xml_text)
/* 103:    */   {
/* 104:193 */     this.output = xml_text;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void set_response_attributes(String name, String value)
/* 108:    */   {
/* 109:203 */     this.attrs.put(name, value);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public boolean is_ready()
/* 113:    */   {
/* 114:212 */     return this.ready;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public String get_new_id()
/* 118:    */   {
/* 119:223 */     return this.nid;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void error()
/* 123:    */   {
/* 124:230 */     this.status = "error";
/* 125:231 */     this.ready = true;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public void invalid()
/* 129:    */   {
/* 130:238 */     this.status = "invalid";
/* 131:239 */     this.ready = true;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public void success(String new_id)
/* 135:    */   {
/* 136:248 */     if (new_id != null) {
/* 137:249 */       this.nid = new_id;
/* 138:    */     }
/* 139:250 */     success();
/* 140:    */   }
/* 141:    */   
/* 142:    */   public void success()
/* 143:    */   {
/* 144:257 */     this.ready = true;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public String to_xml()
/* 148:    */   {
/* 149:266 */     StringBuffer out = new StringBuffer();
/* 150:    */     
/* 151:268 */     out.append("<action type='");out.append(this.status);out.append("' sid='");
/* 152:269 */     out.append(this.id);out.append("' tid='");out.append(this.nid);out.append("' ");
/* 153:    */     
/* 154:271 */     Iterator<String> it = this.attrs.keySet().iterator();
/* 155:272 */     for (; it.hasNext(); out.append("' "))
/* 156:    */     {
/* 157:273 */       String key = (String)it.next();
/* 158:274 */       out.append(key);out.append("='");out.append((String)this.attrs.get(key));
/* 159:    */     }
/* 160:277 */     out.append(">");
/* 161:278 */     if (this.output != null) {
/* 162:279 */       out.append(this.output);
/* 163:    */     }
/* 164:280 */     out.append("</action>");
/* 165:    */     
/* 166:282 */     return out.toString();
/* 167:    */   }
/* 168:    */   
/* 169:    */   public void set_status(String status)
/* 170:    */   {
/* 171:291 */     this.status = status;
/* 172:    */   }
/* 173:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.DataAction
 * JD-Core Version:    0.7.0.1
 */