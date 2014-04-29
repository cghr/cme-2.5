/*  1:   */ package com.kentropy.notifications.dao;
/*  2:   */ 
/*  3:   */ import com.kentropy.notifications.model.Message;
/*  4:   */ import com.kentropy.util.SpringUtils;
/*  5:   */ import org.springframework.jdbc.core.JdbcTemplate;
/*  6:   */ 
/*  7:   */ public class MessageDAO
/*  8:   */ {
/*  9:10 */   JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/* 10:   */   
/* 11:   */   public void saveMessage(Message msg)
/* 12:   */   {
/* 13:15 */     String sql = "INSERT INTO notifications_templates(subject,summary,summaryquery,template,query,type) values(?,?,?,?,?,?)";
/* 14:   */     
/* 15:17 */     this.jt.update(sql, new Object[] { msg.getSubject(), msg.getSummary(), msg.getSummaryquery(), msg.getTemplate(), msg.getQuery(), msg.getType() });
/* 16:   */   }
/* 17:   */   
/* 18:   */   public String getMessageStatus(int id)
/* 19:   */   {
/* 20:26 */     return null;
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-cme-notifications\ken-cme-notifications.jar
 * Qualified Name:     com.kentropy.notifications.dao.MessageDAO
 * JD-Core Version:    0.7.0.1
 */