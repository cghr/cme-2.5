/*   1:    */ package com.kentropy.notifications.service;
/*   2:    */ 
/*   3:    */ import com.kentropy.mail.MailUtils;
/*   4:    */ import com.kentropy.util.DbUtil;
/*   5:    */ import com.kentropy.util.SpringUtils;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Map;
/*   9:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*  10:    */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*  11:    */ import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
/*  12:    */ 
/*  13:    */ public class EmailReports
/*  14:    */ {
/*  15: 17 */   JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/*  16: 18 */   DbUtil db = new DbUtil();
/*  17: 19 */   MailUtils mail = new MailUtils();
/*  18:    */   
/*  19:    */   public void sendEmailsReportsFromQueue()
/*  20:    */   {
/*  21: 28 */     setInitialCountsForCme();
/*  22:    */     
/*  23:    */ 
/*  24: 31 */     String sql = "SELECT email_report_id FROM email_report_logs where status='pending'";
/*  25: 32 */     SqlRowSet rs = this.jt.queryForRowSet(sql);
/*  26: 33 */     Map map = new HashMap();
/*  27: 35 */     while (rs.next())
/*  28:    */     {
/*  29: 37 */       String email_report_id = rs.getString("email_report_id");
/*  30: 38 */       sendReportForEachEntry(email_report_id);
/*  31: 39 */       map.put("status", "success");
/*  32: 40 */       this.db.saveDataFromMap("email_report_logs", "email_report_id='" + email_report_id + "'", map);
/*  33:    */     }
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void sendReportForEachEntry(String id)
/*  37:    */   {
/*  38: 55 */     List list = this.db.getDataAsListofMaps("email_report", "*", "id=?", new Object[] { id });
/*  39: 56 */     Map<String, String> map = (Map)list.get(0);
/*  40: 57 */     String report_type = (String)map.get("report_type");
/*  41: 59 */     if (report_type.equals("general")) {
/*  42: 60 */       sendGeneralReport(map);
/*  43: 62 */     } else if (report_type.equals("personalised")) {
/*  44: 63 */       sendPersonalisedReport(map);
/*  45:    */     }
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void sendGeneralReport(Map<String, String> map)
/*  49:    */   {
/*  50: 75 */     SqlRowSet rs = this.jt.queryForRowSet((String)map.get("mail_list"));
/*  51: 76 */     String[] headings = ((String)map.get("report_heading")).split(";");
/*  52: 77 */     String emailBody = map.get("header") == null ? "" : (String)map.get("header");
/*  53: 78 */     String[] report_sqls = ((String)map.get("report_sql")).split(";");
/*  54: 80 */     for (int i = 0; i < headings.length; i++)
/*  55:    */     {
/*  56: 83 */       emailBody = emailBody + "<h3 style='margin-left:2%;color:#3366CC;font-weight: bold'>" + headings[i] + "</h3>";
/*  57: 84 */       emailBody = emailBody + createEmailBodyForReport(report_sqls[i], null);
/*  58:    */     }
/*  59: 92 */     String attachmentUrl = (String)map.get("attachmenUrl");
/*  60: 93 */     String subject = (String)map.get("subject");
/*  61: 94 */     String cc = (String)map.get("cc");
/*  62: 95 */     String from = (String)map.get("from");
/*  63: 98 */     while (rs.next())
/*  64:    */     {
/*  65:101 */       String email = rs.getString(1);
/*  66:102 */       this.mail.sendHTMLMail(from, email, subject, emailBody, attachmentUrl, cc);
/*  67:    */     }
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void sendPersonalisedReport(Map<String, String> map)
/*  71:    */   {
/*  72:115 */     String[] headings = ((String)map.get("report_heading")).split(";");
/*  73:    */     
/*  74:117 */     String[] report_sqls = ((String)map.get("report_sql")).split(";");
/*  75:118 */     String[] mail_list = ((String)map.get("mail_list")).split(";");
/*  76:    */     
/*  77:    */ 
/*  78:121 */     String attachmentUrl = (String)map.get("attachmenUrl");
/*  79:122 */     String subject = (String)map.get("subject");
/*  80:123 */     String cc = (String)map.get("cc");
/*  81:124 */     String from = (String)map.get("from");
/*  82:125 */     StringBuffer curren_recepient = null;
/*  83:    */     
/*  84:127 */     SqlRowSet mailing_list_rs = this.jt.queryForRowSet(mail_list[0]);
/*  85:129 */     while (mailing_list_rs.next())
/*  86:    */     {
/*  87:131 */       StringBuffer emailBody = new StringBuffer();
/*  88:132 */       for (int c = 0; c < mail_list.length; c++)
/*  89:    */       {
/*  90:135 */         SqlRowSet rs = this.jt.queryForRowSet(mail_list[c]);
/*  91:    */         
/*  92:137 */         rs.next();
/*  93:    */         
/*  94:    */ 
/*  95:140 */         curren_recepient = new StringBuffer(rs.getString(1));
/*  96:    */         
/*  97:142 */         SqlRowSetMetaData rsmd = rs.getMetaData();
/*  98:    */         
/*  99:    */ 
/* 100:145 */         int column_count = rsmd.getColumnCount();
/* 101:146 */         Object[] params = new Object[column_count - 1];
/* 102:148 */         for (int j = 1; j <= params.length; j++) {
/* 103:151 */           params[(j - 1)] = rs.getString(j + 1);
/* 104:    */         }
/* 105:156 */         emailBody.append("<h3 style='margin-left:2%;color:#3366CC;font-weight: bold'>" + headings[c] + "</h3>");
/* 106:157 */         emailBody.append(createEmailBodyForReport(report_sqls[c], params));
/* 107:    */       }
/* 108:161 */       this.mail.sendHTMLMail(from, curren_recepient.toString(), subject, emailBody.toString(), attachmentUrl, cc);
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   public StringBuffer createEmailBodyForReport(String sql, Object[] args)
/* 113:    */   {
/* 114:172 */     StringBuffer gridModel = new StringBuffer();
/* 115:173 */     SqlRowSet rs = null;
/* 116:174 */     StringBuffer headings = new StringBuffer();
/* 117:175 */     int rowCount = 0;
/* 118:    */     try
/* 119:    */     {
/* 120:186 */       rs = this.jt.queryForRowSet(sql, args);
/* 121:187 */       SqlRowSetMetaData rsmd = rs.getMetaData();
/* 122:188 */       int column_count = rsmd.getColumnCount();
/* 123:191 */       for (int i = 1; i <= column_count; i++)
/* 124:    */       {
/* 125:196 */         gridModel.append("<th style='font-size:13px;font-weight:normal;background:#b9c9fe;border-top:4px solid #aabcfe;border-bottom:1px solid #fff;color:#039;padding:8px;'>");
/* 126:    */         
/* 127:    */ 
/* 128:199 */         gridModel.append(rsmd.getColumnLabel(i));
/* 129:200 */         gridModel.append("</th>");
/* 130:    */       }
/* 131:207 */       while (rs.next())
/* 132:    */       {
/* 133:209 */         gridModel.append("<tr>");
/* 134:211 */         for (int i = 1; i <= column_count; i++)
/* 135:    */         {
/* 136:213 */           gridModel.append("<td style='background:#e8edff;border-bottom:1px solid #fff;color:#669;border-top:1px solid transparent;padding:8px;'>");
/* 137:    */           
/* 138:215 */           gridModel.append(rs.getString(i));
/* 139:    */           
/* 140:217 */           gridModel.append("</td>");
/* 141:    */         }
/* 142:220 */         gridModel.append("</tr>");
/* 143:221 */         rowCount++;
/* 144:    */       }
/* 145:224 */       gridModel.insert(0, "<table style='font-family:'Lucida Sans Unicode', 'Lucida Grande', Sans-Serif;font-size:12px;width:480px;text-align:left;border-collapse:collapse;margin:20px;'>");
/* 146:225 */       gridModel.append("</table>");
/* 147:    */     }
/* 148:    */     catch (Exception e)
/* 149:    */     {
/* 150:232 */       e.printStackTrace();
/* 151:    */     }
/* 152:235 */     return gridModel;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void setInitialCountsForCme()
/* 156:    */   {
/* 157:248 */     this.jt.execute("SELECT   @codercount:=COUNT(*) FROM physician WHERE STATUS!='stopped' AND coder=1");
/* 158:249 */     this.jt.execute("SELECT   @adjcount:=COUNT(*) FROM physician WHERE STATUS!='stopped' AND adjudicator=1");
/* 159:    */   }
/* 160:    */   
/* 161:    */   public static void main(String[] args)
/* 162:    */   {
/* 163:261 */     new EmailReports().sendEmailsReportsFromQueue();
/* 164:    */   }
/* 165:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-cme-notifications\ken-cme-notifications.jar
 * Qualified Name:     com.kentropy.notifications.service.EmailReports
 * JD-Core Version:    0.7.0.1
 */