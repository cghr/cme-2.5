/*   1:    */ package com.kentropy.cme.qa.neochecks;
/*   2:    */ 
/*   3:    */ import com.kentropy.util.DbConnection;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.sql.Connection;
/*   6:    */ import java.sql.ResultSet;
/*   7:    */ import java.sql.SQLException;
/*   8:    */ import java.sql.Statement;
/*   9:    */ import java.util.Hashtable;
/*  10:    */ import java.util.Vector;
/*  11:    */ 
/*  12:    */ public class CrossChecks
/*  13:    */   implements QualityCheck
/*  14:    */ {
/*  15: 22 */   Connection c = null;
/*  16: 24 */   public static Hashtable crossCache = new Hashtable();
/*  17:    */   
/*  18:    */   public Vector getCrossChecks(String domain, String field)
/*  19:    */     throws SQLException
/*  20:    */   {
/*  21: 28 */     String key = domain + "-" + field;
/*  22: 29 */     if (crossCache.get(key) != null) {
/*  23: 30 */       return (Vector)crossCache.get(key);
/*  24:    */     }
/*  25: 31 */     String sql = "select * from crosschecks where name='" + field + "' and domain='" + domain + "'";
/*  26:    */     
/*  27: 33 */     Connection con = DbConnection.getConnection();
/*  28: 34 */     ResultSet rs1 = null;
/*  29:    */     
/*  30:    */ 
/*  31: 37 */     Statement stmt = con.createStatement();
/*  32: 38 */     rs1 = stmt.executeQuery(sql);
/*  33: 39 */     String[] flds = { "qc", "name", "errors", "fields", "values" };
/*  34:    */     
/*  35: 41 */     Vector v = new Vector();
/*  36: 42 */     while (rs1.next())
/*  37:    */     {
/*  38: 43 */       Hashtable ht = new Hashtable();
/*  39: 44 */       for (int i = 0; i < flds.length; i++) {
/*  40: 46 */         if (rs1.getString(flds[i]) != null) {
/*  41: 47 */           ht.put(flds[i], rs1.getString(flds[i]));
/*  42:    */         }
/*  43:    */       }
/*  44: 49 */       v.add(ht);
/*  45:    */     }
/*  46: 52 */     crossCache.put(key, v);
/*  47: 53 */     return v;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public boolean validate(Hashtable rs, Hashtable flags, String name, String value, StringBuffer errorMsg, String domain)
/*  51:    */   {
/*  52: 58 */     Hashtable ht = new Hashtable();
/*  53:    */     try
/*  54:    */     {
/*  55: 60 */       boolean flg = true;
/*  56: 61 */       Vector cross = getCrossChecks(domain, name);
/*  57: 62 */       for (int i = 0; i < cross.size(); i++)
/*  58:    */       {
/*  59: 63 */         ht = (Hashtable)cross.get(i);
/*  60: 64 */         String className = (String)ht.get("qc");
/*  61: 65 */         QualityCheck obj = (QualityCheck)Class.forName(className).newInstance();
/*  62:    */         
/*  63:    */ 
/*  64:    */ 
/*  65:    */ 
/*  66:    */ 
/*  67:    */ 
/*  68:    */ 
/*  69:    */ 
/*  70:    */ 
/*  71:    */ 
/*  72:    */ 
/*  73:    */ 
/*  74: 78 */         flg = obj.validate(rs, flags, ht, (String)ht.get("name"), value, errorMsg, domain);
/*  75: 83 */         if (!flg)
/*  76:    */         {
/*  77: 84 */           errorMsg.setLength(0);
/*  78: 85 */           errorMsg.append((String)ht.get("errors"));
/*  79: 86 */           flags.put(name, (String)ht.get("errors"));
/*  80:    */           
/*  81: 88 */           return flg;
/*  82:    */         }
/*  83:    */       }
/*  84: 94 */       return true;
/*  85:    */     }
/*  86:    */     catch (NullPointerException localNullPointerException) {}catch (SQLException e)
/*  87:    */     {
/*  88:101 */       e.printStackTrace();
/*  89:    */     }
/*  90:    */     catch (InstantiationException e)
/*  91:    */     {
/*  92:104 */       e.printStackTrace();
/*  93:    */     }
/*  94:    */     catch (IllegalAccessException e)
/*  95:    */     {
/*  96:107 */       e.printStackTrace();
/*  97:    */     }
/*  98:    */     catch (ClassNotFoundException e)
/*  99:    */     {
/* 100:110 */       e.printStackTrace();
/* 101:    */     }
/* 102:    */     try
/* 103:    */     {
/* 104:115 */       errorMsg.setLength(0);
/* 105:116 */       errorMsg.append((String)ht.get("errors"));
/* 106:    */     }
/* 107:    */     catch (Exception e)
/* 108:    */     {
/* 109:119 */       e.printStackTrace();
/* 110:    */     }
/* 111:121 */     return false;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public boolean validate(ResultSet rs, ResultSet checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 115:    */   {
/* 116:128 */     ResultSet rs1 = null;
/* 117:129 */     boolean flg = true;
/* 118:    */     try
/* 119:    */     {
/* 120:131 */       if (this.c == null) {
/* 121:133 */         this.c = DbConnection.getConnection();
/* 122:    */       }
/* 123:135 */       String crossQuery = "select * from crosschecks where name='" + 
/* 124:136 */         name + "' and domain='" + domain + "'";
/* 125:    */       
/* 126:138 */       Statement stmt1 = this.c.createStatement();
/* 127:139 */       rs1 = stmt1.executeQuery(crossQuery);
/* 128:140 */       if (rs1.next())
/* 129:    */       {
/* 130:142 */         String className = rs1.getString("qc");
/* 131:143 */         QualityCheck obj = (QualityCheck)Class.forName(className).newInstance();
/* 132:    */         
/* 133:    */ 
/* 134:    */ 
/* 135:    */ 
/* 136:    */ 
/* 137:    */ 
/* 138:    */ 
/* 139:    */ 
/* 140:    */ 
/* 141:    */ 
/* 142:    */ 
/* 143:    */ 
/* 144:156 */         flg = obj.validate(rs, rs1, rs1.getString("name"), value, errorMsg, domain);
/* 145:157 */         System.out.println("Result of Idcheck in crosscheck:::" + flg);
/* 146:161 */         if (!flg)
/* 147:    */         {
/* 148:162 */           errorMsg.setLength(0);
/* 149:163 */           errorMsg.append(rs1.getString("errors"));
/* 150:164 */           System.out.println("crossError::" + errorMsg);
/* 151:    */         }
/* 152:167 */         return flg;
/* 153:    */       }
/* 154:169 */       stmt1.close();
/* 155:    */       
/* 156:171 */       return true;
/* 157:    */     }
/* 158:    */     catch (Exception e)
/* 159:    */     {
/* 160:175 */       e.printStackTrace();
/* 161:    */       try
/* 162:    */       {
/* 163:180 */         errorMsg.setLength(0);
/* 164:181 */         errorMsg.append(rs1.getString("errors"));
/* 165:    */       }
/* 166:    */       catch (SQLException e)
/* 167:    */       {
/* 168:184 */         e.printStackTrace();
/* 169:    */       }
/* 170:    */     }
/* 171:186 */     return false;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public boolean validate1(Hashtable rs, Hashtable flags, String name, String value, StringBuffer errorMsg, String domain)
/* 175:    */   {
/* 176:193 */     ResultSet rs1 = null;
/* 177:194 */     boolean flg = true;
/* 178:    */     try
/* 179:    */     {
/* 180:196 */       if (this.c == null) {
/* 181:198 */         this.c = DbConnection.getConnection();
/* 182:    */       }
/* 183:200 */       String crossQuery = "select * from crosschecks where name='" + 
/* 184:201 */         name + "' and domain='" + domain + "'";
/* 185:    */       
/* 186:203 */       Statement stmt1 = this.c.createStatement();
/* 187:204 */       rs1 = stmt1.executeQuery(crossQuery);
/* 188:205 */       while (rs1.next())
/* 189:    */       {
/* 190:207 */         String className = rs1.getString("qc");
/* 191:208 */         QualityCheck obj = (QualityCheck)Class.forName(className).newInstance();
/* 192:    */         
/* 193:    */ 
/* 194:    */ 
/* 195:    */ 
/* 196:    */ 
/* 197:    */ 
/* 198:    */ 
/* 199:    */ 
/* 200:    */ 
/* 201:    */ 
/* 202:    */ 
/* 203:    */ 
/* 204:221 */         flg = obj.validate(rs, flags, rs1, rs1.getString("name"), value, errorMsg, domain);
/* 205:226 */         if (!flg)
/* 206:    */         {
/* 207:227 */           errorMsg.setLength(0);
/* 208:228 */           errorMsg.append(rs1.getString("errors"));
/* 209:229 */           flags.put(name, rs1.getString("errors"));
/* 210:    */           
/* 211:231 */           return flg;
/* 212:    */         }
/* 213:    */       }
/* 214:235 */       stmt1.close();
/* 215:    */       
/* 216:237 */       return true;
/* 217:    */     }
/* 218:    */     catch (Exception e)
/* 219:    */     {
/* 220:241 */       e.printStackTrace();
/* 221:    */       try
/* 222:    */       {
/* 223:246 */         errorMsg.setLength(0);
/* 224:247 */         errorMsg.append(rs1.getString("errors"));
/* 225:    */       }
/* 226:    */       catch (SQLException e)
/* 227:    */       {
/* 228:250 */         e.printStackTrace();
/* 229:    */       }
/* 230:    */     }
/* 231:252 */     return false;
/* 232:    */   }
/* 233:    */   
/* 234:    */   public static void main(String[] args)
/* 235:    */   {
/* 236:    */     try
/* 237:    */     {
/* 238:261 */       CrossChecks cd = new CrossChecks();
/* 239:262 */       Connection c = DbConnection.getConnection();
/* 240:263 */       Statement s = c.createStatement();
/* 241:264 */       ResultSet rs = s.executeQuery("SELECT * from child where uniqno='01200001_01_01'");
/* 242:265 */       rs.next();
/* 243:266 */       StringBuffer err = new StringBuffer();
/* 244:    */       
/* 245:268 */       System.out.println(cd.validate(rs, null, "fever_day", "25", err, "child"));
/* 246:    */     }
/* 247:    */     catch (Exception e)
/* 248:    */     {
/* 249:276 */       e.printStackTrace();
/* 250:    */     }
/* 251:    */   }
/* 252:    */   
/* 253:    */   public boolean validate(Hashtable rs, Hashtable flags, ResultSet checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 254:    */   {
/* 255:285 */     return false;
/* 256:    */   }
/* 257:    */   
/* 258:    */   public boolean validate(Hashtable rs, Hashtable flags, Hashtable checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 259:    */   {
/* 260:290 */     return false;
/* 261:    */   }
/* 262:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-qa\ken-qa.jar
 * Qualified Name:     com.kentropy.cme.qa.neochecks.CrossChecks
 * JD-Core Version:    0.7.0.1
 */