/*  1:   */ package com.kentropy.notifications.service;
/*  2:   */ 
/*  3:   */ import com.kentropy.scheduler.Job;
/*  4:   */ import com.kentropy.scheduler.Scheduler;
/*  5:   */ import com.kentropy.util.DbUtil;
/*  6:   */ import java.util.HashMap;
/*  7:   */ import java.util.Map;
/*  8:   */ 
/*  9:   */ public class NotificationJobs
/* 10:   */   implements Job
/* 11:   */ {
/* 12:17 */   DbUtil db = new DbUtil();
/* 13:19 */   public int task = 0;
/* 14:   */   
/* 15:   */   static
/* 16:   */   {
/* 17:25 */     Scheduler.add("Notifications", "New Tasks", new NotificationJobs(1), "daily");
/* 18:26 */     Scheduler.add("Notifications", "CME-MONITORING REPORT", new NotificationJobs(2), "daily");
/* 19:27 */     Scheduler.add("Notifications", "CME-REPORTS", new NotificationJobs(3), "monthly");
/* 20:   */   }
/* 21:   */   
/* 22:   */   public NotificationJobs(int tmp)
/* 23:   */   {
/* 24:34 */     this.task = tmp;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void execute()
/* 28:   */     throws Exception
/* 29:   */   {
/* 30:41 */     int task = this.task;
/* 31:   */     
/* 32:43 */     MessageService service = new MessageService();
/* 33:44 */     String[] args = new String[0];
/* 34:46 */     switch (task)
/* 35:   */     {
/* 36:   */     case 1: 
/* 37:51 */       Map map = new HashMap();
/* 38:52 */       map.put("notification_id", "1");
/* 39:53 */       this.db.saveDataFromMap("notifications_logs", null, map);
/* 40:54 */       service.sendNotifications(args);
/* 41:55 */       break;
/* 42:   */     case 2: 
/* 43:61 */       Map map2 = new HashMap();
/* 44:62 */       map2.put("notification_id", "8");
/* 45:63 */       this.db.saveDataFromMap("notifications_logs", null, map2);
/* 46:64 */       service.sendNotifications(args);
/* 47:65 */       break;
/* 48:   */     case 3: 
/* 49:70 */       Map map3 = new HashMap();
/* 50:71 */       map3.put("notification_id", "9");
/* 51:72 */       this.db.saveDataFromMap("notifications_logs", null, map3);
/* 52:73 */       service.sendNotifications(args);
/* 53:   */       
/* 54:   */ 
/* 55:76 */       break;
/* 56:   */     }
/* 57:   */   }
/* 58:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-cme-notifications\ken-cme-notifications.jar
 * Qualified Name:     com.kentropy.notifications.service.NotificationJobs
 * JD-Core Version:    0.7.0.1
 */