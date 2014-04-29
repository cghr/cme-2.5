/*  1:   */ package org.springframework.ui.velocity;
/*  2:   */ 
/*  3:   */ import org.apache.commons.logging.Log;
/*  4:   */ import org.apache.commons.logging.LogFactory;
/*  5:   */ import org.apache.velocity.app.VelocityEngine;
/*  6:   */ import org.apache.velocity.runtime.RuntimeServices;
/*  7:   */ import org.apache.velocity.runtime.log.LogSystem;
/*  8:   */ 
/*  9:   */ public class CommonsLoggingLogSystem
/* 10:   */   implements LogSystem
/* 11:   */ {
/* 12:35 */   private static final Log logger = LogFactory.getLog(VelocityEngine.class);
/* 13:   */   
/* 14:   */   public void init(RuntimeServices runtimeServices) {}
/* 15:   */   
/* 16:   */   public void logVelocityMessage(int type, String msg)
/* 17:   */   {
/* 18:41 */     switch (type)
/* 19:   */     {
/* 20:   */     case 3: 
/* 21:43 */       logger.error(msg);
/* 22:44 */       break;
/* 23:   */     case 2: 
/* 24:46 */       logger.warn(msg);
/* 25:47 */       break;
/* 26:   */     case 1: 
/* 27:49 */       logger.info(msg);
/* 28:50 */       break;
/* 29:   */     case 0: 
/* 30:52 */       logger.debug(msg);
/* 31:   */     }
/* 32:   */   }
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ui.velocity.CommonsLoggingLogSystem
 * JD-Core Version:    0.7.0.1
 */