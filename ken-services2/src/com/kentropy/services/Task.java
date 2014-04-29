/*   1:    */ package com.kentropy.services;
/*   2:    */ 
/*   3:    */ import java.io.InputStream;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.net.HttpURLConnection;
/*   6:    */ import java.net.URL;
/*   7:    */ import java.util.Calendar;
/*   8:    */ import java.util.Date;
/*   9:    */ 
/*  10:    */ public class Task
/*  11:    */   implements Runnable
/*  12:    */ {
/*  13: 11 */   int secs1 = 1000;
/*  14: 12 */   int startsecs1 = 0;
/*  15: 13 */   String taskid1 = null;
/*  16:    */   private String url1;
/*  17:    */   private String[] tasksch;
/*  18:    */   
/*  19:    */   public Task(int secs, String taskid, int startsecs, String[] taskssch1, String url)
/*  20:    */   {
/*  21: 18 */     this.secs1 = secs;
/*  22: 19 */     this.startsecs1 = startsecs;
/*  23: 20 */     this.taskid1 = taskid;
/*  24: 21 */     this.tasksch = taskssch1;
/*  25: 22 */     this.url1 = url;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void run()
/*  29:    */   {
/*  30:    */     try
/*  31:    */     {
/*  32: 29 */       Thread.currentThread();Thread.sleep(this.startsecs1 * 1000);
/*  33:    */       for (;;)
/*  34:    */       {
/*  35: 33 */         if (isValid(new Date()))
/*  36:    */         {
/*  37: 35 */           HttpURLConnection con = (HttpURLConnection)new URL(this.url1).openConnection();
/*  38: 36 */           con.setDoInput(true);
/*  39:    */           
/*  40:    */ 
/*  41:    */ 
/*  42: 40 */           con.connect();
/*  43:    */           
/*  44: 42 */           InputStream in1 = con.getInputStream();
/*  45: 43 */           byte[] b = new byte[2048];
/*  46: 44 */           int count = in1.read(b);
/*  47: 45 */           while (count > 0)
/*  48:    */           {
/*  49: 47 */             System.out.println("test" + new String(b, 0, count));
/*  50: 48 */             count = in1.read(b);
/*  51:    */           }
/*  52:    */         }
/*  53: 51 */         Thread.currentThread();Thread.sleep(this.secs1 * 1000);
/*  54:    */       }
/*  55:    */     }
/*  56:    */     catch (Exception e)
/*  57:    */     {
/*  58: 58 */       e.printStackTrace();
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   public boolean isValid(Date dt)
/*  63:    */   {
/*  64: 72 */     Calendar cal = Calendar.getInstance();
/*  65: 73 */     cal.setTime(dt);
/*  66: 74 */     int day = cal.get(5);
/*  67: 75 */     int weekday = cal.get(7);
/*  68: 76 */     int hr = cal.get(11);
/*  69: 77 */     int min = cal.get(12);
/*  70: 78 */     if (checkRange(day, this.tasksch[0])) {
/*  71: 80 */       if (checkRange(weekday, this.tasksch[1])) {
/*  72: 82 */         if (checkRange(hr, this.tasksch[2])) {
/*  73: 84 */           if (checkRange(min, this.tasksch[3])) {
/*  74: 86 */             return true;
/*  75:    */           }
/*  76:    */         }
/*  77:    */       }
/*  78:    */     }
/*  79: 91 */     return false;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public boolean checkRange(int val, String range)
/*  83:    */   {
/*  84: 97 */     String[] ranges = range.split(",");
/*  85: 99 */     for (int i = 0; i < ranges.length; i++)
/*  86:    */     {
/*  87:101 */       String[] vals = ranges[i].split("-");
/*  88:102 */       if ((Integer.parseInt(vals[0]) <= val) && (Integer.parseInt(vals[1]) >= val)) {
/*  89:104 */         return true;
/*  90:    */       }
/*  91:    */     }
/*  92:108 */     return false;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public static void main(String[] args) {}
/*  96:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-services2\ken-services2.jar
 * Qualified Name:     com.kentropy.services.Task
 * JD-Core Version:    0.7.0.1
 */