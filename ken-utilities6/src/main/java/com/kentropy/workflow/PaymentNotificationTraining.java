/*  1:   */ package com.kentropy.workflow;
/*  2:   */ 
/*  3:   */ import com.kentropy.process.notification.ProcessNotification2;
/*  4:   */ import com.kentropy.util.SpringUtils;
/*  5:   */ import java.util.Date;
/*  6:   */ import java.util.Map;
/*  7:   */ import org.springframework.jdbc.core.JdbcTemplate;
/*  8:   */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*  9:   */ 
/* 10:   */ public class PaymentNotificationTraining
/* 11:   */   implements WorkflowEventHandler
/* 12:   */ {
/* 13:   */   public void onEnter(Map map) {}
/* 14:   */   
/* 15:   */   public void onExit(Map map)
/* 16:   */   {
/* 17:22 */     String startdate = map.get("assignment_time").toString();
/* 18:23 */     String pid = map.get("pid").toString();
/* 19:24 */     String qry = "SELECT b.username FROM physician_payment_training a LEFT JOIN physician b ON a.phy_id=b.id  WHERE a.transaction_id='" + startdate + "' and a.success='2'";
/* 20:   */     
/* 21:26 */     JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/* 22:27 */     SqlRowSet rs = jt.queryForRowSet(qry);
/* 23:28 */     int i = 0;
/* 24:29 */     while (rs.next())
/* 25:   */     {
/* 26:30 */       new ProcessNotification2().queue(pid, rs.getString("username"), "email", i + "-" + new Date(), "payment_ack_training", "Pending");
/* 27:31 */       i++;
/* 28:   */     }
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-utilities6\ken-utilities6.jar
 * Qualified Name:     com.kentropy.workflow.PaymentNotificationTraining
 * JD-Core Version:    0.7.0.1
 */