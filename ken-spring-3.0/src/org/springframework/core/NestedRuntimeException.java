/*   1:    */ package org.springframework.core;
/*   2:    */ 
/*   3:    */ public abstract class NestedRuntimeException
/*   4:    */   extends RuntimeException
/*   5:    */ {
/*   6:    */   private static final long serialVersionUID = 5439915454935047936L;
/*   7:    */   
/*   8:    */   static
/*   9:    */   {
/*  10: 45 */     NestedExceptionUtils.class.getName();
/*  11:    */   }
/*  12:    */   
/*  13:    */   public NestedRuntimeException(String msg)
/*  14:    */   {
/*  15: 54 */     super(msg);
/*  16:    */   }
/*  17:    */   
/*  18:    */   public NestedRuntimeException(String msg, Throwable cause)
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
/*  30: 84 */     Throwable rootCause = null;
/*  31: 85 */     Throwable cause = getCause();
/*  32: 86 */     while ((cause != null) && (cause != rootCause))
/*  33:    */     {
/*  34: 87 */       rootCause = cause;
/*  35: 88 */       cause = cause.getCause();
/*  36:    */     }
/*  37: 90 */     return rootCause;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public Throwable getMostSpecificCause()
/*  41:    */   {
/*  42:102 */     Throwable rootCause = getRootCause();
/*  43:103 */     return rootCause != null ? rootCause : this;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public boolean contains(Class exType)
/*  47:    */   {
/*  48:114 */     if (exType == null) {
/*  49:115 */       return false;
/*  50:    */     }
/*  51:117 */     if (exType.isInstance(this)) {
/*  52:118 */       return true;
/*  53:    */     }
/*  54:120 */     Throwable cause = getCause();
/*  55:121 */     if (cause == this) {
/*  56:122 */       return false;
/*  57:    */     }
/*  58:124 */     if ((cause instanceof NestedRuntimeException)) {
/*  59:125 */       return ((NestedRuntimeException)cause).contains(exType);
/*  60:    */     }
/*  61:128 */     while (cause != null)
/*  62:    */     {
/*  63:129 */       if (exType.isInstance(cause)) {
/*  64:130 */         return true;
/*  65:    */       }
/*  66:132 */       if (cause.getCause() == cause) {
/*  67:    */         break;
/*  68:    */       }
/*  69:135 */       cause = cause.getCause();
/*  70:    */     }
/*  71:137 */     return false;
/*  72:    */   }
/*  73:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.NestedRuntimeException
 * JD-Core Version:    0.7.0.1
 */