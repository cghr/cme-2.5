/*   1:    */ package com.kentropy.process.notification;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import com.kentropy.mail.MailUtils;
/*   5:    */ import com.kentropy.notifications.service.MessageService;
/*   6:    */ import com.kentropy.util.DbUtil;
/*   7:    */ import java.io.IOException;
/*   8:    */ import java.io.PrintStream;
/*   9:    */ import java.util.Arrays;
/*  10:    */ import java.util.Date;
/*  11:    */ import java.util.List;
/*  12:    */ import java.util.Map;
/*  13:    */ import java.util.Vector;
/*  14:    */ import net.xoetrope.xui.data.XBaseModel;
/*  15:    */ import net.xoetrope.xui.data.XModel;
/*  16:    */ 
/*  17:    */ public class ProcessNotification2
/*  18:    */ {
/*  19:    */   public void queue(String pid, String email, String type, String notificationId, String template, String status)
/*  20:    */   {
/*  21: 23 */     XModel xm = new XBaseModel();
/*  22: 24 */     ((XBaseModel)xm.get("pid")).set(pid);
/*  23: 25 */     ((XBaseModel)xm.get("email")).set(email);
/*  24: 26 */     ((XBaseModel)xm.get("notificationId")).set(pid + "-" + template + new Date());
/*  25: 27 */     ((XBaseModel)xm.get("template")).set(template);
/*  26:    */     
/*  27: 29 */     ((XBaseModel)xm.get("type1")).set(type);
/*  28:    */     
/*  29: 31 */     ((XBaseModel)xm.get("status")).set(status);
/*  30: 32 */     ((XBaseModel)xm.get("created")).set(new Date());
/*  31: 33 */     ((XBaseModel)xm.get("lastmodified")).set(new Date());
/*  32:    */     try
/*  33:    */     {
/*  34: 35 */       TestXUIDB.getInstance().saveDataM2("process_notifications", "pid='" + pid + "' and notificationId='" + notificationId + "'", xm);
/*  35:    */     }
/*  36:    */     catch (Exception e)
/*  37:    */     {
/*  38: 38 */       e.printStackTrace();
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void queue(String pid, String email, String type, String notificationId, String template, String templateParams, String status)
/*  43:    */   {
/*  44: 44 */     XModel xm = new XBaseModel();
/*  45: 45 */     ((XBaseModel)xm.get("pid")).set(pid);
/*  46: 46 */     ((XBaseModel)xm.get("email")).set(email);
/*  47: 47 */     ((XBaseModel)xm.get("notificationId")).set(pid + "-" + template + new Date());
/*  48: 48 */     ((XBaseModel)xm.get("template")).set(template);
/*  49: 49 */     ((XBaseModel)xm.get("template_params")).set(templateParams);
/*  50: 50 */     ((XBaseModel)xm.get("type1")).set(type);
/*  51:    */     
/*  52: 52 */     ((XBaseModel)xm.get("status")).set(status);
/*  53: 53 */     ((XBaseModel)xm.get("created")).set(new Date());
/*  54: 54 */     ((XBaseModel)xm.get("lastmodified")).set(new Date());
/*  55:    */     try
/*  56:    */     {
/*  57: 56 */       TestXUIDB.getInstance().saveDataM2("process_notifications", "pid='" + pid + "' and notificationId='" + notificationId + "'", xm);
/*  58:    */     }
/*  59:    */     catch (Exception e)
/*  60:    */     {
/*  61: 59 */       e.printStackTrace();
/*  62:    */     }
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void updateQueueStatus(String pid, String email, String notificationId, String status)
/*  66:    */   {
/*  67: 65 */     XModel xm = new XBaseModel();
/*  68: 66 */     ((XBaseModel)xm.get("pid")).set(pid);
/*  69: 67 */     ((XBaseModel)xm.get("notificationId")).set(notificationId);
/*  70: 68 */     ((XBaseModel)xm.get("status")).set(status);
/*  71:    */     
/*  72: 70 */     ((XBaseModel)xm.get("lastmodified")).set(new Date());
/*  73:    */     try
/*  74:    */     {
/*  75: 72 */       TestXUIDB.getInstance().saveDataM2("process_notifications", "pid='" + pid + "' and notificationId='" + notificationId + "' and email='" + email + "' and status='Pending'", xm);
/*  76:    */     }
/*  77:    */     catch (Exception e)
/*  78:    */     {
/*  79: 75 */       e.printStackTrace();
/*  80:    */     }
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void processQueue()
/*  84:    */   {
/*  85: 80 */     XModel xm = new XBaseModel();
/*  86: 81 */     TestXUIDB.getInstance().getData("process_notifications", "*", "status ='Pending'", xm);
/*  87: 82 */     System.out.println("Process ing");
/*  88: 83 */     for (int i = 0; i < xm.getNumChildren(); i++)
/*  89:    */     {
/*  90: 85 */       String email = ((XModel)xm.get(i).get("email")).get().toString();
/*  91: 86 */       String type = ((XModel)xm.get(i).get("type1")).get().toString();
/*  92: 87 */       System.out.println(" Type =" + type + "--");
/*  93:    */       
/*  94: 89 */       String template = ((XModel)xm.get(i).get("template")).get().toString();
/*  95: 90 */       String phyId = ((XModel)xm.get(i).get("pid")).get().toString();
/*  96: 91 */       String templateParams = (String)((XModel)xm.get(i).get("template_params")).get();
/*  97:    */       
/*  98:    */ 
/*  99: 94 */       String notificationId = ((XModel)xm.get(i).get("notificationId")).get().toString();
/* 100: 95 */       System.out.println(" Sending " + phyId + " " + template);
/* 101: 96 */       if (templateParams == null) {
/* 102: 97 */         sendNotification(template, email, "rajeevk@kentropy.com");
/* 103:    */       } else {
/* 104: 99 */         sendNotification(template, email, "rajeevk@kentropy.com", phyId + "," + templateParams);
/* 105:    */       }
/* 106:102 */       updateQueueStatus(phyId, email, notificationId, "completed");
/* 107:    */     }
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void sendNotification(String message, String to, String cc, String templateParams)
/* 111:    */   {
/* 112:108 */     System.out.println("template name " + message);
/* 113:109 */     MailUtils mail = new MailUtils();
/* 114:110 */     DbUtil db = new DbUtil();
/* 115:111 */     String template_id = db.uniqueResult("notifications_templates", "id", "name=?", new Object[] { message });
/* 116:112 */     System.out.println("template Id=" + template_id);
/* 117:113 */     List list = db.getDataAsListofMaps("notifications_templates", "*", "id=?", new Object[] { template_id });
/* 118:114 */     Map dataMap = (Map)list.get(0);
/* 119:    */     
/* 120:116 */     Vector v = new Vector();
/* 121:118 */     if (templateParams != null)
/* 122:    */     {
/* 123:120 */       String[] str = templateParams.split(",");
/* 124:121 */       List l = Arrays.asList(str);
/* 125:122 */       v.addAll(l);
/* 126:    */     }
/* 127:127 */     Object[] params1 = v.toArray();
/* 128:128 */     String templateStr = new MessageService().getCustomizedEmailFromTemplate(template_id, new Object[] { to });
/* 129:129 */     for (int i = 0; i < params1.length; i++)
/* 130:    */     {
/* 131:130 */       System.out.println(" Params " + i + " " + params1[i]);
/* 132:131 */       templateStr = templateStr.replaceAll("<params" + i + ">", (String)params1[i]);
/* 133:    */     }
/* 134:133 */     String email = to.split("-")[0].trim();
/* 135:134 */     String template_cc = ((String)dataMap.get("cc")).trim();
/* 136:    */     
/* 137:    */ 
/* 138:    */ 
/* 139:    */ 
/* 140:    */ 
/* 141:    */ 
/* 142:141 */     System.out.println(templateStr);
/* 143:    */     
/* 144:143 */     mail.sendHTMLMail((String)dataMap.get("from_email"), email, (String)dataMap.get("subject"), templateStr, (String)dataMap.get("attachmentUrl"), cc);
/* 145:    */     
/* 146:145 */     mail.sendHTMLMail((String)dataMap.get("from_email"), template_cc, (String)dataMap.get("subject"), templateStr, (String)dataMap.get("attachmentUrl"), null);
/* 147:    */   }
/* 148:    */   
/* 149:    */   public void sendNotification(String message, String to, String cc)
/* 150:    */   {
/* 151:150 */     System.out.println("template name " + message);
/* 152:151 */     MailUtils mail = new MailUtils();
/* 153:152 */     DbUtil db = new DbUtil();
/* 154:153 */     String template_id = db.uniqueResult("notifications_templates", "id", "name=?", new Object[] { message });
/* 155:154 */     System.out.println("template Id=" + template_id);
/* 156:155 */     List list = db.getDataAsListofMaps("notifications_templates", "*", "id=?", new Object[] { template_id });
/* 157:156 */     Map dataMap = (Map)list.get(0);
/* 158:    */     
/* 159:158 */     String email = to.split("-")[0].trim();
/* 160:159 */     String template_cc = ((String)dataMap.get("cc")).trim();
/* 161:    */     
/* 162:    */ 
/* 163:    */ 
/* 164:163 */     mail.sendHTMLMail((String)dataMap.get("from_email"), email, (String)dataMap.get("subject"), new MessageService().getCustomizedEmailFromTemplate(template_id, new Object[] { to }), (String)dataMap.get("attachmentUrl"), cc);
/* 165:    */     
/* 166:165 */     mail.sendHTMLMail((String)dataMap.get("from_email"), template_cc, (String)dataMap.get("subject"), new MessageService().getCustomizedEmailFromTemplate(template_id, new Object[] { to }), (String)dataMap.get("attachmentUrl"), null);
/* 167:    */   }
/* 168:    */   
/* 169:    */   public static void main(String[] args)
/* 170:    */     throws IOException
/* 171:    */   {
/* 172:175 */     new ProcessNotification2().processQueue();
/* 173:    */   }
/* 174:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-utilities6\ken-utilities6.jar
 * Qualified Name:     com.kentropy.process.notification.ProcessNotification2
 * JD-Core Version:    0.7.0.1
 */