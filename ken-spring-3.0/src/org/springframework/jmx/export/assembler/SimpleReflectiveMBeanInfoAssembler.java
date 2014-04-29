/*  1:   */ package org.springframework.jmx.export.assembler;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Method;
/*  4:   */ 
/*  5:   */ public class SimpleReflectiveMBeanInfoAssembler
/*  6:   */   extends AbstractConfigurableMBeanInfoAssembler
/*  7:   */ {
/*  8:   */   protected boolean includeReadAttribute(Method method, String beanKey)
/*  9:   */   {
/* 10:37 */     return true;
/* 11:   */   }
/* 12:   */   
/* 13:   */   protected boolean includeWriteAttribute(Method method, String beanKey)
/* 14:   */   {
/* 15:45 */     return true;
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected boolean includeOperation(Method method, String beanKey)
/* 19:   */   {
/* 20:53 */     return true;
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.assembler.SimpleReflectiveMBeanInfoAssembler
 * JD-Core Version:    0.7.0.1
 */