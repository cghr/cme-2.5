/*  1:   */ package org.springframework.jdbc.core.namedparam;
/*  2:   */ 
/*  3:   */ import org.springframework.jdbc.core.support.JdbcDaoSupport;
/*  4:   */ 
/*  5:   */ public class NamedParameterJdbcDaoSupport
/*  6:   */   extends JdbcDaoSupport
/*  7:   */ {
/*  8:   */   private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
/*  9:   */   
/* 10:   */   protected void initTemplateConfig()
/* 11:   */   {
/* 12:39 */     this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
/* 13:   */   }
/* 14:   */   
/* 15:   */   public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate()
/* 16:   */   {
/* 17:46 */     return this.namedParameterJdbcTemplate;
/* 18:   */   }
/* 19:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport
 * JD-Core Version:    0.7.0.1
 */