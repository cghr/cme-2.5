/*  1:   */ package org.springframework.dao.support;
/*  2:   */ 
/*  3:   */ import org.apache.commons.logging.Log;
/*  4:   */ import org.apache.commons.logging.LogFactory;
/*  5:   */ import org.springframework.beans.factory.BeanInitializationException;
/*  6:   */ import org.springframework.beans.factory.InitializingBean;
/*  7:   */ 
/*  8:   */ public abstract class DaoSupport
/*  9:   */   implements InitializingBean
/* 10:   */ {
/* 11:39 */   protected final Log logger = LogFactory.getLog(getClass());
/* 12:   */   
/* 13:   */   public final void afterPropertiesSet()
/* 14:   */     throws IllegalArgumentException, BeanInitializationException
/* 15:   */   {
/* 16:44 */     checkDaoConfig();
/* 17:   */     try
/* 18:   */     {
/* 19:48 */       initDao();
/* 20:   */     }
/* 21:   */     catch (Exception ex)
/* 22:   */     {
/* 23:51 */       throw new BeanInitializationException("Initialization of DAO failed", ex);
/* 24:   */     }
/* 25:   */   }
/* 26:   */   
/* 27:   */   protected abstract void checkDaoConfig()
/* 28:   */     throws IllegalArgumentException;
/* 29:   */   
/* 30:   */   protected void initDao()
/* 31:   */     throws Exception
/* 32:   */   {}
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.dao.support.DaoSupport
 * JD-Core Version:    0.7.0.1
 */