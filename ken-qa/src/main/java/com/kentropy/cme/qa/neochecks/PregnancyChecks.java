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
/*  11:    */ public class PregnancyChecks
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
/*  27: 36 */       if (getValue1("deceased_sex").equals("1")) {
/*  28: 37 */         return true;
/*  29:    */       }
/*  30:    */     }
/*  31:    */     catch (Exception e)
/*  32:    */     {
/*  33: 40 */       e.printStackTrace();
/*  34:    */     }
/*  35: 42 */     return false;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public boolean individualVsMother(String value)
/*  39:    */     throws Exception
/*  40:    */   {
/*  41: 54 */     if ((value != null) && 
/*  42: 55 */       (value.equals(getValue1("id_of_deceased_mother"))) && (getQAFlag(getValue1("uniqno"), this.domain, "id_of_deceased_mother").startsWith("V"))) {
/*  43: 56 */       return false;
/*  44:    */     }
/*  45: 58 */     return true;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public boolean individualVsHead(String value)
/*  49:    */     throws Exception
/*  50:    */   {
/*  51: 69 */     if ((value != null) && (value.equals(getValue1("id_of_head"))) && (getQAFlag(getValue1("uniqno"), this.domain, "id_of_head").startsWith("V"))) {
/*  52: 70 */       return false;
/*  53:    */     }
/*  54: 72 */     return true;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public boolean individualVsRespondent(String value)
/*  58:    */     throws Exception
/*  59:    */   {
/*  60: 83 */     if ((value != null) && (value.equals(getValue1("id_of_respondent")))) {
/*  61: 84 */       return false;
/*  62:    */     }
/*  63: 86 */     return true;
/*  64:    */   }
/*  65:    */   
/*  66:    */   private String getQAFlag(String uniqno, String domain, String field)
/*  67:    */   {
/*  68:102 */     if (this.flags == null)
/*  69:    */     {
/*  70:104 */       String sql = "select " + field + " from " + domain + "_qa where uniqno='" + uniqno + "'";
/*  71:105 */       System.out.println(sql);
/*  72:106 */       Connection c = DbConnection.getConnection();
/*  73:    */       try
/*  74:    */       {
/*  75:109 */         Statement s = c.createStatement();
/*  76:110 */         ResultSet rs = s.executeQuery(sql);
/*  77:111 */         rs.next();
/*  78:112 */         return rs.getString(field);
/*  79:    */       }
/*  80:    */       catch (SQLException e)
/*  81:    */       {
/*  82:115 */         e.printStackTrace();
/*  83:    */       }
/*  84:    */     }
/*  85:    */     else
/*  86:    */     {
/*  87:119 */       return (String)this.flags.get(field);
/*  88:    */     }
/*  89:121 */     return null;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public static void main(String[] args)
/*  93:    */     throws SQLException
/*  94:    */   {
/*  95:131 */     PregnancyChecks id = new PregnancyChecks();
/*  96:132 */     Connection con = DbConnection.getConnection();
/*  97:133 */     String sql = "select * from adult where uniqno='03300118_01_04'";
/*  98:134 */     Statement stmt = con.createStatement();
/*  99:135 */     ResultSet rs = stmt.executeQuery(sql);
/* 100:136 */     rs.next();
/* 101:137 */     StringBuffer errorMsg = new StringBuffer();
/* 102:138 */     System.out.println("Flag of idcheck::" + id.validate(rs, null, null, "086010346", errorMsg, "adult"));
/* 103:139 */     System.out.println(errorMsg);
/* 104:    */   }
/* 105:    */   
/* 106:141 */   Hashtable values = null;
/* 107:142 */   Hashtable flags = null;
/* 108:    */   
/* 109:    */   public String getValue1(String fld)
/* 110:    */   {
/* 111:    */     try
/* 112:    */     {
/* 113:154 */       if (this.rs != null) {
/* 114:155 */         return this.rs.getString(fld);
/* 115:    */       }
/* 116:157 */       if (this.values != null) {
/* 117:158 */         return (String)this.values.get(fld);
/* 118:    */       }
/* 119:    */     }
/* 120:    */     catch (SQLException e)
/* 121:    */     {
/* 122:161 */       e.printStackTrace();
/* 123:    */     }
/* 124:163 */     return null;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public boolean validate(Hashtable rs, Hashtable flags, ResultSet checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 128:    */   {
/* 129:168 */     this.values = rs;
/* 130:169 */     this.flags = flags;
/* 131:170 */     this.name = name;
/* 132:171 */     this.value = value;
/* 133:172 */     this.domain = domain;
/* 134:    */     try
/* 135:    */     {
/* 136:175 */       if ((domain.equals("neonatal")) || (domain.equals("child")) || (domain.equals("adult")))
/* 137:    */       {
/* 138:176 */         if ((!domain.equals("adult")) && (!individualVsMother(value))) {
/* 139:177 */           return false;
/* 140:    */         }
/* 141:179 */         if ((!domain.equals("adult")) && (!individualVsHead(value))) {
/* 142:180 */           return false;
/* 143:    */         }
/* 144:181 */         if (!individualVsRespondent(value)) {
/* 145:182 */           return false;
/* 146:    */         }
/* 147:184 */         return true;
/* 148:    */       }
/* 149:    */     }
/* 150:    */     catch (Exception e)
/* 151:    */     {
/* 152:187 */       e.printStackTrace();
/* 153:    */     }
/* 154:189 */     return false;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public boolean validate(Hashtable rs, Hashtable flags, Hashtable checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 158:    */   {
/* 159:195 */     return false;
/* 160:    */   }
/* 161:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-qa\ken-qa.jar
 * Qualified Name:     com.kentropy.cme.qa.neochecks.PregnancyChecks
 * JD-Core Version:    0.7.0.1
 */