/*   1:    */ package com.kentropy.cme.qa.neochecks;
/*   2:    */ 
/*   3:    */ import com.kentropy.util.DbConnection;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.sql.Connection;
/*   6:    */ import java.sql.ResultSet;
/*   7:    */ import java.sql.SQLException;
/*   8:    */ import java.sql.Statement;
/*   9:    */ import java.util.Hashtable;
/*  10:    */ 
/*  11:    */ public class IDChecks
/*  12:    */   implements QualityCheck
/*  13:    */ {
/*  14: 21 */   ResultSet rs = null;
/*  15: 22 */   String name = null;
/*  16: 23 */   String value = null;
/*  17: 24 */   String domain = null;
/*  18:    */   
/*  19:    */   public boolean validate(ResultSet rs, ResultSet checkDef, String name, String value, StringBuffer errorMsg, String domain)
/*  20:    */   {
/*  21: 30 */     this.rs = rs;
/*  22: 31 */     this.name = name;
/*  23: 32 */     this.value = value;
/*  24: 33 */     this.domain = domain;
/*  25:    */     try
/*  26:    */     {
/*  27: 36 */       if ((domain.equals("neonatal")) || (domain.equals("child")) || (domain.equals("adult")))
/*  28:    */       {
/*  29: 37 */         if ((!domain.equals("adult")) && (!individualVsMother(value))) {
/*  30: 38 */           return false;
/*  31:    */         }
/*  32: 40 */         if ((!domain.equals("adult")) && (!individualVsHead(value))) {
/*  33: 41 */           return false;
/*  34:    */         }
/*  35: 42 */         if (!individualVsRespondent(value)) {
/*  36: 43 */           return false;
/*  37:    */         }
/*  38: 45 */         return true;
/*  39:    */       }
/*  40:    */     }
/*  41:    */     catch (Exception e)
/*  42:    */     {
/*  43: 48 */       e.printStackTrace();
/*  44:    */     }
/*  45: 50 */     return false;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public boolean individualVsMother(String value)
/*  49:    */     throws Exception
/*  50:    */   {
/*  51: 62 */     if ((value != null) && (this.values.get("id_of_deceased_mother") != null) && 
/*  52: 63 */       (value.equals(this.values.get("id_of_deceased_mother"))) && (this.flags.get("id_of_deceased_mother").toString().startsWith("V"))) {
/*  53: 64 */       return false;
/*  54:    */     }
/*  55: 66 */     return true;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public boolean individualVsHead(String value)
/*  59:    */     throws Exception
/*  60:    */   {
/*  61: 77 */     if ((value != null) && (this.values.get("id_of_head") != null) && 
/*  62: 78 */       (value.equals(this.values.get("id_of_head"))) && (this.flags.get("id_of_head").toString().startsWith("V"))) {
/*  63: 79 */       return false;
/*  64:    */     }
/*  65: 81 */     return true;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public boolean individualVsRespondent(String value)
/*  69:    */     throws Exception
/*  70:    */   {
/*  71: 92 */     if ((value != null) && (value.equals(this.values.get("id_of_respondent")))) {
/*  72: 93 */       return false;
/*  73:    */     }
/*  74: 95 */     return true;
/*  75:    */   }
/*  76:    */   
/*  77:    */   private String getQAFlag(String uniqno, String domain, String field)
/*  78:    */   {
/*  79:111 */     if (this.flags == null) {
/*  80:126 */       this.flags.get(field);
/*  81:    */     } else {
/*  82:129 */       return (String)this.flags.get(field);
/*  83:    */     }
/*  84:131 */     return null;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public static void main(String[] args)
/*  88:    */     throws SQLException
/*  89:    */   {
/*  90:141 */     IDChecks id = new IDChecks();
/*  91:142 */     Connection con = DbConnection.getConnection();
/*  92:143 */     String sql = "select * from adult where uniqno='03300118_01_04'";
/*  93:144 */     Statement stmt = con.createStatement();
/*  94:145 */     ResultSet rs = stmt.executeQuery(sql);
/*  95:146 */     rs.next();
/*  96:147 */     StringBuffer errorMsg = new StringBuffer();
/*  97:148 */     System.out.println("Flag of idcheck::" + id.validate(rs, null, null, "086010346", errorMsg, "adult"));
/*  98:149 */     System.out.println(errorMsg);
/*  99:    */   }
/* 100:    */   
/* 101:151 */   Hashtable values = null;
/* 102:152 */   Hashtable flags = null;
/* 103:    */   
/* 104:    */   public String getValue1(String fld)
/* 105:    */   {
/* 106:    */     try
/* 107:    */     {
/* 108:164 */       if (this.rs != null) {
/* 109:165 */         return this.rs.getString(fld);
/* 110:    */       }
/* 111:167 */       if (this.values != null) {
/* 112:168 */         return (String)this.values.get(fld);
/* 113:    */       }
/* 114:    */     }
/* 115:    */     catch (SQLException e)
/* 116:    */     {
/* 117:171 */       e.printStackTrace();
/* 118:    */     }
/* 119:173 */     return null;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public boolean validate(Hashtable rs, Hashtable flags, ResultSet checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 123:    */   {
/* 124:178 */     this.values = rs;
/* 125:179 */     this.flags = flags;
/* 126:180 */     this.name = name;
/* 127:181 */     this.value = value;
/* 128:182 */     this.domain = domain;
/* 129:    */     try
/* 130:    */     {
/* 131:185 */       if ((domain.equals("neonatal")) || (domain.equals("child")) || (domain.equals("adult")))
/* 132:    */       {
/* 133:186 */         if ((!domain.equals("adult")) && (!individualVsMother(value))) {
/* 134:187 */           return false;
/* 135:    */         }
/* 136:189 */         if ((!domain.equals("adult")) && (!individualVsHead(value))) {
/* 137:190 */           return false;
/* 138:    */         }
/* 139:191 */         if (!individualVsRespondent(value)) {
/* 140:192 */           return false;
/* 141:    */         }
/* 142:194 */         return true;
/* 143:    */       }
/* 144:    */     }
/* 145:    */     catch (Exception e)
/* 146:    */     {
/* 147:197 */       e.printStackTrace();
/* 148:    */     }
/* 149:199 */     return false;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public boolean validate(Hashtable rs, Hashtable flags, Hashtable checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 153:    */   {
/* 154:204 */     this.values = rs;
/* 155:205 */     this.flags = flags;
/* 156:206 */     this.name = name;
/* 157:207 */     this.value = value;
/* 158:208 */     this.domain = domain;
/* 159:    */     try
/* 160:    */     {
/* 161:211 */       if ((domain.equals("neonatal")) || (domain.equals("child"))) {
/* 162:212 */         return (individualVsMother(value)) || (individualVsHead(value));
/* 163:    */       }
/* 164:214 */       return individualVsRespondent(value);
/* 165:    */     }
/* 166:    */     catch (Exception e)
/* 167:    */     {
/* 168:216 */       e.printStackTrace();
/* 169:    */     }
/* 170:218 */     return false;
/* 171:    */   }
/* 172:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-qa\ken-qa.jar
 * Qualified Name:     com.kentropy.cme.qa.neochecks.IDChecks
 * JD-Core Version:    0.7.0.1
 */