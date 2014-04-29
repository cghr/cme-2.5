/*   1:    */ package com.dhtmlx.connector;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ 
/*   5:    */ public class DataConfig
/*   6:    */ {
/*   7:    */   public ConnectorField relation_id;
/*   8:    */   public ConnectorField id;
/*   9:    */   public ArrayList<ConnectorField> text;
/*  10:    */   public ArrayList<ConnectorField> data;
/*  11:    */   
/*  12:    */   public DataConfig()
/*  13:    */   {
/*  14: 32 */     this.text = new ArrayList();
/*  15: 33 */     this.data = new ArrayList();
/*  16: 34 */     this.id = new ConnectorField();
/*  17: 35 */     this.relation_id = new ConnectorField();
/*  18:    */   }
/*  19:    */   
/*  20:    */   public DataConfig(DataConfig original)
/*  21:    */   {
/*  22: 44 */     this();
/*  23: 45 */     copy(original);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void copy(DataConfig original)
/*  27:    */   {
/*  28: 54 */     this.id = original.id;
/*  29: 55 */     this.relation_id = original.relation_id;
/*  30: 56 */     this.text = original.text;
/*  31: 57 */     this.data = original.data;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void minimize(String name)
/*  35:    */     throws ConnectorConfigException
/*  36:    */   {
/*  37: 68 */     for (int i = 0; i < this.text.size(); i++)
/*  38:    */     {
/*  39: 69 */       ConnectorField field = (ConnectorField)this.text.get(i);
/*  40: 70 */       if (field.name.equals(name))
/*  41:    */       {
/*  42: 71 */         this.text = new ArrayList();
/*  43: 72 */         this.text.add(new ConnectorField(name, "value"));
/*  44: 73 */         this.data = new ArrayList();
/*  45: 74 */         this.data.add(new ConnectorField(name, "value"));
/*  46: 75 */         return;
/*  47:    */       }
/*  48:    */     }
/*  49: 78 */     throw new ConnectorConfigException("Incorrect dataset minimization, master field not found.");
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void init(String id, String fields, String extra, String relation)
/*  53:    */   {
/*  54: 90 */     this.id = parse_one(id);
/*  55: 91 */     this.relation_id = parse_one(relation);
/*  56:    */     
/*  57: 93 */     parse_many(fields, this.text);
/*  58: 94 */     parse_many(fields, this.data);
/*  59: 95 */     parse_many(extra, this.data);
/*  60:    */   }
/*  61:    */   
/*  62:    */   private void parse_many(String key, ArrayList<ConnectorField> collection)
/*  63:    */   {
/*  64:105 */     String[] keys = key.trim().split(",");
/*  65:106 */     for (int i = 0; i < keys.length; i++) {
/*  66:107 */       if (!keys[i].trim().equals("")) {
/*  67:108 */         collection.add(parse_one(keys[i]));
/*  68:    */       }
/*  69:    */     }
/*  70:    */   }
/*  71:    */   
/*  72:    */   private ConnectorField parse_one(String key)
/*  73:    */   {
/*  74:119 */     key = key.trim();
/*  75:120 */     int sub = key.indexOf("(");
/*  76:121 */     if (sub == -1) {
/*  77:122 */       return new ConnectorField(key);
/*  78:    */     }
/*  79:124 */     return new ConnectorField(key.substring(0, sub), key.substring(sub + 1, key.length() - 1));
/*  80:    */   }
/*  81:    */   
/*  82:    */   public String db_names_list()
/*  83:    */   {
/*  84:133 */     StringBuffer list = new StringBuffer();
/*  85:134 */     if (!this.id.isEmpty()) {
/*  86:135 */       list.append(this.id.db_name);
/*  87:    */     }
/*  88:136 */     if (!this.relation_id.isEmpty())
/*  89:    */     {
/*  90:137 */       if (list.length() != 0) {
/*  91:138 */         list.append(",");
/*  92:    */       }
/*  93:139 */       list.append(this.relation_id.db_name);
/*  94:    */     }
/*  95:142 */     for (int i = 0; i < this.data.size(); i++)
/*  96:    */     {
/*  97:143 */       ConnectorField field = (ConnectorField)this.data.get(i);
/*  98:144 */       if (list.length() != 0) {
/*  99:145 */         list.append(",");
/* 100:    */       }
/* 101:147 */       list.append(field.db_name);
/* 102:148 */       if (!field.db_name.equals(field.name)) {
/* 103:149 */         list.append(" as " + field.name);
/* 104:    */       }
/* 105:    */     }
/* 106:152 */     return list.toString();
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void add_field(String name)
/* 110:    */     throws ConnectorConfigException
/* 111:    */   {
/* 112:163 */     add_field(name, name);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public void add_field(String name, String alias)
/* 116:    */     throws ConnectorConfigException
/* 117:    */   {
/* 118:175 */     if (!this.id.db_name.equals(name)) {
/* 119:175 */       this.relation_id.db_name.equals(name);
/* 120:    */     }
/* 121:178 */     if (is_field(name, this.text) != -1) {
/* 122:179 */       throw new ConnectorConfigException("There was no such data field registered as: " + name);
/* 123:    */     }
/* 124:181 */     this.text.add(new ConnectorField(name, alias));
/* 125:182 */     if (is_field(name, this.data) == -1) {
/* 126:183 */       this.data.add(new ConnectorField(name, alias));
/* 127:    */     }
/* 128:    */   }
/* 129:    */   
/* 130:    */   public void remove_field(String name)
/* 131:    */     throws ConnectorConfigException
/* 132:    */   {
/* 133:194 */     int index = is_field(name);
/* 134:195 */     if (index == -1) {
/* 135:196 */       throw new ConnectorConfigException("There was no such data field registered as: " + name);
/* 136:    */     }
/* 137:197 */     this.text.remove(index);
/* 138:    */   }
/* 139:    */   
/* 140:    */   private int is_field(String name)
/* 141:    */   {
/* 142:208 */     return is_field(name, this.text);
/* 143:    */   }
/* 144:    */   
/* 145:    */   private int is_field(String name, ArrayList<ConnectorField> collection)
/* 146:    */   {
/* 147:220 */     for (int i = 0; i < collection.size(); i++)
/* 148:    */     {
/* 149:221 */       ConnectorField field = (ConnectorField)collection.get(i);
/* 150:222 */       if ((field.name.equals(name)) || (field.db_name.equals(name))) {
/* 151:223 */         return i;
/* 152:    */       }
/* 153:    */     }
/* 154:226 */     return -1;
/* 155:    */   }
/* 156:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.DataConfig
 * JD-Core Version:    0.7.0.1
 */