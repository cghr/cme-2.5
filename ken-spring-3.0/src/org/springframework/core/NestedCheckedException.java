/*   1:    */ package org.springframework.core;
/*   2:    */ 
/*   3:    */ public abstract class NestedCheckedException
/*   4:    */   extends Exception
/*   5:    */ {
/*   6:    */   private static final long serialVersionUID = 7100714597678207546L;
/*   7:    */   
/*   8:    */   static
/*   9:    */   {
/*  10: 45 */     NestedExceptionUtils.class.getName();
/*  11:    */   }
/*  12:    */   
/*  13:    */   public NestedCheckedException(String msg)
/*  14:    */   {
/*  15: 54 */     super(msg);
/*  16:    */   }
/*  17:    */   
/*  18:    */   public NestedCheckedException(String msg, Throwable cause)
/*  19:    */   {
/*  20: 64 */     super(msg, cause);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public String getMessage()
/*  24:    */   {
/*  25: 74 */     return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
/*  26:    */   }
/*  27:    */   
/*  28:    */   public Throwable getRootCause()
/*  29:    */   {
/*  30: 83 */     Throwable rootCause = null;
/*  31: 84 */     Throwable cause = getCause();
/*  32: 85 */     while ((cause != null) && (cause != rootCause))
/*  33:    */     {
/*  34: 86 */       rootCause = cause;
/*  35: 87 */       cause = cause.getCause();
/*  36:    */     }
/*  37: 89 */     return rootCause;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public Throwable getMostSpecificCause()
/*  41:    */   {
/*  42:101 */     Throwable rootCause = getRootCause();
/*  43:102 */     return rootCause != null ? rootCause : this;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public boolean contains(Class exType)
/*  47:    */   {
/*  48:113 */     if (exType == null) {
/*  49:114 */       return false;
/*  50:    */     }
/*  51:116 */     if (exType.isInstance(this)) {
/*  52:117 */       return true;
/*  53:    */     }
/*  54:119 */     Throwable cause = getCause();
/*  55:120 */     if (cause == this) {
/*  56:121 */       return false;
/*  57:    */     }
/*  58:123 */     if ((cause instanceof NestedCheckedException)) {
/*  59:124 */       return ((NestedCheckedException)cause).contains(exType);
/*  60:    */     }
/*  61:127 */     while (cause != null)
/*  62:    */     {
/*  63:128 */       if (exType.isInstance(cause)) {
/*  64:129 */         return true;
/*  65:    */       }
/*  66:131 */       if (cause.getCause() == cause) {
/*  67:    */         break;
/*  68:    */       }
/*  69:134 */       cause = cause.getCause();
/*  70:    */     }
/*  71:136 */     return false;
/*  72:    */   }
/*  73:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.NestedCheckedException
 * JD-Core Version:    0.7.0.1
 */