/*  1:   */ package com.kentropy.services;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ import java.util.Iterator;
/*  5:   */ import java.util.Set;
/*  6:   */ 
/*  7:   */ public class TaskScheduler
/*  8:   */ {
/*  9:13 */   HashMap tasks = new HashMap();
/* 10:   */   
/* 11:   */   public void addTask(String id, int secs2, int startsecs2, String[] taskssch, String url)
/* 12:   */   {
/* 13:16 */     int secs = secs2;
/* 14:17 */     String taskid = id;
/* 15:18 */     int startsecs = startsecs2;
/* 16:   */     
/* 17:20 */     Thread th = new Thread(new Task(secs, taskid, startsecs, taskssch, url));
/* 18:21 */     th.start();
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void stopTask(String id)
/* 22:   */   {
/* 23:29 */     ((Thread)this.tasks.get(id)).stop();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void stopAllTasks()
/* 27:   */   {
/* 28:33 */     Iterator i = this.tasks.keySet().iterator();
/* 29:34 */     while (i.hasNext())
/* 30:   */     {
/* 31:36 */       Object key = i.next();
/* 32:37 */       stopTask(key.toString());
/* 33:   */     }
/* 34:   */   }
/* 35:   */   
/* 36:   */   public static void main(String[] args)
/* 37:   */   {
/* 38:49 */     String[] taskSch = { "1-31", "1-7", "0-16", "1-60", "1-60" };
/* 39:50 */     new TaskScheduler().addTask("1", 10, 1, taskSch, "http://www.cghr.org:8080/cme/runTasks.jsp?task=44");
/* 40:   */   }
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-services2\ken-services2.jar
 * Qualified Name:     com.kentropy.services.TaskScheduler
 * JD-Core Version:    0.7.0.1
 */