/*  1:   */ package com.kentropy.util;
/*  2:   */ 
/*  3:   */ import org.springframework.context.ApplicationContext;
/*  4:   */ import org.springframework.jdbc.core.JdbcTemplate;
/*  5:   */ 
/*  6:   */ public class SpringUtils
/*  7:   */ {
/*  8:   */   private JdbcTemplate jdbcTemplate;
/*  9:   */   private ApplicationContext appContext;
/* 10:   */   
/* 11:   */   public JdbcTemplate getJdbcTemplate()
/* 12:   */   {
/* 13:19 */     this.jdbcTemplate = ((JdbcTemplate)SpringApplicationContext.getBean("jdbcTemp"));
/* 14:   */     
/* 15:   */ 
/* 16:   */ 
/* 17:   */ 
/* 18:24 */     return this.jdbcTemplate;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
/* 22:   */   {
/* 23:29 */     this.jdbcTemplate = jdbcTemplate;
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-util\ken-util.jar
 * Qualified Name:     com.kentropy.util.SpringUtils
 * JD-Core Version:    0.7.0.1
 */