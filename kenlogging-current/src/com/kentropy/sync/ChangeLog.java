/*   1:    */ package com.kentropy.sync;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.io.StringWriter;
/*   5:    */ import java.sql.PreparedStatement;
/*   6:    */ import java.sql.ResultSet;
/*   7:    */ import java.sql.SQLException;
/*   8:    */ import java.text.DateFormat;
/*   9:    */ import java.text.SimpleDateFormat;
/*  10:    */ import java.util.Date;
/*  11:    */ import java.util.Hashtable;
/*  12:    */ import net.xoetrope.optional.data.sql.CachedDatabaseTable;
/*  13:    */ import net.xoetrope.optional.data.sql.DatabaseTableModel;
/*  14:    */ 
/*  15:    */ public class ChangeLog
/*  16:    */ {
/*  17: 28 */   static Hashtable ht = new Hashtable();
/*  18:    */   
/*  19:    */   public static StringWriter getSW()
/*  20:    */   {
/*  21: 32 */     String id = Thread.currentThread().getId();
/*  22: 33 */     System.out.println(id);
/*  23: 34 */     StringWriter sb = (StringWriter)ht.get(id);
/*  24: 35 */     if (sb == null)
/*  25:    */     {
/*  26: 37 */       System.out.println(id + " SW not found");
/*  27: 38 */       return newSW();
/*  28:    */     }
/*  29: 42 */     return sb;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public static StringWriter newSW()
/*  33:    */   {
/*  34: 46 */     String id = Thread.currentThread().getId();
/*  35: 47 */     StringWriter sw = new StringWriter();
/*  36: 48 */     ht.put(id, sw);
/*  37: 49 */     return sw;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static synchronized void startLog(String dataType, String key, String user)
/*  41:    */     throws Exception
/*  42:    */   {
/*  43: 56 */     StringWriter sw = newSW();
/*  44: 57 */     sw.write("<l id='" + new Date() + "'>\n");
/*  45: 58 */     sw.write("<dt id='dt' table='" + dataType + "' key=\"" + key + "\"  user='" + user + "'>\n");
/*  46: 59 */     sw.write("<d id='data' ");
/*  47:    */   }
/*  48:    */   
/*  49:    */   public static synchronized void startLog(String dataType, String op, String key, String user)
/*  50:    */     throws Exception
/*  51:    */   {
/*  52: 65 */     StringWriter sw = newSW();
/*  53: 66 */     sw.write("<l id='" + new Date() + "'>\n");
/*  54: 67 */     sw.write("<dt id='dt' table='" + dataType + "' op='" + op + "' key=\"" + key + "\"  user='" + user + "'>\n");
/*  55: 68 */     sw.write("<d id='data' ");
/*  56:    */   }
/*  57:    */   
/*  58:    */   public static void logField(String fld, String value)
/*  59:    */     throws Exception
/*  60:    */   {
/*  61: 75 */     StringWriter sw = getSW();
/*  62: 76 */     if (fld.equals("id")) {
/*  63: 77 */       fld = "_id";
/*  64:    */     }
/*  65: 78 */     if (value != null)
/*  66:    */     {
/*  67: 80 */       String val1 = "'" + value + "'";
/*  68: 81 */       String val = fld + "=" + val1;
/*  69: 82 */       sw.write(val + " ");
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73:    */   public static synchronized void endLog()
/*  74:    */     throws Exception
/*  75:    */   {
/*  76: 90 */     StringWriter sw = getSW();
/*  77: 91 */     if (sw == null) {
/*  78: 92 */       return;
/*  79:    */     }
/*  80: 95 */     sw.write("/>");
/*  81: 96 */     sw.write("</dt>\n");
/*  82: 97 */     sw.write("</l>\n");
/*  83:    */     try
/*  84:    */     {
/*  85:102 */       saveChangeLog(sw.toString());
/*  86:    */       
/*  87:104 */       sw = null;
/*  88:105 */       ht.remove(Long.valueOf(Thread.currentThread().getId()));
/*  89:    */     }
/*  90:    */     catch (Exception e)
/*  91:    */     {
/*  92:109 */       e.printStackTrace();
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   public static synchronized void endLog1()
/*  97:    */     throws Exception
/*  98:    */   {
/*  99:116 */     StringWriter sw = getSW();
/* 100:117 */     if (sw == null) {
/* 101:118 */       return;
/* 102:    */     }
/* 103:121 */     sw.write("/>");
/* 104:122 */     sw.write("</dt>\n");
/* 105:123 */     sw.write("</l>\n");
/* 106:    */     try
/* 107:    */     {
/* 108:128 */       saveChangeLog(sw.toString());
/* 109:    */       
/* 110:130 */       sw = null;
/* 111:131 */       ht.remove(Long.valueOf(Thread.currentThread().getId()));
/* 112:    */     }
/* 113:    */     catch (Exception e)
/* 114:    */     {
/* 115:135 */       e.printStackTrace();
/* 116:    */     }
/* 117:    */   }
/* 118:    */   
/* 119:    */   public static synchronized void saveChangeLog(String log)
/* 120:    */     throws Exception
/* 121:    */   {
/* 122:141 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 123:    */     
/* 124:143 */     dt.setupTable("changelogs", "*", "", "test", true);
/* 125:    */     
/* 126:    */ 
/* 127:    */ 
/* 128:    */ 
/* 129:148 */     String s = "insert into changelogs";
/* 130:149 */     String flds = "";
/* 131:150 */     String values = "";
/* 132:151 */     s = s + " (value,time) VALUES(?,Now())";
/* 133:    */     
/* 134:    */ 
/* 135:154 */     System.out.println(" Debug sql " + s);
/* 136:    */     
/* 137:    */ 
/* 138:157 */     PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 139:158 */     ps.setBytes(1, log.getBytes("utf-8"));
/* 140:    */     
/* 141:    */ 
/* 142:    */ 
/* 143:162 */     ps.execute();
/* 144:    */     
/* 145:    */ 
/* 146:165 */     closePs(dt, ps);
/* 147:    */   }
/* 148:    */   
/* 149:    */   public static void closePs(DatabaseTableModel dt, PreparedStatement ps)
/* 150:    */   {
/* 151:    */     try
/* 152:    */     {
/* 153:172 */       if (ps.getResultSet() != null) {
/* 154:173 */         ps.getResultSet().close();
/* 155:    */       }
/* 156:175 */       dt.getTable().releasePreparedStatement(ps);
/* 157:    */     }
/* 158:    */     catch (SQLException e)
/* 159:    */     {
/* 160:178 */       e.printStackTrace();
/* 161:    */     }
/* 162:    */   }
/* 163:    */   
/* 164:    */   public static synchronized void saveChangeLog1(String log)
/* 165:    */     throws Exception
/* 166:    */   {
/* 167:184 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 168:    */     
/* 169:186 */     dt.setupTable("inbound_changelogs", "*", "", "test", true);
/* 170:    */     
/* 171:    */ 
/* 172:    */ 
/* 173:    */ 
/* 174:191 */     String s = "insert into incoming_changelogs";
/* 175:192 */     String flds = "";
/* 176:193 */     String values = "";
/* 177:194 */     s = s + " (value,time) VALUES(?,Now())";
/* 178:    */     
/* 179:    */ 
/* 180:197 */     System.out.println(" Debug sql " + s);
/* 181:    */     
/* 182:    */ 
/* 183:200 */     PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 184:201 */     ps.setBytes(1, log.getBytes("utf-8"));
/* 185:    */     
/* 186:    */ 
/* 187:    */ 
/* 188:205 */     ps.execute();
/* 189:    */     
/* 190:207 */     ps.close();
/* 191:    */   }
/* 192:    */   
/* 193:    */   public static DateFormat getMysqlDateFormat()
/* 194:    */   {
/* 195:213 */     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 196:214 */     return df;
/* 197:    */   }
/* 198:    */   
/* 199:    */   public static void logChange(String datatype, String data, String key)
/* 200:    */     throws Exception
/* 201:    */   {}
/* 202:    */   
/* 203:    */   public static void main(String[] args)
/* 204:    */     throws Exception
/* 205:    */   {
/* 206:233 */     System.out.println(getSW().toString());
/* 207:    */   }
/* 208:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenlogging-current\kenlogging-current.jar
 * Qualified Name:     com.kentropy.sync.ChangeLog
 * JD-Core Version:    0.7.0.1
 */