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
/*  17: 17 */   static Hashtable ht = new Hashtable();
/*  18:    */   
/*  19:    */   public static StringWriter getSW()
/*  20:    */   {
/*  21: 21 */     throw new Error("Unresolved compilation problem: \n\tType mismatch: cannot convert from long to String\n");
/*  22:    */   }
/*  23:    */   
/*  24:    */   public static synchronized void startLog(String dataType, String key, String user)
/*  25:    */     throws Exception
/*  26:    */   {
/*  27: 36 */     StringWriter sw = getSW();
/*  28: 37 */     sw.write("<l id='" + new Date() + "'>\n");
/*  29: 38 */     sw.write("<dt id='dt' table='" + dataType + "' key=\"" + key + "\"  user='" + user + "'>\n");
/*  30: 39 */     sw.write("<d id='data' ");
/*  31:    */   }
/*  32:    */   
/*  33:    */   public static synchronized void startLog(String dataType, String op, String key, String user)
/*  34:    */     throws Exception
/*  35:    */   {
/*  36: 45 */     StringWriter sw = getSW();
/*  37: 46 */     sw.write("<l id='" + new Date() + "'>\n");
/*  38: 47 */     sw.write("<dt id='dt' table='" + dataType + "' op='" + op + "' key=\"" + key + "\"  user='" + user + "'>\n");
/*  39: 48 */     sw.write("<d id='data' ");
/*  40:    */   }
/*  41:    */   
/*  42:    */   public static void logField(String fld, String value)
/*  43:    */     throws Exception
/*  44:    */   {
/*  45: 54 */     StringWriter sw = getSW();
/*  46: 55 */     if (fld.equals("id")) {
/*  47: 56 */       fld = "_id";
/*  48:    */     }
/*  49: 57 */     if (value != null)
/*  50:    */     {
/*  51: 59 */       String val1 = "'" + value + "'";
/*  52: 60 */       String val = fld + "=" + val1;
/*  53: 61 */       sw.write(val + " ");
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   public static synchronized void endLog()
/*  58:    */     throws Exception
/*  59:    */   {
/*  60: 68 */     StringWriter sw = getSW();
/*  61: 69 */     if (sw == null) {
/*  62: 70 */       return;
/*  63:    */     }
/*  64: 73 */     sw.write("/>");
/*  65: 74 */     sw.write("</dt>\n");
/*  66: 75 */     sw.write("</l>\n");
/*  67:    */     try
/*  68:    */     {
/*  69: 78 */       saveChangeLog(sw.toString());
/*  70:    */       
/*  71: 80 */       sw = null;
/*  72: 81 */       ht.remove(Long.valueOf(Thread.currentThread().getId()));
/*  73:    */     }
/*  74:    */     catch (Exception e)
/*  75:    */     {
/*  76: 85 */       e.printStackTrace();
/*  77:    */     }
/*  78:    */   }
/*  79:    */   
/*  80:    */   public static synchronized void endLog1()
/*  81:    */     throws Exception
/*  82:    */   {
/*  83: 92 */     StringWriter sw = getSW();
/*  84: 93 */     if (sw == null) {
/*  85: 94 */       return;
/*  86:    */     }
/*  87: 97 */     sw.write("/>");
/*  88: 98 */     sw.write("</dt>\n");
/*  89: 99 */     sw.write("</l>\n");
/*  90:    */     try
/*  91:    */     {
/*  92:102 */       saveChangeLog(sw.toString());
/*  93:    */       
/*  94:104 */       sw = null;
/*  95:105 */       ht.remove(Long.valueOf(Thread.currentThread().getId()));
/*  96:    */     }
/*  97:    */     catch (Exception e)
/*  98:    */     {
/*  99:109 */       e.printStackTrace();
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   public static synchronized void saveChangeLog(String log)
/* 104:    */     throws Exception
/* 105:    */   {
/* 106:115 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 107:    */     
/* 108:117 */     dt.setupTable("changelogs", "*", "", "test", true);
/* 109:    */     
/* 110:119 */     String s = "insert into changelogs";
/* 111:120 */     String flds = "";
/* 112:121 */     String values = "";
/* 113:122 */     s = s + " (value,time) VALUES(?,Now())";
/* 114:    */     
/* 115:124 */     System.out.println(" Debug sql " + s);
/* 116:    */     
/* 117:126 */     PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 118:127 */     ps.setBytes(1, log.getBytes("utf-8"));
/* 119:    */     
/* 120:129 */     ps.execute();
/* 121:    */     
/* 122:131 */     closePs(dt, ps);
/* 123:    */   }
/* 124:    */   
/* 125:    */   public static void closePs(DatabaseTableModel dt, PreparedStatement ps)
/* 126:    */   {
/* 127:    */     try
/* 128:    */     {
/* 129:138 */       if (ps.getResultSet() != null) {
/* 130:139 */         ps.getResultSet().close();
/* 131:    */       }
/* 132:141 */       dt.getTable().releasePreparedStatement(ps);
/* 133:    */     }
/* 134:    */     catch (SQLException e)
/* 135:    */     {
/* 136:144 */       e.printStackTrace();
/* 137:    */     }
/* 138:    */   }
/* 139:    */   
/* 140:    */   public static synchronized void saveChangeLog1(String log)
/* 141:    */     throws Exception
/* 142:    */   {
/* 143:150 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 144:    */     
/* 145:152 */     dt.setupTable("inbound_changelogs", "*", "", "test", true);
/* 146:    */     
/* 147:154 */     String s = "insert into incoming_changelogs";
/* 148:155 */     String flds = "";
/* 149:156 */     String values = "";
/* 150:157 */     s = s + " (value,time) VALUES(?,Now())";
/* 151:    */     
/* 152:159 */     System.out.println(" Debug sql " + s);
/* 153:    */     
/* 154:161 */     PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 155:162 */     ps.setBytes(1, log.getBytes("utf-8"));
/* 156:    */     
/* 157:164 */     ps.execute();
/* 158:    */     
/* 159:166 */     ps.close();
/* 160:    */   }
/* 161:    */   
/* 162:    */   public static DateFormat getMysqlDateFormat()
/* 163:    */   {
/* 164:171 */     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 165:172 */     return df;
/* 166:    */   }
/* 167:    */   
/* 168:    */   public static void logChange(String datatype, String data, String key)
/* 169:    */     throws Exception
/* 170:    */   {}
/* 171:    */   
/* 172:    */   public static void main(String[] args)
/* 173:    */     throws Exception
/* 174:    */   {
/* 175:183 */     System.out.println(getSW().toString());
/* 176:    */   }
/* 177:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.sync.ChangeLog
 * JD-Core Version:    0.7.0.1
 */