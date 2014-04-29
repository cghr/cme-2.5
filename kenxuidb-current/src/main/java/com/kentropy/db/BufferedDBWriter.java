/*   1:    */ package com.kentropy.db;
/*   2:    */ 
/*   3:    */ import com.kentropy.util.DbConnection;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.sql.Connection;
/*   7:    */ import java.sql.ResultSet;
/*   8:    */ import java.sql.ResultSetMetaData;
/*   9:    */ import java.sql.SQLException;
/*  10:    */ import java.sql.Statement;
/*  11:    */ import java.util.Date;
/*  12:    */ import java.util.Enumeration;
/*  13:    */ import java.util.Hashtable;
/*  14:    */ import java.util.Vector;
/*  15:    */ 
/*  16:    */ public class BufferedDBWriter
/*  17:    */ {
/*  18: 18 */   public static Hashtable currentJob = new Hashtable();
/*  19: 19 */   private static int jobCount = 0;
/*  20:    */   private JobMonitor monitor;
/*  21: 21 */   public static BufferedDBWriter bdb = null;
/*  22:    */   
/*  23:    */   public BufferedDBWriter()
/*  24:    */   {
/*  25: 25 */     System.out.println("Starting .. ");
/*  26: 26 */     this.monitor = new JobMonitor(this);
/*  27:    */     
/*  28: 28 */     this.monitor.start();
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void start()
/*  32:    */   {
/*  33: 32 */     this.monitor.stop = false;
/*  34: 33 */     this.monitor.start();
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void stop()
/*  38:    */   {
/*  39: 37 */     this.monitor.stop = true;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public static BufferedDBWriter getInstance()
/*  43:    */   {
/*  44: 42 */     if (bdb == null) {
/*  45: 43 */       bdb = new BufferedDBWriter();
/*  46:    */     }
/*  47: 46 */     return bdb;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void saveHashTableToDb(Hashtable ht, String table)
/*  51:    */   {
/*  52: 51 */     addToJob(ht, table);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public synchronized void addToJob(Hashtable ht, String table)
/*  56:    */   {
/*  57: 55 */     System.out.println(" Adding " + table);
/*  58: 56 */     Vector v = get(table);
/*  59: 57 */     v.add(ht);
/*  60: 58 */     currentJob.put(table, v);
/*  61: 59 */     jobCount += 1;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public Vector get(String table)
/*  65:    */   {
/*  66: 64 */     Vector v = (Vector)currentJob.get(table);
/*  67: 65 */     if (v == null)
/*  68:    */     {
/*  69: 66 */       v = new Vector();
/*  70: 67 */       currentJob.put(table, v);
/*  71:    */     }
/*  72: 69 */     return v;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public synchronized void execute()
/*  76:    */   {
/*  77: 73 */     DbJob db = new DbJob();
/*  78: 74 */     db.job = currentJob;
/*  79: 75 */     currentJob = new Hashtable();
/*  80: 76 */     jobCount = 0;
/*  81: 77 */     Thread th = new Thread(db);
/*  82: 78 */     th.start();
/*  83:    */   }
/*  84:    */   
/*  85:    */   public static String getTableFields(String table)
/*  86:    */   {
/*  87: 82 */     StringBuffer tableFields = new StringBuffer();
/*  88:    */     try
/*  89:    */     {
/*  90: 84 */       Connection con = DbConnection.getConnection();
/*  91: 85 */       Statement stmt = con.createStatement();
/*  92: 86 */       ResultSet rs = stmt
/*  93: 87 */         .executeQuery("desc " + table);
/*  94: 88 */       while (rs.next()) {
/*  95: 89 */         tableFields.append(rs.getString(1) + ",");
/*  96:    */       }
/*  97: 90 */       tableFields.deleteCharAt(tableFields.length() - 1);
/*  98: 91 */       stmt.close();
/*  99: 92 */       con.close();
/* 100:    */     }
/* 101:    */     catch (Exception e)
/* 102:    */     {
/* 103: 96 */       e.printStackTrace();
/* 104:    */     }
/* 105: 99 */     return tableFields.toString();
/* 106:    */   }
/* 107:    */   
/* 108:    */   public static void readResultSet(ResultSet rs, Hashtable ht)
/* 109:    */   {
/* 110:    */     try
/* 111:    */     {
/* 112:104 */       for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
/* 113:    */       {
/* 114:105 */         String value = rs.getString(i);
/* 115:106 */         String key = rs.getMetaData().getColumnName(i);
/* 116:108 */         if (value != null) {
/* 117:109 */           ht.put(key, value);
/* 118:    */         } else {
/* 119:111 */           ht.put(key, "");
/* 120:    */         }
/* 121:    */       }
/* 122:    */     }
/* 123:    */     catch (SQLException e)
/* 124:    */     {
/* 125:116 */       e.printStackTrace();
/* 126:    */     }
/* 127:    */   }
/* 128:    */   
/* 129:    */   public static void main(String[] args)
/* 130:    */     throws SQLException, IOException
/* 131:    */   {
/* 132:121 */     BufferedDBWriter bdb = new BufferedDBWriter();
/* 133:    */     
/* 134:123 */     ResultSet rs = DbConnection.getConnection().createStatement()
/* 135:124 */       .executeQuery("select * from adult");
/* 136:125 */     System.out.println("Completed adding " + new Date());
/* 137:126 */     while (rs.next())
/* 138:    */     {
/* 139:127 */       Hashtable ht = new Hashtable();
/* 140:128 */       readResultSet(rs, ht);
/* 141:129 */       System.out.println("Adding to Job:" + rs.getString("uniqno"));
/* 142:130 */       bdb.addToJob(ht, "adult_copy");
/* 143:131 */       bdb.addToJob(ht, "adult_qa");
/* 144:    */     }
/* 145:133 */     System.out.println("Completed adding " + new Date());
/* 146:134 */     bdb.stop();
/* 147:135 */     bdb.start();
/* 148:    */   }
/* 149:    */   
/* 150:    */   public static class DbJob
/* 151:    */     implements Runnable
/* 152:    */   {
/* 153:    */     public Hashtable job;
/* 154:142 */     private boolean running = false;
/* 155:    */     
/* 156:    */     public void run()
/* 157:    */     {
/* 158:145 */       Vector tables = new Vector(this.job.keySet());
/* 159:146 */       Statement stmt = null;
/* 160:147 */       int count1 = 0;
/* 161:    */       try
/* 162:    */       {
/* 163:150 */         Connection con = DbConnection.getConnection();
/* 164:151 */         stmt = con.createStatement();
/* 165:153 */         for (int i = 0; i < tables.size(); i++)
/* 166:    */         {
/* 167:154 */           Vector qrys = (Vector)this.job.get(tables.get(i));
/* 168:155 */           if (qrys.size() > 0)
/* 169:    */           {
/* 170:157 */             String query = createQuery(qrys, 
/* 171:158 */               (String)tables.get(i));
/* 172:    */             
/* 173:160 */             stmt.addBatch(query);
/* 174:    */           }
/* 175:163 */           count1 += qrys.size();
/* 176:    */         }
/* 177:166 */         int[] n = stmt.executeBatch();
/* 178:167 */         this.job = new Hashtable();
/* 179:168 */         stmt.clearBatch();
/* 180:169 */         stmt.close();
/* 181:170 */         con.close();
/* 182:    */       }
/* 183:    */       catch (Exception e)
/* 184:    */       {
/* 185:174 */         e.printStackTrace();
/* 186:    */         
/* 187:176 */         System.out.println(count1);
/* 188:    */       }
/* 189:    */     }
/* 190:    */     
/* 191:    */     public String createQuery(Vector v, String table)
/* 192:    */       throws Exception
/* 193:    */     {
/* 194:183 */       String fieldNames = BufferedDBWriter.getTableFields(table);
/* 195:    */       
/* 196:185 */       StringBuffer qry = new StringBuffer();
/* 197:186 */       qry.append("insert into " + table + " (" + fieldNames + 
/* 198:187 */         ") values ");
/* 199:189 */       if (v == null) {
/* 200:190 */         return null;
/* 201:    */       }
/* 202:192 */       for (int i = 0; i < v.size(); i++)
/* 203:    */       {
/* 204:193 */         Hashtable ht = (Hashtable)v.get(i);
/* 205:194 */         String token = i == 0 ? "" : ",";
/* 206:195 */         qry.append(token);
/* 207:    */         
/* 208:197 */         qry.append(getInsertString(ht, fieldNames));
/* 209:    */       }
/* 210:199 */       return qry.toString();
/* 211:    */     }
/* 212:    */     
/* 213:    */     private String getInsertString(Hashtable ht, String fieldNames)
/* 214:    */     {
/* 215:203 */       String[] fieldNamesArray = fieldNames.split(",");
/* 216:204 */       StringBuffer strBuf = new StringBuffer();
/* 217:205 */       for (int i = 0; i < fieldNamesArray.length; i++)
/* 218:    */       {
/* 219:206 */         String key = fieldNamesArray[i];
/* 220:207 */         String value = (String)ht.get(key);
/* 221:208 */         strBuf.append(i == 0 ? "(" : ",");
/* 222:209 */         strBuf.append("'" + (value == null ? "" : value.replace("\\", "\\\\").trim()) + "'");
/* 223:    */       }
/* 224:211 */       strBuf.append(")");
/* 225:212 */       return strBuf.toString();
/* 226:    */     }
/* 227:    */     
/* 228:    */     private void getFieldNames(Hashtable values, Vector fieldNames)
/* 229:    */     {
/* 230:216 */       Enumeration en = values.keys();
/* 231:217 */       while (en.hasMoreElements())
/* 232:    */       {
/* 233:218 */         String field = (String)en.nextElement();
/* 234:219 */         fieldNames.add(field);
/* 235:    */       }
/* 236:    */     }
/* 237:    */   }
/* 238:    */   
/* 239:    */   public class JobMonitor
/* 240:    */     extends Thread
/* 241:    */   {
/* 242:    */     public BufferedDBWriter bdb;
/* 243:227 */     public int count = 0;
/* 244:228 */     public boolean stop = false;
/* 245:    */     
/* 246:    */     public JobMonitor(BufferedDBWriter b)
/* 247:    */     {
/* 248:231 */       this.bdb = b;
/* 249:    */     }
/* 250:    */     
/* 251:    */     public void run()
/* 252:    */     {
/* 253:236 */       while (!this.stop)
/* 254:    */       {
/* 255:238 */         this.count += 1;
/* 256:    */         try
/* 257:    */         {
/* 258:241 */           Thread.currentThread();Thread.sleep(100L);
/* 259:    */         }
/* 260:    */         catch (InterruptedException e)
/* 261:    */         {
/* 262:244 */           e.printStackTrace();
/* 263:    */         }
/* 264:246 */         if ((this.count > 5) || (BufferedDBWriter.jobCount > 10))
/* 265:    */         {
/* 266:248 */           this.bdb.execute();
/* 267:249 */           this.count = 0;
/* 268:    */         }
/* 269:    */       }
/* 270:    */     }
/* 271:    */   }
/* 272:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.db.BufferedDBWriter
 * JD-Core Version:    0.7.0.1
 */