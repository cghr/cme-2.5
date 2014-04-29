/*   1:    */ package com.kentropy.cme.qa.neochecks;
/*   2:    */ 
/*   3:    */ import com.kentropy.cme.qa.Handler1;
/*   4:    */ import com.kentropy.util.DbConnection;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.sql.Connection;
/*   7:    */ import java.sql.ResultSet;
/*   8:    */ import java.sql.SQLException;
/*   9:    */ import java.sql.Statement;
/*  10:    */ import java.util.Hashtable;
/*  11:    */ import java.util.Vector;
/*  12:    */ 
/*  13:    */ public class FlowErrorClean
/*  14:    */   implements ErrorCleaning
/*  15:    */ {
/*  16:    */   public boolean cleanField(ResultSet rs, String field, String value, StringBuffer errBuf, String domain)
/*  17:    */   {
/*  18: 25 */     String sql = "select * from corrections where name='" + field + "' and domain='" + domain + "'";
/*  19: 26 */     Connection con = DbConnection.getConnection();
/*  20: 27 */     ResultSet rs1 = null;
/*  21:    */     try
/*  22:    */     {
/*  23: 30 */       Statement stmt = con.createStatement();
/*  24: 31 */       rs1 = stmt.executeQuery(sql);
/*  25: 32 */       String uniqno = rs.getString("uniqno");
/*  26: 34 */       if (rs1.next())
/*  27:    */       {
/*  28: 35 */         String correctionField = rs1.getString("fields");
/*  29:    */         
/*  30:    */ 
/*  31:    */ 
/*  32: 39 */         String[] oldValueArray = rs.getString(rs1.getString("name")).split(",");
/*  33: 40 */         String correctionValues = rs1.getString("source_value");
/*  34: 41 */         boolean isCorrectable = false;
/*  35: 42 */         String oldValue = null;
/*  36: 43 */         for (int j = 0; j < oldValueArray.length; j++)
/*  37:    */         {
/*  38: 44 */           oldValue = oldValueArray[j];
/*  39: 45 */           if (correctionValues.contains(","))
/*  40:    */           {
/*  41: 46 */             String[] values = correctionValues.split(",");
/*  42: 47 */             for (int i = 0; i < values.length; i++) {
/*  43: 48 */               if (oldValue.equals(values[i]))
/*  44:    */               {
/*  45: 49 */                 isCorrectable = true;
/*  46: 50 */                 break;
/*  47:    */               }
/*  48:    */             }
/*  49:    */           }
/*  50: 53 */           else if (correctionValues.contains("-"))
/*  51:    */           {
/*  52: 54 */             String[] values = correctionValues.split("-");
/*  53: 55 */             for (int i = 0; i < values.length; i++)
/*  54:    */             {
/*  55: 56 */               int min = Integer.parseInt(values[0]);
/*  56: 57 */               int max = Integer.parseInt(values[1]);
/*  57: 58 */               if ((oldValue.equals("")) || (oldValue == null))
/*  58:    */               {
/*  59: 59 */                 isCorrectable = false;
/*  60:    */               }
/*  61:    */               else
/*  62:    */               {
/*  63: 61 */                 int oldValueInt = Integer.parseInt(oldValue);
/*  64: 62 */                 if ((oldValueInt >= min) && (oldValueInt <= max))
/*  65:    */                 {
/*  66: 63 */                   isCorrectable = true;
/*  67: 64 */                   break;
/*  68:    */                 }
/*  69:    */               }
/*  70:    */             }
/*  71:    */           }
/*  72: 69 */           else if (oldValue.equals(correctionValues))
/*  73:    */           {
/*  74: 70 */             isCorrectable = true;
/*  75:    */           }
/*  76:    */         }
/*  77: 74 */         if (isCorrectable)
/*  78:    */         {
/*  79: 76 */           String newValue = rs1.getString("values");
/*  80: 77 */           Handler1 h = null;
/*  81: 78 */           h = new Handler1(null, null);
/*  82: 79 */           h.insertInQaHistory(uniqno, correctionField, oldValue, newValue, domain, "auto");
/*  83:    */           
/*  84: 81 */           h.updateField(domain, uniqno, rs1.getString("fields"), newValue);
/*  85: 82 */           errBuf.append(uniqno + " " + field + " " + value + ": Correcting " + rs1.getString("fields") + " to " + newValue + "\n");
/*  86: 83 */           System.out.println("successfully updated");
/*  87:    */         }
/*  88: 85 */         return true;
/*  89:    */       }
/*  90:    */     }
/*  91:    */     catch (SQLException e)
/*  92:    */     {
/*  93: 90 */       e.printStackTrace();
/*  94:    */     }
/*  95: 92 */     return false;
/*  96:    */   }
/*  97:    */   
/*  98: 96 */   public static Hashtable correctionCache = new Hashtable();
/*  99:    */   
/* 100:    */   public Vector getCorrections(String domain)
/* 101:    */     throws SQLException
/* 102:    */   {
/* 103:100 */     String key = domain;
/* 104:101 */     if (correctionCache.get(key) != null) {
/* 105:102 */       return (Vector)correctionCache.get(key);
/* 106:    */     }
/* 107:103 */     String sql = "select * from corrections where domain='" + domain + "'";
/* 108:    */     
/* 109:105 */     Connection con = DbConnection.getConnection();
/* 110:106 */     ResultSet rs1 = null;
/* 111:    */     
/* 112:    */ 
/* 113:109 */     Statement stmt = con.createStatement();
/* 114:110 */     rs1 = stmt.executeQuery(sql);
/* 115:111 */     String[] flds = { "fields", "name", "source_value", "values", "errors" };
/* 116:    */     
/* 117:113 */     Vector v = new Vector();
/* 118:114 */     while (rs1.next())
/* 119:    */     {
/* 120:115 */       Hashtable ht = new Hashtable();
/* 121:116 */       for (int i = 0; i < flds.length; i++) {
/* 122:117 */         if (rs1.getString(flds[i]) != null) {
/* 123:118 */           ht.put(flds[i], rs1.getString(flds[i]));
/* 124:    */         }
/* 125:    */       }
/* 126:120 */       v.add(ht);
/* 127:    */     }
/* 128:123 */     correctionCache.put(key, v);
/* 129:124 */     return v;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public boolean cleanField(Hashtable rs, Hashtable flags, Hashtable newValues, String field, String value, StringBuffer errBuf, String domain)
/* 133:    */   {
/* 134:132 */     String uniqno = (String)rs.get("uniqno");
/* 135:    */     try
/* 136:    */     {
/* 137:134 */       Vector corrections = getCorrections(domain);
/* 138:135 */       System.out.println("INSIDE cleanField()" + corrections.size() + " domain:" + domain);
/* 139:136 */       for (int k = 0; k < corrections.size(); k++)
/* 140:    */       {
/* 141:138 */         Hashtable ht = (Hashtable)corrections.get(k);
/* 142:139 */         String correctionField = (String)ht.get("fields");
/* 143:140 */         System.out.println("CORRECTION FIELD1:" + correctionField + " ERRORS: " + ht.get("errors"));
/* 144:141 */         System.out.println("NAME:" + ht.get("name") + " ERRORS: " + ht.get("errors"));
/* 145:142 */         if (flags.get((String)ht.get("name")).toString().startsWith(ht.get("errors").toString()))
/* 146:    */         {
/* 147:145 */           String[] oldValueArray = rs.get((String)ht.get("name")).toString().split(",");
/* 148:146 */           String correctionValues = (String)ht.get("source_value");
/* 149:147 */           boolean isCorrectable = false;
/* 150:148 */           String oldValue = null;
/* 151:149 */           System.out.println("CORRECTION FIELD: " + correctionField + " OldVale" + rs.get((String)ht.get("name")) + " correctionValues:" + correctionValues + " NEWVALUE:" + (String)ht.get("values"));
/* 152:150 */           for (int j = 0; j < oldValueArray.length; j++)
/* 153:    */           {
/* 154:151 */             oldValue = oldValueArray[j];
/* 155:152 */             if (correctionValues.contains(","))
/* 156:    */             {
/* 157:153 */               String[] values = correctionValues.split(",");
/* 158:154 */               for (int i = 0; i < values.length; i++) {
/* 159:155 */                 if (oldValue.equals(values[i]))
/* 160:    */                 {
/* 161:156 */                   isCorrectable = true;
/* 162:157 */                   break;
/* 163:    */                 }
/* 164:    */               }
/* 165:    */             }
/* 166:160 */             else if (correctionValues.contains("-"))
/* 167:    */             {
/* 168:161 */               String[] values = correctionValues.split("-");
/* 169:162 */               for (int i = 0; i < values.length; i++)
/* 170:    */               {
/* 171:163 */                 int min = Integer.parseInt(values[0]);
/* 172:164 */                 int max = Integer.parseInt(values[1]);
/* 173:165 */                 System.out.println("OLD VALUE:" + oldValue);
/* 174:166 */                 if ((oldValue == null) || (oldValue.equals("")))
/* 175:    */                 {
/* 176:167 */                   isCorrectable = false;
/* 177:    */                 }
/* 178:    */                 else
/* 179:    */                 {
/* 180:    */                   try
/* 181:    */                   {
/* 182:171 */                     oldValueInt = Integer.parseInt(oldValue);
/* 183:    */                   }
/* 184:    */                   catch (NumberFormatException nfe)
/* 185:    */                   {
/* 186:    */                     int oldValueInt;
/* 187:173 */                     continue;
/* 188:    */                   }
/* 189:    */                   int oldValueInt;
/* 190:175 */                   if ((oldValueInt >= min) && (oldValueInt <= max))
/* 191:    */                   {
/* 192:176 */                     isCorrectable = true;
/* 193:177 */                     break;
/* 194:    */                   }
/* 195:    */                 }
/* 196:    */               }
/* 197:    */             }
/* 198:182 */             else if (oldValue.equals(correctionValues))
/* 199:    */             {
/* 200:183 */               isCorrectable = true;
/* 201:    */             }
/* 202:    */           }
/* 203:187 */           System.out.println("CORRECTABLE:" + isCorrectable);
/* 204:188 */           if (isCorrectable)
/* 205:    */           {
/* 206:190 */             String newValue = (String)ht.get("values");
/* 207:191 */             Handler1 h = null;
/* 208:    */             
/* 209:    */ 
/* 210:    */ 
/* 211:195 */             rs.put((String)ht.get("fields"), newValue);
/* 212:    */             
/* 213:    */ 
/* 214:198 */             errBuf.append(uniqno + " " + field + " " + value + ": Correcting " + (String)ht.get("fields") + " to " + newValue + "\n");
/* 215:    */           }
/* 216:    */         }
/* 217:    */       }
/* 218:205 */       return true;
/* 219:    */     }
/* 220:    */     catch (SQLException e)
/* 221:    */     {
/* 222:208 */       e.printStackTrace();
/* 223:    */     }
/* 224:210 */     return false;
/* 225:    */   }
/* 226:    */   
/* 227:    */   public boolean cleanField1(Hashtable rs, Hashtable flags, Hashtable newValues, String field, String value, StringBuffer errBuf, String domain)
/* 228:    */   {
/* 229:216 */     String sql = "select * from corrections where name='" + field + "' and domain='" + domain + "'";
/* 230:217 */     Connection con = DbConnection.getConnection();
/* 231:218 */     ResultSet rs1 = null;
/* 232:    */     try
/* 233:    */     {
/* 234:221 */       Statement stmt = con.createStatement();
/* 235:222 */       rs1 = stmt.executeQuery(sql);
/* 236:223 */       String uniqno = (String)rs.get("uniqno");
/* 237:225 */       if (rs1.next())
/* 238:    */       {
/* 239:226 */         String correctionField = rs1.getString("fields");
/* 240:    */         
/* 241:    */ 
/* 242:    */ 
/* 243:230 */         String[] oldValueArray = rs.get(rs1.getString("name")).toString().split(",");
/* 244:231 */         String correctionValues = rs1.getString("source_value");
/* 245:232 */         boolean isCorrectable = false;
/* 246:233 */         String oldValue = null;
/* 247:234 */         for (int j = 0; j < oldValueArray.length; j++)
/* 248:    */         {
/* 249:235 */           oldValue = oldValueArray[j];
/* 250:236 */           if (correctionValues.contains(","))
/* 251:    */           {
/* 252:237 */             String[] values = correctionValues.split(",");
/* 253:238 */             for (int i = 0; i < values.length; i++) {
/* 254:239 */               if (oldValue.equals(values[i]))
/* 255:    */               {
/* 256:240 */                 isCorrectable = true;
/* 257:241 */                 break;
/* 258:    */               }
/* 259:    */             }
/* 260:    */           }
/* 261:244 */           else if (correctionValues.contains("-"))
/* 262:    */           {
/* 263:245 */             String[] values = correctionValues.split("-");
/* 264:246 */             for (int i = 0; i < values.length; i++)
/* 265:    */             {
/* 266:247 */               int min = Integer.parseInt(values[0]);
/* 267:248 */               int max = Integer.parseInt(values[1]);
/* 268:249 */               if ((oldValue.equals("")) || (oldValue == null))
/* 269:    */               {
/* 270:250 */                 isCorrectable = false;
/* 271:    */               }
/* 272:    */               else
/* 273:    */               {
/* 274:252 */                 int oldValueInt = Integer.parseInt(oldValue);
/* 275:253 */                 if ((oldValueInt >= min) && (oldValueInt <= max))
/* 276:    */                 {
/* 277:254 */                   isCorrectable = true;
/* 278:255 */                   break;
/* 279:    */                 }
/* 280:    */               }
/* 281:    */             }
/* 282:    */           }
/* 283:260 */           else if (oldValue.equals(correctionValues))
/* 284:    */           {
/* 285:261 */             isCorrectable = true;
/* 286:    */           }
/* 287:    */         }
/* 288:265 */         if (isCorrectable)
/* 289:    */         {
/* 290:267 */           String newValue = rs1.getString("values");
/* 291:268 */           Handler1 h = null;
/* 292:    */           
/* 293:    */ 
/* 294:271 */           newValues.put(rs1.getString("fields"), newValue);
/* 295:    */           
/* 296:    */ 
/* 297:274 */           errBuf.append(uniqno + " " + field + " " + value + ": Correcting " + rs1.getString("fields") + " to " + newValue + "\n");
/* 298:    */         }
/* 299:277 */         return true;
/* 300:    */       }
/* 301:    */     }
/* 302:    */     catch (SQLException e)
/* 303:    */     {
/* 304:282 */       e.printStackTrace();
/* 305:    */     }
/* 306:284 */     return false;
/* 307:    */   }
/* 308:    */   
/* 309:    */   public static void main(String[] arg)
/* 310:    */   {
/* 311:288 */     FlowErrorClean fec = new FlowErrorClean();
/* 312:    */   }
/* 313:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-qa\ken-qa.jar
 * Qualified Name:     com.kentropy.cme.qa.neochecks.FlowErrorClean
 * JD-Core Version:    0.7.0.1
 */