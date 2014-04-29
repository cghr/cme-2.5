/*  1:   */ package org.springframework.scheduling.quartz;
/*  2:   */ 
/*  3:   */ import org.springframework.core.NestedRuntimeException;
/*  4:   */ import org.springframework.util.MethodInvoker;
/*  5:   */ 
/*  6:   */ public class JobMethodInvocationFailedException
/*  7:   */   extends NestedRuntimeException
/*  8:   */ {
/*  9:   */   public JobMethodInvocationFailedException(MethodInvoker methodInvoker, Throwable cause)
/* 10:   */   {
/* 11:40 */     super("Invocation of method '" + methodInvoker.getTargetMethod() + "' on target class [" + methodInvoker.getTargetClass() + "] failed", cause);
/* 12:   */   }
/* 13:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.quartz.JobMethodInvocationFailedException
 * JD-Core Version:    0.7.0.1
 */