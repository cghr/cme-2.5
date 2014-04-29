/*  1:   */ package com.kentropy.cme;
/*  2:   */ 
/*  3:   */ import com.kentropy.scheduler.Job;
/*  4:   */ import com.kentropy.scheduler.Scheduler;
/*  5:   */ import java.io.PrintStream;
/*  6:   */ 
/*  7:   */ public class CMEControllerJobs
/*  8:   */   implements Job
/*  9:   */ {
/* 10: 9 */   int task = 0;
/* 11:   */   
/* 12:   */   static
/* 13:   */   {
/* 14:14 */     Scheduler.add("CME PROCESS", "CREATE PROCESS", new CMEControllerJobs(1), "never");
/* 15:   */   }
/* 16:   */   
/* 17:   */   public CMEControllerJobs(int tmp)
/* 18:   */   {
/* 19:22 */     this.task = tmp;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void execute()
/* 23:   */     throws Exception
/* 24:   */   {
/* 25:30 */     System.out.println("Inside execute method of cme controller");
/* 26:31 */     int task = this.task;
/* 27:32 */     String[] args = new String[0];
/* 28:34 */     switch (task)
/* 29:   */     {
/* 30:   */     case 1: 
/* 31:38 */       CMEController.createProcess(args);
/* 32:   */       
/* 33:40 */       break;
/* 34:   */     }
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-cme-controller\ken-cme-controller.jar
 * Qualified Name:     com.kentropy.cme.CMEControllerJobs
 * JD-Core Version:    0.7.0.1
 */