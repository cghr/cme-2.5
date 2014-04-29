/*   1:    */ package com.kentropy.agents.va;
/*   2:    */ 
/*   3:    */ import com.kentropy.cme.qa.transformation.Transformation;
/*   4:    */ import com.kentropy.crypt.DesEncryptor;
/*   5:    */ import com.kentropy.db.TestXUIDB;
/*   6:    */ import com.kentropy.process.Agent;
/*   7:    */ import com.kentropy.process.Process;
/*   8:    */ import com.kentropy.process.VAStateMachine;
/*   9:    */ import com.kentropy.transfer.Client;
/*  10:    */ import com.kentropy.util.DbConnection;
/*  11:    */ import java.io.PrintStream;
/*  12:    */ import java.sql.Connection;
/*  13:    */ import java.sql.ResultSet;
/*  14:    */ import java.sql.ResultSetMetaData;
/*  15:    */ import java.sql.SQLException;
/*  16:    */ import java.sql.Statement;
/*  17:    */ import java.text.SimpleDateFormat;
/*  18:    */ import java.util.Date;
/*  19:    */ import java.util.Enumeration;
/*  20:    */ import java.util.Hashtable;
/*  21:    */ import java.util.Vector;
/*  22:    */ import net.xoetrope.xui.data.XBaseModel;
/*  23:    */ import net.xoetrope.xui.data.XModel;
/*  24:    */ 
/*  25:    */ public class PushToCMEAgent
/*  26:    */   implements Agent
/*  27:    */ {
/*  28: 28 */   VAStateMachine sm = null;
/*  29: 30 */   Process p = null;
/*  30: 32 */   public String ext = "png";
/*  31: 34 */   int count = 0;
/*  32:    */   
/*  33:    */   public void readFromDatabase(String uniqno, String domain, Hashtable ht)
/*  34:    */     throws InterruptedException, Exception
/*  35:    */   {
/*  36: 39 */     String sql = "SELECT * from " + domain + " WHERE uniqno='" + uniqno + "'";
/*  37: 40 */     Connection con = DbConnection.getConnection();
/*  38: 41 */     Statement stmt = con.createStatement();
/*  39:    */     
/*  40: 43 */     ResultSet rs = stmt.executeQuery(sql);
/*  41: 44 */     ResultSetMetaData rsMetaData = rs.getMetaData();
/*  42: 45 */     if (rs.next()) {
/*  43: 46 */       for (int i = 0; i < rsMetaData.getColumnCount(); i++)
/*  44:    */       {
/*  45: 47 */         String field = rsMetaData.getColumnName(i + 1);
/*  46: 48 */         String value = rs.getString(field);
/*  47: 49 */         ht.put(field, value);
/*  48:    */       }
/*  49:    */     } else {
/*  50: 52 */       System.out.println("Uniqno:" + uniqno + " Not Found");
/*  51:    */     }
/*  52: 54 */     stmt.close();
/*  53: 55 */     con.close();
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void stateChange(Process p)
/*  57:    */   {
/*  58: 60 */     System.out.println("Inside PushToCMEAgent.stateChange()");
/*  59: 61 */     this.p = p;
/*  60: 62 */     this.sm = ((VAStateMachine)p.states);
/*  61: 63 */     if (this.sm.currentState.equals("pushtocme"))
/*  62:    */     {
/*  63: 65 */       this.sm = ((VAStateMachine)p.states);
/*  64: 66 */       String uniqno = p.pid.split("/")[1];
/*  65:    */       
/*  66: 68 */       Hashtable values = new Hashtable();
/*  67: 69 */       System.out.println("Domain:" + this.sm.domain);
/*  68:    */       
/*  69: 71 */       System.out.println("Uniqno:" + p.pid.split("/")[1]);
/*  70: 72 */       System.out.println("size:" + values.size());
/*  71:    */       try
/*  72:    */       {
/*  73: 74 */         System.out.println("starting " + p.pid);
/*  74: 75 */         readFromDatabase(uniqno, this.sm.domain, values);
/*  75: 76 */         System.out.println("Read from database " + p.pid);
/*  76: 77 */         uploadToCME(values, this.sm.domain);
/*  77: 78 */         System.out.println("uploaded " + p.pid);
/*  78: 79 */         sendImage(uniqno, "png", this.sm.domain);
/*  79: 80 */         System.out.println("Sent images " + p.pid);
/*  80: 81 */         this.sm.pushedToCME = true;
/*  81:    */         
/*  82: 83 */         Process.transition(p.pid);
/*  83:    */       }
/*  84:    */       catch (Exception e)
/*  85:    */       {
/*  86: 86 */         e.printStackTrace();
/*  87:    */       }
/*  88:    */     }
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void saveCmeValues(Hashtable cme)
/*  92:    */     throws Exception
/*  93:    */   {
/*  94: 93 */     Enumeration e = cme.keys();
/*  95: 95 */     while (e.hasMoreElements())
/*  96:    */     {
/*  97: 96 */       String key = e.nextElement().toString();
/*  98: 97 */       String value = cme.get(key).toString();
/*  99: 98 */       TestXUIDB.getInstance().saveKeyValue("keyvalue", key, value);
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   public String saveCmeValues1(Hashtable cme)
/* 104:    */     throws Exception
/* 105:    */   {
/* 106:102 */     Enumeration e = cme.keys();
/* 107:103 */     String value1 = "";
/* 108:104 */     String sep = "@#$%^";
/* 109:105 */     int count = 0;
/* 110:106 */     while (e.hasMoreElements())
/* 111:    */     {
/* 112:107 */       String key = e.nextElement().toString();
/* 113:108 */       String value = cme.get(key).toString();
/* 114:109 */       value1 = value1 + (count == 0 ? "" : sep) + key + "=" + value;
/* 115:110 */       count++;
/* 116:    */     }
/* 117:112 */     return value1;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void sendImage(String uniqnos, String ext, String domain)
/* 121:    */     throws Exception
/* 122:    */   {
/* 123:116 */     String imagepath = TestXUIDB.getInstance().getImagePath();
/* 124:117 */     DesEncryptor encryptor = new DesEncryptor();
/* 125:118 */     String[] uniqnoArray = uniqnos.split(",");
/* 126:119 */     Client cl = new Client();
/* 127:120 */     for (int i = 0; i < uniqnoArray.length; i++)
/* 128:    */     {
/* 129:121 */       String firstpage = uniqnoArray[i] + "_0_blank." + ext;
/* 130:122 */       String secondpage = uniqnoArray[i] + "_1_blank." + ext;
/* 131:123 */       String codcrop = uniqnoArray[i] + "_cod." + ext;
/* 132:    */       
/* 133:125 */       cl.run(imagepath + "encrypted/" + firstpage, firstpage, "admin");
/* 134:126 */       cl.run(imagepath + "encrypted/" + secondpage, secondpage, "admin");
/* 135:127 */       cl.run(imagepath + "encrypted/" + codcrop, codcrop, "admin");
/* 136:    */     }
/* 137:    */   }
/* 138:    */   
/* 139:    */   private void readXModel(XModel xm, Hashtable ht)
/* 140:    */   {
/* 141:132 */     for (int i = 0; i < xm.getNumChildren(); i++)
/* 142:    */     {
/* 143:133 */       String key = xm.get(i).getId();
/* 144:134 */       String value = (String)xm.get(i).get();
/* 145:135 */       System.out.println("Key:" + key + " Value:" + value);
/* 146:136 */       ht.put(key, value == null ? "" : value.replace("\\", "\\\\"));
/* 147:    */     }
/* 148:    */   }
/* 149:    */   
/* 150:    */   public void uploadToCME(Hashtable rs, String domain)
/* 151:    */     throws Exception
/* 152:    */   {
/* 153:143 */     cmeTransformation(rs, domain, "cme/mapping");
/* 154:144 */     String values = saveCmeValues1(rs);
/* 155:145 */     XModel xm = new XBaseModel();
/* 156:146 */     String uniqno = this.p.pid.split("/")[1];
/* 157:147 */     Hashtable ht = new Hashtable();
/* 158:    */     
/* 159:149 */     xm.set("uniqno", uniqno);
/* 160:150 */     xm.set("value1", values);
/* 161:    */     
/* 162:152 */     TestXUIDB.getInstance().saveDataM1("cme_records", " uniqno='" + uniqno + "' ", xm);
/* 163:    */     
/* 164:154 */     TestXUIDB.getInstance().sendLogs("qa", "admin", "cme");
/* 165:    */     
/* 166:156 */     updateRecordStatus(uniqno, "Complete", domain, null);
/* 167:    */   }
/* 168:    */   
/* 169:    */   public void updateRecordStatus(String uniqno, String status, String domain, String comments)
/* 170:    */     throws SQLException
/* 171:    */   {
/* 172:160 */     String sql = "select uniqno,status,domain,modified,comments from record_qa where uniqno='" + 
/* 173:161 */       uniqno + "'";
/* 174:162 */     System.out.println("uniqno::" + uniqno + " status::" + status + 
/* 175:163 */       " domain::" + domain + " comments::" + comments);
/* 176:164 */     Statement stmt = null;
/* 177:165 */     ResultSet rs = null;
/* 178:    */     try
/* 179:    */     {
/* 180:168 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
/* 181:169 */       Connection con = DbConnection.getConnection();
/* 182:170 */       stmt = con
/* 183:171 */         .createStatement(1005, 
/* 184:172 */         1008);
/* 185:173 */       rs = stmt.executeQuery(sql);
/* 186:174 */       if (rs.next())
/* 187:    */       {
/* 188:175 */         rs.updateString("status", status);
/* 189:176 */         rs.updateString("modified", sdf.format(new Date()));
/* 190:177 */         if (comments != null) {
/* 191:178 */           rs.updateString("comments", comments);
/* 192:    */         }
/* 193:180 */         rs.updateRow();
/* 194:    */       }
/* 195:    */       else
/* 196:    */       {
/* 197:183 */         rs.moveToInsertRow();
/* 198:184 */         rs.updateString("status", status);
/* 199:185 */         rs.updateString("uniqno", uniqno);
/* 200:186 */         rs.updateString("modified", sdf.format(new Date()));
/* 201:187 */         rs.updateString("domain", domain);
/* 202:188 */         if (comments != null) {
/* 203:189 */           rs.updateString("comments", comments);
/* 204:    */         }
/* 205:191 */         rs.insertRow();
/* 206:    */       }
/* 207:194 */       stmt.close();
/* 208:195 */       con.close();
/* 209:196 */       System.out.println("successful");
/* 210:    */     }
/* 211:    */     catch (Exception e)
/* 212:    */     {
/* 213:200 */       e.printStackTrace();
/* 214:    */     }
/* 215:    */     finally
/* 216:    */     {
/* 217:202 */       rs.close();
/* 218:203 */       stmt.close();
/* 219:    */     }
/* 220:    */   }
/* 221:    */   
/* 222:    */   public void cmeTransformation(Hashtable vaDataTable, String domain, String type)
/* 223:    */     throws Exception
/* 224:    */   {
/* 225:208 */     Vector keys = new Vector(vaDataTable.keySet());
/* 226:    */     
/* 227:210 */     String sql = "SELECT * FROM transformations WHERE table1='" + type + "'";
/* 228:211 */     Connection con = DbConnection.getConnection();
/* 229:212 */     Statement stmt = con.createStatement();
/* 230:213 */     ResultSet rs = stmt.executeQuery(sql);
/* 231:214 */     while (rs.next())
/* 232:    */     {
/* 233:215 */       String transformationClass = rs.getString("transformation_class");
/* 234:216 */       Transformation transformation = (Transformation)Class.forName(transformationClass).newInstance();
/* 235:217 */       System.out.println("dataTableSize:" + vaDataTable.size());
/* 236:218 */       transformation.transform(vaDataTable, "uniqno", vaDataTable.get("uniqno").toString(), null, domain);
/* 237:    */     }
/* 238:220 */     stmt.close();
/* 239:221 */     con.close();
/* 240:    */   }
/* 241:    */   
/* 242:    */   public void batchExecute() {}
/* 243:    */   
/* 244:    */   public static void main(String[] args)
/* 245:    */     throws Exception
/* 246:    */   {
/* 247:231 */     Process.createProcess("01300088_01_02");
/* 248:232 */     Process.processTransitions();
/* 249:    */   }
/* 250:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.agents.va.PushToCMEAgent
 * JD-Core Version:    0.7.0.1
 */