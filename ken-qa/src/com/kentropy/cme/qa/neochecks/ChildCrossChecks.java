/*   1:    */ package com.kentropy.cme.qa.neochecks;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.sql.ResultSet;
/*   5:    */ import java.sql.SQLException;
/*   6:    */ import java.util.Hashtable;
/*   7:    */ 
/*   8:    */ public class ChildCrossChecks
/*   9:    */   implements QualityCheck
/*  10:    */ {
/*  11: 17 */   ResultSet rs = null;
/*  12: 18 */   Hashtable values = null;
/*  13: 19 */   Hashtable flags = null;
/*  14: 20 */   String name = null;
/*  15: 21 */   String value = null;
/*  16: 22 */   public String[] fields = null;
/*  17: 23 */   public String[] fieldValues = null;
/*  18: 25 */   public String[] ops = null;
/*  19:    */   
/*  20:    */   public boolean validate(ResultSet rs, ResultSet checkDef, String name, String value, StringBuffer errorMsg, String domain)
/*  21:    */   {
/*  22:    */     try
/*  23:    */     {
/*  24: 30 */       this.rs = rs;
/*  25: 31 */       this.name = name;
/*  26: 32 */       this.value = value;
/*  27: 33 */       String fieldsStr = checkDef.getString("fields");
/*  28: 34 */       System.out.println(" correction field" + fieldsStr);
/*  29: 35 */       this.fields = fieldsStr.split(",");
/*  30: 36 */       String fieldsValStr = checkDef.getString("values");
/*  31: 37 */       System.out.println(" check field " + fieldsValStr);
/*  32: 38 */       this.fieldValues = fieldsValStr.split(",");
/*  33: 39 */       String errorStr = checkDef.getString("errors");
/*  34: 41 */       for (int i = 0; i < this.fields.length; i++)
/*  35:    */       {
/*  36: 42 */         String field = this.fields[i];
/*  37: 43 */         System.out.println(" split correction field" + field);
/*  38: 44 */         String fldvalue = new String(this.fieldValues[i]);
/*  39: 45 */         System.out.println(" split check field" + fldvalue);
/*  40: 46 */         String[] tt = fldvalue.split(" ");
/*  41: 47 */         String op = tt[0];
/*  42:    */         
/*  43: 49 */         Comparable val = getValue(tt[1]);
/*  44: 50 */         System.out.println("value of check field:" + val);
/*  45: 51 */         double death_age = calculateAbsoluteChildAge(getValue1("death_age_month"), getValue1("death_age_year"));
/*  46: 52 */         Double valtoCompare = null;
/*  47: 53 */         if ((val instanceof Double)) {
/*  48: 54 */           valtoCompare = Double.valueOf(rs.getDouble(field) / 365.0D);
/*  49:    */         } else {
/*  50: 56 */           valtoCompare = Double.valueOf(rs.getDouble(field) / 365.0D);
/*  51:    */         }
/*  52: 57 */         System.out.println(death_age + " " + op + " " + valtoCompare);
/*  53: 59 */         if (op.equals("="))
/*  54:    */         {
/*  55: 60 */           if (valtoCompare.doubleValue() != death_age) {
/*  56: 62 */             return false;
/*  57:    */           }
/*  58:    */         }
/*  59: 64 */         else if (op.equals(">"))
/*  60:    */         {
/*  61: 65 */           System.out.println("compare" + valtoCompare.compareTo(Double.valueOf(death_age)));
/*  62: 66 */           if (valtoCompare.doubleValue() < death_age) {
/*  63: 68 */             return true;
/*  64:    */           }
/*  65:    */         }
/*  66: 72 */         else if ((op.equals("<")) && 
/*  67: 73 */           (valtoCompare.compareTo(Double.valueOf(death_age)) > 0))
/*  68:    */         {
/*  69: 75 */           return false;
/*  70:    */         }
/*  71:    */       }
/*  72:    */     }
/*  73:    */     catch (Exception e)
/*  74:    */     {
/*  75: 85 */       e.printStackTrace();
/*  76:    */     }
/*  77: 88 */     return true;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public String getValue1(String fld)
/*  81:    */   {
/*  82:    */     try
/*  83:    */     {
/*  84:106 */       if (this.rs != null) {
/*  85:107 */         return this.rs.getString(fld);
/*  86:    */       }
/*  87:109 */       if (this.values != null) {
/*  88:110 */         return (String)this.values.get(fld);
/*  89:    */       }
/*  90:    */     }
/*  91:    */     catch (SQLException e)
/*  92:    */     {
/*  93:113 */       e.printStackTrace();
/*  94:    */     }
/*  95:115 */     return null;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public Comparable getValue(String val1)
/*  99:    */     throws Exception
/* 100:    */   {
/* 101:126 */     String fld = null;
/* 102:127 */     String val = null;
/* 103:128 */     if ((val1.startsWith("$")) || (val1.startsWith("%")))
/* 104:    */     {
/* 105:129 */       fld = val1.substring(1);
/* 106:131 */       if (fld.equals("death_age")) {
/* 107:132 */         return calculateChildDeathAge();
/* 108:    */       }
/* 109:134 */       val = getValue1(fld);
/* 110:137 */       if (val1.startsWith("%")) {
/* 111:138 */         return new Integer(val);
/* 112:    */       }
/* 113:140 */       return val;
/* 114:    */     }
/* 115:143 */     return val1;
/* 116:    */   }
/* 117:    */   
/* 118:    */   private Comparable calculateChildDeathAge()
/* 119:    */   {
/* 120:149 */     return Double.valueOf(calculateAbsoluteChildAge(getValue1("death_age_month"), getValue1("death_age_year")));
/* 121:    */   }
/* 122:    */   
/* 123:    */   public static double calculateAbsoluteChildAge(String ageM, String ageY)
/* 124:    */   {
/* 125:153 */     double age = 0.0D;
/* 126:    */     try
/* 127:    */     {
/* 128:155 */       int ageMInt = 0;
/* 129:156 */       int ageYInt = 0;
/* 130:157 */       if ((ageM != null) && (!ageM.trim().equals(""))) {
/* 131:158 */         ageMInt = Integer.parseInt(ageM);
/* 132:    */       }
/* 133:160 */       if ((ageY != null) && (!ageY.trim().equals(""))) {
/* 134:161 */         ageYInt = Integer.parseInt(ageY);
/* 135:    */       }
/* 136:164 */       if (ageYInt == 0) {
/* 137:165 */         age = ageMInt / 12.0D;
/* 138:166 */       } else if ((ageMInt == 0) || (ageMInt > 12)) {
/* 139:167 */         age = ageYInt;
/* 140:    */       } else {
/* 141:169 */         age = ageMInt / 12.0D + ageYInt;
/* 142:    */       }
/* 143:    */     }
/* 144:    */     catch (Exception e)
/* 145:    */     {
/* 146:173 */       e.printStackTrace();
/* 147:    */     }
/* 148:176 */     return age;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public static String calculateChildAge(int month, int year)
/* 152:    */   {
/* 153:180 */     String childAge = null;
/* 154:    */     try
/* 155:    */     {
/* 156:184 */       if (year == 0) {
/* 157:185 */         childAge = month + " Months";
/* 158:186 */       } else if ((month == 0) || (month > 12)) {
/* 159:187 */         childAge = year + " Years";
/* 160:    */       } else {
/* 161:189 */         childAge = year + "Y " + month + "M";
/* 162:    */       }
/* 163:    */     }
/* 164:    */     catch (Exception e)
/* 165:    */     {
/* 166:193 */       e.printStackTrace();
/* 167:    */     }
/* 168:196 */     return childAge;
/* 169:    */   }
/* 170:    */   
/* 171:    */   public boolean validate(Hashtable rs, Hashtable flags, ResultSet checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 172:    */   {
/* 173:202 */     this.values = rs;
/* 174:203 */     this.flags = flags;
/* 175:204 */     this.name = name;
/* 176:205 */     this.value = value;
/* 177:    */     try
/* 178:    */     {
/* 179:207 */       String fieldsStr = checkDef.getString("fields");
/* 180:208 */       System.out.println(" correction field" + fieldsStr);
/* 181:209 */       this.fields = fieldsStr.split(",");
/* 182:210 */       String fieldsValStr = checkDef.getString("values");
/* 183:211 */       System.out.println(" check field " + fieldsValStr);
/* 184:212 */       this.fieldValues = fieldsValStr.split(",");
/* 185:213 */       String errorStr = checkDef.getString("errors");
/* 186:215 */       for (int i = 0; i < this.fields.length; i++)
/* 187:    */       {
/* 188:216 */         String field = this.fields[i];
/* 189:217 */         System.out.println(" split correction field" + field);
/* 190:218 */         String fldvalue = new String(this.fieldValues[i]);
/* 191:219 */         System.out.println(" split check field" + fldvalue);
/* 192:220 */         String[] tt = fldvalue.split(" ");
/* 193:221 */         String op = tt[0];
/* 194:    */         
/* 195:223 */         Comparable val = getValue(tt[1]);
/* 196:224 */         System.out.println("value of check field:" + val);
/* 197:225 */         double death_age = calculateAbsoluteChildAge(getValue1("death_age_month"), getValue1("death_age_year"));
/* 198:226 */         Double valtoCompare = null;
/* 199:    */         
/* 200:228 */         valtoCompare = Double.valueOf(Double.parseDouble(getValue1(field)) / 365.0D);
/* 201:    */         
/* 202:    */ 
/* 203:231 */         System.out.println(death_age + " " + op + " " + valtoCompare);
/* 204:233 */         if (op.equals("="))
/* 205:    */         {
/* 206:234 */           if (valtoCompare.doubleValue() != death_age) {
/* 207:236 */             return false;
/* 208:    */           }
/* 209:    */         }
/* 210:238 */         else if (op.equals(">"))
/* 211:    */         {
/* 212:239 */           System.out.println("compare" + valtoCompare.compareTo(Double.valueOf(death_age)));
/* 213:240 */           if (valtoCompare.doubleValue() < death_age) {
/* 214:242 */             return true;
/* 215:    */           }
/* 216:    */         }
/* 217:246 */         else if ((op.equals("<")) && 
/* 218:247 */           (valtoCompare.compareTo(Double.valueOf(death_age)) > 0))
/* 219:    */         {
/* 220:249 */           return false;
/* 221:    */         }
/* 222:    */       }
/* 223:254 */       return false;
/* 224:    */     }
/* 225:    */     catch (Exception localException) {}
/* 226:260 */     return false;
/* 227:    */   }
/* 228:    */   
/* 229:    */   public boolean validate(Hashtable rs, Hashtable flags, Hashtable checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 230:    */   {
/* 231:266 */     this.values = rs;
/* 232:267 */     this.flags = flags;
/* 233:268 */     this.name = name;
/* 234:269 */     this.value = value;
/* 235:    */     try
/* 236:    */     {
/* 237:271 */       String fieldsStr = (String)checkDef.get("fields");
/* 238:272 */       System.out.println(" correction field" + fieldsStr);
/* 239:273 */       this.fields = fieldsStr.split(",");
/* 240:274 */       String fieldsValStr = (String)checkDef.get("values");
/* 241:275 */       System.out.println(" check field " + fieldsValStr);
/* 242:276 */       this.fieldValues = fieldsValStr.split(",");
/* 243:277 */       String errorStr = (String)checkDef.get("errors");
/* 244:279 */       for (int i = 0; i < this.fields.length; i++)
/* 245:    */       {
/* 246:280 */         String field = this.fields[i];
/* 247:281 */         System.out.println(" split correction field" + field);
/* 248:282 */         String fldvalue = new String(this.fieldValues[i]);
/* 249:283 */         System.out.println(" split check field" + fldvalue);
/* 250:284 */         String[] tt = fldvalue.split(" ");
/* 251:285 */         String op = tt[0];
/* 252:    */         
/* 253:287 */         Comparable val = getValue(tt[1]);
/* 254:288 */         System.out.println("value of check field:" + val);
/* 255:289 */         double death_age = calculateAbsoluteChildAge(getValue1("death_age_month"), getValue1("death_age_year"));
/* 256:290 */         Double valtoCompare = null;
/* 257:    */         
/* 258:292 */         valtoCompare = Double.valueOf(Double.parseDouble(getValue1(field)) / 365.0D);
/* 259:    */         
/* 260:    */ 
/* 261:295 */         System.out.println(death_age + " " + op + " " + valtoCompare);
/* 262:297 */         if (op.equals("="))
/* 263:    */         {
/* 264:298 */           if (valtoCompare.doubleValue() != death_age) {
/* 265:300 */             return false;
/* 266:    */           }
/* 267:    */         }
/* 268:302 */         else if (op.equals(">"))
/* 269:    */         {
/* 270:303 */           System.out.println("compare" + valtoCompare.compareTo(Double.valueOf(death_age)));
/* 271:304 */           if (valtoCompare.doubleValue() < death_age) {
/* 272:306 */             return true;
/* 273:    */           }
/* 274:    */         }
/* 275:310 */         else if ((op.equals("<")) && 
/* 276:311 */           (valtoCompare.compareTo(Double.valueOf(death_age)) > 0))
/* 277:    */         {
/* 278:313 */           return false;
/* 279:    */         }
/* 280:    */       }
/* 281:318 */       return false;
/* 282:    */     }
/* 283:    */     catch (Exception localException) {}
/* 284:324 */     return false;
/* 285:    */   }
/* 286:    */   
/* 287:    */   public boolean validate1(Hashtable rs, Hashtable flags, Hashtable checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 288:    */   {
/* 289:331 */     return false;
/* 290:    */   }
/* 291:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-qa\ken-qa.jar
 * Qualified Name:     com.kentropy.cme.qa.neochecks.ChildCrossChecks
 * JD-Core Version:    0.7.0.1
 */