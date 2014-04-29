/*  1:   */ package com.kentropy.workflow;
/*  2:   */ 
/*  3:   */ import com.kentropy.process.notification.ProcessNotification2;
/*  4:   */ import java.io.PrintStream;
/*  5:   */ import java.util.Date;
/*  6:   */ import java.util.Map;
/*  7:   */ 
/*  8:   */ public class PaymentApprovalNotification
/*  9:   */   implements WorkflowEventHandler
/* 10:   */ {
/* 11:   */   public void onEnter(Map map) {}
/* 12:   */   
/* 13:   */   public void onExit(Map map)
/* 14:   */   {
/* 15:23 */     String startdate = map.get("assignment_time").toString();
/* 16:24 */     String pid = map.get("pid").toString();
/* 17:25 */     String[] assignedTo = ((String)map.get("assignedTo")).split(",");
/* 18:26 */     System.out.println(" Completed By " + (String)map.get("completedby"));
/* 19:27 */     for (int i = 0; i < assignedTo.length; i++) {
/* 20:29 */       new ProcessNotification2().queue(pid, assignedTo[i], "email", "payment_complete-" + new Date(), "payment_approval_coding", (String)map.get("completedby"), "Pending");
/* 21:   */     }
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-utilities6\ken-utilities6.jar
 * Qualified Name:     com.kentropy.workflow.PaymentApprovalNotification
 * JD-Core Version:    0.7.0.1
 */