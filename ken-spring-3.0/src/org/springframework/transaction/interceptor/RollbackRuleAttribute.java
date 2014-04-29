/*   1:    */ package org.springframework.transaction.interceptor;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.springframework.util.Assert;
/*   5:    */ 
/*   6:    */ public class RollbackRuleAttribute
/*   7:    */   implements Serializable
/*   8:    */ {
/*   9: 41 */   public static final RollbackRuleAttribute ROLLBACK_ON_RUNTIME_EXCEPTIONS = new RollbackRuleAttribute(RuntimeException.class);
/*  10:    */   private final String exceptionName;
/*  11:    */   
/*  12:    */   public RollbackRuleAttribute(Class clazz)
/*  13:    */   {
/*  14: 62 */     Assert.notNull(clazz, "'clazz' cannot be null.");
/*  15: 63 */     if (!Throwable.class.isAssignableFrom(clazz)) {
/*  16: 64 */       throw new IllegalArgumentException(
/*  17: 65 */         "Cannot construct rollback rule from [" + clazz.getName() + "]: it's not a Throwable");
/*  18:    */     }
/*  19: 67 */     this.exceptionName = clazz.getName();
/*  20:    */   }
/*  21:    */   
/*  22:    */   public RollbackRuleAttribute(String exceptionName)
/*  23:    */   {
/*  24: 89 */     Assert.hasText(exceptionName, "'exceptionName' cannot be null or empty.");
/*  25: 90 */     this.exceptionName = exceptionName;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public String getExceptionName()
/*  29:    */   {
/*  30: 98 */     return this.exceptionName;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public int getDepth(Throwable ex)
/*  34:    */   {
/*  35:108 */     return getDepth(ex.getClass(), 0);
/*  36:    */   }
/*  37:    */   
/*  38:    */   private int getDepth(Class exceptionClass, int depth)
/*  39:    */   {
/*  40:113 */     if (exceptionClass.getName().indexOf(this.exceptionName) != -1) {
/*  41:115 */       return depth;
/*  42:    */     }
/*  43:118 */     if (exceptionClass.equals(Throwable.class)) {
/*  44:119 */       return -1;
/*  45:    */     }
/*  46:121 */     return getDepth(exceptionClass.getSuperclass(), depth + 1);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public boolean equals(Object other)
/*  50:    */   {
/*  51:127 */     if (this == other) {
/*  52:128 */       return true;
/*  53:    */     }
/*  54:130 */     if (!(other instanceof RollbackRuleAttribute)) {
/*  55:131 */       return false;
/*  56:    */     }
/*  57:133 */     RollbackRuleAttribute rhs = (RollbackRuleAttribute)other;
/*  58:134 */     return this.exceptionName.equals(rhs.exceptionName);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public int hashCode()
/*  62:    */   {
/*  63:139 */     return this.exceptionName.hashCode();
/*  64:    */   }
/*  65:    */   
/*  66:    */   public String toString()
/*  67:    */   {
/*  68:144 */     return "RollbackRuleAttribute with pattern [" + this.exceptionName + "]";
/*  69:    */   }
/*  70:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.interceptor.RollbackRuleAttribute
 * JD-Core Version:    0.7.0.1
 */