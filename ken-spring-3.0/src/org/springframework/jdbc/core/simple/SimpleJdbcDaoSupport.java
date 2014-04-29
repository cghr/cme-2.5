/*  1:   */ package org.springframework.jdbc.core.simple;
/*  2:   */ 
/*  3:   */ import org.springframework.jdbc.core.support.JdbcDaoSupport;
/*  4:   */ 
/*  5:   */ @Deprecated
/*  6:   */ public class SimpleJdbcDaoSupport
/*  7:   */   extends JdbcDaoSupport
/*  8:   */ {
/*  9:   */   private SimpleJdbcTemplate simpleJdbcTemplate;
/* 10:   */   
/* 11:   */   protected void initTemplateConfig()
/* 12:   */   {
/* 13:45 */     this.simpleJdbcTemplate = new SimpleJdbcTemplate(getJdbcTemplate());
/* 14:   */   }
/* 15:   */   
/* 16:   */   public SimpleJdbcTemplate getSimpleJdbcTemplate()
/* 17:   */   {
/* 18:52 */     return this.simpleJdbcTemplate;
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport
 * JD-Core Version:    0.7.0.1
 */