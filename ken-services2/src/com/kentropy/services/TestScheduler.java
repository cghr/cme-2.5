/*  1:   */ package com.kentropy.services;
/*  2:   */ 
/*  3:   */ import java.io.FileNotFoundException;
/*  4:   */ import java.io.FileReader;
/*  5:   */ import java.io.IOException;
/*  6:   */ import java.io.PrintStream;
/*  7:   */ import java.util.Properties;
/*  8:   */ import java.util.concurrent.Executors;
/*  9:   */ import java.util.concurrent.ScheduledExecutorService;
/* 10:   */ import java.util.concurrent.TimeUnit;
/* 11:   */ 
/* 12:   */ public class TestScheduler
/* 13:   */ {
/* 14:   */   public static void main(String[] args)
/* 15:   */     throws InterruptedException, FileNotFoundException, IOException
/* 16:   */   {
/* 17:22 */     ScheduledExecutorService sec = Executors.newScheduledThreadPool(5);
/* 18:23 */     int secs = 60;
/* 19:24 */     if (args.length > 0) {
/* 20:26 */       secs = Integer.parseInt(args[0]);
/* 21:   */     }
/* 22:37 */     Properties p = new Properties();
/* 23:38 */     p.load(new FileReader("cme-phy-agent.properties"));
/* 24:   */     
/* 25:40 */     sec.scheduleAtFixedRate(new CMEToPhysicianAgent(p), 0L, secs, TimeUnit.SECONDS);
/* 26:41 */     p = new Properties();
/* 27:42 */     p.load(new FileReader("cme-qa-agent.properties"));
/* 28:43 */     sec.scheduleAtFixedRate(new CMEToQAAgent(p), 60L, 2L, TimeUnit.HOURS);
/* 29:   */     
/* 30:   */ 
/* 31:   */ 
/* 32:   */ 
/* 33:   */ 
/* 34:   */ 
/* 35:   */ 
/* 36:   */ 
/* 37:   */ 
/* 38:53 */     sec.scheduleAtFixedRate(new Runnable()
/* 39:   */     {
/* 40:   */       public void run()
/* 41:   */       {
/* 42:58 */         System.out.println("Running2");
/* 43:   */       }
/* 44:61 */     }, 8L, 5L, TimeUnit.SECONDS);
/* 45:   */   }
/* 46:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-services2\ken-services2.jar
 * Qualified Name:     com.kentropy.services.TestScheduler
 * JD-Core Version:    0.7.0.1
 */