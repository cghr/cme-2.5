/*  1:   */ package org.springframework.jmx;
/*  2:   */ 
/*  3:   */ public class MBeanServerNotFoundException
/*  4:   */   extends JmxException
/*  5:   */ {
/*  6:   */   public MBeanServerNotFoundException(String msg)
/*  7:   */   {
/*  8:36 */     super(msg);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public MBeanServerNotFoundException(String msg, Throwable cause)
/* 12:   */   {
/* 13:46 */     super(msg, cause);
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.MBeanServerNotFoundException
 * JD-Core Version:    0.7.0.1
 */