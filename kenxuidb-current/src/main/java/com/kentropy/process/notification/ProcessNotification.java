/*  1:   */ package com.kentropy.process.notification;
/*  2:   */ 
/*  3:   */ import com.kentropy.db.TestXUIDB;
/*  4:   */ import com.kentropy.notifications.service.MessageService;
/*  5:   */ import java.io.IOException;
/*  6:   */ import java.io.InputStream;
/*  7:   */ import java.io.PrintStream;
/*  8:   */ import java.util.Date;
/*  9:   */ import java.util.Properties;
/* 10:   */ import net.xoetrope.xui.data.XBaseModel;
/* 11:   */ import net.xoetrope.xui.data.XModel;
/* 12:   */ 
/* 13:   */ public class ProcessNotification
/* 14:   */ {
/* 15:   */   public void queue(String pid, String type, String notificationId, String template, String status)
/* 16:   */   {
/* 17:17 */     XModel xm = new XBaseModel();
/* 18:18 */     ((XBaseModel)xm.get("pid")).set(pid);
/* 19:19 */     ((XBaseModel)xm.get("notificationId")).set(notificationId);
/* 20:20 */     ((XBaseModel)xm.get("template")).set(template);
/* 21:   */     
/* 22:22 */     ((XBaseModel)xm.get("type1")).set(type);
/* 23:   */     
/* 24:24 */     ((XBaseModel)xm.get("status")).set(status);
/* 25:25 */     ((XBaseModel)xm.get("created")).set(new Date());
/* 26:26 */     ((XBaseModel)xm.get("lastmodified")).set(new Date());
/* 27:   */     try
/* 28:   */     {
/* 29:28 */       TestXUIDB.getInstance().saveDataM2("process_notifications", "pid='" + pid + "' and notificationId='" + notificationId + "'", xm);
/* 30:   */     }
/* 31:   */     catch (Exception e)
/* 32:   */     {
/* 33:31 */       e.printStackTrace();
/* 34:   */     }
/* 35:   */   }
/* 36:   */   
/* 37:   */   public void updateQueueStatus(String pid, String notificationId, String status)
/* 38:   */   {
/* 39:37 */     XModel xm = new XBaseModel();
/* 40:38 */     ((XBaseModel)xm.get("pid")).set(pid);
/* 41:39 */     ((XBaseModel)xm.get("notificationId")).set(notificationId);
/* 42:40 */     ((XBaseModel)xm.get("status")).set(status);
/* 43:   */     
/* 44:42 */     ((XBaseModel)xm.get("lastmodified")).set(new Date());
/* 45:   */     try
/* 46:   */     {
/* 47:44 */       TestXUIDB.getInstance().saveDataM2("process_notifications", "pid='" + pid + "' and notificationId='" + notificationId + "'", xm);
/* 48:   */     }
/* 49:   */     catch (Exception e)
/* 50:   */     {
/* 51:47 */       e.printStackTrace();
/* 52:   */     }
/* 53:   */   }
/* 54:   */   
/* 55:   */   public void processQueue()
/* 56:   */   {
/* 57:52 */     XModel xm = new XBaseModel();
/* 58:53 */     TestXUIDB.getInstance().getData("process_notifications", "*", "status ='init'", xm);
/* 59:54 */     for (int i = 0; i < xm.getNumChildren(); i++)
/* 60:   */     {
/* 61:56 */       String type = ((XModel)xm.get(i).get("type1")).get().toString();
/* 62:57 */       System.out.println(" Type =" + type + "--");
/* 63:58 */       if (type.equals("physician"))
/* 64:   */       {
/* 65:60 */         String template = ((XModel)xm.get(i).get("template")).get().toString();
/* 66:61 */         String phyId = ((XModel)xm.get(i).get("pid")).get().toString();
/* 67:62 */         String notificationId = ((XModel)xm.get(i).get("notificationId")).get().toString();
/* 68:63 */         System.out.println(" Sending " + phyId + " " + template);
/* 69:64 */         new MessageService().sendToPhysician(template, phyId, ((Properties)TestXUIDB.getInstance().getBean("ProcessNotificationConfiguration")).getProperty("physician-cc"));
/* 70:65 */         updateQueueStatus(phyId, notificationId, "completed");
/* 71:   */       }
/* 72:   */     }
/* 73:   */   }
/* 74:   */   
/* 75:   */   public static void main(String[] args)
/* 76:   */     throws IOException
/* 77:   */   {
/* 78:73 */     new ProcessNotification().queue("1", "physician", "startup", "temp1", "init");
/* 79:74 */     System.in.read();
/* 80:   */     
/* 81:76 */     new ProcessNotification().processQueue();
/* 82:   */   }
/* 83:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.process.notification.ProcessNotification
 * JD-Core Version:    0.7.0.1
 */