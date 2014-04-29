/*   1:    */ package com.kentropy.notifications.service;
/*   2:    */ 
/*   3:    */ import com.kentropy.mail.MailUtils;
/*   4:    */ import com.kentropy.util.DbUtil;
/*   5:    */ import com.kentropy.util.SpringApplicationContext;
/*   6:    */ import com.kentropy.util.SpringUtils;
/*   7:    */ import java.io.IOException;
/*   8:    */ import java.io.PrintStream;
/*   9:    */ import java.net.HttpURLConnection;
/*  10:    */ import java.net.URL;
/*  11:    */ import java.net.URLEncoder;
/*  12:    */ import java.util.List;
/*  13:    */ import java.util.Map;
/*  14:    */ import java.util.Properties;
/*  15:    */ import org.apache.commons.mail.EmailException;
/*  16:    */ import org.apache.log4j.Logger;
/*  17:    */ import org.springframework.jdbc.InvalidResultSetAccessException;
/*  18:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*  19:    */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*  20:    */ import org.springframework.web.client.RestTemplate;
/*  21:    */ 
/*  22:    */ public class MessageService
/*  23:    */ {
/*  24: 34 */   MailUtils mail = new MailUtils();
/*  25: 35 */   Logger log = Logger.getLogger(MessageService.class);
/*  26: 36 */   JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/*  27: 37 */   DbUtil db = new DbUtil();
/*  28: 38 */   String emailWebService = ((Properties)SpringApplicationContext.getBean("webServices")).getProperty("mailService");
/*  29: 39 */   String smsWebService = ((Properties)SpringApplicationContext.getBean("webServices")).getProperty("smsService");
/*  30: 40 */   RestTemplate tmpl = new RestTemplate();
/*  31:    */   
/*  32:    */   public synchronized void sendNotifications(String[] args)
/*  33:    */     throws InvalidResultSetAccessException, EmailException, IOException
/*  34:    */   {
/*  35: 49 */     SqlRowSet rs1 = this.jt.queryForRowSet("SELECT notification_id FROM notifications_logs WHERE status='pending'");
/*  36:    */     SqlRowSet rs;
/*  37: 55 */     for (; rs1.next(); rs.next())
/*  38:    */     {
/*  39: 57 */       String sql = "SELECT * FROM notifications_templates WHERE id=?";
/*  40: 58 */       String notification_id = rs1.getString("notification_id");
/*  41: 59 */       rs = this.jt.queryForRowSet(sql, new Object[] { notification_id });
/*  42:    */       
/*  43:    */ 
/*  44: 62 */       continue;
/*  45:    */       
/*  46:    */ 
/*  47:    */ 
/*  48: 66 */       String type = rs.getString("type");
/*  49: 67 */       this.log.info("Type is " + type);
/*  50: 68 */       String[] types = type != null ? type.split(",") : null;
/*  51: 69 */       if ((types != null) && (types.length == 2))
/*  52:    */       {
/*  53: 73 */         sendM1(rs);
/*  54: 74 */         sendM2(rs);
/*  55:    */       }
/*  56: 78 */       else if ((types != null) && (types.length == 1))
/*  57:    */       {
/*  58: 81 */         if (types[0].equals("email"))
/*  59:    */         {
/*  60: 83 */           this.log.info("Inside email type");
/*  61: 84 */           sendM1(rs);
/*  62:    */         }
/*  63: 87 */         else if (types[0].equals("sms"))
/*  64:    */         {
/*  65: 89 */           this.log.info("Inside sms type");
/*  66: 90 */           sendM2(rs);
/*  67:    */         }
/*  68:    */       }
/*  69:    */     }
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void sendM1(SqlRowSet rs)
/*  73:    */   {
/*  74:106 */     SqlRowSet rowset = this.jt.queryForRowSet(rs.getString("query"));
/*  75:107 */     String[] values = rs.getString("template").split("\\$");
/*  76:108 */     String attachment_url = rs.getString("attachment_url");
/*  77:109 */     String from = rs.getString("from_email");
/*  78:110 */     if (from == null) {
/*  79:111 */       from = "support@kentropy.com";
/*  80:    */     }
/*  81:112 */     String cc = rs.getString("cc");
/*  82:113 */     String id = rs.getString("id");
/*  83:114 */     String subject = rs.getString("subject");
/*  84:115 */     int count = 0;
/*  85:    */     try
/*  86:    */     {
/*  87:120 */       while (rowset.next())
/*  88:    */       {
/*  89:123 */         count++;
/*  90:124 */         String body = "";
/*  91:125 */         int j = 2;
/*  92:126 */         for (int i = 0; i < values.length; i++) {
/*  93:128 */           if ((i + 1) % 2 != 0)
/*  94:    */           {
/*  95:130 */             body = body + values[i];
/*  96:    */           }
/*  97:    */           else
/*  98:    */           {
/*  99:135 */             body = body + rowset.getString(j);
/* 100:    */             
/* 101:137 */             j++;
/* 102:    */           }
/* 103:    */         }
/* 104:141 */         this.log.debug("From:" + from + " To:" + rs.getString(1) + " Subject:" + subject + " attachment:" + attachment_url + " cc:" + cc);
/* 105:    */         
/* 106:    */ 
/* 107:    */ 
/* 108:    */ 
/* 109:    */ 
/* 110:    */ 
/* 111:    */ 
/* 112:    */ 
/* 113:    */ 
/* 114:    */ 
/* 115:    */ 
/* 116:    */ 
/* 117:    */ 
/* 118:    */ 
/* 119:    */ 
/* 120:    */ 
/* 121:    */ 
/* 122:    */ 
/* 123:    */ 
/* 124:    */ 
/* 125:    */ 
/* 126:    */ 
/* 127:    */ 
/* 128:    */ 
/* 129:    */ 
/* 130:    */ 
/* 131:    */ 
/* 132:    */ 
/* 133:    */ 
/* 134:    */ 
/* 135:    */ 
/* 136:    */ 
/* 137:174 */         this.mail.sendHTMLMail(from, rowset.getString(1), subject, body, attachment_url, cc);
/* 138:    */         
/* 139:    */ 
/* 140:    */ 
/* 141:178 */         this.log.info("Email Sent to" + rowset.getString(1));
/* 142:    */       }
/* 143:182 */       this.log.info("No of Emails Sent: " + count);
/* 144:183 */       if (count != 0) {
/* 145:184 */         this.jt.update("UPDATE  notifications_logs set sent=NOW(),status='success' WHERE notification_id=? AND status='pending'", new Object[] { id });
/* 146:    */       }
/* 147:    */     }
/* 148:    */     catch (Exception e)
/* 149:    */     {
/* 150:188 */       this.log.error("==> Failed to Send Notification", e);
/* 151:    */       
/* 152:190 */       this.jt.update("UPDATE  notifications_logs set sent=NOW(),status='failed' WHERE notification_id=? AND status='pending'", new Object[] { id });
/* 153:    */     }
/* 154:    */   }
/* 155:    */   
/* 156:    */   public void sendM2(SqlRowSet rs)
/* 157:    */   {
/* 158:199 */     SqlRowSet rowset = this.jt.queryForRowSet(rs.getString("summaryquery"));
/* 159:200 */     String[] values = rs.getString("summary").split("\\$");
/* 160:201 */     String id = rs.getString("id");
/* 161:    */     
/* 162:203 */     int count = 0;
/* 163:    */     try
/* 164:    */     {
/* 165:207 */       while (rowset.next())
/* 166:    */       {
/* 167:209 */         count++;
/* 168:210 */         String summary = "";
/* 169:211 */         int j = 2;
/* 170:212 */         for (int i = 0; i < values.length; i++) {
/* 171:214 */           if ((i + 1) % 2 != 0)
/* 172:    */           {
/* 173:216 */             summary = summary + values[i];
/* 174:    */           }
/* 175:    */           else
/* 176:    */           {
/* 177:221 */             summary = summary + rowset.getString(j);
/* 178:    */             
/* 179:223 */             j++;
/* 180:    */           }
/* 181:    */         }
/* 182:228 */         summary = URLEncoder.encode(summary, "utf-8");
/* 183:    */         
/* 184:    */ 
/* 185:    */ 
/* 186:    */ 
/* 187:    */ 
/* 188:234 */         String tmp = this.smsWebService.replaceAll("mobileno", rowset.getString(1));
/* 189:235 */         String tmp1 = tmp.replaceAll("messageBody", summary);
/* 190:236 */         String finalSmsWebService = tmp1.replaceAll("%26", "&");
/* 191:    */         
/* 192:    */ 
/* 193:    */ 
/* 194:240 */         URL apiurl = new URL(finalSmsWebService);
/* 195:    */         
/* 196:    */ 
/* 197:    */ 
/* 198:244 */         HttpURLConnection urlconn = (HttpURLConnection)apiurl.openConnection();
/* 199:245 */         this.log.info("Sms to " + rowset.getString(1) + " " + urlconn.getResponseMessage());
/* 200:    */       }
/* 201:251 */       this.log.info("No of Sms Sent: " + count);
/* 202:    */       
/* 203:253 */       this.jt.update("UPDATE  notifications_logs set sent=NOW(),status='success' WHERE notification_id=?", new Object[] { id });
/* 204:    */     }
/* 205:    */     catch (Exception e)
/* 206:    */     {
/* 207:257 */       this.log.error("==> Failed to Send Notification", e);
/* 208:258 */       this.jt.update("UPDATE  notifications_logs set sent=NOW(),status='failed' WHERE notification_id=?", new Object[] { id });
/* 209:    */     }
/* 210:    */   }
/* 211:    */   
/* 212:    */   public void sendTemplateToPhysician(String template_id, String physicianId, String cc)
/* 213:    */   {
/* 214:268 */     MailUtils mail = new MailUtils();
/* 215:269 */     DbUtil db = new DbUtil();
/* 216:270 */     List list = db.getDataAsListofMaps("notifications_templates", "*", "id=?", new Object[] { template_id });
/* 217:271 */     Map<String, String> dataMap = (Map)list.get(0);
/* 218:272 */     String to = db.uniqueResult("physician", "username", "id=?", new Object[] { physicianId });
/* 219:273 */     String name = db.uniqueResult("physician", "name", "id=?", new Object[] { physicianId });
/* 220:274 */     String template = ((String)dataMap.get("template")).replace("$name$", name);
/* 221:    */     
/* 222:    */ 
/* 223:    */ 
/* 224:278 */     mail.sendHTMLMail((String)dataMap.get("from_email"), to, (String)dataMap.get("subject"), template, (String)dataMap.get("attachmentUrl"), cc);
/* 225:    */   }
/* 226:    */   
/* 227:    */   public void sendToPhysician(String message, String physicianId, String cc)
/* 228:    */   {
/* 229:288 */     this.log.info("Inside sendtoPHysician");
/* 230:289 */     System.out.println("template name " + message);
/* 231:290 */     MailUtils mail = new MailUtils();
/* 232:291 */     DbUtil db = new DbUtil();
/* 233:292 */     String template_id = db.uniqueResult("notifications_templates", "id", "name=?", new Object[] { message });
/* 234:293 */     System.out.println("template Id" + template_id);
/* 235:294 */     List list = db.getDataAsListofMaps("notifications_templates", "*", "id=?", new Object[] { template_id });
/* 236:295 */     Map<String, String> dataMap = (Map)list.get(0);
/* 237:    */     
/* 238:297 */     String to = db.uniqueResult("physician", "username", "id=?", new Object[] { physicianId });
/* 239:    */     
/* 240:    */ 
/* 241:300 */     mail.sendHTMLMail((String)dataMap.get("from_email"), to, (String)dataMap.get("subject"), getCustomizedEmailFromTemplate(template_id, new Object[] { to }), (String)dataMap.get("attachmentUrl"), cc);
/* 242:    */     
/* 243:302 */     this.log.info("End of the method sendtoPhysician");
/* 244:    */   }
/* 245:    */   
/* 246:    */   public String getCustomizedEmailFromTemplate(String template_id, Object[] params)
/* 247:    */   {
/* 248:308 */     List list = this.db.getDataAsListofMaps("notifications_templates", "*", "id=?", new Object[] { template_id });
/* 249:309 */     Map<String, String> map = (Map)list.get(0);
/* 250:    */     
/* 251:311 */     SqlRowSet rowset = this.jt.queryForRowSet((String)map.get("query"), params);
/* 252:312 */     int count = 0;
/* 253:313 */     String[] values = ((String)map.get("template")).split("\\$");
/* 254:314 */     String body = "";
/* 255:    */     int i;
/* 256:316 */     for (; rowset.next(); i < values.length)
/* 257:    */     {
/* 258:319 */       count++;
/* 259:    */       
/* 260:321 */       int j = 2;
/* 261:322 */       i = 0; continue;
/* 262:324 */       if ((i + 1) % 2 != 0)
/* 263:    */       {
/* 264:326 */         body = body + values[i];
/* 265:    */       }
/* 266:    */       else
/* 267:    */       {
/* 268:331 */         body = body + rowset.getString(j);
/* 269:    */         
/* 270:333 */         j++;
/* 271:    */       }
/* 272:322 */       i++;
/* 273:    */     }
/* 274:341 */     return body;
/* 275:    */   }
/* 276:    */   
/* 277:    */   public static void main(String[] args)
/* 278:    */     throws InvalidResultSetAccessException, EmailException, IOException
/* 279:    */   {
/* 280:353 */     new MessageService().sendNotifications(args);
/* 281:    */   }
/* 282:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-cme-notifications\ken-cme-notifications.jar
 * Qualified Name:     com.kentropy.notifications.service.MessageService
 * JD-Core Version:    0.7.0.1
 */