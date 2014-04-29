/*   1:    */ package com.kentropy.scheduler;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import com.kentropy.scheduler.jobs.CMEJobs;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.util.Calendar;
/*   7:    */ import java.util.Date;
/*   8:    */ import java.util.Hashtable;
/*   9:    */ import java.util.Vector;
/*  10:    */ import net.xoetrope.xui.data.XBaseModel;
/*  11:    */ import net.xoetrope.xui.data.XModel;
/*  12:    */ 
/*  13:    */ public class Scheduler
/*  14:    */ {
/*  15: 15 */   public static Vector queue = new Vector();
/*  16: 17 */   public static Hashtable jobs = new Hashtable();
/*  17: 19 */   public static Hashtable names = new Hashtable();
/*  18: 20 */   public static Hashtable categories = new Hashtable();
/*  19: 22 */   public static Hashtable times = new Hashtable();
/*  20:    */   
/*  21:    */   public static boolean isInQueue(String id)
/*  22:    */   {
/*  23: 26 */     return queue.contains(id);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public static void addToQueue(String id)
/*  27:    */   {
/*  28: 31 */     if (!isInQueue(id)) {
/*  29: 32 */       queue.add(id);
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   public static void remove(String id)
/*  34:    */   {
/*  35: 36 */     if (queue.indexOf(id) > -1) {
/*  36: 37 */       queue.remove(queue.indexOf(id));
/*  37:    */     }
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static void add(String category, String name, Job job, String period)
/*  41:    */   {
/*  42: 42 */     Vector v = (Vector)jobs.get("period");
/*  43: 43 */     if (v == null) {
/*  44: 44 */       v = new Vector();
/*  45:    */     }
/*  46: 46 */     v.add(job);
/*  47: 47 */     jobs.put(period, v);
/*  48: 48 */     Vector v1 = (Vector)categories.get(category);
/*  49: 49 */     if (v1 == null) {
/*  50: 50 */       v1 = new Vector();
/*  51:    */     }
/*  52: 52 */     v1.add(name);
/*  53: 53 */     categories.put(category, v1);
/*  54: 54 */     names.put(name, job);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void runJobs(String period)
/*  58:    */   {
/*  59: 59 */     Date dt = (Date)times.get(period);
/*  60: 60 */     if (dt != null)
/*  61:    */     {
/*  62: 62 */       Calendar cal = Calendar.getInstance();
/*  63: 63 */       int time = 0;
/*  64: 64 */       if (period.equals("15,min")) {
/*  65: 66 */         time = 15;
/*  66: 68 */       } else if (period.equals("daily")) {
/*  67: 70 */         time = 1440;
/*  68: 72 */       } else if (period.equals("weekly")) {
/*  69: 74 */         time = 10080;
/*  70:    */       }
/*  71: 77 */       cal.add(12, 0 - time);
/*  72: 78 */       System.out.println("Comparing " + cal.getTime() + " with " + dt);
/*  73: 80 */       if (cal.getTime().before(dt)) {
/*  74: 82 */         return;
/*  75:    */       }
/*  76:    */     }
/*  77: 87 */     times.put(period, new Date());
/*  78: 88 */     Vector v = (Vector)jobs.get(period);
/*  79: 89 */     for (int i = 0; i < v.size(); i++)
/*  80:    */     {
/*  81: 91 */       Job job = (Job)v.get(i);
/*  82: 92 */       System.out.println(" jobs " + i);
/*  83:    */       try
/*  84:    */       {
/*  85: 94 */         job.execute();
/*  86:    */       }
/*  87:    */       catch (Exception e)
/*  88:    */       {
/*  89: 98 */         e.printStackTrace();
/*  90:    */       }
/*  91:    */     }
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void runJob(String name)
/*  95:    */     throws Exception
/*  96:    */   {
/*  97:106 */     Job job = (Job)names.get(name);
/*  98:107 */     job.execute();
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void processQueue()
/* 102:    */   {
/* 103:112 */     XModel xm = new XBaseModel();
/* 104:113 */     TestXUIDB.getInstance().getData("server_tasks", "taskClass", "status=null", xm);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public static void main(String[] args)
/* 108:    */   {
/* 109:117 */     CMEJobs cme = new CMEJobs(10);
/* 110:118 */     System.out.println(jobs);
/* 111:    */   }
/* 112:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.scheduler.Scheduler
 * JD-Core Version:    0.7.0.1
 */