/*   1:    */ package com.kentropy.cme.qa.neochecks;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.sql.ResultSet;
/*   5:    */ import java.sql.SQLException;
/*   6:    */ import java.util.Hashtable;
/*   7:    */ 
/*   8:    */ public class FieldCrossChecks
/*   9:    */   implements QualityCheck
/*  10:    */ {
/*  11: 15 */   ResultSet rs = null;
/*  12: 16 */   Hashtable values = null;
/*  13: 17 */   String name = null;
/*  14: 18 */   String value = null;
/*  15: 19 */   public String[] fields = null;
/*  16: 20 */   public String[] fieldValues = null;
/*  17: 22 */   public String[] ops = null;
/*  18:    */   
/*  19:    */   public boolean validate(ResultSet rs, ResultSet checkDef, String name, String value, StringBuffer errorMsg, String domain)
/*  20:    */   {
/*  21:    */     try
/*  22:    */     {
/*  23: 27 */       this.rs = rs;
/*  24: 28 */       this.name = name;
/*  25: 29 */       this.value = value;
/*  26: 30 */       String fieldsStr = checkDef.getString("fields");
/*  27: 31 */       System.out.println(" " + fieldsStr);
/*  28: 32 */       this.fields = fieldsStr.split(",");
/*  29: 33 */       String fieldsValStr = checkDef.getString("values");
/*  30: 34 */       System.out.println(" " + fieldsValStr);
/*  31: 35 */       this.fieldValues = fieldsValStr.split(",");
/*  32: 36 */       String errorStr = checkDef.getString("errors");
/*  33: 38 */       for (int i = 0; i < this.fields.length; i++)
/*  34:    */       {
/*  35: 39 */         String field = this.fields[i];
/*  36:    */         
/*  37: 41 */         String fldvalue = new String(this.fieldValues[i]);
/*  38: 42 */         String[] tt = fldvalue.split(" ");
/*  39: 43 */         String op = tt[0];
/*  40: 44 */         Comparable val = getValue(tt[1]);
/*  41: 45 */         Comparable valtoCompare = null;
/*  42: 46 */         if ((val instanceof Integer)) {
/*  43: 47 */           valtoCompare = new Integer(rs.getString(field));
/*  44:    */         } else {
/*  45: 49 */           valtoCompare = rs.getString(field);
/*  46:    */         }
/*  47: 50 */         System.out.println(valtoCompare + " " + op + " " + val);
/*  48: 51 */         if (op.equals("="))
/*  49:    */         {
/*  50: 52 */           if (!valtoCompare.equals(val)) {
/*  51: 54 */             return false;
/*  52:    */           }
/*  53:    */         }
/*  54: 56 */         else if (op.equals(">"))
/*  55:    */         {
/*  56: 57 */           if (val.compareTo(valtoCompare) <= 0) {
/*  57: 59 */             return true;
/*  58:    */           }
/*  59:    */         }
/*  60: 63 */         else if ((op.equals("<")) && 
/*  61: 64 */           (val.compareTo(valtoCompare) > 0)) {
/*  62: 66 */           return false;
/*  63:    */         }
/*  64:    */       }
/*  65: 72 */       System.out.println(errorStr);
/*  66:    */     }
/*  67:    */     catch (Exception e)
/*  68:    */     {
/*  69: 76 */       e.printStackTrace();
/*  70:    */     }
/*  71: 79 */     return true;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public Comparable getValue(String val1)
/*  75:    */     throws Exception
/*  76:    */   {
/*  77: 93 */     String fld = null;
/*  78: 94 */     String val = null;
/*  79: 95 */     if ((val1.startsWith("$")) || (val1.startsWith("%")))
/*  80:    */     {
/*  81: 96 */       fld = val1.substring(1);
/*  82: 97 */       System.out.println(fld);
/*  83: 98 */       val = getValue1(fld);
/*  84: 99 */       System.out.println(val);
/*  85:100 */       if (val1.startsWith("%")) {
/*  86:101 */         return new Integer(val);
/*  87:    */       }
/*  88:103 */       return val;
/*  89:    */     }
/*  90:106 */     return val1;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public String getValue1(String fld)
/*  94:    */   {
/*  95:    */     try
/*  96:    */     {
/*  97:121 */       if (this.rs != null) {
/*  98:122 */         return this.rs.getString(fld);
/*  99:    */       }
/* 100:124 */       if (this.values != null) {
/* 101:125 */         return (String)this.values.get(fld);
/* 102:    */       }
/* 103:    */     }
/* 104:    */     catch (SQLException e)
/* 105:    */     {
/* 106:128 */       e.printStackTrace();
/* 107:    */     }
/* 108:130 */     return null;
/* 109:    */   }
/* 110:    */   
/* 111:132 */   Hashtable flags = null;
/* 112:    */   
/* 113:    */   public boolean validate(Hashtable rs, Hashtable flags, ResultSet checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 114:    */   {
/* 115:    */     try
/* 116:    */     {
/* 117:138 */       this.values = rs;
/* 118:139 */       this.flags = flags;
/* 119:140 */       this.name = name;
/* 120:141 */       this.value = value;
/* 121:142 */       String fieldsStr = checkDef.getString("fields");
/* 122:143 */       this.fields = fieldsStr.split(",");
/* 123:144 */       String fieldsValStr = checkDef.getString("values");
/* 124:145 */       this.fieldValues = fieldsValStr.split(",");
/* 125:146 */       String errorStr = checkDef.getString("errors");
/* 126:148 */       for (int i = 0; i < this.fields.length; i++)
/* 127:    */       {
/* 128:149 */         String field = this.fields[i];
/* 129:    */         
/* 130:151 */         String fldvalue = new String(this.fieldValues[i]);
/* 131:152 */         String[] tt = fldvalue.split(" ");
/* 132:153 */         String op = tt[0];
/* 133:154 */         Comparable val = getValue(tt[1]);
/* 134:155 */         Comparable valtoCompare = null;
/* 135:156 */         if ((val instanceof Integer)) {
/* 136:157 */           valtoCompare = new Integer((String)rs.get(field));
/* 137:    */         } else {
/* 138:159 */           valtoCompare = (String)rs.get(field);
/* 139:    */         }
/* 140:160 */         System.out.println(valtoCompare + " " + op + " " + val);
/* 141:161 */         if (op.equals("="))
/* 142:    */         {
/* 143:162 */           if (!valtoCompare.equals(val)) {
/* 144:164 */             return false;
/* 145:    */           }
/* 146:    */         }
/* 147:166 */         else if (op.equals(">"))
/* 148:    */         {
/* 149:167 */           if (val.compareTo(valtoCompare) <= 0) {
/* 150:169 */             return true;
/* 151:    */           }
/* 152:    */         }
/* 153:173 */         else if ((op.equals("<")) && 
/* 154:174 */           (val.compareTo(valtoCompare) > 0)) {
/* 155:176 */           return false;
/* 156:    */         }
/* 157:    */       }
/* 158:    */     }
/* 159:    */     catch (Exception e)
/* 160:    */     {
/* 161:186 */       e.printStackTrace();
/* 162:    */     }
/* 163:189 */     return true;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public boolean validate(Hashtable rs, Hashtable flags, Hashtable checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 167:    */   {
/* 168:    */     try
/* 169:    */     {
/* 170:197 */       this.values = rs;
/* 171:198 */       this.flags = flags;
/* 172:199 */       this.name = name;
/* 173:200 */       this.value = value;
/* 174:201 */       String fieldsStr = (String)checkDef.get("fields");
/* 175:202 */       this.fields = fieldsStr.split(",");
/* 176:203 */       String fieldsValStr = (String)checkDef.get("values");
/* 177:204 */       this.fieldValues = fieldsValStr.split(",");
/* 178:205 */       String errorStr = (String)checkDef.get("errors");
/* 179:207 */       for (int i = 0; i < this.fields.length; i++)
/* 180:    */       {
/* 181:208 */         String field = this.fields[i];
/* 182:    */         
/* 183:210 */         String fldvalue = new String(this.fieldValues[i]);
/* 184:211 */         String[] tt = fldvalue.split(" ");
/* 185:212 */         String op = tt[0];
/* 186:213 */         Comparable val = getValue(tt[1]);
/* 187:214 */         Comparable valtoCompare = null;
/* 188:215 */         if ((val instanceof Integer)) {
/* 189:216 */           valtoCompare = new Integer((String)rs.get(field));
/* 190:    */         } else {
/* 191:218 */           valtoCompare = (String)rs.get(field);
/* 192:    */         }
/* 193:219 */         System.out.println(valtoCompare + " " + op + " " + val);
/* 194:220 */         if (op.equals("="))
/* 195:    */         {
/* 196:221 */           if (!valtoCompare.equals(val)) {
/* 197:223 */             return false;
/* 198:    */           }
/* 199:    */         }
/* 200:225 */         else if (op.equals(">"))
/* 201:    */         {
/* 202:226 */           if (val.compareTo(valtoCompare) <= 0) {
/* 203:228 */             return true;
/* 204:    */           }
/* 205:    */         }
/* 206:232 */         else if ((op.equals("<")) && 
/* 207:233 */           (val.compareTo(valtoCompare) > 0)) {
/* 208:235 */           return false;
/* 209:    */         }
/* 210:    */       }
/* 211:    */     }
/* 212:    */     catch (Exception localException) {}
/* 213:248 */     return true;
/* 214:    */   }
/* 215:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-qa\ken-qa.jar
 * Qualified Name:     com.kentropy.cme.qa.neochecks.FieldCrossChecks
 * JD-Core Version:    0.7.0.1
 */