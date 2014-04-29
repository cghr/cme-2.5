/*  1:   */ package org.springframework.remoting.support;
/*  2:   */ 
/*  3:   */ import java.util.HashSet;
/*  4:   */ import java.util.Set;
/*  5:   */ 
/*  6:   */ public abstract class RemoteInvocationUtils
/*  7:   */ {
/*  8:   */   public static void fillInClientStackTraceIfPossible(Throwable ex)
/*  9:   */   {
/* 10:46 */     if (ex != null)
/* 11:   */     {
/* 12:47 */       StackTraceElement[] clientStack = new Throwable().getStackTrace();
/* 13:48 */       Set<Throwable> visitedExceptions = new HashSet();
/* 14:49 */       Throwable exToUpdate = ex;
/* 15:50 */       while ((exToUpdate != null) && (!visitedExceptions.contains(exToUpdate)))
/* 16:   */       {
/* 17:51 */         StackTraceElement[] serverStack = exToUpdate.getStackTrace();
/* 18:52 */         StackTraceElement[] combinedStack = new StackTraceElement[serverStack.length + clientStack.length];
/* 19:53 */         System.arraycopy(serverStack, 0, combinedStack, 0, serverStack.length);
/* 20:54 */         System.arraycopy(clientStack, 0, combinedStack, serverStack.length, clientStack.length);
/* 21:55 */         exToUpdate.setStackTrace(combinedStack);
/* 22:56 */         visitedExceptions.add(exToUpdate);
/* 23:57 */         exToUpdate = exToUpdate.getCause();
/* 24:   */       }
/* 25:   */     }
/* 26:   */   }
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.support.RemoteInvocationUtils
 * JD-Core Version:    0.7.0.1
 */