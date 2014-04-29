/*   1:    */ package org.springframework.core;
/*   2:    */ 
/*   3:    */ import java.io.PrintWriter;
/*   4:    */ import java.io.StringWriter;
/*   5:    */ import org.springframework.util.Assert;
/*   6:    */ 
/*   7:    */ public abstract class ControlFlowFactory
/*   8:    */ {
/*   9:    */   public static ControlFlow createControlFlow()
/*  10:    */   {
/*  11: 41 */     return new Jdk14ControlFlow();
/*  12:    */   }
/*  13:    */   
/*  14:    */   static class Jdk14ControlFlow
/*  15:    */     implements ControlFlow
/*  16:    */   {
/*  17:    */     private StackTraceElement[] stack;
/*  18:    */     
/*  19:    */     public Jdk14ControlFlow()
/*  20:    */     {
/*  21: 58 */       this.stack = new Throwable().getStackTrace();
/*  22:    */     }
/*  23:    */     
/*  24:    */     public boolean under(Class clazz)
/*  25:    */     {
/*  26: 65 */       Assert.notNull(clazz, "Class must not be null");
/*  27: 66 */       String className = clazz.getName();
/*  28: 67 */       for (int i = 0; i < this.stack.length; i++) {
/*  29: 68 */         if (this.stack[i].getClassName().equals(className)) {
/*  30: 69 */           return true;
/*  31:    */         }
/*  32:    */       }
/*  33: 72 */       return false;
/*  34:    */     }
/*  35:    */     
/*  36:    */     public boolean under(Class clazz, String methodName)
/*  37:    */     {
/*  38: 80 */       Assert.notNull(clazz, "Class must not be null");
/*  39: 81 */       Assert.notNull(methodName, "Method name must not be null");
/*  40: 82 */       String className = clazz.getName();
/*  41: 83 */       for (int i = 0; i < this.stack.length; i++) {
/*  42: 84 */         if ((this.stack[i].getClassName().equals(className)) && 
/*  43: 85 */           (this.stack[i].getMethodName().equals(methodName))) {
/*  44: 86 */           return true;
/*  45:    */         }
/*  46:    */       }
/*  47: 89 */       return false;
/*  48:    */     }
/*  49:    */     
/*  50:    */     public boolean underToken(String token)
/*  51:    */     {
/*  52: 97 */       if (token == null) {
/*  53: 98 */         return false;
/*  54:    */       }
/*  55:100 */       StringWriter sw = new StringWriter();
/*  56:101 */       new Throwable().printStackTrace(new PrintWriter(sw));
/*  57:102 */       String stackTrace = sw.toString();
/*  58:103 */       return stackTrace.indexOf(token) != -1;
/*  59:    */     }
/*  60:    */     
/*  61:    */     public String toString()
/*  62:    */     {
/*  63:108 */       StringBuilder sb = new StringBuilder("Jdk14ControlFlow: ");
/*  64:109 */       for (int i = 0; i < this.stack.length; i++)
/*  65:    */       {
/*  66:110 */         if (i > 0) {
/*  67:111 */           sb.append("\n\t@");
/*  68:    */         }
/*  69:113 */         sb.append(this.stack[i]);
/*  70:    */       }
/*  71:115 */       return sb.toString();
/*  72:    */     }
/*  73:    */   }
/*  74:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.ControlFlowFactory
 * JD-Core Version:    0.7.0.1
 */